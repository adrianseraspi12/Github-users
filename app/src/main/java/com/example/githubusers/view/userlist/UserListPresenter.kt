package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.util.constants.noInternetConnectionErrorMessage
import com.example.githubusers.util.constants.requestUserListErrorMessage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class UserListPresenter(
        private val view: UserListContract.View,
        private val mainRepository: IMainRepository
) : UserListContract.Presenter {

    private var userListHasLoaded = false
    private var isLoading: Boolean = false
    private val scope = MainScope()
    private var listOfUser = mutableListOf<UserWithProfile>()

    init {
        view.setupPresenter(this)
    }

    override fun requestUserList() {
        if (userListHasLoaded) return
        scope.launch {
            view.showLoading()
            view.hideScreenMesasge()

            mainRepository.loadUserList(object : Listener<List<UserWithProfile>> {
                override fun onSuccess(data: List<UserWithProfile>?) {
                    view.stopLoading()
                    if (data != null) {
                        userListHasLoaded = true
                        listOfUser.addAll(data)
                        view.setUserList(data)
                    } else {
                        view.showToastMessage(requestUserListErrorMessage)
                    }
                }

                override fun onFailed(errorMessage: String) {
                    view.stopLoading()
                    if (noInternetConnectionErrorMessage == errorMessage && !userListHasLoaded) {
                        view.showScreenMessage(errorMessage)
                        return
                    }
                    view.showToastMessage(errorMessage)
                }
            })
        }
    }

    override fun onScroll(visibleItemCount: Int, totalItemCount: Int, pastVisibleItems: Int) {
        if (!isLoading) {
            //  Check if the recyclerview reach the bottom
            if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                val lastItem = listOfUser.last()
                lastItem.user?.id?.let {
                    isLoading = true
                    view.showLoading()
                    loadNewList(it)
                }
            }
        }
    }

    private fun loadNewList(since: Int) {
        scope.launch {
            mainRepository.loadUserList(since, object : Listener<List<UserWithProfile>> {
                override fun onSuccess(data: List<UserWithProfile>?) {
                    isLoading = false
                    view.stopLoading()
                    if (data != null) {
                        listOfUser.addAll(data)
                        view.addNewList(data)
                    } else {
                        view.showToastMessage(requestUserListErrorMessage)
                    }
                }

                override fun onFailed(errorMessage: String) {
                    isLoading = false
                    view.stopLoading()
                    view.showToastMessage(errorMessage)
                }
            })
        }
    }
}