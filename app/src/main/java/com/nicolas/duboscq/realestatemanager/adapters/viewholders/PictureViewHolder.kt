package com.nicolas.duboscq.realestatemanager.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.picture_main_item.view.*



class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun updateWithPicture(picturePath : String,glide :RequestManager) {
        glide.load(picturePath).into(itemView.activity_edit_update_item_picture)
    }
}