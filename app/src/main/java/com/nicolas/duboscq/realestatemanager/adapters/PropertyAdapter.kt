package com.nicolas.duboscq.realestatemanager.adapters

import android.view.ViewGroup
import com.nicolas.duboscq.realestatemanager.models.Property
import android.view.LayoutInflater
import com.bumptech.glide.RequestManager
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.adapters.viewholders.PropertyViewHolder
import com.nicolas.duboscq.realestatemanager.models.Picture


class PropertyAdapter (private var listProperty : MutableList<Property>,private var listAddress : MutableList<Address>, private var listPicture:MutableList<Picture>,private var glide: RequestManager) : androidx.recyclerview.widget.RecyclerView.Adapter<PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.property_list_view, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listProperty.isNullOrEmpty()){return 0}
        else {return listProperty.size}
    }

    override fun onBindViewHolder(propertyviewholder: PropertyViewHolder, position: Int) {
        propertyviewholder.updateWithProperty(this.listProperty[position],this.listPicture[position],this.listAddress[position],glide)
    }
}