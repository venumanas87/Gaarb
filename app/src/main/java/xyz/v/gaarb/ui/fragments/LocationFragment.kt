package xyz.v.gaarb.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import xyz.v.gaarb.R

class LocationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        enterTransition = backward

        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = forward
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseDatabase.getInstance().getReference("users")
        val addET = view.findViewById<EditText>(R.id.add1)
        val landET = view.findViewById<EditText>(R.id.landmark_et)
        val phnET = view.findViewById<EditText>(R.id.phone_et)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val wards  = resources.getStringArray(R.array.wards)
        val spinner:Spinner = view.findViewById(R.id.spinner)
        val dbins = FirebaseDatabase.getInstance()
        val dbref = dbins.getReference("users")
        if (spinner!=null){
            val adapter = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,wards)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (wards[p2] == "WARD 4"){
                    Toast.makeText(context,"Sorry, We currently dont provide service in your area",Toast.LENGTH_LONG).show()
                }else {
                    dbref.child(uid).child("ward").setValue(wards[p2])
                }
                }


            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        db.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val ldd = p0.child("landmark").value.toString()
                if (ldd != "null") {
                    landET.setText(ldd)
                }
            }
        })


        db.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val add = p0.child("location").value.toString()
                if (add!="null")
                addET.setText(add)

            }
        })

        db.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val add = p0.child("phone").value.toString()
                if (add!="null")
               phnET.setText(add)

            }
        })

    }



}
