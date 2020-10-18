package xyz.v.gaarb.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

    var totalWeight = 0f
    var totalAmount = 0f
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
        val slider1 = view.findViewById<Slider>(R.id.weight_slider1)
        val wtET1 = view.findViewById<EditText>(R.id.wtET1)
        val slider2 = view.findViewById<Slider>(R.id.weight_slider2)
        val wtET2 = view.findViewById<EditText>(R.id.wtET2)
        val slider3 = view.findViewById<Slider>(R.id.weight_slider3)
        val wtET3 = view.findViewById<EditText>(R.id.wtET3)
        val totalWeightET = view.findViewById<TextView>(R.id.totalWeight)

        val amt:TextView = view.findViewById(R.id.amt)
        val amt1:TextView = view.findViewById(R.id.amt1)
        val amt2:TextView = view.findViewById(R.id.amt2)
        val amt3:TextView = view.findViewById(R.id.amt3)
        val spinner = view.findViewById<NiceSpinner>(R.id.nice_spinner)
        val cnBtn:MaterialButton = view.findViewById(R.id.contBtn)
        val db = FirebaseDatabase.getInstance().getReference("users")
        val orDb = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dataset: List<String> = LinkedList(Arrays.asList("kgs"))
        var ms = "kgs"
        var gList:String? = " "
        var orderNo:Int? = 0
        wtET.setText("0.0")
        wtET1.setText("0.0")
        wtET2.setText("0.0")
        wtET3.setText("0.0")
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
            val price = value*5
            amt.text = "\u20B9$price"
            upte()
        }

        slider1.addOnChangeListener{ _, value, fromUser ->
            wtET1.setText(value.toString())
            val price = value*12
            amt1.text = "\u20B9$price"
            upte()
        }

        slider2.addOnChangeListener{ _, value, fromUser ->
            wtET2.setText(value.toString())
            val price = value*45
            amt2.text = "\u20B9$price"
            upte()
        }

        slider3.addOnChangeListener{ _, value, fromUser ->
            wtET3.setText(value.toString())
            val price = value*2
            amt3.text = "\u20B9$price"
            upte()
        }
        val updat = object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                  upte()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }

        wtET.addTextChangedListener(updat)
        wtET1.addTextChangedListener(updat)
        wtET2.addTextChangedListener(updat)
        wtET3.addTextChangedListener(updat)

      totalWeightET.setText(totalWeight.toString())





     spinner.setOnSpinnerItemSelectedListener { parent, _, position, id ->
         ms = parent.getItemAtPosition(position).toString()
     }
        cnBtn.setOnClickListener {

            if (wtET.text.toString() != "0.0"){
                gList += "Plastic | "
            }
            if (wtET1.text.toString() != "0.0"){
                gList += "Metal | "
            }
            if (wtET2.text.toString() != "0.0"){
                gList += "Paper | "
            }
            if (wtET3.text.toString() != "0.0"){
                gList += "Other | "
            }

            totalWeight = wtET.text.toString().toFloat() + wtET1.text.toString().toFloat() + wtET2.text.toString().toFloat() +wtET3.text.toString().toFloat()
            if (totalWeight < 5f){
                Toast.makeText(activity,"Total weight should be minimum 5 kgs",Toast.LENGTH_SHORT).show()
            }else{
                    db.child(uid).child("orders").child(orderNo.toString()).child("types").setValue(gList.toString())
                db.child(uid).child("orders").child(orderNo.toString()).child("Weight").setValue(totalWeight)
                db.child(uid).child("orders").child(orderNo.toString()).child("measure").setValue(ms)
                db.child(uid).child("orders").child(orderNo.toString()).child("amount").setValue(totalAmount)
                 startActivity(Intent(activity,SummaryActivity::class.java))
                activity?.overridePendingTransition(R.anim.screen_slide_in_from_right,R.anim.screen_slide_out_to_left)
                activity?.finish()
            }
        }



    }

    private fun upte() {
        val totalWeightET = view?.findViewById<TextView>(R.id.totalWeight)
        val wtET = view?.findViewById<EditText>(R.id.wtET)
        val wtET1 = view?.findViewById<EditText>(R.id.wtET1)
        val wtET2 = view?.findViewById<EditText>(R.id.wtET2)
        val wtET3 = view?.findViewById<EditText>(R.id.wtET3)
        val totalAmtET :TextView? = view?.findViewById(R.id.toatlAmount)
        val amt:TextView? = view?.findViewById(R.id.amt)
        val amt1:TextView? = view?.findViewById(R.id.amt1)
        val amt2:TextView? = view?.findViewById(R.id.amt2)
        val amt3:TextView? = view?.findViewById(R.id.amt3)
        val p = wtET?.text.toString().toFloat()*5
        val p1 = wtET1?.text.toString().toFloat()*12
        val p2 = wtET2?.text.toString().toFloat()*45
        val p3 = wtET3?.text.toString().toFloat()*2
        amt?.text = "\u20B9$p"
        amt1?.text = "\u20B9$p1"
        amt2?.text = "\u20B9$p2"
        amt3?.text = "\u20B9$p3"
        totalWeight = wtET?.text.toString().toFloat() + wtET1?.text.toString().toFloat() + wtET2?.text.toString().toFloat() +wtET3?.text.toString().toFloat()
        totalAmount = wtET?.text.toString().toFloat()*5 + wtET1?.text.toString().toFloat()*12 + wtET2?.text.toString().toFloat()*45 + wtET3?.text.toString().toFloat()*2
        totalWeightET?.text = totalWeight.toString()
        totalAmtET?.text = "\u20b9$totalAmount"
    }


}