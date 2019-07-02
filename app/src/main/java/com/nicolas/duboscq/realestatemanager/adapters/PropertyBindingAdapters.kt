package com.nicolas.duboscq.realestatemanager.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nicolas.duboscq.realestatemanager.models.Address
import java.util.*

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
    view.visibility = if (monthly=="") {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:dateSold")
fun dateSold (view: View, dateModified:String) {
    view.visibility = if (dateModified==" ") {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:soldBanner")
fun soldBanner (view: View, dateSold: Date? =null) {
    view.visibility = if (dateSold==null) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:AddDollarSymbol")
fun bindDollarSymbol(view: TextView,price:String){
    val priceDollar = "$price $"
    view.text = priceDollar
}