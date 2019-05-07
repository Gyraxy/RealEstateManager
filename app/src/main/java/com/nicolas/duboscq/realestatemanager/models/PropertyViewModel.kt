package com.nicolas.duboscq.realestatemanager.models


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class PropertyViewModel(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val pictureDataSource : PictureRepository,
    private val executor: Executor
) : ViewModel() {

    //CREATE PROPERTY AND ADDRESS

    fun createPropertyandAddress(property: Property,street_number:Int,street_name:String,zipcode:Int,city:String,country:String,picturePathList:MutableList<String>,descriptionList:MutableList<String>){
        executor.execute {
            val id = propertyDataSource.createProperty(property)
            val address = Address(id,street_number,street_name,zipcode,city,country)
            addressDataSource.createAddress(address)
            for (i in 0..(picturePathList.size-1)){
                val picture = Picture(id,picturePathList[i],descriptionList[i])
                pictureDataSource.createPicture(picture)
            }
        }
    }

    //GET PROPERTY
    fun getProperty(): LiveData<MutableList<Property>> {
        return propertyDataSource.getProperty()
    }

    fun getAddress(): LiveData<MutableList<Address>> {
        return addressDataSource.getAddress()
    }

    fun getPicture(): LiveData<MutableList<Picture>>{
        return pictureDataSource.getPicture()
    }
}