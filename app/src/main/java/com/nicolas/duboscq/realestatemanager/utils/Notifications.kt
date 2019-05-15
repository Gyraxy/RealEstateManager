package com.nicolas.duboscq.realestatemanager.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.activities.MainActivity
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.util.Log

class Notifications {
    private val notificationID = 105

    private lateinit var mContext: Context
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mBuilder:NotificationCompat.Builder

    private val channelID = "com.nicolas.duboscq.realestatemanager"
    private val channelName = "RealEstateManager"
    private lateinit var notificationMessage : String

    /**
     * Create and push the notification
     */
    fun sendNotification(context: Context, activity: String) {
        this.mContext = context
        Log.e("TAG", "sendNotification: ")
        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (activity.equals("create")){
            notificationMessage = context.resources.getString(R.string.notification_property_created)
        } else {
            notificationMessage = context.resources.getString(R.string.notification_property_updated)
        }

        //Build notification
        val notification = buildLocalNotification(mContext, pendingIntent,notificationMessage).build()

        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelID, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            mBuilder.setChannelId(channelID)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        mNotificationManager.notify(notificationID, notification)
    }

    private fun buildLocalNotification(mContext: Context, pendingIntent: PendingIntent, message:String): NotificationCompat.Builder {
        Log.e("TAG", "buildLocalNotification: ")
        mBuilder = NotificationCompat.Builder(mContext, channelID)
        mBuilder.setSmallIcon(R.drawable.ic_home)
        mBuilder.setContentTitle(mContext.resources.getString(R.string.app_name))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return mBuilder
    }
}