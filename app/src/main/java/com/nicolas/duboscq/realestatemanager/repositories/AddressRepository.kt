package com.nicolas.duboscq.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.nicolas.duboscq.realestatemanager.database.dao.AddressDao
import com.nicolas.duboscq.realestatemanager.models.Address

class AddressRepository(private val addressDao: AddressDao) {

    val allAddress : LiveData<List<Address>> = addressDao.getAll()

    @WorkerThread
    suspend fun insertAddress(address: Address){
        addressDao.insert(address)
    }
}