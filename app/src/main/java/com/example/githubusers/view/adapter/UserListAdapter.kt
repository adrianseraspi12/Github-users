package com.example.githubusers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.databinding.ItemSpinnerBinding
import com.example.githubusers.databinding.ItemUsersBinding
import java.util.*

class UserListAdapter(private val onClickItemListener: (UserWithProfile) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private val dataType = 0
    private val spinnerType = 1

    var listOfUserWithProfile = mutableListOf<UserWithProfile>()
    var filteredListOfUserWithProfile = mutableListOf<UserWithProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == dataType) {
            val binding = ItemUsersBinding.inflate(layoutInflater, parent, false)
            UserWithProfileViewHolder(binding)
        } else {
            val binding = ItemSpinnerBinding.inflate(layoutInflater, parent, false)
            SpinnerViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SpinnerViewHolder) return
        val viewHolder = holder as UserWithProfileViewHolder
        val userWithProfile = filteredListOfUserWithProfile[position]
        viewHolder.bind(userWithProfile)
        viewHolder.binding.usersRootContainer.setOnClickListener {
            onClickItemListener.invoke(userWithProfile)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val lastUserWithProfile = filteredListOfUserWithProfile[position]
        //  Return spinnerType if the item has null values
        return if (lastUserWithProfile.user == null && lastUserWithProfile.profile == null) {
            spinnerType
        } else {
            dataType
        }
    }

    override fun getItemCount(): Int {
        return filteredListOfUserWithProfile.size
    }

    fun setData(data: List<UserWithProfile>) {
        this.listOfUserWithProfile = data.toMutableList()
        this.filteredListOfUserWithProfile = data.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(newData: List<UserWithProfile>) {
        this.listOfUserWithProfile.addAll(newData)
        this.filteredListOfUserWithProfile.addAll(newData)
        notifyDataSetChanged()
    }

    fun update(userWithProfile: UserWithProfile) {
        val user = userWithProfile.user ?: LocalUser()
        //  Find user from the list and update the note
        listOfUserWithProfile.find { it.user?.id == user.id }?.user?.notes = user.notes
        //  Get the index and notify adapter by index
        val index = listOfUserWithProfile.indexOfFirst { it.user?.id == user.id }
        //  Update note from filter list
        this.filteredListOfUserWithProfile[index].user?.notes = user.notes
        notifyItemChanged(index)
    }

    fun showLoading() {
        //  Add an null value to set it as spinner
        filteredListOfUserWithProfile.add(UserWithProfile())
        notifyItemInserted(filteredListOfUserWithProfile.size)
    }

    fun stopLoading() {
        //  Remove the null value that stand as spinner
        filteredListOfUserWithProfile.removeLast()
        notifyItemRemoved(filteredListOfUserWithProfile.size)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0?.toString()?.toLowerCase(Locale.ROOT) ?: ""
                if (charSearch.isEmpty()) {
                    //  Reset the data
                    filteredListOfUserWithProfile = listOfUserWithProfile
                } else {
                    val resultFilterList = mutableListOf<UserWithProfile>()
                    for (item in listOfUserWithProfile) {
                        //  Get the username and notes
                        val username = item.user?.username?.toLowerCase(Locale.ROOT) ?: ""
                        val notes = item.user?.notes?.toLowerCase(Locale.ROOT) ?: ""

                        //  Add the item to the filter list when character contains
                        //  with username or notes
                        if (username.contains(charSearch) || notes.contains(charSearch)) {
                            resultFilterList.add(item)
                        }
                    }

                    filteredListOfUserWithProfile = resultFilterList
                }

                val filterResult = FilterResults()
                filterResult.values = filteredListOfUserWithProfile
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredListOfUserWithProfile = p1?.values as MutableList<UserWithProfile>
                notifyDataSetChanged()
            }
        }
    }

    class UserWithProfileViewHolder(val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {

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

    class SpinnerViewHolder(binding: ItemSpinnerBinding) : RecyclerView.ViewHolder(binding.root)

}