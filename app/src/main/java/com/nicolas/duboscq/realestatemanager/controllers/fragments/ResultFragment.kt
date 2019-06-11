package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.adapters.PropertyAdapter
import com.nicolas.duboscq.realestatemanager.controllers.activities.MainActivity
import com.nicolas.duboscq.realestatemanager.controllers.activities.MapDetailActivity
import com.nicolas.duboscq.realestatemanager.controllers.activities.SearchResultActivity
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.utils.DividerItemDecoration
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.utils.ItemClickSupport
import com.nicolas.duboscq.realestatemanager.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_property_list.*
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    private lateinit var propertyAdapter: PropertyAdapter
    private var propertylist : MutableList<Property> = mutableListOf()
    private var addresslist : MutableList<Address> = mutableListOf()
    private var picturelist : MutableList<Picture> = mutableListOf()

    private lateinit var viewModel: SearchViewModel
    private var propertyId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = Injection.provideSearchViewModelFactory(activity!!.applicationContext)
        viewModel = ViewModelProviders.of(activity as SearchResultActivity,factory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        viewModel.propertyListResult.observe(this,Observer {
            if (!it.isNullOrEmpty()){
                Log.i("Fragment ResultProperty",it.size.toString())
                updateProperty(it)
                viewModel.getAddressList(it)
            }
        })
        viewModel.addressListResult.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                Log.i("Fragment ResultAddress",it.size.toString())
            }
        })
    }

    private fun configureRecyclerView(){
        propertyAdapter = PropertyAdapter(propertylist, addresslist,picturelist, Glide.with(this))
        fragment_result_recyclerView.adapter = propertyAdapter
        val mDividerItemDecoration = DividerItemDecoration(fragment_result_recyclerView.context, R.drawable.horizontal_divider)
        fragment_result_recyclerView.addItemDecoration(mDividerItemDecoration)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(fragment_result_recyclerView, R.layout.property_list_view)
            .setOnItemClickListener{_, position, _ ->
                val intentDetail = Intent(activity, MapDetailActivity::class.java)
                intentDetail.putExtra("activity","detail")
                propertyId = propertylist[position].id
                Log.i("Property",propertyId.toString())
                intentDetail.putExtra("id",propertyId)
                startActivity(intentDetail)

            }
    }

    private fun updateProperty(proplist: MutableList<Property>) {
        propertylist.addAll(proplist)
        propertyAdapter.notifyDataSetChanged()
    }
}
