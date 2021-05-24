package com.soten.abnb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }
    private lateinit var locationsSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        // 맵의 시작 위치
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.604660, 127.154905))
        naverMap.moveCamera(cameraUpdate)

        // 현 위치 (권한 필요)
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true

        // 위치에 마커 찍기위한 권한
        locationsSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationsSource

        // 마커 찍기
        val marker = Marker(LatLng(37.608718, 127.161160))
        marker.map = naverMap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) return

        if (locationsSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationsSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return // 권한이 없다면 거절
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}