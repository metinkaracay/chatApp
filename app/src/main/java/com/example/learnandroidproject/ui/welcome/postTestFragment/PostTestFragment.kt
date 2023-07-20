package com.example.learnandroidproject.ui.welcome.postTestFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.databinding.FragmentPostTestBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostTestFragment : BaseFragment<FragmentPostTestBinding>() {


    private val postViewModel: PostViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_post_test

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()

        with(postViewModel){
            postPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }

    }

    fun handleViewOptions(){

        binding.sendButton.setOnClickListener {
            Log.e("title","${binding.title.text}")
            Log.e("desc","${binding.description.text}")
            Log.e("thum","${binding.thumbnail.text}")
            Log.e("author","${binding.author.text}")

            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val thumbnail = binding.thumbnail.text.toString()
            val author: ArrayList<String> = arrayListOf()

            author.add(binding.author.text.toString())

            val book = BookResponse(title,description,thumbnail,author)
            postViewModel.book1 = book

            postViewModel.readBookData()
        }

    }
}