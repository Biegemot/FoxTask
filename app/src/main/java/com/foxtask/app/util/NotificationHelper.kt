package com.foxtask.app.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.foxtask.app.MainActivity
import com.foxtask.app.R

object NotificationHelper {
    private const val CHANNEL_ID = "foxtask_reminders"
    private const val CHANNEL_NAME = "FoxTask Reminders"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Напоминания о задачах и привычках"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildReminderNotification(
        context: Context,
        taskId: Int,
        title: String,
        description: String? = null,
        soundEnabled: Boolean = true,
        vibrateEnabled: Boolean = true
    ): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("taskId", taskId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description ?: "Пора выполнить задачу!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (soundEnabled) {
            builder.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
        }
        if (vibrateEnabled) {
            builder.setVibrate(longArrayOf(0, 250, 250, 250))
        }

        return builder.build()
    }

    fun showReminder(context: Context, taskId: Int, title: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = buildReminderNotification(context, taskId, title)
        notificationManager.notify(taskId, notification)
    }

    fun cancelReminder(context: Context, taskId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(taskId)
    }
}
