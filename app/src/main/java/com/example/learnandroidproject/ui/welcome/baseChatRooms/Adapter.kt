package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.GeneralChatUsersFragment
import com.example.learnandroidproject.ui.welcome.localChatUsersFragment.LocalChatUsersFragment

class Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        Log.e("poss","$position")
        return when(position){
            0->{
                GeneralChatUsersFragment()

            }
            1->{
                LocalChatUsersFragment()
            }
            else->{
                GeneralChatUsersFragment()
            }
        }
    }
}