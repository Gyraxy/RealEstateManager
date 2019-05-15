package com.nicolas.duboscq.realestatemanager.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.RequestManager
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import kotlinx.android.synthetic.main.picture_main_item.view.*
import kotlinx.android.synthetic.main.property_list_view.view.*


class PropertyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

    fun updateWithProperty(property: Property,picture:Picture,address: Address,glide:RequestManager) {
        itemView.property_listview_type_txt.text = property.type
        itemView.property_listview_price_txt.text = property.price.toString()
        val ref = property.id.toString()
        itemView.property_listview_ref_txt.text = "Ref: $ref"
        itemView.property_listview_city_txt.text = address.city
        if (!picture.picture_link.isNullOrEmpty()) {
            glide.load(picture.picture_link).into(itemView.property_listview_picture_imv)
        }
    }
}