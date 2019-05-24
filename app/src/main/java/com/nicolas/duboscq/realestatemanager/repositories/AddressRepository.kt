package com.nicolas.duboscq.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.dao.AddressDao
import com.nicolas.duboscq.realestatemanager.models.Address

class AddressRepository(private val addressDao: AddressDao) {

    fun createAddress(address: Address) {
        addressDao.insert(address)
    }

    fun getAddress(): LiveData<MutableList<Address>> {
        return addressDao.getAll()
    }

    fun getAddressByPropId(prop_id: Int): LiveData<Address>{
        return addressDao.getAddressFromPropId(prop_id)
    }

    fun updateAddressByPropId(prop_id:Int,streetNumber:String,streetName:String,zipcode:String,city:String,country:String){
        return addressDao.updateAddressFromPropertyId(prop_id,streetNumber,streetName,zipcode,city,country)
    }
}