package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentBaseChatRoomsBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class BaseChatRoomsFragment : BaseFragment<FragmentBaseChatRoomsBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_base_chat_rooms

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = Adapter(childFragmentManager,lifecycle)

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

            binding.tabInternet.textSize = 24f
            binding.tabInternet.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabInternet.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 360f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabLocal.textSize = 17f
            binding.tabLocal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabLocal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)

        }else if(tabId == 1){

            binding.tabLocal.textSize = 24f
            binding.tabLocal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabLocal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 588f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabInternet.textSize = 17f
            binding.tabInternet.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabInternet.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)
        }
    }
}