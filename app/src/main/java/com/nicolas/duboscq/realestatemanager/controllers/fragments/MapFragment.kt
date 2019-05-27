package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
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
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.MarkerOptions
import com.nicolas.duboscq.realestatemanager.controllers.activities.MapDetailActivity
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.utils.Utils
import com.nicolas.duboscq.realestatemanager.viewmodels.PropertyMapViewModel
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : androidx.fragment.app.Fragment() , OnMapReadyCallback,GoogleMap.OnMarkerClickListener {


    private lateinit var googleMap :GoogleMap
    private lateinit var mView: View
    private lateinit var mMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var mapViewModel: PropertyMapViewModel

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_map, container, false)
        Log.i("MapFragment","OnCreateView")
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("MapFragment","OnViewCreated")
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!.applicationContext)
        mMapView = mView.findViewById(R.id.mapView) as MapView
        if (mMapView!= null){
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
        fragment_map_view_my_location_floating_btn.setOnClickListener { centerMyLocation() }
        val factory = Injection.provideMapViewModelFactory(activity!!.applicationContext)
        mapViewModel = ViewModelProviders.of(this,factory).get(PropertyMapViewModel::class.java)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
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
        mapViewModel.address.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                Log.i("MapFragment","Generate Markers")
                generateMarker(it)
            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        p0.uiSettings.isZoomControlsEnabled = true
        p0.setOnMarkerClickListener(this)
        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val intentDetail = Intent(activity, MapDetailActivity::class.java)
        intentDetail.putExtra("activity","detail")
        val property_id = p0!!.title
        Log.i("Property",property_id)
        intentDetail.putExtra("id",property_id.toInt())
        startActivity(intentDetail)
        return false
    }

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

    private fun generateMarker(addressList: MutableList<Address>){
        for (i in 0..addressList.size-1){
            val addressTxt = "${addressList[i].streetNumber} ${addressList[i].streetName} ${addressList[i].zipcode} ${addressList[i].city} ${addressList[i].country}"
            val addressLatLng = Utils.getLocationFromAddress(activity!!.applicationContext,addressTxt)
            googleMap.addMarker(MarkerOptions()
                .position(addressLatLng)
                .title(addressList[i].propertyId.toString()))
        }
    }

}
