package com.nicolas.duboscq.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["property_id"])])

data class Picture(
    @ColumnInfo(name = "property_id") var propertyId : Long,
    @ColumnInfo(name = "picture_link") var pictureLink: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "picture_property_number")var picturePropertyDescription:Int
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}