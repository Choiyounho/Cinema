package com.soten.fooddelivery.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.databinding.FragmentHomeBinding
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.screen.main.MainActivity
import com.soten.fooddelivery.screen.main.MainTabMenu
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListFragment
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantOrder
import com.soten.fooddelivery.screen.mylocation.MyLocationActivity
import com.soten.fooddelivery.screen.order.OrderMenuListActivity
import com.soten.fooddelivery.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: LocationListener

    private val auth by lazy { Firebase.auth }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInformationEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInformation ->
                        viewModel.loadReverseGeoInformation(myLocationInformation.locationLatLngEntity)
                    }
            }
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleTextView) {
                    text = getString(R.string.please_setup_your_location)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.cannot_assigned_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun initViews() = with(binding) {
        locationTitleTextView.setOnClickListener {
            viewModel.getMapSearchInformation()?.let { mapInformation ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(requireContext(), mapInformation)
                )
            }
        }
        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipDeliveryTip -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)
                }
            }
        }
    }

    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }

    private fun initViewPager(locationLatLngEntity: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            orderChipGroup.isVisible = true
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLngEntity)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLngEntity
            )
            viewPager.adapter = viewPagerAdapter
            // 매번 페이지가 바뀔 때 마다 프래그먼트를 다시 만드는 것이 아니라 프래그먼트를 다시 쓸 수 있도록 하는 기능
            viewPager.offscreenPageLimit = restaurantCategories.size

            // 탭 레이아웃에 인플레이트
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }

        if (locationLatLngEntity != viewPagerAdapter.locationLatLngEntity) {
            viewPagerAdapter.locationLatLngEntity = locationLatLngEntity
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLngEntity)
            }
        }
    }

    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Uninitialized -> getMyLocation()
                is HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationTitleTextView.text = getString(R.string.loading)
                }
                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleTextView.text =
                        state.mapSearchInformationEntity.fullAddress
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true
                    initViewPager(state.mapSearchInformationEntity.locationLatLngEntity)
                    if (state.isLocationSame.not()) {
                        Toast.makeText(
                            requireContext(),
                            R.string.please_set_your_current_location,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is HomeState.Error -> {
                    binding.locationTitleTextView.text = getString(R.string.cannot_load_address)
                    binding.locationLoading.isGone = true
                    binding.locationTitleTextView.setOnClickListener {
                        getMyLocation()
                    }
                    Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.foodMenuLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.basketButtonContainer.isVisible = true
                binding.basketButton.setOnClickListener {
                    if (auth.currentUser == null) {
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                        }
                    } else {
                        startActivity(
                            OrderMenuListActivity.newIntent(requireContext())
                        )
                    }
                }
                binding.basketCountTextView.text =
                    getString(R.string.basket_count, it.size)
            } else {
                binding.basketButtonContainer.isGone = true
                binding.basketButton.setOnClickListener(null)
            }
        }
    }

    private fun alertLoginNeed(action: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("로그인이 필요합니다.")
            .setMessage("주문하려면 로그인이 필요합니다. My탭으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnable) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkMyBasket()
    }

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            removeLocationListener()
        }
    }
}