package com.soten.fooddelivery.screen.main.home

import com.google.android.material.tabs.TabLayoutMediator
import com.soten.fooddelivery.databinding.FragmentHomeBinding
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListFragment
import com.soten.fooddelivery.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun observeData() {}

    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    private fun initViewPager() = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
        }
        // 매번 페이지가 바뀔 때 마다 프래그먼트를 다시 만드는 것이 아니라 프래그먼트를 다시 쓸 수 있도록 하는 기능
        viewPager.offscreenPageLimit = restaurantCategories.size

        // 탭 레이아웃에 인플레이트
       TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    companion object {
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}