package com.soten.fooddelivery.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListFragment

class RestaurantListFragmentPagerAdapter(
    fragment: Fragment,
    val fragmentList: List<RestaurantListFragment>,
    var locationLatLngEntity: LocationLatLngEntity
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}