package com.nicolas.duboscq.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.adapters.viewholders.PictureViewHolder


class PictureAdapter (private var listPicturePath : MutableList<String>,private var listPictureDescription : MutableList<String>,private var glide: RequestManager) : androidx.recyclerview.widget.RecyclerView.Adapter<PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.picture_main_item, parent, false)
        return PictureViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listPicturePath.isNullOrEmpty()){return 0}
        else {return listPicturePath.size}
    }

    override fun onBindViewHolder(pictureviewholder: PictureViewHolder, position: Int) {
        pictureviewholder.updateWithPicture(this.listPicturePath[position],this.listPictureDescription[position],this.glide)
    }
}