package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity (foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["property_id"],
    onDelete = CASCADE)])

data class Address(
    @ColumnInfo(name = "property_id") var propertyId : Long,
    @ColumnInfo(name = "street_number") var streetNumber: String,
    @ColumnInfo(name = "street_name") var streetName: String,
    @ColumnInfo(name = "zipcode") var zipcode: String,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "country") var country: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
