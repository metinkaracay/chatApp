package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.databinding.UsersItemBinding

class GeneralUsersAdapter : RecyclerView.Adapter<GeneralUsersAdapter.UsersItemViewHolder>() {

    private var list: List<UserInfo> = emptyList()

    private var itemClickListener: ((UserInfo) -> Unit)? = null
    private var photoIemClickListener: ((String) -> Unit)? = null

    fun setItemClickListener(listener: (UserInfo) -> Unit) {
        itemClickListener = listener
    }
    fun setPhotoItemClickListener(listener: (String) -> Unit) {
        photoIemClickListener = listener
    }
    fun setItems(page: List<UserInfo>) {
        // Yeni mesajı göndermeden önceki ve gönderdikte sonraki liste boyutlarını aldık
        val previousItemCount = list.size
        list = page
        val newItemCount = list.size
        // Yeni mesajı ekleyeceğimiz yeri belirledik
        notifyItemRangeInserted(previousItemCount, newItemCount - previousItemCount)
        // Son mesajdan iki önceki mesajdan itibaren sondan bir önceki mesaja kadar güncelledik
        notifyItemRangeChanged(previousItemCount-1, newItemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralUsersAdapter.UsersItemViewHolder {
        val binding: UsersItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.users_item,
            parent,
            false)
        return UsersItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GeneralUsersAdapter.UsersItemViewHolder, position: Int) {
        val user = list[position]

        holder.bind(user)
        holder.itemSelect(position)
    }

    inner class UsersItemViewHolder(private var binding: UsersItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(users : UserInfo){
            with(binding){
                pageViewState = GeneralUsersItemPageViewState(users)
            }
        }
        fun itemSelect(position: Int) {
            binding.userItem.setOnClickListener {
                val user = UserInfo(list[position].uId,list[position].uName,list[position].uStatu,list[position].uPhoto)
                itemClickListener?.invoke(user)
            }
            binding.userPhoto.setOnClickListener {
                photoIemClickListener?.invoke(list[position].uPhoto)
            }
        }
    }
}