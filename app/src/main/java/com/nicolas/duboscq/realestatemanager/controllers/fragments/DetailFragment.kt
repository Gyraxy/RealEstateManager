package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.duboscq.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_detail.*
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.adapters.SlidingPictureAdapter
import com.nicolas.duboscq.realestatemanager.databinding.FragmentDetailBinding
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.viewmodels.PropertyDetailViewModel
import com.nicolas.duboscq.realestatemanager.utils.GOOGLE_KEY
import java.util.*



class DetailFragment : androidx.fragment.app.Fragment() {

    // DATA
    private lateinit var viewModel: PropertyDetailViewModel
    private lateinit var addressLatLng : String
    private lateinit var binding: FragmentDetailBinding
    private lateinit var slidingViewPager : ViewPager
    private lateinit var slidingPictureAdapter: SlidingPictureAdapter
    private lateinit var pictureList: MutableList<String>
    private lateinit var pictDescList : MutableList<String>
    private var currentPage = 0
    private var numPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val propertyId = arguments?.getInt("property_id",0)
        val factory = propertyId?.let { Injection.provideDetailViewModelFactory(activity!!.applicationContext, it) }
        viewModel = ViewModelProviders.of(this,factory).get(PropertyDetailViewModel::class.java)
        binding = DataBindingUtil.inflate<FragmentDetailBinding>(inflater, R.layout.fragment_detail, container,false).apply {
            viewmodel = viewModel
            setLifecycleOwner (this@DetailFragment)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPictureByPropId().observe(this,androidx.lifecycle.Observer { configViewPager(it) })
        binding.clickListener = View.OnClickListener { launchGoogleMapsRoute() }
    }

    // VIEWPAGER

    private fun configViewPager(pictureL: MutableList<Picture>){

        pictureList = mutableListOf()
        pictDescList = mutableListOf()
        for (i in 0..pictureL.size-1){
            pictDescList.add(i,pictureL[i].description)
        }
        for (i in 0..pictureL.size-1){
            pictureList.add(i,pictureL[i].pictureLink)
        }
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

        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) { currentPage = position }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}

            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    // ----------------
    // GOOGLE MAP ROUTE
    // ----------------

    // GOOGLE MAP ROUTE TO GO TO PROPERTY WHEN CLICKED ON PROPERTY MAP
    private fun launchGoogleMapsRoute(){
        addressLatLng = viewModel.address.value?.lat.toString()+","+viewModel.address.value?.lng.toString()
        val gmmIntentUri = Uri.parse("google.navigation:q=$addressLatLng&mode=w")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}
