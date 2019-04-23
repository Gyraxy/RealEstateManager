package com.nicolas.duboscq.realestatemanager.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "property")
data class Property(
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "surface") val surface: Int,
    @ColumnInfo(name = "room") val room: Int,
    @ColumnInfo(name = "bedroom") val bedroom: Int,
    @ColumnInfo(name = "bathroom") val bathroom: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type:String)
    {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
