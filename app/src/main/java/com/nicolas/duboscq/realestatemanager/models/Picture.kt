package com.nicolas.duboscq.realestatemanager.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity (foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["property_id"])])

data class Picture(
    @ColumnInfo(name = "property_id") val propertyId : Long,
    @ColumnInfo(name = "picture_link") val picture_link: String,
    @ColumnInfo(name = "description") val description: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}