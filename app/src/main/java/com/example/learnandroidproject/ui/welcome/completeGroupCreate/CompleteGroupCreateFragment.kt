package com.example.learnandroidproject.ui.welcome.completeGroupCreate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentCompleteGroupCreateBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.completeGroupCreate.adapter.GroupMembersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompleteGroupCreateFragment : BaseFragment<FragmentCompleteGroupCreateBinding>() {

    @Inject
    lateinit var recyclerAdapter: GroupMembersAdapter

    private val viewModel: CompleteGroupCreateViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_complete_group_create

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val users = welcomeViewModel.getSelectedUsersForGroupChat()
        viewModel.groupMembers = users
        viewModel.fillMembers(users)
        handleViewOption()
        initResultsItemsRecyclerView()
        with(viewModel){
            completeGroupCreatePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.members)
            }
            errorMessagesLiveData.observeNonNull(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
                binding.completeButton.isEnabled = true // Eğer grup oluşturamazsa tekrar deneyebilmesi için butonu aktif eder
            }
            newGroupCreatedLiveData.observeNonNull(viewLifecycleOwner){
                welcomeViewModel.fillNewGroupListResponse(it)
                val navController = findNavController()
                navController.popBackStack(R.id.baseChatRoomsFragment, false)
            }
        }
        adapterListener()
        editTextController()
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.completeButton.setOnClickListener {


            val groupName = binding.groupName.text.toString().trim()
            val result = viewModel.checkField(groupName)

            if (result){
                viewModel.editDatas(groupName)
            }
            binding.completeButton.isEnabled = false // Birden fazla grup oluşturmasın diye
        }
    }

    fun editTextController(){
        binding.groupName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                val trimmedText = inputText.replace("\\s+".toRegex(), " ")

                // Eğer başta boşluk varsa, boşlukları temizleyin
                if (inputText.startsWith(" ")) {
                    val trimText = inputText.trimStart()
                    binding.groupName.setText(trimText)
                    binding.groupName.setSelection(trimText.length) // Cursor'ı doğru konuma getirin
                }
                // Birden fazla boşluk bırakılmasını engeller
                if (inputText != trimmedText) {
                    binding.groupName.setText(trimmedText)
                    binding.groupName.setSelection(trimmedText.length) // Cursor'ı doğru konuma getirin
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    fun adapterListener(){
        recyclerAdapter.setItemClickListener {
            var members = viewModel.groupMembers.toMutableList()
            members.remove(it)

            viewModel.groupMembers = members.toList()
            viewModel.fillMembers(members)
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
}