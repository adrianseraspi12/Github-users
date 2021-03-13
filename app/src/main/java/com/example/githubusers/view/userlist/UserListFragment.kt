package com.example.githubusers.view.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.databinding.FragmentUserListBinding
import com.example.githubusers.view.adapter.UserListAdapter

class UserListFragment : Fragment(), UserListContract.View {

    private var _binding: FragmentUserListBinding? = null
    private val userListAdapter = UserListAdapter()
    private val binding get() = _binding!!
    private lateinit var presenter: UserListContract.Presenter

    companion object {
        fun newInstance() = UserListFragment()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setupPresenter(presenter: UserListContract.Presenter) {
        this.presenter = presenter
    }

    override fun setUserList(list: List<UserWithProfile>) {
        binding.userListProgressbar.visibility = View.GONE
        userListAdapter.updateData(list)
    }

    override fun showLoading() {
//        binding.userListProgressbar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        binding.userListRecylerview.apply {
            layoutManager = linearLayoutManager
            adapter = userListAdapter
        }
    }
}