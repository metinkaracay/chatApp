package com.example.learnandroidproject.ui.welcome.createGroupFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.databinding.UserItemForCreateGroupBinding

class CreateGroupUsersAdapter : RecyclerView.Adapter<CreateGroupUsersAdapter.UsersItemViewHolder>() {

    private var list: List<UserInfo> = emptyList()
    private var selectedUsersList: MutableList<UserInfo> = arrayListOf()
    private var selectedUsersIdList: MutableList<Int> = arrayListOf()

    private var itemClickListener: ((List<UserInfo>) -> Unit)? = null
    private var isItemSelectedListener: ((Boolean) -> Unit)? = null

    fun setItemClickListener(listener: (List<UserInfo>) -> Unit) {
        itemClickListener = listener
    }

    fun setIsItemSelectedListener(listener: (Boolean) -> Unit) {
        isItemSelectedListener = listener
    }

    fun getSelectedUserList(): List<UserInfo>{
        return selectedUsersList
    }

    fun setItems(page: List<UserInfo>) {
        list = page
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateGroupUsersAdapter.UsersItemViewHolder {
        val binding: UserItemForCreateGroupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.user_item_for_create_group,
            parent,
            false)
        return UsersItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun selectUser(user: UserInfo,position: Int){
        Log.e("seçilen kullanıcı","$user")
        val existingIndex = selectedUsersIdList.indexOfFirst { it == user.uId }
        val existingIndexUser = selectedUsersList.indexOfFirst { it == user }

        if (existingIndex != -1) {
            selectedUsersIdList.removeAt(existingIndex)
            selectedUsersList.removeAt(existingIndexUser)
        } else {
            selectedUsersIdList.add(user.uId)
            selectedUsersList.add(user)
        }
        notifyItemRangeChanged(position,1)
    }

    override fun onBindViewHolder(holder: UsersItemViewHolder, position: Int) {
        val user = list[position]

        holder.bind(user)
        holder.itemSelect(position)

        if (selectedUsersList.isNotEmpty()){
            isItemSelectedListener?.invoke(true)
        }else{
            isItemSelectedListener?.invoke(false)
        }
    }

    inner class UsersItemViewHolder(private var binding: UserItemForCreateGroupBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(user : UserInfo){
            with(binding){
                pageViewState = CreateGroupUserItemPageViewState(user,selectedUsersIdList.toList())
            }
        }
        fun itemSelect(position: Int) {
            binding.userItem.setOnClickListener {
                val user = UserInfo(list[position].uId,list[position].uName,list[position].uStatu,list[position].uPhoto,null,null,true)
                selectUser(user,position)
                //itemClickListener?.invoke()
            }
        }
        fun isItemSelected(isSelected: Boolean){
            isItemSelectedListener?.invoke(isSelected)
        }
    }

}