package com.nicolas.duboscq.realestatemanager.adapters.viewholders

import android.view.View
import com.bumptech.glide.RequestManager
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import kotlinx.android.synthetic.main.property_list_view.view.*

class PropertyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

    fun updateWithProperty(listProperty: MutableList<Property>,listPicture:MutableList<Picture>,listAddress:MutableList<Address>,glide:RequestManager,position:Int) {

        // SHOW PROPERTY INFO
        itemView.property_listview_type_txt.text = listProperty[position].type
        val priceEuro = listProperty[position].price.toString()+" €"
        itemView.property_listview_price_txt.text = priceEuro
        val ref = "Ref: "+listProperty[position].id.toString()
        itemView.property_listview_ref_txt.text = ref

        // SHOW ADDRESS INFO IF ADDRESSLIST NOT NULL
        if (listAddress.isNullOrEmpty()){
            itemView.property_listview_city_txt.text = ""
        } else {
            itemView.property_listview_city_txt.text =listAddress[position].city
        }

        // SHOW PICTURE INFO IF PICTURELIST NOT NULL
        if (!listPicture.isNullOrEmpty()) {
            glide.load(listPicture[position].pictureLink).into(itemView.property_listview_picture_imv)
        }

        // SHOW BANNER SOLD IF PROPERTY SOLD STATUS
        if (listProperty[position].date_sold==""){
            itemView.activity_list_soldbanner_txt.visibility = View.GONE
        }
    }
}