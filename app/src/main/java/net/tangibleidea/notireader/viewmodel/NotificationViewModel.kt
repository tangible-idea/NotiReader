package net.tangibleidea.notireader.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import net.tangibleidea.notireader.MyNotificationListener
import net.tangibleidea.notireader.model.NotificationData

class NotificationViewModel : ViewModel() {
    val notifications: LiveData<List<NotificationData>> = MyNotificationListener.notifications
}