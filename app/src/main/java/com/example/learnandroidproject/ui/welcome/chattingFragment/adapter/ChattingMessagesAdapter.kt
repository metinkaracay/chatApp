package com.example.learnandroidproject.ui.welcome.chattingFragment.adapter

import android.util.Log
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
    private var messageTime = ""
    fun setItems(page: List<MessageItem>,loggedUserId: Int) {
        uId = loggedUserId
        list = page
        notifyItemRangeInserted(list.size,10)
    }

    fun formattedDate(timestamp: Long): String{
        val date = Date(timestamp)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(date)
        // TODO hata çıkmazsa sil
        /*val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())
        val format = dateFormat.format(date)
        Log.e("işlenenDateee","$format")

        if (format.matches(Regex("\\d{2}:\\d{2}"))) {
            return format
        }

        if (timestamp != null){
            try {
                //val messageTime = dateFormat.parse(format)

                Log.e("işlenenDateeeif","$format")
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val currentTime = timeFormat.format(format)

                Log.e("işlenenDateeeif2","$currentTime")

                return currentTime
            } catch (e: Exception) {
                println("Geçersiz tarih formatı veya hata: ${e.message}")
                return "Geçersiz"
            }
        }else{
            return "mesaj yok"
        }*/
        return formattedTime
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
        if (isTimestampFormat(list[position].messageTime)) { //Veriyi görüntüde değiştirip backend'e gönderirken sıkıntı olmasın diye sadece görüntüde düzeltmek için kontrol
            messageTime = formattedDate(list[position].messageTime.toLong())
        }
        list[position].messageTime = messageTime
        holder.bind(message)
    }
    fun isTimestampFormat(input: String): Boolean {
        return input.matches(Regex("\\d{13}"))
    }

    inner class MessageItemViewHolder(private var binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(messages : MessageItem){
            with(binding){
                pageViewState = MessageItemPageViewState(messages,uId!!)
            }
        }
    }
}