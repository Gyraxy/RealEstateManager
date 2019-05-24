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