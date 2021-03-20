package com.example.githubusers.view.userlist

import android.app.Activity.CONNECTIVITY_SERVICE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
        presenter.requestUserList()
        setupRecyclerView()
        setupSearch()
        setupNetworkConnection()
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

    private var recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = linearLayoutManager.childCount
            val totalItemCount = linearLayoutManager.itemCount
            val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
            presenter.onScroll(visibleItemCount, totalItemCount, pastVisibleItems)
        }

    }

    private var focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        val searchView = view as SearchView
        if (hasFocus) {
            //  This prevents calling for api
            binding.userListRecylerview.clearOnScrollListeners()
        } else {
            if (searchView.query.isEmpty()) {
                binding.userListRecylerview.addOnScrollListener(recyclerViewOnScrollListener)
            }
        }
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            presenter.requestUserList()
        }

    }

    private fun initAdapter() {
        userListAdapter = UserListAdapter {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(USER_PROFILE_ARG, it)
            startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE)
        }
    }

    private fun setupRecyclerView() {
        binding.userListRecylerview.apply {
            layoutManager = linearLayoutManager
            adapter = userListAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun setupSearch() {
        binding.userListSearchView.setOnQueryTextFocusChangeListener(focusChangeListener)
        binding.userListSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userListAdapter.filter.filter(newText)
                return false
            }
        })
        binding.userListSearchView.clearFocus()
        binding.userListSearchView.isFocusable = false
    }

    private fun setupNetworkConnection() {
        val connectivityManager = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
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
        userListAdapter.addData(list)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showScreenMessage(message: String) {
        binding.userListTvMessage.text = message
        binding.userListTvMessage.visibility = View.VISIBLE
    }

    override fun hideScreenMesasge() {
        binding.userListTvMessage.visibility = View.GONE
    }
}