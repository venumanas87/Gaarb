package xyz.v.gaarb.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.stefanodp91.android.circularseekbar.CircularSeekBar
import com.github.stefanodp91.android.circularseekbar.OnCircularSeekBarChangeListener
import com.google.android.material.button.MaterialButton
import xyz.v.gaarb.R
import xyz.v.gaarb.reciever.AlarmReceiver
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
        val back:ImageView = findViewById(R.id.back)
        val setRemBtn:MaterialButton = findViewById(R.id.set_rem)
        var days = 0
        val seekbar:CircularSeekBar = findViewById(R.id.seekbar)
        back.setOnClickListener {
            onBackPressed()
        }

        seekbar.setOnRoundedSeekChangeListener(object : OnCircularSeekBarChangeListener {
            /**
             * Progress change
             * @param CircularSeekBar
             * @param progress the progress
             */
            override fun onProgressChange(CircularSeekBar: CircularSeekBar, progress: Float) {
                Log.d("progress", "" + progress)
                days = progress.toInt()
            }

            /**
             * Indicator touched
             * @param CircularSeekBar
             */
            override fun onStartTrackingTouch(CircularSeekBar: CircularSeekBar) {}

            /**
             * Indicator released
             * @param CircularSeekBar
             */
            override fun onStopTrackingTouch(CircularSeekBar: CircularSeekBar) {}
        })

        dtll.setOnClickListener {
            val nCal = Calendar.getInstance()
            val hourm = nCal.get(Calendar.HOUR_OF_DAY)
            val minutem =nCal.get(Calendar.MINUTE)
            TimePickerDialog(this, { timePicker, i, i2 ->
                hourTV.text = i.toString()
                minTV.text = i2.toString()
                calender.set(Calendar.HOUR_OF_DAY, i)
                calender.set(Calendar.MINUTE, i2)
                calender.set(Calendar.SECOND, 0)
                calender.set(Calendar.MILLISECOND, 0)
            }, hourm, minutem, true).show()
        }

        setRemBtn.setOnClickListener {
            val alrm = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, 0)
            }

            if (days == 0){
            alrm?.set(AlarmManager.RTC_WAKEUP, calender.timeInMillis, alarmIntent)
            Toast.makeText(this,"Reminder for ${calender.time}", Toast.LENGTH_LONG).show();
               }else{
                var calll = Calendar.getInstance()
                calll = calender.clone() as Calendar
                calll.set(Calendar.MILLISECOND,days*1440*60000)
                alrm?.setRepeating(AlarmManager.RTC_WAKEUP,calender.timeInMillis,days.toLong()*1440*60000,alarmIntent)
                Toast.makeText(this,"Next reminder on ${calll.time}",Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.scree_slide_in_from_left, R.anim.screen_slide_out_to_right)
    }
}