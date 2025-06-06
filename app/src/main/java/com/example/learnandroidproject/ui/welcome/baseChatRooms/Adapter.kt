package com.example.learnandroidproject.ui.welcome.baseChatRooms

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.FriendsChatUsersFragment
import com.example.learnandroidproject.ui.welcome.groupChatsFragment.GroupChatsFragment

class Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle)  {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> GroupChatsFragment()
            1 -> FriendsChatUsersFragment()
            else -> GroupChatsFragment()
        }
    }
}