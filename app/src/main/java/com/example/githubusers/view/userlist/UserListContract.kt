package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.UserWithProfile

interface UserListContract {

    interface View {

        fun setupPresenter(presenter: Presenter)

        fun setUserList(list: List<UserWithProfile>)

        fun showLoading()

        fun stopLoading()

        fun showLoadMoreLoading()

        fun hideLoadMoreLoading()

        fun addNewList(list: List<UserWithProfile>)

        fun showToastMessage(message: String)

        fun showScreenMessage(message: String)

        fun hideScreenMessage()
    }

    interface Presenter {

        fun requestUserList()

        fun onScroll(visibleItemCount: Int, totalItemCount: Int, pastVisibleItems: Int)

    }

}