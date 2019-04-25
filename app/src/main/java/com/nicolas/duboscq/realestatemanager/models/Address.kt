package com.nicolas.duboscq.realestatemanager.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class Address(
    @ColumnInfo(name = "street_number") val street_number: Int,
    @ColumnInfo(name = "street_name") val street_name: String,
    @ColumnInfo(name = "zipcode") val zipcode: Int,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "LatLng") val latLng: LatLng
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}