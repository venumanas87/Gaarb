package xyz.v.gaarb.models

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import xyz.v.gaarb.objects.User

class UserViewModel:ViewModel() {
    var userN:MutableLiveData<User> = MutableLiveData()

    fun getUser():LiveData<User>{
        val db = FirebaseDatabase.getInstance().getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        db.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
             val newN = p0.value.toString()
                if (newN != userN.toString()){
                    val Name = p0.child("name").value.toString()
                    val ap = p0.child("ap").value.toString()
                    val level = p0.child("level").value.toString()
                    val goalsC = p0.child("goalsC").value.toString()
                    val gSold = p0.child("gSold").value.toString()
                    val userD:User = User(Name,ap,level,goalsC, gSold)
                    userN.postValue(userD)
                }
            }
        })
        // Inflate the layout for this fragment
        return userN
       }




    }
