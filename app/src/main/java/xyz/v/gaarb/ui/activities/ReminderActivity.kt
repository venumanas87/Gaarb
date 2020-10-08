package xyz.v.gaarb.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import kotlinx.serialization.json.Json.Default.context
import xyz.v.gaarb.R
import java.util.*

class ReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        val dtll:RelativeLayout = findViewById(R.id.dtll)
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        val hourTV = findViewById<TextView>(R.id.hours)
        val minTV:TextView = findViewById(R.id.mins)
        val setRemBtn:MaterialButton = findViewById(R.id.set_rem)
        dtll.setOnClickListener {
            val nCal = Calendar.getInstance()
            val hourm = nCal.get(Calendar.HOUR_OF_DAY)
            val minutem =nCal.get(Calendar.MINUTE)
            TimePickerDialog(this, { timePicker, i, i2 ->
                  hourTV.text = i.toString()
                minTV.text = i2.toString()
                calender.set(Calendar.HOUR_OF_DAY,i)
                calender.set(Calendar.MINUTE,i2)
                calender.set(Calendar.SECOND,0)
                calender.set(Calendar.MILLISECOND,0)
            },hourm,minutem,true).show()
        }

        setRemBtn.setOnClickListener {
            val alrm = getSystemService(  Context.ALARM_SERVICE) as? AlarmManager
            val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, 0)
            }
            alrm?.set(AlarmManager.RTC_WAKEUP,calender.timeInMillis,alarmIntent)
            //Toast.makeText(this,"Reminder for ${calender.time}",Toast.LENGTH_LONG).show();
        }


    }
}