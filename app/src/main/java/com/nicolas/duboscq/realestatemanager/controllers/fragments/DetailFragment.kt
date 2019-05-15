package com.nicolas.duboscq.realestatemanager.controllers.fragments

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import android.os.Handler
import androidx.viewpager.widget.ViewPager
import com.nicolas.duboscq.realestatemanager.adapters.PropertyAdapter
import com.nicolas.duboscq.realestatemanager.adapters.SlidingPictureAdapter
import com.nicolas.duboscq.realestatemanager.utils.GOOGLE_KEY
import com.viewpagerindicator.CirclePageIndicator
import java.util.*

class DetailFragment : androidx.fragment.app.Fragment() {

    private lateinit var addressLatLng : String
    private lateinit var slidingViewPager : androidx.viewpager.widget.ViewPager
    private lateinit var slidingPictureAdapter: SlidingPictureAdapter
    private lateinit var pictureList: MutableList<String>
    private lateinit var pictDescList : MutableList<String>
    private var currentPage = 0
    private var numPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.configViewPager()
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

    // ---
    // UI
    // ---

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
        fragment_detail_price_txtv.text = property.price.toString()
        fragment_detail_create_status_txtv.text = property.dateCreation
    }

    private fun updateUIAddress(address: Address) {
        val addressTxt = address.street_number.toString()+" "+address.street_name+" "+address.zipcode.toString()+" "+address.city+" "+address.country
        fragment_detail_adress_txtv.text = addressTxt
        addressLatLng = address.lat.toString()+","+address.lng.toString()
        Glide.with(this).load(googleStaticMapURL(addressLatLng)).into(fragment_detail_adress_imv)
    }

    // VIEWPAGER

    private fun configViewPager(){

        pictureList = mutableListOf()
        pictDescList = mutableListOf()
        pictureList.add(0,"https://helpx.adobe.com/content/dam/help/en/stock/how-to/visual-reverse-image-search/_jcr_content/main-pars/image/visual-reverse-image-search-v2_1000x560.jpg")
        pictureList.add(1,"https://cdn.pixabay.com/photo/2017/09/03/17/19/beach-2711264_960_720.jpg")
        pictDescList.add(0,"Papillon")
        pictDescList.add(1,"Plage")
        slidingViewPager = picture_viewpager
        slidingPictureAdapter = SlidingPictureAdapter(activity!!.applicationContext, this.pictureList, this.pictDescList)
        slidingViewPager.adapter = slidingPictureAdapter

        val indicator = indicator
        indicator.setViewPager(slidingViewPager)
        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5*density)
        numPage = pictureList.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == numPage) {
                currentPage = 0
                }
            slidingViewPager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 5000, 5000)

        indicator.setOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) { currentPage = position }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}

            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    // ----------
    // GOOGLE MAP
    // ----------

    // GOOGLE MAP URL FOR STATIC MAP
    private fun googleStaticMapURL(address_lat_lng:String) : String{
        return "https://maps.googleapis.com/maps/api/staticmap?center=$address_lat_lng&markers=$address_lat_lng&zoom=17&size=600x300&maptype=roadmap&key=$GOOGLE_KEY"
    }

    // GOOGLE MAP ROUTE TO GO TO PROPERTY WHEN CLICKED ON PROPERTY MAP
    private fun launchGoogleMapsRoute(){
        val gmmIntentUri = Uri.parse("google.navigation:q=$addressLatLng&mode=w")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}
