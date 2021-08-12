package com.soten.fooddelivery.screen.main.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.soten.fooddelivery.data.preference.AppPreferenceManager
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val appPreferenceManager: AppPreferenceManager
) : BaseViewModel() {

    private val _myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)
    val myStateLiveData: LiveData<MyState> = _myStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _myStateLiveData.value = MyState.Loading
        appPreferenceManager.getIdToken()?.let {
            _myStateLiveData.value = MyState.Login(it)
        } ?: run {
            _myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            _myStateLiveData.value = MyState.Success.Registered(
                userName = user.displayName ?: "익명",
                profileImageUri = user.photoUrl
            )
        } ?: run {
            _myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.removeIdToken()
        }
        fetchData()
    }

}