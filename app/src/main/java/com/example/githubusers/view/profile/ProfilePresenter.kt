package com.example.githubusers.view.profile

import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.util.constants.successfulSaveNote
import kotlinx.coroutines.runBlocking

class ProfilePresenter(
    private val view: ProfileContract.View,
    private var userWithProfile: UserWithProfile,
    private val mainRepository: IMainRepository
) : ProfileContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun setup() {
        val user = userWithProfile.user ?: LocalUser()
        val profile = userWithProfile.profile
        val followersCount = (profile?.followersCount ?: 0).toString()
        val followingCount = (profile?.followingCount ?: 0).toString()
        view.showProfileDetails(
            profile?.name ?: "",
            user.image ?: "",
            followersCount,
            followingCount,
            profile?.company ?: "",
            profile?.blog ?: "",
            user.notes ?: ""
        )
    }

    override fun saveNote(notes: String) = runBlocking {
        userWithProfile.user?.notes = notes
        val user = userWithProfile.user ?: LocalUser()
        mainRepository.updateUserOnLocal(user, object : Listener<Nothing> {
            override fun onSuccess(data: Nothing?) {
                view.showToastMessage(successfulSaveNote)
                view.updateUserWithProfileFromLastScreen(userWithProfile)
            }

            override fun onFailed(errorMessage: String) {
                view.showToastMessage(errorMessage)
            }

        })
    }
}