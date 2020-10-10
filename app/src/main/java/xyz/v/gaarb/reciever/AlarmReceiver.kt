package xyz.v.gaarb.reciever

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import xyz.v.gaarb.R
import xyz.v.gaarb.ui.activities.OrdersActivity

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        //Toast.makeText(p0,"This is alarm",Toast.LENGTH_SHORT).show()
        sendLocalNotification("Garbage Reminder !!!","Sell your garbage today",p0)
    }

    private fun sendLocalNotification(notificationTitle: String?, notificationBody: String?,context:Context?) {
        val intent = Intent(context, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notificationBuilder: NotificationCompat.Builder? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context!!,"1234")
                .setAutoCancel(true) //Automatically delete the notification
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
        } else {
            NotificationCompat.Builder(context)
                .setAutoCancel(true) //Automatically delete the notification
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
        }
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager!!.notify(1234, notificationBuilder?.build())
    }


}
