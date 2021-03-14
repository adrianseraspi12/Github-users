package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import kotlinx.coroutines.runBlocking

class UserListPresenter(
        private val view: UserListContract.View,
        private val mainRepository: IMainRepository
) :
        UserListContract.Presenter {

    init {
        view.setupPresenter(this)
    }

    override fun setup() = runBlocking {
        view.showLoading()
        mainRepository.loadUserList(object : Listener<List<UserWithProfile>> {
            override fun onSuccess(data: List<UserWithProfile>?) {
                view.stopLoading()
                if (data != null) {
                    view.setUserList(data)
                }
            }

            override fun onFailed(errorMessage: String) {
                view.stopLoading()
            }
        })
    }
}