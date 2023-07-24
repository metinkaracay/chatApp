package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentGeneralChatUsersBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class GeneralChatUsersFragment : BaseFragment<FragmentGeneralChatUsersBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_general_chat_users

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}