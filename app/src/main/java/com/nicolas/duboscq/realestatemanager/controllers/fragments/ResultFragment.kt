package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.adapters.PropertyAdapter
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertylist : MutableList<Property>
    private lateinit var addresslist : MutableList<Address>
    private lateinit var picturelist : MutableList<Picture>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.configureRecyclerView()
    }

    private fun configureRecyclerView(){
        propertylist = mutableListOf()
        addresslist = mutableListOf()
        picturelist = mutableListOf()
        propertyAdapter = PropertyAdapter(propertylist, addresslist,picturelist, Glide.with(this))
        fragment_result_recyclerView.adapter = propertyAdapter
        val mDividerItemDecoration = DividerItemDecoration(fragment_result_recyclerView.context, R.drawable.horizontal_divider)
        fragment_result_recyclerView.addItemDecoration(mDividerItemDecoration)
    }
}
