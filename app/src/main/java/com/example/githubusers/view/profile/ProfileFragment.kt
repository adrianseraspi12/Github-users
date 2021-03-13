package com.example.githubusers.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.githubusers.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), ProfileContract.View {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ProfileContract.Presenter

    companion object {

        fun newInstance() = ProfileFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
    }

    override fun showProfileDetails(
        name: String,
        image: String,
        followers: String,
        following: String,
        company: String,
        blog: String,
        notes: String
    ) {
        binding.profileTvInfoName.text = name
        binding.profileTvFollowers.text = followers
        binding.profileTvFollowing.text = following
        binding.profileTvCompany.text = company
        binding.profileTvBlog.text = blog
        binding.profileEtNote.setText(notes)
        Glide.with(this)
            .load(image)
            .centerCrop()
            .into(binding.profileImageview)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}