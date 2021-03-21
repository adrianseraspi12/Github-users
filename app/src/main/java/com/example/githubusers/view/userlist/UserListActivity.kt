package com.example.githubusers.view.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.Injection
import com.example.githubusers.R
import com.example.githubusers.data.main.repository.MainRepository
import kotlinx.coroutines.Dispatchers

class UserListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupFragment()
    }

    private fun setupFragment() {
        val fm = supportFragmentManager
        //  Initialize fragment and presenter
        var userListFragment = fm.findFragmentByTag(UserListFragment.TAG) as? UserListFragment
        val mainRepository = MainRepository(
                Injection.provideUserRepository(applicationContext),
                Injection.provideGithubRepository(applicationContext),
                Dispatchers.IO
        )

        //  Initialize fragment and presenter if not created
        if (userListFragment == null) {
            userListFragment = UserListFragment.newInstance()
            UserListPresenter(userListFragment, mainRepository)
        }
        //  setup fragment view
        fm.beginTransaction().apply {
            replace(R.id.base_root_view, userListFragment, UserListFragment.TAG)
            commit()
        }
    }

}