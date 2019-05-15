package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["property_id"])])

data class Picture(
    @ColumnInfo(name = "property_id") val propertyId : Long,
    @ColumnInfo(name = "picture_link") val picture_link: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture_property_number")val picture_property_description:Int
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}