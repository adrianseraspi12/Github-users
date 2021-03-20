package com.example.githubusers.data.main.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.local.repository.IUserRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.repository.IGithubRepository
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.util.constants.somethingWentWrongErrorMessage
import com.example.githubusers.util.extensions.toLocalProfile
import com.example.githubusers.util.extensions.toLocalUser
import kotlinx.coroutines.*

class MainRepository(
        private val localRepository: IUserRepository,
        private val remoteRepository: IGithubRepository,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IMainRepository {
    override suspend fun loadUserList(listener: Listener<List<UserWithProfile>>) =
        withContext(ioDispatcher) {
            try {
                //  Show list of users from local if available
                val listFromLocal = getListFromLocal()
                if (listFromLocal.isNotEmpty()) {
                    listener.onSuccess(listFromLocal)
                }

                //  Call fail if list from local and remote is not available
                val listFromRemote = getListFromRemote(0)
                if (listFromLocal.isEmpty() && listFromRemote.isEmpty()) {
                    launch(Dispatchers.Main) { listener.onFailed(requestUserListErrorMessage) }
                    return@withContext
                } else if (listFromRemote.isEmpty()) {
                    launch(Dispatchers.Main) { listener.onSuccess(listFromLocal) }
                    return@withContext
                }

                //  Request for another list on remote
                //  until they have the same data
                loop@ while (listFromLocal.size > listFromRemote.size) {
                    val lastItemId = listFromRemote.last().id ?: break@loop
                    val newListFromRemote = getListFromRemote(lastItemId)

                    if (newListFromRemote.isEmpty()) {
                        break@loop
                    }
                    listFromRemote.addAll(newListFromRemote)
                }

                //  If the local and remote does not have the same data
                //  return the local list to retain the data from
                //  the local
                if (listFromLocal.size > listFromRemote.size) {
                    launch(Dispatchers.Main) { listener.onSuccess(listFromLocal) }
                    return@withContext
                }

                //  Convert the list from UserResponse to UserWithProfile
                val listOfUserWithProfileFromRemote = convertToUserWithProfile(listFromRemote)
                //  Merge the local and remote list to set the notes in remote list
                val mergedUserWithProfileList = mergeLocalAndRemoteList(
                    listFromLocal,
                    listOfUserWithProfileFromRemote
                )

                //  Update the local database and show the list
                saveToLocalDatabase(mergedUserWithProfileList)
                launch(Dispatchers.Main) { listener.onSuccess(mergedUserWithProfileList) }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    listener.onFailed(
                        e.message ?: somethingWentWrongErrorMessage
                    )
                }
            }
        }

    override suspend fun loadUserList(since: Int, listener: Listener<List<UserWithProfile>>) =
            withContext(ioDispatcher) {
                try {
                    //  Call fail if list from remote is not empty
                    val listFromRemote = getListFromRemote(since)
                    if (listFromRemote.isEmpty()) {
                        launch(Dispatchers.Main) { listener.onFailed(requestUserListErrorMessage) }
                        return@withContext
                    }

                    //  Convert the list from UserResponse to UserWithProfile
                    val listOfUserWithProfile = convertToUserWithProfile(listFromRemote)

                    //  Update the local database and show the list
                    saveToLocalDatabase(listOfUserWithProfile)
                    launch(Dispatchers.Main) { listener.onSuccess(listOfUserWithProfile) }
                } catch (e: Exception) {
                    launch(Dispatchers.Main) {
                        listener.onFailed(
                                e.message ?: somethingWentWrongErrorMessage
                        )
                    }
                }
            }

    override suspend fun updateUserOnLocal(user: LocalUser, listener: Listener<Nothing>) {
        val result = localRepository.updateUser(user)
        if (result is Result.onSuccess) {
            listener.onSuccess()
            return
        } else {
            val errorResult = result as Result.onFailed
            listener.onFailed(errorResult.errorMessage)
        }
    }

    private suspend fun getListFromLocal(): List<UserWithProfile> {
        val resultFromLocal = localRepository.getAllUserWithProfile()
        var listOfUser = listOf<UserWithProfile>()

        if (resultFromLocal is Result.onSuccess) {
            listOfUser = resultFromLocal.data ?: listOf()
        }
        return listOfUser
    }

    private suspend fun getListFromRemote(since: Int): MutableList<UserResponse> {
        val resultFromRemote = remoteRepository.requestUserList(since)
        val listOfUser: MutableList<UserResponse>
        if (resultFromRemote is Result.onSuccess) {
            listOfUser = resultFromRemote.data?.toMutableList() ?: mutableListOf()
        } else {
            val errorMessage = (resultFromRemote as Result.onFailed).errorMessage
            throw Exception(errorMessage)
        }
        return listOfUser
    }

    private suspend fun saveToLocalDatabase(userWithProfileList: List<UserWithProfile>) {
        for (userWithProfile in userWithProfileList) {
            userWithProfile.user?.let {
                localRepository.insertUser(it)
            }
            userWithProfile.profile?.let {
                localRepository.insertProfile(it)
            }
        }
    }

    private suspend fun convertToUserWithProfile(list: List<UserResponse>): List<UserWithProfile> {
        val listOfUser = mutableListOf<UserWithProfile>()
        list.forEach {
            val localUser = it.toLocalUser()
            var localProfile = LocalProfile()

            //  Create an async action that calls
            //  profile from server before continue
            //  the loop
            val deferredProfileResult = GlobalScope.async {
                remoteRepository.requestUserProfile(it.name ?: "")
            }

            //  Wait for the request to finish
            //  and convert the result data to
            //  LocalProfile
            val profileResult = deferredProfileResult.await()
            if (profileResult is Result.onSuccess) {
                localProfile = profileResult.data?.toLocalProfile(it.id) ?: LocalProfile()
            }

            listOfUser.add(UserWithProfile(localUser, localProfile))
        }

        return listOfUser.toList()
    }

    private fun mergeLocalAndRemoteList(
            listFromLocal: List<UserWithProfile>,
            listFromRemote: List<UserWithProfile>
    ): List<UserWithProfile> {
        val mutableListFromRemote = listFromRemote.toMutableList()
        mutableListFromRemote.forEachIndexed { index, userWithProfileRemote ->
            //  Get the UserWithProfile from local
            val userWithProfileLocal = listFromLocal.find {
                it.user?.id == userWithProfileRemote.user?.id
            }

            //  Set the notes of remote object
            mutableListFromRemote[index].user?.notes = userWithProfileLocal?.user?.notes ?: ""
        }
        return mutableListFromRemote
    }

}