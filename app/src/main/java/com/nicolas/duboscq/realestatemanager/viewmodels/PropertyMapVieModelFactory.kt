package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository

class PropertyMapViewModelFactory(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val pictureDataSource: PictureRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyMapViewModel::class.java)) {
            return PropertyMapViewModel(
                addressDataSource,
                pictureDataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}