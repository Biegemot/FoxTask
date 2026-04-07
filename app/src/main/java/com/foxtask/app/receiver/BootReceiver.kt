package com.foxtask.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.foxtask.app.di.ServiceLocator
import com.foxtask.app.util.AlarmManagerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BootReceiver переустанавливает напоминания после перезагрузки устройства.
 * 
 * Регистрируется в AndroidManifest.xml с:
 * - android:enabled="true"
 * - android:exported="true"
 * - <action android:name="android.intent.action.BOOT_COMPLETED" />
 * - <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 */
class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(TAG, "Boot completed, rescheduling reminders...")
            
            // Reschedule all reminders in background
            CoroutineScope(Dispatchers.IO + ServiceLocator.globalExceptionHandler).launch {
                try {
                    val taskDao = ServiceLocator.getDatabase().taskDao()
                    val tasksWithReminders = taskDao.getAllTasks().filter { 
                        it.reminderEnabled && !it.isHabit 
                    }
                    
                    tasksWithReminders.forEach { task ->
                        val reminderTime = task.reminderTime ?: return@forEach
                        val (hour, minute) = AlarmManagerHelper.parseReminderTime(reminderTime)
                        
                        AlarmManagerHelper.scheduleReminder(
                            context = context,
                            taskId = task.id,
                            title = task.title,
                            hour = hour,
                            minute = minute
                        )
                    }
                    
                    Log.i(TAG, "Rescheduled ${tasksWithReminders.size} reminders after boot")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to reschedule reminders", e)
                }
            }
        }
    }
}
