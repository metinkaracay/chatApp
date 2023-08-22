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
    var socketHandler: SocketHandler? = null
    override fun getLayoutResId(): Int = R.layout.fragment_base_chat_rooms

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption(socketHandler!!)
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
        socketHandler = SocketHandler
        welcomeViewModel.socketListener(socketHandler!!,requireContext())
    }

    fun handleViewOption(socket: SocketHandler){
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

            binding.tabInternet.textSize = 20f
            binding.tabInternet.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabInternet.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 320f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabLocal.textSize = 17f
            binding.tabLocal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabLocal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)

        }else if(tabId == 1){

            binding.tabLocal.textSize = 20f
            binding.tabLocal.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabLocal.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_black)
            binding.scrollLine.x = 610f

            //Diğer sekmenin özelliklerini düzeltir
            binding.tabInternet.textSize = 17f
            binding.tabInternet.setTextColor(ContextCompat.getColor(requireContext(), R.color.register_title_color))
            binding.tabInternet.typeface = ResourcesCompat.getFont(requireContext(), R.font.gotham_book)
        }
    }
    private fun showExitPopUpDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}