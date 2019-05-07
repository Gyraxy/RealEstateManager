package com.nicolas.duboscq.realestatemanager.models

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log

class PicturesViewModel : ViewModel() {

    var pictureListLive: MutableLiveData<MutableList<String>> = MutableLiveData()

    init {
        pictureListLive.value = mutableListOf()
    }
}