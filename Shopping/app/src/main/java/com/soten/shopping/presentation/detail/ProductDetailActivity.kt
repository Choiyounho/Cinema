package com.soten.shopping.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.soten.shopping.databinding.ActivityProductDetailBinding
import com.soten.shopping.extensions.load
import com.soten.shopping.extensions.loadCenterCrop
import com.soten.shopping.extensions.toast
import com.soten.shopping.presentation.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class ProductDetailActivity : BaseActivity<ProductDetailViewModel, ActivityProductDetailBinding>() {

    override val viewModel by inject<ProductDetailViewModel> {
        parametersOf(
            intent.getLongExtra(PRODUCT_ID_KEY, -1)
        )
    }

    override fun getViewBinding() = ActivityProductDetailBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.productDetailStateLiveData.observe(this) {
        when (it) {
            is ProductDetailState.UnInitialized -> initViews()
            is ProductDetailState.Loading -> handleLoading()
            is ProductDetailState.Success -> handleSuccess(it)
            is ProductDetailState.Error -> handleError()
            is ProductDetailState.Order -> handleOrder()
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }

        orderButton.setOnClickListener {
            viewModel.orderProduct()
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: ProductDetailState.Success) = with(binding) {
        progressBar.isGone = true
        val product = state.productEntity
        title = product.productName
        productCategoryTextView.text = product.productType
        productImageView.loadCenterCrop(product.productImage, 8f)
        productPriceTextView.text = "${product.productPrice}원"
        introductionImageView.load(state.productEntity.productImage)
    }

    private fun handleError() {
        toast("제품 정보를 불러올 수 없습니다.")
        finish()
    }

    private fun handleOrder() {
        setResult(PRODUCT_ORDERED_RESULT_CODE)
        toast("성공적으로 주문이 완료되었습니다.")
        finish()
    }

    companion object {
        const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
        const val PRODUCT_ORDERED_RESULT_CODE = 109

        fun newIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, productId)
            }
    }
}