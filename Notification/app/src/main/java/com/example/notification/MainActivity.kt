package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.notification1 -> showNotification1()
            R.id.notification2 -> showNotification2()
        }
        return super.onOptionsItemSelected(item)
    }

    private val channelID1 = "default"
    private val myNotificationID1 = 1
    private val channelID2 = "notification"
    private val myNotificationID2 = 2


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
            val channel = NotificationChannel(
                    channelID1, "default channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "description text of this channel."
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val channel2 = NotificationChannel(
                channelID2, "notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel2.description = "description text of this channel."
            val notificationManager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun showNotification1() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.danta)
        val intent = Intent(this, Noti2Acitivity::class.java)
        val pendingIntent = with(TaskStackBuilder.create(this)) {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(this, channelID1)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle("notification1")
                .setContentText("this is notification1")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        NotificationManagerCompat.from(this)
                .notify(myNotificationID1, builder.build())
    }

    private fun showNotification2() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.danta)
        val builder = NotificationCompat.Builder(this, channelID2)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bitmap)
            .setContentTitle("notification2")
            .setContentText("this is notification2")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(myNotificationID2, builder.build())
    }
}