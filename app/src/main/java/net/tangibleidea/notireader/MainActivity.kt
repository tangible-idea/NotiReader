package net.tangibleidea.notireader

import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.tangibleidea.notireader.model.NotificationData
import net.tangibleidea.notireader.viewmodel.NotificationViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationScreen(viewModel)
        }
    }
}

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val notificationList by viewModel.notifications.observeAsState(emptyList())
    val context = LocalContext.current
    val listState = rememberLazyListState() // LazyColumn scroll state

    // Scroll to the last item when a new notification arrives
    LaunchedEffect(notificationList.size) {
        if (notificationList.isNotEmpty()) {
            listState.animateScrollToItem(notificationList.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Grant Notification Access")
            }

//            Button(onClick = { viewModel.clearNotifications() }) {
//                Text("Clear")
//            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(state = listState) {
            items(notificationList) { notification ->
                NotificationItem(notification)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "App: ${notification.packageName}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Title: ${notification.title}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Message: ${notification.message}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}