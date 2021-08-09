package com.soten.fooddelivery.screen.main.home.restaurant.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.appbar.AppBarLayout
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.databinding.ActivityRestaurantDetailBinding
import com.soten.fooddelivery.extensions.fromDpToPx
import com.soten.fooddelivery.extensions.load
import com.soten.fooddelivery.screen.base.BaseActivity
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListFragment.Companion.RESTAURANT_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.Math.abs

class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RESTAURANT_KEY)
        )
    }

    override fun getViewBinding() = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override fun initViews() {
        initAppBar()
    }

    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else (1 - percentage) * 2)
        })
        toolbar.setNavigationOnClickListener { finish() }
        callButton.setOnClickListener {
            viewModel.getRestaurantPhoneNumber()?.let { telNumber ->
                if (telNumber.length < 3) {
                    Toast.makeText(applicationContext, getString(R.string.nonexistent_tel_number), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startActivity(
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                )
            }

        }
        shareButton.setOnClickListener {

        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) { state ->
        when (state) {
            is RestaurantDetailState.Success -> {
                handleSuccess(state)
            }
        }
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleText.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        deliveryTimeText.text =
            getString(
                R.string.delivery_expected_time,
                restaurantEntity.deliveryTimeRange.first,
                restaurantEntity.deliveryTimeRange.second
            )
        deliveryTipText.text =
            getString(
                R.string.delivery_tip_range,
                restaurantEntity.deliveryTipRange.first,
                restaurantEntity.deliveryTipRange.second
            )

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ),
            null, null, null
        )
    }

    companion object {

        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RESTAURANT_KEY, restaurantEntity)
            }
    }
}