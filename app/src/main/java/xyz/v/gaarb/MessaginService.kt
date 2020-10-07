package xyz.v.gaarb

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import xyz.v.gaarb.ui.activities.HomeActivity
import xyz.v.gaarb.ui.activities.OrdersActivity


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessaginService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationTitle: String? = null
        var notificationBody: String? = null

        // Check if message contains a notification payload
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
            val name = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            notificationTitle = remoteMessage.notification!!.title.toString()
            notificationBody = remoteMessage.notification!!.body
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody)
    }

    private fun sendLocalNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notificationBuilder: Notification.Builder? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder(this,"1234")
                .setAutoCancel(true) //Automatically delete the notification
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setStyle(Notification.BigTextStyle().bigText(notificationBody))
        } else {
            Notification.Builder(this)
                .setAutoCancel(true) //Automatically delete the notification
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager!!.notify(1234, notificationBuilder?.build())
    }

    companion object {
        private const val TAG = "FirebaseMessagingServce"
    }
}