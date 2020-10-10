package xyz.v.gaarb.models

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.v.gaarb.objects.GoalsL
import java.util.*

class GoalsViewModel:ViewModel() {
    val goals:MutableLiveData<GoalsL> = MutableLiveData()
    val dbref = Firebase.firestore




    fun getGoals(level:String) : LiveData<GoalsL>{
        dbref.collection("goals").addSnapshotListener { value, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (doc in value!!){
                Log.d("TAG", "getGoals:called ")
                if (doc.id.toLowerCase(Locale.ROOT) == level.toLowerCase(Locale.ROOT)) {
                    Log.d("TAG", "getGoals: $doc ")
                    goals.postValue(doc.toObject(GoalsL::class.java))
                }
            }

        }
     return goals
    }
}