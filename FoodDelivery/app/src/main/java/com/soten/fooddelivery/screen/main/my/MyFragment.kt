package com.soten.fooddelivery.screen.main.my

import com.soten.fooddelivery.databinding.FragmentMyBinding
import com.soten.fooddelivery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    companion object {
        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }

}