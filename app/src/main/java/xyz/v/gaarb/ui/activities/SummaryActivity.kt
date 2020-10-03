package xyz.v.gaarb.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.v.gaarb.R
import xyz.v.gaarb.ui.fragments.ModalFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class SummaryActivity : AppCompatActivity() {
    val orDb = Firebase.firestore
    val dbRef = FirebaseDatabase.getInstance().getReference("users")
    var addre:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        val kgTV = findViewById<TextView>(R.id.wtTV)
        val msTV = findViewById<TextView>(R.id.msTV)
        val addTV:TextView = findViewById(R.id.addTV)
        val phnTV:TextView = findViewById(R.id.phnTV)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var orderNo:Int = 0
        val langBottomSheet:RelativeLayout = findViewById(R.id.lang_rl)
        val orDb = Firebase.firestore
        val rg = findViewById<RadioGroup>(R.id.rg)
        val regTV = findViewById<TextView>(R.id.regTV)
        val noET = findViewById<EditText>(R.id.noET)
        var kg:String = ""
        var ms:String = ""
        var addre:String = ""
        var landmark:String? = ""
        var phnNo:String = ""
        val btmSht = BottomSheetBehavior.from(langBottomSheet)
        val link:MaterialButton = findViewById(R.id.link)
        val cnfBtn:MaterialButton = findViewById(R.id.cnfrmBtn)
        val cnclBtn:MaterialButton = findViewById(R.id.cnclBtn)
        val modalFragment:ModalFragment = ModalFragment()
        val ENTER_UPI = "Enter your UPI address"
        val user: MutableMap<String, Any> = HashMap()
             getDetails()

        rg.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            val ENTER_NUMBER = "Enter your registered  ${radio.text} number"
            noET.setText("")
            if(radio.text.toString() == "UPI"){
                regTV.text = ENTER_UPI
                noET.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }else{
                regTV.text = ENTER_NUMBER
                noET.inputType = InputType.TYPE_CLASS_PHONE
            }
            btmSht.state = BottomSheetBehavior.STATE_EXPANDED
            cnclBtn.visibility= View.GONE
            cnfBtn.visibility= View.GONE
        }

        link.setOnClickListener {
            btmSht.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        phnTV.setOnClickListener {
            modalFragment.show(supportFragmentManager, modalFragment.tag)
        }

        btmSht.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        cnclBtn.visibility = View.GONE
                        cnfBtn.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        cnclBtn.visibility = View.VISIBLE
                        cnfBtn.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })



    }



    @SuppressLint("SimpleDateFormat")
    private fun getDetails() {


        Log.d("TAG", "getDetails: ")
        val simpleDateFormat:SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy 'at' hh:mm")
        val idformat = SimpleDateFormat("yyyyddMMhh")
        val id = idformat.format(Date())
        val curDT:String  = simpleDateFormat.format(Date())
        val user: MutableMap<String, Any> = HashMap()
        val kgTV = findViewById<TextView>(R.id.wtTV)
        val msTV = findViewById<TextView>(R.id.msTV)
        val addTV:TextView = findViewById(R.id.addTV)
        val phnTV:TextView = findViewById(R.id.phnTV)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var orderNo:Int? = null
        val langBottomSheet:RelativeLayout = findViewById(R.id.lang_rl)
        val orDb = Firebase.firestore
        val rg = findViewById<RadioGroup>(R.id.rg)
        val regTV = findViewById<TextView>(R.id.regTV)
        val noET = findViewById<EditText>(R.id.noET)




        orDb.collection("order").document("2020").get()
            .addOnSuccessListener {
                orderNo =  it.data?.getValue("orderNo").toString().toInt()

                dbRef.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val add = p0.child("location").value.toString()
                        val landmrk = p0.child("landmark").value.toString()
                        val phone = p0.child("phone").value.toString()
                        val weight = p0.child("orders").child((orderNo).toString())
                            .child("Weight").value.toString()
                        val meas = p0.child("orders").child((orderNo).toString())
                            .child("measure").value.toString()
                        val name = p0.child("name").value.toString()

                        addTV.text = "$landmrk , $add"
                        phnTV.text = phone
                        kgTV.text = weight
                        msTV.text = meas
                        user["name"] = name
                        user["Address"] = add
                        user["landmark"] = landmrk
                        user["phone"] = phone
                        user["weight"] = "$weight $meas"
                        user["dateTime"] =  curDT
                        user["id"] = "$id$orderNo"
                        user["status"] = "confirmed"
                        initButtons(orderNo,uid,user)

                        /*orDb.collection("user").document(uid).collection("orders")
                            .document(orderNo.toString()).set(user)*/
                        Log.d("TAG", "onDataChange: Inside of listener ")
                    }
                })

            }







    }


    private fun initButtons(orderNo: Int?, uid: String, user: MutableMap<String, Any>) {
        val cnfBtn:MaterialButton = findViewById(R.id.cnfrmBtn)
        val cnclBtn:MaterialButton = findViewById(R.id.cnclBtn)
        val orDb = Firebase.firestore
        val bundle:Bundle = Bundle()
        if (orderNo != null) {
            bundle.putInt("orderNO",orderNo)
            bundle.putString("id", user["id"] as String?)
            bundle.putString("name",user["name"]as String?)
            bundle.putString("dateTime", user["dateTime"] as String?)
        }
        cnfBtn.setOnClickListener {

            orDb.collection("user").document(uid).collection("orders")
                .document(orderNo.toString()).set(user)
            orDb.collection("all").document(orderNo.toString()).set(user)
            startActivity(Intent(this, ConfirmedActivity::class.java).putExtras(bundle))
            overridePendingTransition(
                R.anim.screen_slide_in_from_right,
                R.anim.screen_slide_out_to_left
            )
            orDb.collection("order").document("2020").update("orderNo", FieldValue.increment(1))
            finishAfterTransition()
        }
        cnclBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(
                R.anim.screen_slide_out_to_left,
                R.anim.screen_slide_in_from_right
            )
            finishAfterTransition()
        }
    }
}