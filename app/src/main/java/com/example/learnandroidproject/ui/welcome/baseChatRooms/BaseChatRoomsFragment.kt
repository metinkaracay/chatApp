package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentBaseChatRoomsBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseChatRoomsFragment : BaseFragment<FragmentBaseChatRoomsBinding>() {

    private val viewModel: BaseChatRoomsViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_base_chat_rooms

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption()
        with(viewModel){
            baseChatRoomsPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
        viewModel.getLoggedUser()
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
        // Telefonun navigation bar'ında ki geri tuşuna basılmasını kontrol eder
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val socketHandler = SocketHandler
        welcomeViewModel.socketListener(socketHandler,requireContext())
    }

    fun handleViewOption(){
        binding.profile.setOnClickListener {
            welcomeViewModel.goToProfilePage()
            //welcomeViewModel.goToRaceFragment()
        }
        binding.exitButton.setOnClickListener {
            showExitPopUpDialog(4)
        }
        binding.allUsersButton.setOnClickListener {
            welcomeViewModel.goToGenerelChatUsersFragment()
        }
    }

    fun selectedTab(tabId : Int){

        if(tabId == 0){

            binding.tabGroups.textSize = 20f
            binding.tabGroups.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabGroups.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 320f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabPersonal.textSize = 17f
            binding.tabPersonal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabPersonal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)

        }else if(tabId == 1){

            binding.tabPersonal.textSize = 20f
            binding.tabPersonal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabPersonal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 610f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabGroups.textSize = 17f
            binding.tabGroups.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabGroups.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)
        }
    }
    private fun showExitPopUpDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}