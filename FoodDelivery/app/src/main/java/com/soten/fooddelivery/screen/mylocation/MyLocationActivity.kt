package com.soten.fooddelivery.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.databinding.ActivityMyLocationBinding
import com.soten.fooddelivery.screen.base.BaseActivity
import com.soten.fooddelivery.screen.main.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    OnMapReadyCallback {

    override val viewModel by viewModel<MyLocationViewModel> {
        parametersOf(
            intent.getParcelableExtra<MapSearchInformationEntity>(
                HomeViewModel.MY_LOCATION_KEY
            )
        )
    }

    override fun getViewBinding() = ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var map: GoogleMap

    private var isMapInitialized = false
    private var isChangeLocation = false

    override fun onMapReady(map: GoogleMap) {
        this.map = map ?: return
        viewModel.fetchData()
    }

    private fun setUpGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
        setUpGoogleMap()
    }

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) { state ->
        when (state) {
            is MyLocationState.Loading -> {
                handleLoadingState()
            }
            is MyLocationState.Success -> {
                if (::map.isInitialized) {
                    handleSuccessState(state)
                }
            }
            is MyLocationState.Confirm -> {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(HomeViewModel.MY_LOCATION_KEY, state.mapSearchInformationEntity)
                })
                finish()
            }
            is MyLocationState.Error -> {
                Toast.makeText(this, state.messageId, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleTextView.text = getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInformation = state.mapSearchInformationEntity
        locationLoading.isGone = true
        locationTitleTextView.text = mapSearchInformation.fullAddress
        if (isMapInitialized.not()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInformation.locationLatLngEntity.latitude,
                        mapSearchInformation.locationLatLngEntity.longitude,
                    ), CAMERA_ZOOM_LEVEL
                )
            )
            map.setOnCameraIdleListener {
                if (isChangeLocation.not()) {
                    isChangeLocation = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInformation(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    }, 1000L)
                }
            }
            isMapInitialized = true
        }
    }

    companion object {

        private const val CAMERA_ZOOM_LEVEL = 17f

        fun newIntent(context: Context, mapSearchInformationEntity: MapSearchInformationEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInformationEntity)
            }
    }

}