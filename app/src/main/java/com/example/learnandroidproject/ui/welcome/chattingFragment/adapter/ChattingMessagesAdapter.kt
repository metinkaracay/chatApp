package com.example.learnandroidproject.ui.welcome.chattingFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.databinding.MessageItemBinding
import java.text.SimpleDateFormat
import java.util.*

class ChattingMessagesAdapter : RecyclerView.Adapter<ChattingMessagesAdapter.MessageItemViewHolder>() {

    private var list: List<MessageItem> = emptyList()
    private var uId: Int? = null
    fun setItems(page: List<MessageItem>,userId: Int) {
        uId = userId
        list = page
        notifyItemRangeInserted(list.size,10)
    }

    fun formattedDate(date: String): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())

        if (date.matches(Regex("\\d{2}:\\d{2}"))) {
            return date
        }

        if (date != "null"){
            try {
                val messageTime = dateFormat.parse(date)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val currentTime = timeFormat.format(messageTime)

                return currentTime
            } catch (e: Exception) {
                println("Geçersiz tarih formatı veya hata: ${e.message}")
                return "Geçersiz"
            }
        }else{
            return "mesaj yok"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingMessagesAdapter.MessageItemViewHolder{
        val binding: MessageItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.message_item,
            parent,
            false)
        return MessageItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChattingMessagesAdapter.MessageItemViewHolder, position: Int) {
        val message = list[position]
        val messageTime = formattedDate(list[position].messageTime)
        list[position].messageTime = messageTime
        holder.bind(message)
    }

    inner class MessageItemViewHolder(private var binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(messages : MessageItem){
            with(binding){
                pageViewState = MessageItemPageViewState(messages,uId)
            }
        }
    }
}