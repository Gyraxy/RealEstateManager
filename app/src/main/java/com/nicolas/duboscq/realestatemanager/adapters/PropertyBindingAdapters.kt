package com.nicolas.duboscq.realestatemanager.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.utils.GOOGLE_KEY

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:propertyAddress",requireAll = false)
fun bindAddress(view: TextView, address : Address? =null){
    val addressTxt = address?.streetNumber+" "+address?.streetName+" "+address?.zipcode+" "+address?.city+" "+address?.country
    view.text = addressTxt
}

@BindingAdapter("app:googleStaticMap",requireAll = false)
fun bindGoogleStaticMap(view: ImageView, address : Address? =null){
    val addressLatLng = address?.lat.toString()+","+address?.lng.toString()
    val googleStaticURL = "https://maps.googleapis.com/maps/api/staticmap?center=$addressLatLng&markers=$addressLatLng&zoom=17&size=600x300&maptype=roadmap&key=$GOOGLE_KEY"
    Glide.with(view.context)
            .load(googleStaticURL)
            .into(view)
}