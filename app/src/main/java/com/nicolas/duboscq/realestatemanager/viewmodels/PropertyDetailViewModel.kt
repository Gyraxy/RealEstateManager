package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class PropertyDetailViewModel (
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource : AddressRepository,
    private val pictureDataSource: PictureRepository,
    private val property_id : Int,
    private val executor: Executor
    ): ViewModel() {

    fun getPropertyById(): LiveData<Property>{
        return propertyDataSource.getPropertyById(property_id)
    }

    fun getAddressByPropId(): LiveData<Address>{
        return addressDataSource.getAddressByPropId(property_id)
    }

    fun getPictureByPropId(): LiveData<MutableList<Picture>>{
        return pictureDataSource.getPictureByPropId(property_id)
    }
}