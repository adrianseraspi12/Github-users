package com.example.githubusers.view.userlist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.databinding.FragmentUserListBinding
import com.example.githubusers.view.adapter.UserListAdapter
import com.example.githubusers.view.profile.ProfileActivity
import com.example.githubusers.view.profile.UPDATED_NOTE_RESULT_ARG
import com.example.githubusers.view.profile.USER_PROFILE_ARG


const val UPDATE_NOTE_REQUEST_CODE = 200

class UserListFragment : Fragment(), UserListContract.View {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: UserListContract.Presenter
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        fun newInstance() = UserListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setup()
        setupRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UPDATE_NOTE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val userWithProfile =
                        data?.getSerializableExtra(UPDATED_NOTE_RESULT_ARG) as UserWithProfile
                    userListAdapter.update(userWithProfile)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        userListAdapter = UserListAdapter {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(USER_PROFILE_ARG, it)
            startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE)
        }
    }

    var isLoading = false

    private fun setupRecyclerView() {
        binding.userListRecylerview.apply {
            layoutManager = linearLayoutManager
            adapter = userListAdapter
        }
        binding.userListRecylerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                presenter.onScroll(visibleItemCount, totalItemCount, pastVisibleItems)
            }
        })

    }

    override fun setupPresenter(presenter: UserListContract.Presenter) {
        this.presenter = presenter
    }

    override fun setUserList(list: List<UserWithProfile>) {
        //  Move progressbar at the bottom
        val params: ConstraintLayout.LayoutParams =
            binding.userListProgressbar.layoutParams as ConstraintLayout.LayoutParams
        params.verticalBias = 1f
        binding.userListProgressbar.layoutParams = params
        userListAdapter.setData(list)

    }

    override fun showLoading() {
        binding.userListProgressbar.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        binding.userListProgressbar.visibility = View.GONE
    }

    override fun addNewList(list: List<UserWithProfile>) {
        isLoading = false
        userListAdapter.addData(list)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}