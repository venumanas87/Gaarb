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
    private val firestore = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val dbRef = firestore.collection("user").document(uid).collection("orders")
    private val db = FirebaseDatabase.getInstance().getReference("users")
    var c = 0
    var w = 0
    fun getOrderList():LiveData<List<Orders>>{

        dbRef.addSnapshotListener{value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            val ol = ArrayList<Orders>()
            for (doc in value!!){
                c++

                val order = doc.toObject(Orders::class.java)
                ol.add(order)
            }
            updateOrderNos(c)
            orderList.postValue(ol)

        }

        return orderList
    }

   fun updateOrderNos(c:Int){
        db.child(uid).child("gSold").setValue(c.toString())
    }

    fun updateTotalWeight(w:Int){

    }


}