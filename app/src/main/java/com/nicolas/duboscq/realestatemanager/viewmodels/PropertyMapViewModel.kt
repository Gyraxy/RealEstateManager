package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository

class PropertyMapViewModel (private val addressDataSource : AddressRepository,
                            private val pictureDataSource: PictureRepository
): ViewModel() {
    val address: LiveData<MutableList<Address>>
    val picture: LiveData<MutableList<Picture>>

    init {
        address = addressDataSource.getAddress()
        picture = pictureDataSource.getFirstPicture()
    }
}