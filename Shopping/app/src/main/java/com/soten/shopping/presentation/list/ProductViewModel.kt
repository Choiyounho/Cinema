package com.soten.shopping.presentation.list

import androidx.lifecycle.viewModelScope
import com.soten.shopping.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch {  }

}