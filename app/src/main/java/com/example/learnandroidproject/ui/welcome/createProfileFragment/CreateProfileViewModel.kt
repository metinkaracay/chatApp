package com.example.learnandroidproject.ui.welcome.createProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.data.remote.model.dating.response.userResponse.UserResponse
import com.example.learnandroidproject.ui.base.BaseViewModel

class CreateProfileViewModel: BaseViewModel() {

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    fun postUser(userName: String?,email:String?,password:String?, firstName:String?, lastName:String?){
        val user: UserResponse = UserResponse(userName,email,password,firstName,lastName)
        Log.e("test","$user")
    }

    fun checkMessage(userName: String,email:String,password:String, firstName:String, lastName:String){

        val arrayList = arrayListOf<String>(userName,email,password,firstName,lastName)

        for (i in 0 until arrayList.size){
            Log.e("eleman","${arrayList[i]}")

            if(arrayList[i].isNotEmpty() && arrayList[i].isNotBlank()){
                postUser(userName,email,password, firstName, lastName)
            }else{
                _errorMessageLiveData.value = "Boş Bırakılamaz"
            }

        }
    }
}