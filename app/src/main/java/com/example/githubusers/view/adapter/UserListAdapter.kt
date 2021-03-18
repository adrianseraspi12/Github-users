package com.example.githubusers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.databinding.ItemUsersBinding

class UserListAdapter(private val onClickItemListener: (UserWithProfile) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var data = mutableListOf<UserWithProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUsersBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userWithProfile = data[position]
        holder.bind(userWithProfile)
        holder.binding.usersRootContainer.setOnClickListener {
            onClickItemListener.invoke(userWithProfile)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<UserWithProfile>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(newData: List<UserWithProfile>) {
        this.data.addAll(newData)
        notifyDataSetChanged()
    }

    fun update(userWithProfile: UserWithProfile) {
        val user = userWithProfile.user ?: LocalUser()
        //  Find user from the list and update the note
        data.find { it.user?.id == user.id }?.user?.notes = user.notes
        //  Get the index and notify adapter by index
        val index = data.indexOfFirst { it.user?.id == user.id }
        notifyItemChanged(index)
    }

    class ViewHolder(val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userWithProfile: UserWithProfile) {
            binding.usersTvUsername.text = userWithProfile.user?.username
            binding.usersTvDetails.text = userWithProfile.profile?.bio

            val notes = userWithProfile.user?.notes ?: ""
            if (notes.isNotEmpty()) {
                binding.usersIvNotes.visibility = View.VISIBLE
            } else {
                binding.usersIvNotes.visibility = View.GONE
            }

            Glide.with(binding.root)
                .load(userWithProfile.user?.image)
                .centerCrop()
                .dontAnimate()
                .into(binding.usersIvProfile)
        }
    }
}