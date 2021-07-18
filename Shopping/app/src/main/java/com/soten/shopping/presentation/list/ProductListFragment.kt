package com.soten.shopping.presentation.list

import com.soten.shopping.databinding.FragmentProductListBinding
import com.soten.shopping.presentation.BaseFragment
import org.koin.android.ext.android.inject

internal class ProductListFragment: BaseFragment<ProductViewModel, FragmentProductListBinding>() {

    override val viewModel by inject<ProductViewModel>()

    override fun getViewBinding() = FragmentProductListBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        const val TAG = "ProductListFragment"
    }
}
