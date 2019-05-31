package com.nicolas.duboscq.realestatemanager.adapters

import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property

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

@BindingAdapter("app:monthlyCalculated")
fun monthlyCalculated (view: View, monthly:String) {
    view.visibility = if (monthly.equals("")) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:dateSold")
fun dateSold (view: View, dateModified:String) {
    view.visibility = if (dateModified.equals(" ")) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:soldBanner")
fun soldBanner (view: View, dateSold:String? =null) {
    view.visibility = if (dateSold.equals("")) {
        View.GONE
    } else {
        View.VISIBLE
    }
}