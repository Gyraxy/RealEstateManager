package com.nicolas.duboscq.realestatemanager.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.models.Address

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:PropertyAddress")
fun bindAddress(view: TextView, address : Address){
    view.text = address.city
}