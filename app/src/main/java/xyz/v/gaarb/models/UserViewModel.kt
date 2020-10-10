package xyz.v.gaarb.models

import android.view.View
import androidx.annotation.IntegerRes
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


        db.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
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
                    val tw = p0.child("tw").value.toString()
                    var vm = p0.child("vm").value.toString()
                    val vl = p0.child("vl").value.toString()
                    if (vm =="null"){vm = 0.toString()}
                    val userD = User(Name,ap,level,goalsC, gSold,tw,vm,vl)
                    userN.postValue(userD)
                }
            }
        })
        // Inflate the layout for this fragment
        return userN
       }

    fun promotevm(){
        val db = FirebaseDatabase.getInstance().getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var vmm = 0
        db.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val vm = snapshot.child("vm").value.toString().toFloat()
                    vmm = vm.toInt()+1
                    db.child(uid).child("vm").setValue(vmm)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun calWeight() {
        val db = FirebaseDatabase.getInstance().getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var w = 0
        db.child(uid).child("orders").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val ww = ds.child("Weight").value.toString().toFloat()
                    w += ww.toInt()
                    db.child(uid).child("tw").setValue(w)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}





