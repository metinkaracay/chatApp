package com.example.learnandroidproject.ui.welcome.groupChattingFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.databinding.GroupMessageItemBinding
import com.example.learnandroidproject.databinding.MessageItemBinding
import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.MessageItemPageViewState

class GroupChattingMessagesAdapter : RecyclerView.Adapter<GroupChattingMessagesAdapter.MessageItemViewHolder>() {

    private var list: List<MessageItem> = emptyList()
    private var loggedUserId: Int? = null

    fun setItems(page: List<MessageItem>,userId: Int) {
        loggedUserId = userId
        list = page
        notifyItemRangeInserted(list.size,10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChattingMessagesAdapter.MessageItemViewHolder {
        val binding: GroupMessageItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_message_item,
            parent,
            false)
        return MessageItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupChattingMessagesAdapter.MessageItemViewHolder, position: Int) {
        val message = list[position]

        holder.bind(message)
    }

    inner class MessageItemViewHolder(private var binding: GroupMessageItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(messages : MessageItem){
            with(binding){
                pageViewState = GroupMessageItemPageViewState(messages,loggedUserId!!)
            }
        }
    }
}