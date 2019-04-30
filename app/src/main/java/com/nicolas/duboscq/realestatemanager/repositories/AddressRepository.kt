package com.nicolas.duboscq.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.dao.AddressDao
import com.nicolas.duboscq.realestatemanager.models.Address

class AddressRepository(private val addressDao: AddressDao) {

    fun createAddress(address: Address) {
        addressDao.insert(address)
    }

    fun getAddress(): LiveData<MutableList<Address>> {
        return addressDao.getAll()
    }
}