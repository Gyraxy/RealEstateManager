package com.nicolas.duboscq.realestatemanager.adapters

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import kotlinx.android.synthetic.main.picture_item_sliding.view.*
import java.util.ArrayList

class SlidingPictureAdapter(private val context: Context, private val pictureList: MutableList<String>, private val pictDescList: MutableList<String>) : PagerAdapter() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return pictureList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.picture_item_sliding, view, false)!!
        val imageView = imageLayout.findViewById(R.id.picture_item_sliding_imv) as ImageView
        val textView = imageLayout.findViewById(R.id.picture_item_sliding_description_txt) as TextView
        Glide.with(context).load(pictureList[position]).into(imageView)
        textView.text = pictDescList[position]
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}