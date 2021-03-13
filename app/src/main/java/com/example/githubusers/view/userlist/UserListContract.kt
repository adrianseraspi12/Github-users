package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.UserWithProfile

interface UserListContract {

    interface View {

        fun setupPresenter(presenter: Presenter)

        fun setUserList(list: List<UserWithProfile>)

        fun showLoading()

    }

    interface Presenter {

        fun setup()

    }

}