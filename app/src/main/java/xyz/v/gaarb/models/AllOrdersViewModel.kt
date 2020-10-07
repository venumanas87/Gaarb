package xyz.v.gaarb.models

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.v.gaarb.objects.Orders

class AllOrdersViewModel:ViewModel() {
    private val firestore = Firebase.firestore
    private val dbRef = firestore.collection("all")
    private val orderId:MutableLiveData<String> = MutableLiveData()
    private val orderList:MutableLiveData<List<Orders>> = MutableLiveData()
    fun allOrders():LiveData<String>{
          dbRef.addSnapshotListener { value, error ->
              if (error != null) {
                  Log.w(ContentValues.TAG, "Listen failed.", error)
                  return@addSnapshotListener
              }
             if (value!=null){
                 val s = value.size()
                 val n = value.toObjects(Orders::class.java)
                 val na = n[s.minus(1)]?.id
                 orderId.postValue(na)
             }

          }
        return orderId
    }

    fun getOrderList():LiveData<List<Orders>>{

        dbRef.addSnapshotListener{value, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
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