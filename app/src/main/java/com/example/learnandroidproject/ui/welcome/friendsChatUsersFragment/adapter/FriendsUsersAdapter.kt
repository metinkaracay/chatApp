package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.databinding.FriendsUsersItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class FriendsUsersAdapter : RecyclerView.Adapter<FriendsUsersAdapter.UsersItemViewHolder>() {

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsUsersAdapter.UsersItemViewHolder {
            val binding: FriendsUsersItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.friends_users_item,
            parent,
            false)
        return UsersItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FriendsUsersAdapter.UsersItemViewHolder, position: Int) {
        val user = list[position]

        val elapsedTime = formattedDate(list[position].elapsedTime.toString())
        holder.bind(user,elapsedTime)
        holder.itemSelect(position)
    }

    inner class UsersItemViewHolder(private var binding: FriendsUsersItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(users : UserInfo,elapsedTime: String){
            with(binding){
                pageViewState = FriendsUsersItemPageViewState(users,elapsedTime)
            }
        }
        fun itemSelect(position: Int) {
            binding.userItem.setOnClickListener {
                val user = UserInfo(list[position].uId,list[position].uName,list[position].uStatu,list[position].uPhoto,null,null,true)
                itemClickListener?.invoke(user)
            }
            binding.userPhoto.setOnClickListener {
                photoIemClickListener?.invoke(list[position].uPhoto)
            }
        }
    }
}