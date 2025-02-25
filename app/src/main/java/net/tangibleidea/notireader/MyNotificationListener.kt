package net.tangibleidea.notireader

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.tangibleidea.notireader.model.NotificationData

class MyNotificationListener : NotificationListenerService() {

    companion object {
        private val _notifications = MutableLiveData<List<NotificationData>>(emptyList())
        val notifications: LiveData<List<NotificationData>> = _notifications

        fun addNotification(notification: NotificationData) {
            val currentList = _notifications.value ?: emptyList()
            _notifications.postValue(currentList + notification)
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let { notification ->
            val extras = notification.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: "Unknown"
            val text = extras.getString(Notification.EXTRA_TEXT) ?: "No message"
            val packageName = notification.packageName

            if(packageName == "com.google.android.apps.messaging") {
                val newNotification = NotificationData(title, text, packageName)
                Log.d("NotificationListener", "Received: $newNotification")

                // new message.
                addNotification(newNotification)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        Log.d("NotificationListener", "Notification removed: ${sbn?.packageName}")
    }
}