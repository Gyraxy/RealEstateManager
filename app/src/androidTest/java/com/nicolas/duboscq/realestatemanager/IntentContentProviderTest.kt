package com.nicolas.duboscq.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.provider.ItemContentProvider
import com.nicolas.duboscq.realestatemanager.utils.Utils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ItemContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    // DATA SET FOR TEST
    @Before
    fun setUp() {
        AppDatabase.TEST_MODE=true
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }


    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor = mContentResolver?.query(ContentUris.withAppendedId(ItemContentProvider().URI_ESTATE, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(0, `is`(0))
        cursor?.close()
    }

    @Test
    fun insertAndGetItem() {

        // BEFORE : Adding demo item
        val propertyUri = mContentResolver?.insert(ItemContentProvider().URI_ESTATE, generateEstate())

        // TEST
        val cursor = mContentResolver?.query(ContentUris.withAppendedId(ItemContentProvider().URI_ESTATE, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(1))
        assertThat(cursor?.moveToFirst(), `is`(true))
        assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("description")), `is`("Description Test"))
    }

    private fun generateEstate(): ContentValues {

        val values = ContentValues()
        values.put("agent", "DUBOSCQ Nicolas")
        values.put("status", "Vendu")
        values.put("price", 235000)
        values.put("surface", 90)
        values.put("room", 4)
        values.put("bedroom", 3)
        values.put("bathroom", 1)
        values.put("description", "Description Test")
        values.put("type", "Maison")
        values.put("PointOfInterest", "Ecole")
        values.put("nb_of_pictures",2)
        values.put("date_entry", "28/06/2019")
        values.put("date_sold","04/07/2019")
        values.put("date_creation",Utils.getTodayDate())
        values.put("date_modified","")

        return values
    }

    companion object {
        private const val USER_ID: Long = 1
    }
}