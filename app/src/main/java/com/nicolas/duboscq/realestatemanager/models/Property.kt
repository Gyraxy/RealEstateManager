package com.nicolas.duboscq.realestatemanager.models

import android.content.ContentValues
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicolas.duboscq.realestatemanager.utils.toFRDate
import java.util.*

@Entity
data class Property(
    @ColumnInfo(name = "agent") var agent: String,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "surface") var surface: Int,
    @ColumnInfo(name = "room") var room: Int,
    @ColumnInfo(name = "bedroom") var bedroom: Int,
    @ColumnInfo(name = "bathroom") var bathroom: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "points_of_interest") var points_interest: String,
    @ColumnInfo(name = "nb_of_pictures") var nbPictures:Int,
    @ColumnInfo(name = "date_entry") var date_entry: Date,
    @ColumnInfo(name = "date_sold") var date_sold:Date?,
    @ColumnInfo(name = "date_creation") var dateCreation: String,
    @ColumnInfo(name = "date_modified") var dateModified: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    constructor() : this("", "", 0, 0, 0,
        0, 0, "", "", "",
        0, Date(), null, "", "")
}

fun fromContentValues(values: ContentValues): Property {
    val property = Property()
    if (values.containsKey("agent")) property.agent = values.getAsString("agent")
    if (values.containsKey("status")) property.status = values.getAsString("status")
    if (values.containsKey("price")) property.price = values.getAsInteger("price")
    if (values.containsKey("surface")) property.surface = values.getAsInteger("surface")
    if (values.containsKey("room")) property.room = values.getAsInteger("room")
    if (values.containsKey("bedroom")) property.bedroom = values.getAsInteger("bedroom")
    if (values.containsKey("bathroom")) property.bathroom = values.getAsInteger("bathroom")
    if (values.containsKey("description")) property.description = values.getAsString("description")
    if (values.containsKey("type")) property.type = values.getAsString("type")
    if (values.containsKey("points_of_interest")) property.points_interest = values.getAsString("points_of_interest")
    if (values.containsKey("nb_of_pictures")) property.nbPictures = values.getAsInteger("nb_of_pictures")
    if (values.containsKey("date_entry")) property.date_entry = values.getAsString("date_entry").toFRDate()
    if (values.containsKey("date_sold")) property.date_sold = values.getAsString("date_sold").toFRDate()
    if (values.containsKey("date_creation")) property.dateCreation = values.getAsString("date_creation")
    if (values.containsKey("date_modified")) property.dateModified = values.getAsString("date_modified")

    Log.e("ContentValues", "Property : $property")
    return property
}
