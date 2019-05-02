package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.injections.Injection
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.adapters.PropertyAdapter
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertylist : MutableList<Property>
    private lateinit var addresslist : MutableList<Address>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.configureRecyclerView()
        this.configureViewModel()
    }

    // VIEW MODEL

    private fun configureViewModel(){
        val mViewModelFactory = Injection.provideViewModelFactory(activity!!.applicationContext)
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel::class.java!!)
        this.propertyViewModel.getProperty().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    fragment_list_recycler_view_empty.visibility = View.VISIBLE
                } else {
                    fragment_list_recycler_view_empty.visibility = View.GONE
                    updateProperty(it)
                }
            }
        })
        this.propertyViewModel.getAddress().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                } else {
                    updateAddress(it)
                }
            }
        })
    }

    // RECYCLERVIEW

    private fun configureRecyclerView(){
        propertylist = mutableListOf()
        addresslist = mutableListOf()
        propertyAdapter = PropertyAdapter(propertylist, addresslist)
        fragment_list_recyclerView.layoutManager = LinearLayoutManager(activity)
        fragment_list_recyclerView.adapter = propertyAdapter
    }

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
}
