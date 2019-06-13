package com.nicolas.duboscq.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.nicolas.duboscq.realestatemanager.models.Property


@Dao
interface PropertyDao {

    @Query("SELECT * from property")
    fun getAll(): LiveData<MutableList<Property>>

    @Query("SELECT * from property WHERE property.id = :prop_id")
    fun getPropertyById(prop_id:Int) : LiveData<Property>

    @Insert
    fun insert(property: Property) : Long

    @Query("UPDATE property SET agent= :agent,status= :status,price= :price,surface= :surface,room= :room,bedroom= :bedroom,bathroom= :bathroom,description= :description,type= :type,points_of_interest= :points_interest,nb_of_pictures= :nbPictures,date_entry= :dateEntry,date_sold= :dateSold,date_modified= :dateModified WHERE property.id = :prop_id")
    fun updatePropertyById(prop_id:Int,agent: String,status: String,price: Int, surface:Int,room: Int,bedroom:Int,bathroom: Int,description:String,type:String,points_interest:String,nbPictures:Int,dateEntry:String,dateSold:String,dateModified:String)

    @Query("DELETE FROM property")
    fun deleteAll()

    @RawQuery
    fun getPropertyBySearch(query:SupportSQLiteQuery) : MutableList<Property>

    @Query("SELECT * FROM Property WHERE id = :id")
    fun getItemsWithCursor(id: Long): Cursor

    @Update
    fun updateProperty(property: Property): Int

}