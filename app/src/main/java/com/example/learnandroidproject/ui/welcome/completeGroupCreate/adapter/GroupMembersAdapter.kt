package com.example.learnandroidproject.ui.welcome.completeGroupCreate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.databinding.GroupMemberBinding

class GroupMembersAdapter : RecyclerView.Adapter<GroupMembersAdapter.MemberItemViewHolder>() {

    private var list: List<UserInfo> = emptyList()
    private var itemClickListener: ((UserInfo) -> Unit)? = null

    fun setItemClickListener(listener: (UserInfo) -> Unit) {
        itemClickListener = listener
    }

    fun setItems(users: List<UserInfo>) {
        list = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersAdapter.MemberItemViewHolder {
        val binding: GroupMemberBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_member,
            parent,
            false)
        return MemberItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupMembersAdapter.MemberItemViewHolder, position: Int) {
        val user = list[position]

        holder.bind(user)
        holder.itemSelect(position)
    }

    inner class MemberItemViewHolder(private var binding: GroupMemberBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(member : UserInfo){
            with(binding){
                pageViewState = GroupMemberPageViewState(member)
            }
        }
        fun itemSelect(position: Int) {
            binding.crossIcon.setOnClickListener {
                val user = UserInfo(list[position].uId,list[position].uName,list[position].uStatu,list[position].uPhoto,null,null,true)
                itemClickListener?.invoke(user)
            }

        }
    }
}