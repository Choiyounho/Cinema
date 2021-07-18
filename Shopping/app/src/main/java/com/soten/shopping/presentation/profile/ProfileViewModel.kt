package com.soten.shopping.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.soten.shopping.data.preference.PreferenceManager
import com.soten.shopping.domain.DeleteOrderedProductListUseCase
import com.soten.shopping.domain.GetOrderedProductListUseCase
import com.soten.shopping.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager,
    private val getOrderedProductListUseCase: GetOrderedProductListUseCase,
    private val deleteOrderedProductListUseCase: DeleteOrderedProductListUseCase
) : BaseViewModel() {

    private var _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.UnInitialized)
    val profileStateLiveData: LiveData<ProfileState> = _profileStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            ProfileState.Loading
        )
        preferenceManager.getIdToken()?.let {
            setState(
                ProfileState.Login(it)
            )
        } ?: run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            setState(
                ProfileState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl,
                    getOrderedProductListUseCase()
                )
            )
        } ?: run {
            setState(
                ProfileState.Success.NotRegistered
            )
        }
    }

    fun signOut() = viewModelScope.launch {
        preferenceManager.removedToken()
        deleteOrderedProductListUseCase()
        fetchData()
    }

    fun saveToken(idToken: String) {
        preferenceManager.putIdToken(idToken)
        fetchData()
    }

    private fun setState(state: ProfileState) {
        _profileStateLiveData.postValue(state)
    }
}
