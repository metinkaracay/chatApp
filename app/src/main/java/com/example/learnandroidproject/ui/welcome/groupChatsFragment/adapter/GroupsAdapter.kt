package com.example.learnandroidproject.ui.welcome.groupChatsFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.databinding.FriendsUsersItemBinding
import com.example.learnandroidproject.databinding.GroupItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class GroupsAdapter : RecyclerView.Adapter<GroupsAdapter.GroupsItemViewHolder>() {

    private var list: List<GroupInfo> = emptyList()

    private var itemClickListener: ((GroupInfo) -> Unit)? = null
    private var photoIemClickListener: ((String) -> Unit)? = null

    fun setItemClickListener(listener: (GroupInfo) -> Unit) {
        itemClickListener = listener
    }
    fun setPhotoItemClickListener(listener: (String) -> Unit) {
        photoIemClickListener = listener
    }
    fun setItems(page: List<GroupInfo>) {
        list = page
        notifyDataSetChanged()
    }

    fun formattedDate(date: String): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())
        val dateNow = Date()

        if (date.matches(Regex("\\d{2}:\\d{2}:\\d{2}"))) {
            try {
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val messageTime = timeFormat.parse(date)
                val currentTime = timeFormat.parse(timeFormat.format(dateNow))

                val minDifference = abs(currentTime.time - messageTime.time) / (60 * 1000)

                val result = when {
                    minDifference < 1 -> "birkaç saniye önce"
                    minDifference < 60 -> "${minDifference.toInt()} dakika önce"
                    else -> {
                        val hourDifference = minDifference / 60
                        if (hourDifference < 24) {
                            "$hourDifference saat önce"
                        }else{
                            "Bir Gün Önce"
                        }
                    }
                }
                return result
            } catch (e: Exception) {
                println("Geçersiz saat formatı veya hata2: ${e.message}")
                return "Geçersiz"
            }
        }else if (date != "null"){

            try {
                val messageTime = dateFormat.parse(date)

                val minDifference = abs(dateNow.time - messageTime.time) / (60 * 1000)
                val dayDifference = minDifference / (24 * 60)
                val yearDifference = dayDifference / 365

                val result = when {
                    yearDifference >= 1 -> "${yearDifference} yıl önce"
                    dayDifference >= 7 -> "${dayDifference / 7} hafta önce"
                    dayDifference >= 1 -> "$dayDifference gün önce"
                    minDifference >= 60 -> "${minDifference / 60} saat önce"
                    minDifference.toInt() == 0 -> "birkaç saniye önce"
                    else -> "$minDifference dakika önce"
                }

                return result
            } catch (e: Exception) {
                println("Geçersiz tarih formatı veya hata: ${e.message}")
                return "Geçersiz"
            }
        }else{
            return "mesaj yok"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.GroupsItemViewHolder {
        val binding: GroupItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.group_item,
            parent,
            false)
        return GroupsItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GroupsAdapter.GroupsItemViewHolder, position: Int) {
        val group = list[position]

        val elapsedTime = formattedDate(list[position].messageTime)
        holder.itemSelect(position)
        holder.bind(group, elapsedTime)
    }

    inner class GroupsItemViewHolder(private var binding: GroupItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(group : GroupInfo, elapsedTime: String){
            with(binding){
                pageViewState = GroupsItemPageViewState(group,elapsedTime)
            }
        }
        fun itemSelect(position: Int) {
            binding.userItem.setOnClickListener {
                val group = GroupInfo(list[position].groupId,list[position].groupName,list[position].groupPhoto,list[position].messageTime,list[position].lastMessage,null)
                itemClickListener?.invoke(group)
            }
           /*binding.userPhoto.setOnClickListener {
                photoIemClickListener?.invoke(list[position].uPhoto)
            }*/
        }
    }
}