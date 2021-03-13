package com.example.githubusers.view.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.MainRepository
import com.example.githubusers.databinding.ActivityProfileBinding

const val USER_PROFILE_ARG = "userProfileArg"

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setupToolbar()
        setupFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindView() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.profileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFragment() {
        //  Get arguments from last screen
        val userProfile = intent.getSerializableExtra(USER_PROFILE_ARG) as UserWithProfile
        //  Set name on toolbar
        title = userProfile.profile?.name

        //  Initialize presenter and profile fragment
        val profileFragment = ProfileFragment.newInstance()
        ProfilePresenter(profileFragment, userProfile, MainRepository())

        //  setup fragment view
        val fm = supportFragmentManager
        fm.beginTransaction().apply {
            replace(binding.profileContainerView.id, profileFragment)
            commit()
        }
    }
}