package com.nicolas.duboscq.realestatemanager.views

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nicolas.duboscq.realestatemanager.models.Property
import android.view.LayoutInflater
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.PropertyViewHolder


class PropertyAdapter (private var listProperty : List<Property>) : RecyclerView.Adapter<PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.property_list_view, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listProperty.isEmpty()){return 0}
        else {return listProperty.size}
    }

    override fun onBindViewHolder(propertyviewholder: PropertyViewHolder, position: Int) {
        propertyviewholder.updateWithProperty(this.listProperty[position])
    }
}