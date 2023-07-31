package com.example.learnandroidproject.ui.welcome.localChatUsersFragment

import android.os.Bundle
import android.view.View
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentLocalChatUsersBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class LocalChatUsersFragment : BaseFragment<FragmentLocalChatUsersBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_local_chat_users

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}