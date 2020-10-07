package xyz.v.gaarb.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.AdminOrderAdapter
import xyz.v.gaarb.adapters.OrderAdapter
import xyz.v.gaarb.models.AllOrdersViewModel

class AdminActiviy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_activiy)
        FirebaseMessaging.getInstance().subscribeToTopic("admin")

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL,true)
        recyclerView.layoutManager = layoutManager

        val vm:AllOrdersViewModel = ViewModelProvider(this).get(AllOrdersViewModel::class.java)
        vm.getOrderList().observe(this, Observer {
           val orderAdapter = AdminOrderAdapter(it)
            recyclerView.adapter = orderAdapter
            recyclerView.scrollToPosition(it.lastIndex)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin")
    }
}