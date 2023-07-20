package com.example.learnandroidproject.ui.welcome.postTestFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _postPageViewStateLiveData: MutableLiveData<PostPageViewState> = MutableLiveData()

    val postPageViewStateLiveData: LiveData<PostPageViewState> = _postPageViewStateLiveData

    var book1:BookResponse = BookResponse("","","", arrayListOf())

    init {
        fetchData()
    }

    private fun fetchData(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.book(book1)
        }
    }

    fun readBookData(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.book(book1)
            Log.e("test","${book1.author}")
        }
    }
}