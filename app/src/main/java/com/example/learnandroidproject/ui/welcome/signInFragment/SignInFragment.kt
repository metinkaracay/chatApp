package com.example.learnandroidproject.ui.welcome.signInFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentSignInBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    override fun getLayoutResId(): Int = R.layout.fragment_sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SignInAdapter(childFragmentManager,lifecycle)

        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    fun selectedTab(tabId : Int){

        if(tabId == 0){

            binding.tabAccount.textSize = 24f
            binding.tabAccount.setTextColor(ContextCompat.getColor(requireContext(), R.color.party_call_type_premium_bg_color))
            binding.tabAccount.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabProfile.textSize = 17f
            binding.tabProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabProfile.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)

        }else if(tabId == 1){

            binding.tabProfile.textSize = 24f
            binding.tabProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.party_call_type_premium_bg_color))
            binding.tabProfile.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabAccount.textSize = 17f
            binding.tabAccount.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabAccount.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)
        }
    }
}