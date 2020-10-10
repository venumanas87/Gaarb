package xyz.v.gaarb.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.jsonBody
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
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import org.w3c.dom.Text
import xyz.v.gaarb.R
import xyz.v.gaarb.models.UserViewModel
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
        val phnTV:TextView = findViewById(R.id.phnTV)
        val langBottomSheet:RelativeLayout = findViewById(R.id.lang_rl)
        val rg = findViewById<RadioGroup>(R.id.rg)
        val regTV = findViewById<TextView>(R.id.regTV)
        val noET = findViewById<EditText>(R.id.noET)
        val btmSht = BottomSheetBehavior.from(langBottomSheet)
        val link:MaterialButton = findViewById(R.id.link)
        val cnfBtn:MaterialButton = findViewById(R.id.cnfrmBtn)
        val cnclBtn:MaterialButton = findViewById(R.id.cnclBtn)
        val modalFragment:ModalFragment = ModalFragment()
        val ENTER_UPI = "Enter your UPI address"
             getDetails()

        rg.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            noET.setText("")
            if(radio.text.toString() == "Upi"){
                regTV.text = ENTER_UPI
                noET.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                btmSht.state = BottomSheetBehavior.STATE_EXPANDED
                cnclBtn.visibility= View.GONE
                cnfBtn.visibility= View.GONE
            }
        }

        link.setOnClickListener {
            btmSht.state = BottomSheetBehavior.STATE_COLLAPSED
            Toast.makeText(this,"Upi linked to profile",Toast.LENGTH_SHORT).show()
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
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy 'at' hh:mm")
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val odate = sdf.format(Date())
        val idformat = SimpleDateFormat("yyyyddMMhh")
        val id = idformat.format(Date())
        val curDT:String  = simpleDateFormat.format(Date())
        val user: MutableMap<String, Any> = HashMap()
        val kgTV = findViewById<TextView>(R.id.wtTV)
        val addTV:TextView = findViewById(R.id.addTV)
        val phnTV:TextView = findViewById(R.id.phnTV)
        val amtTV:TextView = findViewById(R.id.amtTV)
        val amtTV1:TextView = findViewById(R.id.amtTV1)
        val nameTV:TextView = findViewById(R.id.nameTV)
        val dateTV:TextView = findViewById(R.id.dateTV)
        dateTV.text = odate
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var orderNo:Int? = null
        val orDb = Firebase.firestore
        var amount:String = ""


        orDb.collection("order").document("2020").get()
            .addOnSuccessListener {
                orderNo =  it.data?.getValue("orderNo").toString().toInt()

                dbRef.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(p0: DataSnapshot) {
                        val add = p0.child("location").value.toString()
                        val landmrk = p0.child("landmark").value.toString()
                        val phone = p0.child("phone").value.toString()
                        val weight = p0.child("orders").child((orderNo).toString())
                            .child("Weight").value.toString()
                        val meas = p0.child("orders").child((orderNo).toString())
                            .child("measure").value.toString()
                        val name = p0.child("name").value.toString()
                        nameTV.text = name
                        addTV.text = "$landmrk , $add"
                        phnTV.text ="+91 $phone"
                        kgTV.text = "$weight $meas"
                        if (meas == "gms"){
                            amount = ((weight.toFloat()/1000)*2).toString()
                        }else{
                            amount = (weight.toFloat()*2).toString()
                        }
                        amtTV.text = "\u20B9$amount"
                        amtTV1.text = "\u20B9$amount"

                        user["name"] = name
                        user["address"] = add
                        user["landmark"] = landmrk
                        user["phone"] = phone
                        user["weight"] = "$weight $meas"
                        user["dateTime"] =  curDT
                        user["id"] = "$id$orderNo"
                        user["status"] = "Confirmed"
                        user["amount"] = amount
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
             notifyAdmin(orderNo.toString(),user["name"].toString())
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
            overridePendingTransition(R.anim.scree_slide_in_from_left,R.anim.screen_slide_out_to_right)
            finishAfterTransition()
        }
    }

    private fun notifyAdmin(ordr:String, name :String) {

          FirebaseMessaging.getInstance().subscribeToTopic(ordr)
            CoroutineScope(Dispatchers.IO).launch {

                val (ignoredRequest, ignoredResponse, result) = Fuel.post("https://fcm.googleapis.com/fcm/send")
                    .header("Authorization" to "key=AAAAhrwxqtk:APA91bGY5yMA28eZ3go5wqtseuCwEjsIKW_bLrS7xlDxtF4U2ZlMOw-v1h06KlacPmqqDSy0MIxqu_trSidwhb7FnPX3OKXVb0vM0F-S4SkAvdplwUWC9OiBuKEnCKH5c4_wCOZMChIK")
                    .header(Headers.CONTENT_TYPE to "application/json")
                    .jsonBody("{\n" +
                            "  \"to\":\n" +
                            "  \"/topics/admin\"\n" +
                            ",\n" +
                            "  \"data\": {\n" +
                            "    \"extra_information\": \"This is some extra information\"\n" +
                            "  },\n" +
                            "  \"notification\": {\n" +
                            "    \"title\": \"NEW ORDER! ACTION REQUIRED\",\n" +
                            "    \"text\": \"$name with orderno $ordr\",\n" +
                            "    \"click_action\": \"SOMEACTIVITY\"\n" +
                            "  }\n" +
                            "}")
                    .responseString()

                println(result.toString())


            }
    }
}