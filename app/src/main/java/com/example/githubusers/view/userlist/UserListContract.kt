package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.UserWithProfile

interface UserListContract {

    interface View {

        fun setupPresenter(presenter: Presenter)

        fun setUserList(list: List<UserWithProfile>)

        fun showLoading()

        fun stopLoading()

        fun addNewList(list: List<UserWithProfile>)

        fun showToastMessage(message: String)

    }

    interface Presenter {

        fun setup()

        fun onScroll(visibleItemCount: Int, totalItemCount: Int, pastVisibleItems: Int)

    }

}