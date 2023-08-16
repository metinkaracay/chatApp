package com.example.learnandroidproject.ui.welcome.chatInfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember
import com.example.learnandroidproject.databinding.MembersItemBinding

class MembersAdapter : RecyclerView.Adapter<MembersAdapter.MemberItemViewHolder>() {

    private var list: List<GroupMember> = emptyList()

    fun setItems(page: List<GroupMember>) {
        list = page
        notifyItemRangeInserted(list.size,10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersAdapter.MemberItemViewHolder {
        val binding: MembersItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.members_item,
            parent,
            false)
        return MemberItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MembersAdapter.MemberItemViewHolder, position: Int) {
        val member = list[position]

        holder.bind(member)
    }

    inner class MemberItemViewHolder(private var binding: MembersItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(member : GroupMember){
            with(binding){
                pageViewState = MembersPageViewState(member)
            }
        }
    }
}