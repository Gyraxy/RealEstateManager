package com.nicolas.duboscq.realestatemanager.utils

import java.text.SimpleDateFormat

private val BASE_FORMAT = SimpleDateFormat("dd/MM/yyyy")

fun String.toFRDate() = BASE_FORMAT.parse(this)