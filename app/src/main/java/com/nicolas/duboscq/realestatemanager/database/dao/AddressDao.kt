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

    @Query("UPDATE address SET street_number= :streetNumber,street_name= :streetName,zipcode= :zipcode,city= :city,country= :country WHERE property_id = :prop_id")
    fun updateAddressFromPropertyId(prop_id:Int,streetNumber:String,streetName:String,zipcode:String,city:String,country:String)

    @Query("DELETE FROM address")
    fun deleteAll()
}