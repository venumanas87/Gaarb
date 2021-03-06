package xyz.v.gaarb.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.v.gaarb.R

class ConfirmedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        val close:ImageView = findViewById(R.id.close)
        var orderNo:Int? = null
        val orDb = Firebase.firestore
        val doneAnim:LottieAnimationView = findViewById(R.id.done_anim)
        doneAnim.speed = 0.5f

        orDb.collection("order").document("2020").get()
            .addOnSuccessListener {
                orderNo = it.data?.getValue("orderNo").toString().toInt()
                initViews(orderNo!!)
            }
        close.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            overridePendingTransition(R.anim.slide_in_from_top,R.anim.screen_slide_out_to_bottom)
        }
    }

    private fun initViews(orderNo: Int) {
        val orDb = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val nameTV = findViewById<TextView>(R.id.con_name)
        val oidTV = findViewById<TextView>(R.id.oidTV)
        val dateTV = findViewById<TextView>(R.id.dateTV)
        val canclBtn = findViewById<MaterialButton>(R.id.cancel_button)
        val ordrHisBtn:MaterialButton = findViewById(R.id.order_history_btn)
        val b: Bundle? = this@ConfirmedActivity.intent.extras
        val id = b?.getString("id")
        val name = b?.getString("name")
        val dt = b?.getString("dateTime")
        val orderNoo = b?.getInt("orderNO").toString()


        nameTV.text = "Congrats $name!"
        oidTV.text = "Order Id - #$id"
        dateTV.text = "Request Placed On - $dt"

        canclBtn.setOnClickListener {
            orDb.collection("user").document(uid).collection("orders")
                .document(orderNoo.toString()).update("status","Cancelled")
            orDb.collection("all").document(orderNoo).update("status","Cancelled")
            Toast.makeText(this,"Your pickup has been Cancelled",Toast.LENGTH_SHORT).show()
        }
        ordrHisBtn.setOnClickListener {
            startActivity(Intent(this,OrdersActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,HomeActivity::class.java))
    }
}