package xyz.v.gaarb.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.BuyGarbAdapter
import xyz.v.gaarb.adapters.OrderAdapter
import xyz.v.gaarb.models.OrderViewModel
import xyz.v.gaarb.objects.Orders

class BuyGarbListActivity : AppCompatActivity() {
    private var orderAdapter:BuyGarbAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_garb_list)
        window.statusBarColor = Color.parseColor("#F3340F")
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL,true)
        recyclerView.layoutManager = layoutManager
        val orderList:MutableList<Orders> = ArrayList()
        val ordrviewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        ordrviewModel.getOrderList().observe(this, Observer {

            for (doc in it!!){
                orderList.add(doc)
            }
            orderAdapter = BuyGarbAdapter(orderList)
            recyclerView.adapter = orderAdapter
            recyclerView.scrollToPosition(it.lastIndex)
        })

    }
}