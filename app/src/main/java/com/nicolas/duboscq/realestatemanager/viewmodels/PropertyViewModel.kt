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

class PropertyViewModel(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val pictureDataSource : PictureRepository,
    private val executor: Executor
) : ViewModel() {

    //CREATE PROPERTY AND ADDRESS

    fun createPropertyandAddress(property: Property, street_number:Int, street_name:String, zipcode:Int, city:String, country:String, lat: Double, lng: Double, picturePathList:MutableList<String>, descriptionList:MutableList<String>, context:Context){
        executor.execute {
            val id = propertyDataSource.createProperty(property)
            val address = Address(
                id,
                street_number,
                street_name,
                zipcode,
                city,
                country,
                lat,
                lng
            )
            addressDataSource.createAddress(address)
            for (i in 0..(picturePathList.size-1)){
                val picture =
                    Picture(id, picturePathList[i], descriptionList[i], i)
                pictureDataSource.createPicture(picture)
            }
        }
        Notifications().sendNotification(context,"create")
    }

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