package com.soten.shopping.presentation.main

import androidx.lifecycle.viewModelScope
import com.soten.shopping.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MainViewModel: BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch {

    }

}