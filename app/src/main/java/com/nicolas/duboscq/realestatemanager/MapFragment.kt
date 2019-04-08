package com.nicolas.duboscq.realestatemanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() , OnMapReadyCallback {


    private lateinit var googleMap :GoogleMap
    private lateinit var mView: View
    private lateinit var mMapView: MapView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_map, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById(R.id.mapView) as MapView
        if (mMapView!= null){
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap =  p0
        val nantes = LatLng(47.218371, -1.553621)
        val zoomLevel = 15f
        val markerOptions:MarkerOptions=MarkerOptions().position(nantes).title("Nantes")

        googleMap.let {
            it!!.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(nantes, zoomLevel))
        }
    }
}
