package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "surface") var surface: Int,
    @ColumnInfo(name = "room") var room: Int,
    @ColumnInfo(name = "bedroom") var bedroom: Int,
    @ColumnInfo(name = "bathroom") var bathroom: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "date_creation") var dateCreation: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
