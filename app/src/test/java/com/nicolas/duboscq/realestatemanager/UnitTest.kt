package com.nicolas.duboscq.realestatemanager

import com.nicolas.duboscq.realestatemanager.utils.Utils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {

    @Test
    fun convertDollarToEuroTest() {
        val dollar = 20
        assertEquals(16, Utils.convertDollarToEuro(dollar))
    }

    @Test
    fun convertEuroToDollarTest(){
        val euro = 20
        assertEquals(23,Utils.convertEuroToDollar(euro))
    }
}
