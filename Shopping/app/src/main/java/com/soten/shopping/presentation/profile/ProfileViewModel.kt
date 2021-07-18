package com.soten.shopping.presentation.profile

import androidx.lifecycle.viewModelScope
import com.soten.shopping.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProfileViewModel : BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch { }
}
