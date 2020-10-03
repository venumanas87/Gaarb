package xyz.v.gaarb.models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.v.gaarb.objects.Orders

class OrderViewModel:ViewModel() {
    private val orderList:MutableLiveData<List<Orders>> = MutableLiveData()

    fun getOrderList():LiveData<List<Orders>>{
        val firestore = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dbRef = firestore.collection("user").document(uid).collection("orders")

        dbRef.addSnapshotListener{value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            val ol = ArrayList<Orders>()
            for (doc in value!!){
                val order = doc.toObject(Orders::class.java)
                ol.add(order)
            }

            orderList.postValue(ol)

        }

        return orderList
    }
}