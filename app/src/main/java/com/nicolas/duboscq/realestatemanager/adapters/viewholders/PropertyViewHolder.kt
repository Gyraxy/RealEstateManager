package com.nicolas.duboscq.realestatemanager.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import kotlinx.android.synthetic.main.property_list_view.view.*


class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun updateWithProperty(property: Property,address: Address) {
        itemView.property_listview_type_txt.text = property.type
        itemView.property_listview_price_txt.text = property.price.toString()
        val ref = property.id.toString()
        itemView.property_listview_ref_txt.text = "Ref: $ref"
        itemView.property_listview_city_txt.text = address.city
    }
}