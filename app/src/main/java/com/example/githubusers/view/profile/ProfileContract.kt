package com.example.githubusers.view.profile

interface ProfileContract {

    interface View {

        fun setPresenter(presenter: Presenter)

        fun showProfileDetails(name: String,
                               image: String,
                               followers: String,
                               following: String,
                               company: String,
                               blog: String,
                               notes: String)

        fun showToastMessage(message: String)
    }

    interface Presenter {

        fun setup()

        fun saveNote(notes: String)

    }

}