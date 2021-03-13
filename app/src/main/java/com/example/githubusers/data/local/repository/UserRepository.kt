package com.example.githubusers.data.local.repository

import com.example.githubusers.data.local.dao.ProfileDao
import com.example.githubusers.data.local.dao.UsersDao
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.util.constants.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val usersDao: UsersDao,
    private val profileDao: ProfileDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IUserRepository {
    override suspend fun getAllUsers(listener: IUserRepository.Listener<List<LocalUser>>) =
        withContext(ioDispatcher) {
            try {
                val listOfUsers = usersDao.getAllUsers()
                listener.onSuccess(listOfUsers)
            } catch (e: Exception) {
                listener.onFailed(getAllUsersErrorMessage)
            }
        }

    override suspend fun getAllUserWithProfile(listener: IUserRepository.Listener<List<UserWithProfile>>) =
        withContext(ioDispatcher) {
            try {
                val listOfUserWithProfile = usersDao.getAllUserWithProfile()
                listener.onSuccess(listOfUserWithProfile)
            } catch (e: Exception) {
                listener.onFailed(getAllUserWithProfileErrorMessage)
            }
        }

    override suspend fun getUser(id: Int, listener: IUserRepository.Listener<LocalUser>) =
        withContext(ioDispatcher) {
            try {
                val user = usersDao.findUserById(id)
                listener.onSuccess(user)
            } catch (e: Exception) {
                listener.onFailed(getUserErrorMessage)
            }
        }

    override suspend fun insertProfile(
        profile: LocalProfile,
        listener: IUserRepository.Listener<Any>
    ) = withContext(ioDispatcher) {
        try {
            profileDao.insertProfile(profile)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onFailed(insertProfileErrorMessage)
        }
    }

    override suspend fun updateProfile(
        profile: LocalProfile,
        listener: IUserRepository.Listener<Any>
    ) = withContext(ioDispatcher) {
        try {
            profileDao.updateProfile(profile)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onFailed(updateProfileErrorMessage)
        }
    }

    override suspend fun updateUser(user: LocalUser, listener: IUserRepository.Listener<Any>) =
        withContext(ioDispatcher) {
            try {
                usersDao.updateUser(user)
                listener.onSuccess()
            } catch (e: Exception) {
                listener.onFailed(updateUserErrorMessage)
            }
        }

    override suspend fun saveAll(user: List<LocalUser>, listener: IUserRepository.Listener<Any>) =
        withContext(ioDispatcher) {
            try {
                usersDao.insertAll(user)
                listener.onSuccess()
            } catch (e: Exception) {
                listener.onFailed(saveAllErrorMessage)
            }
        }

    override suspend fun deleteAllUser(listener: IUserRepository.Listener<Any>) =
        withContext(ioDispatcher) {
            try {
                usersDao.deleteAll()
                listener.onSuccess()
            } catch (e: Exception) {
                listener.onFailed(deleteAllUserErrorMessage)
            }
        }
}