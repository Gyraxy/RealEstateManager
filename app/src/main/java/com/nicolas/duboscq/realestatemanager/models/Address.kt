package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["property_id"])])

data class Address(
    @ColumnInfo(name = "property_id") val propertyId : Long,
    @ColumnInfo(name = "street_number") val street_number: Int,
    @ColumnInfo(name = "street_name") val street_name: String,
    @ColumnInfo(name = "zipcode") val zipcode: Int,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "Lat") val lat: Double,
    @ColumnInfo(name= "Lng") val lng: Double
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
