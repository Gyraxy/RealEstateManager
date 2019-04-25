package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.nicolas.duboscq.realestatemanager.R
import com.google.android.gms.maps.CameraUpdateFactory
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment() , OnMapReadyCallback,GoogleMap.OnMarkerClickListener {


    private lateinit var googleMap :GoogleMap
    private lateinit var mView: View
    private lateinit var mMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_map, container, false)
        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!.applicationContext)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById(R.id.mapView) as MapView
        if (mMapView!= null){
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
        fragment_map_view_my_location_floating_btn.setOnClickListener { centerMyLocation() }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        p0.uiSettings.isZoomControlsEnabled = true
        p0.setOnMarkerClickListener(this)
        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun centerMyLocation() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getLastLocation().addOnSuccessListener { location ->
                if (location != null) {
                    val myLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 17f))
                }
            }
        }
    }
}
