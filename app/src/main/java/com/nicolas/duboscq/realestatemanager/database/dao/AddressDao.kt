package com.nicolas.duboscq.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nicolas.duboscq.realestatemanager.models.Address

@Dao
interface AddressDao {

    @Query("SELECT * from address")
    fun getAll(): LiveData<MutableList<Address>>

    @Insert
    fun insert(address: Address)

    @Query("SELECT * from address WHERE property_id = :property_id")
    fun getAddressFromPropId(property_id: Int): LiveData<Address>

    @Query("DELETE FROM address")
    fun deleteAll()
}