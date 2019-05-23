package com.nicolas.duboscq.realestatemanager.controllers.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.viewmodels.PropertyListViewModel
import com.nicolas.duboscq.realestatemanager.adapters.PropertyAdapter
import com.nicolas.duboscq.realestatemanager.controllers.activities.MapDetailActivity
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.utils.DividerItemDecoration
import com.nicolas.duboscq.realestatemanager.utils.ItemClickSupport
import kotlinx.android.synthetic.main.fragment_property_list.*


class ListFragment : androidx.fragment.app.Fragment() {

    private lateinit var propertyListViewModel: PropertyListViewModel
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertylist : MutableList<Property>
    private lateinit var addresslist : MutableList<Address>
    private lateinit var picturelist : MutableList<Picture>
    private var property_id:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_property_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        this.configureViewModel()
    }

    // VIEW MODEL

    private fun configureViewModel(){
        val mViewModelFactory = Injection.provideListViewModelFactory(activity!!.applicationContext)
        this.propertyListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyListViewModel::class.java!!)
        this.propertyListViewModel.getProperty().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    fragment_list_recycler_view_empty.visibility = View.VISIBLE
                } else {
                    fragment_list_recycler_view_empty.visibility = View.GONE
                    updateProperty(it)
                }
            }
        })
        this.propertyListViewModel.getAddress().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                } else {
                    updateAddress(it)
                }
            }
        })
        this.propertyListViewModel.getFirstPicture().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                } else {
                    updatePicture(it)
                }
            }
        })
    }

    // RECYCLERVIEW

    private fun configureRecyclerView(){
        propertylist = mutableListOf()
        addresslist = mutableListOf()
        picturelist = mutableListOf()
        propertyAdapter = PropertyAdapter(propertylist, addresslist,picturelist,Glide.with(this))
        fragment_list_recyclerView.adapter = propertyAdapter
        val mDividerItemDecoration = DividerItemDecoration(fragment_list_recyclerView.getContext(), R.drawable.horizontal_divider)
        fragment_list_recyclerView.addItemDecoration(mDividerItemDecoration)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(fragment_list_recyclerView, R.layout.property_list_view)
            .setOnItemClickListener{recyclerView, position, v ->
                val intentDetail = Intent(activity,MapDetailActivity::class.java)
                intentDetail.putExtra("activity","detail")
                property_id = propertylist[position].id
                Log.i("Property",property_id.toString())
                intentDetail.putExtra("id",property_id)
                startActivity(intentDetail)
            }
    }

    // UPDATE UI

    private fun updateProperty(proplist: MutableList<Property>) {
        propertylist.clear()
        propertylist.addAll(proplist)
        propertyAdapter.notifyDataSetChanged()
    }

    private fun updateAddress(addlist: MutableList<Address>) {
        addresslist.clear()
        addresslist.addAll(addlist)
        propertyAdapter.notifyDataSetChanged()
    }

    private fun updatePicture(piclist: MutableList<Picture>) {
        picturelist.clear()
        picturelist.addAll(piclist)
        propertyAdapter.notifyDataSetChanged()
    }
}
