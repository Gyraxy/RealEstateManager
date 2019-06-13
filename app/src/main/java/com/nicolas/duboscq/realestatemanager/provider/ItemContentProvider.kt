package com.nicolas.duboscq.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.models.fromContentValues

class ItemContentProvider : ContentProvider() {

    private val AUTHORITY = "com.nicolas.duboscq.realestatemanager.provider"
    private val TABLE_NAME = Property::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null && values != null) {
            Log.e("EstateContentProvider", "ContentValues : $values")
            val index = AppDatabase.getDatabase(context).propertyDao().insert(fromContentValues(values))
            if (index != 0L) {
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, index)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        if (context != null) {
            val index: Long = ContentUris.parseId(uri)
            val cursor = AppDatabase.getDatabase(context).propertyDao().getItemsWithCursor(index)
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        if (context != null && values != null) {
            val count: Int = AppDatabase.getDatabase(context).propertyDao().updateProperty(fromContentValues(values))
            context.contentResolver.notifyChange(uri, null)
            return count
        }

        throw IllegalArgumentException("Failed to update row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

}