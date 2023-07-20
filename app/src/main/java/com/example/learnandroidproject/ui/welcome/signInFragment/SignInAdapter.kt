package com.example.learnandroidproject.ui.welcome.signInFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnandroidproject.ui.welcome.createAccountFragment.CreateAccountFragment
import com.example.learnandroidproject.ui.welcome.createProfileFragment.CreateProfileFragment

class SignInAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                CreateAccountFragment()

            }
            1->{
                CreateProfileFragment()
            }
            else->{
                CreateAccountFragment()
            }
        }

    }
}