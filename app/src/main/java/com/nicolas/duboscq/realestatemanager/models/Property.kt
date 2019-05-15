package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import android.content.ContentValues

@Entity
data class Property(
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "surface") val surface: Int,
    @ColumnInfo(name = "room") val room: Int,
    @ColumnInfo(name = "bedroom") val bedroom: Int,
    @ColumnInfo(name = "bathroom") val bathroom: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type:String,
    @ColumnInfo(name = "date_creation") val dateCreation: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
