package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class PropertyDetailViewModelFactory(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource : AddressRepository,
    private val pictureDataSource : PictureRepository,
    private val property_id : Int
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyDetailViewModel::class.java)) {
            return PropertyDetailViewModel(
                propertyDataSource,
                addressDataSource,
                pictureDataSource,
                property_id
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}