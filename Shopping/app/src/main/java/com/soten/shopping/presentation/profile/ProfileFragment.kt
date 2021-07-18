package com.soten.shopping.presentation.profile

import com.soten.shopping.databinding.FragmentProfileBinding
import com.soten.shopping.presentation.BaseFragment
import org.koin.android.ext.android.inject

internal class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel by inject<ProfileViewModel>()

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}