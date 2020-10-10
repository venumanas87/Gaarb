package xyz.v.gaarb.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaeger.library.StatusBarUtil
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.OrderAdapter
import xyz.v.gaarb.models.OrderViewModel
import xyz.v.gaarb.objects.Orders
import java.lang.StringBuilder

class OrdersActivity : AppCompatActivity() {
    private var orderAdapter:OrderAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val TV = findViewById<TextView>(R.id.text)
        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,true)
        recyclerView.layoutManager = layoutManager
        val orderList:MutableList<Orders> = ArrayList()
        val ordrviewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val back: ImageView = findViewById(R.id.back)


        ordrviewModel.getOrderList().observe(this, Observer {

            for (doc in it!!){
                orderList.add(doc)
        }
            orderAdapter = OrderAdapter(orderList)
            recyclerView.adapter = orderAdapter
            recyclerView.scrollToPosition(it.lastIndex)
        })


        back.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.scree_slide_in_from_left,R.anim.screen_slide_out_to_right)
    }
}