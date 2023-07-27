package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentGeneralChatUsersBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter.GeneralUsersAdapter
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class GeneralChatUsersFragment : BaseFragment<FragmentGeneralChatUsersBinding>() {

    @Inject
    lateinit var recyclerAdapter: GeneralUsersAdapter

    private val viewModel: GeneralChatUsersViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_general_chat_users

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerAdapter = GeneralUsersAdapter()
        initResultsItemsRecyclerView()
        with(viewModel){
            generalChatUsersPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.users)
            }
        }
        recyclerAdapter.setItemClickListener{
            welcomeViewModel.fillUserInfoData(it.uId,it.uName,it.uStatu,it.uPhoto)
            welcomeViewModel.goToChattingPage()
        }
        recyclerAdapter.setPhotoItemClickListener {
            welcomeViewModel.fillClickedUserPhoto(it)
            Log.e("deffoto","$it")
            showUserProfilePhotoDialog(3)
        }
    }

    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = recyclerAdapter
        }
    }
    private fun showUserProfilePhotoDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}