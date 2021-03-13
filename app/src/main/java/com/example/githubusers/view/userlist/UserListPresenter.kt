package com.example.githubusers.view.userlist

import android.os.Handler
import android.os.Looper
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository

class UserListPresenter(
    private val view: UserListContract.View,
    private val mainRepository: IMainRepository
) :
    UserListContract.Presenter {

    init {
        view.setupPresenter(this)
    }

    override fun setup() {
        view.showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            view.setUserList(getFakeData())
        }, 2000)
    }

    private fun getFakeData(): List<UserWithProfile> {
        return listOf(
            UserWithProfile(
                LocalUser(
                    0, "john",
                    "https://www.jd.com/png",
                    "",
                ),
                LocalProfile(
                    0, 0,
                    "John Doe",
                    "",
                    12,
                    23,
                    "Apple",
                    ""
                )
            ),

            UserWithProfile(
                LocalUser(
                    0, "john",
                    "https://www.jd.com/png",
                    "",
                ),
                LocalProfile(
                    0, 0,
                    "Jane Doe",
                    "I'm Jane Doe Hello",
                    5,
                    100,
                    "Orange",
                    ""
                )
            ),

            UserWithProfile(
                LocalUser(
                    0, "john",
                    "https://www.jd.com/png",
                    "",
                ),
                LocalProfile(
                    0, 0,
                    "John Doe",
                    "Hello",
                    100,
                    23,
                    "Apple",
                    ""
                )
            ),
        )
    }
}