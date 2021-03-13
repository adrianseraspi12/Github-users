package com.example.githubusers.view.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.R
import com.example.githubusers.data.main.repository.MainRepository

class UserListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupFragment()
    }

    private fun setupFragment() {
        //  Initialize fragment and presenter
        val userListFragment = UserListFragment.newInstance()
        UserListPresenter(userListFragment, MainRepository())

        //  setup fragment view
        val fm = supportFragmentManager
        fm.beginTransaction().apply {
            replace(R.id.base_root_view, userListFragment)
            commit()
        }
    }

}