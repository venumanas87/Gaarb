package xyz.v.gaarb.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import xyz.v.gaarb.R
import xyz.v.gaarb.models.OrderViewModel
import java.lang.StringBuilder

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val TV = findViewById<TextView>(R.id.text)
        val ordrviewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        ordrviewModel.getOrderList().observe(this, Observer {
            val str = StringBuilder()
            val s = it.size
            TV.text = it[s-1].toString()
        })
    }
}