package com.nicolas.duboscq.realestatemanager.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import com.nicolas.duboscq.realestatemanager.utils.Notifications
import java.util.concurrent.Executor

class PropertyListViewModel(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val pictureDataSource : PictureRepository,
    private val executor: Executor
) : ViewModel() {

    //GET PROPERTY
    fun getProperty(): LiveData<MutableList<Property>> {
        return propertyDataSource.getProperty()
    }

    fun getPropertyById(prop_id : Int): LiveData<Property>{
        return propertyDataSource.getPropertyById(prop_id)
    }

    fun getAddress(): LiveData<MutableList<Address>> {
        return addressDataSource.getAddress()
    }

    fun getAddressById(prop_id: Int): LiveData<Address>{
        return addressDataSource.getAddressByPropId(prop_id)
    }

    fun getFirstPicture(): LiveData<MutableList<Picture>>{
        return pictureDataSource.getFirstPicture()
    }
}