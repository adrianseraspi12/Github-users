package com.example.githubusers.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.Injection
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.MainRepository
import com.example.githubusers.databinding.ActivityProfileBinding
import kotlinx.coroutines.Dispatchers

const val USER_PROFILE_ARG = "userProfileArg"
const val UPDATED_NOTE_RESULT_ARG = "newNoteResultArg"

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

    fun passResultFromFragmentToPreviousFragment(userWithProfile: UserWithProfile) {
        val resultIntent = Intent()
        resultIntent.putExtra(UPDATED_NOTE_RESULT_ARG, userWithProfile)
        setResult(RESULT_OK, resultIntent)
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
        val fm = supportFragmentManager
        //  Get arguments from last screen
        val userProfile = intent.getSerializableExtra(USER_PROFILE_ARG) as UserWithProfile
        //  Set name on toolbar
        title = userProfile.profile?.name

        //  Initialize presenter and profile fragment
        var profileFragment = fm.findFragmentByTag(ProfileFragment.TAG) as? ProfileFragment
        val mainRepository = MainRepository(
                Injection.provideUserRepository(applicationContext),
                Injection.provideGithubRepository(applicationContext),
                Dispatchers.IO
        )

        //  Initialize fragment and presenter if not created
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance()
            ProfilePresenter(profileFragment, userProfile, mainRepository)
        }

        //  setup fragment view
        fm.beginTransaction().apply {
            replace(binding.profileContainerView.id, profileFragment, ProfileFragment.TAG)
            commit()
        }
    }
}