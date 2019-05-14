package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import kotlinx.android.synthetic.main.fragment_detail.*
import android.content.Intent
import android.net.Uri

class DetailFragment : Fragment() {

    lateinit var address_lat_lng : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val database = AppDatabase.getDatabase(activity!!.applicationContext)
        val propertyList = database.propertyDao().getPropertyById(1)
        propertyList.observe(this, Observer {
            if (it != null) {
                updateUIProp(it)
            }
        })
        val addressList = database.addressDao().getAddressFromPropId(1)
        addressList.observe(this, Observer {
            if (it != null) {
                updateUIAddress(it)
        } })

        fragment_detail_adress_imv.setOnClickListener {
            launchGoogleMapsRoute()
        }
    }

    private fun updateUIProp(property: Property) {
        if (property.status.equals("Vendu")){
            fragment_detail_status_txtv.setTextColor(resources.getColor(R.color.colorAccent))
        }
        fragment_detail_status_txtv.text = property.status

        fragment_detail_surface_txtv.text = property.surface.toString()
        fragment_detail_bathroom_txt.text = property.bathroom.toString()
        fragment_detail_bedroom_txtv.text = property.bedroom.toString()
        fragment_detail_room_txtv.text = property.room.toString()

        fragment_detail_description_txtv.text = property.description

        fragment_detail_create_status_txtv.text = property.dateCreation
    }

    private fun updateUIAddress(address: Address) {
        val address_txt = address.street_number.toString()+" "+address.street_name+" "+address.zipcode.toString()+" "+address.city+" "+address.country
        fragment_detail_adress_txtv.text = address_txt
        address_lat_lng = address.lat.toString()+","+address.lng.toString()
        Glide.with(this).load(googleStaticMapURL(address_lat_lng)).into(fragment_detail_adress_imv)
    }

    // GOOGLE MAP URL FOR STATIC MAP
    private fun googleStaticMapURL(address_lat_lng:String) : String{
        return "https://maps.googleapis.com/maps/api/staticmap?center="+address_lat_lng+"&markers="+address_lat_lng+"&zoom=17&size=600x300&maptype=roadmap&key=AIzaSyDE8xBMmrWat5ugUmWhLANHGDui3ngNJjI"
    }

    // GOOGLE MAP ROUTE TO GO TO PROPERTY WHEN CLICKED ON PROPERTY MAP
    private fun launchGoogleMapsRoute(){
        val gmmIntentUri = Uri.parse("google.navigation:q="+address_lat_lng+"&mode=w")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}
