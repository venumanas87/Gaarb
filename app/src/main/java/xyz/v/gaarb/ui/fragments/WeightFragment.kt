package xyz.v.gaarb.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.angmarch.views.NiceSpinner
import xyz.v.gaarb.R
import xyz.v.gaarb.ui.activities.SummaryActivity
import java.util.*

class WeightFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        enterTransition = forward

        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = backward
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slider = view.findViewById<Slider>(R.id.weight_slider)
        val wtET = view.findViewById<EditText>(R.id.wtET)
        val spinner = view.findViewById<NiceSpinner>(R.id.nice_spinner)
        val cnBtn:MaterialButton = view.findViewById(R.id.contBtn)
        val db = FirebaseDatabase.getInstance().getReference("users")
        val orDb = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dataset: List<String> = LinkedList(Arrays.asList("kgs", "gms"))
        val wetC:CheckBox? = view.findViewById(R.id.wetG)
        val dryC:CheckBox? = view.findViewById(R.id.dryG)
        val medC:CheckBox? = view.findViewById(R.id.medicalG)
        val hazC:CheckBox? = view.findViewById(R.id.chemicalG)
        var ms = "kgs"
        var gList:String? = " "
        var orderNo:Int? = 0
        wtET.setText("0.0")

        orDb.collection("order").document("2020").addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("TAG", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                Log.d("TAG", "Current data: ${value.get("orderNo")}")
                orderNo = value.get("orderNo").toString().toInt()
            } else {
                Log.d("TAG", "Current data: null")
            }
        }


        spinner.attachDataSource(dataset)
        slider.addOnChangeListener{ _, value, fromUser ->
             wtET.setText(value.toString())
        }
     spinner.setOnSpinnerItemSelectedListener { parent, _, position, id ->
         ms = parent.getItemAtPosition(position).toString()
     }
        cnBtn.setOnClickListener {

            if (wetC?.isChecked!!){
                gList += "wet garbage || "
            }

            if (dryC?.isChecked!!){
                gList += "dry garbage || "
            }

            if (medC?.isChecked!!){
                gList += "Medical garbage || "
            }

            if (hazC?.isChecked!!){
                gList += "Chemical garbage || "
            }

            if (wtET.text.toString() == "0.0" || gList.toString() == " "){
                Toast.makeText(activity,"Please fill the correct details and choose type",Toast.LENGTH_SHORT).show()
            }else{
                    db.child(uid).child("orders").child(orderNo.toString()).child("types").setValue(gList.toString())
                db.child(uid).child("orders").child(orderNo.toString()).child("Weight").setValue(wtET.text.toString())
                db.child(uid).child("orders").child(orderNo.toString()).child("measure").setValue(ms)
                 startActivity(Intent(activity,SummaryActivity::class.java))
                activity?.overridePendingTransition(R.anim.screen_slide_in_from_right,R.anim.screen_slide_out_to_left)
            }
        }



    }



}