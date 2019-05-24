package com.nicolas.duboscq.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture

@Dao
interface PictureDao {

    @Query("SELECT * from picture")
    fun getAll(): LiveData<MutableList<Picture>>

    @Query("SELECT * from picture WHERE picture.picture_property_number=0")
    fun getFirstPicture(): LiveData<MutableList<Picture>>

    @Query("SELECT * from picture WHERE property_id = :property_id")
    fun getPictureFromPropId(property_id: Int): LiveData<MutableList<Picture>>

    @Insert
    fun insert(picture: Picture)

    @Query("DELETE FROM picture WHERE property_id = :property_id")
    fun delPictureFromPropId(property_id: Int)

    @Query("DELETE FROM picture")
    fun deleteAll()
}