package xyz.v.gaarb.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.orders_card.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.v.gaarb.R
import xyz.v.gaarb.models.OrderViewModel
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Orders
import java.io.File

class AdminOrderAdapter(private val ordrList:List<Orders>):RecyclerView.Adapter<AdminOrderAdapter.mvh>() {

    class mvh(view: View):RecyclerView.ViewHolder(view){
        val idTV: TextView = view.findViewById(R.id.orderId)
        val dateTV: TextView = view.findViewById(R.id.idTV)
        val addTV: TextView = view.findViewById(R.id.add)
        val wtTV:TextView = view.findViewById(R.id.wtTV)
        val amtTV:TextView = view.findViewById(R.id.amtTV)
        val nameTV:TextView = view.findViewById(R.id.name)
        val btn:MaterialButton = view.findViewById(R.id.allo_btn)
        val statusTV:TextView = view.findViewById(R.id.status)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mvh {
        val ordersCard = LayoutInflater.from(parent.context).inflate(R.layout.admin_order_card,parent,false)
        return mvh(ordersCard)
    }

    override fun onBindViewHolder(holder: mvh, position: Int) {
        val obj = ordrList[position]
        val orDb = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val allocate = holder.addTV.context.getString(R.string.allocate_collector)
        val payamount = holder.addTV.context.getString(R.string.pay_amount)
        holder.idTV.text = "#${obj.id}"
        holder.addTV.text = obj.Address
        holder.dateTV.text = obj.dateTime
        holder.amtTV.text = obj.amount
        holder.wtTV.text = obj.weight
        holder.nameTV.text = obj.name
        holder.statusTV.text = obj.status
        when(obj.status){
            "Confirmed" ->{
                holder.statusTV.setTextColor(Color.parseColor("#FFFFAB00"))
                holder.btn.isEnabled = true
            }
            "confirmed" ->{
                holder.statusTV.setTextColor(Color.parseColor("#FFFFAB00"))
                holder.btn.isEnabled = true
            }
            "Cancelled"->{
                holder.statusTV.setTextColor(Color.parseColor("#FFFF5252"))
            }
            "Processing"->{
                holder.statusTV.setTextColor(Color.parseColor("#FFFFAB00"))
                holder.btn.isEnabled = true
                holder.btn.text = payamount
            }

        }
      //    val vm:OrderViewModel = ViewModelProvider(holder.idTV.context as ViewModelStoreOwner).get(OrderViewModel::class.java)

        holder.btn.setOnClickListener {

            when(holder.btn.text){
                allocate->{


                    orDb.collection("user").document(uid).collection("orders")
                        .document(obj.id.takeLast(3)).update("status","Processing")
                    orDb.collection("all").document(obj.id.takeLast(3)).update("status","Processing")
                    holder.btn.text = payamount
                        CoroutineScope(IO).launch {

                            val (ignoredRequest, ignoredResponse, result) = Fuel.post("https://fcm.googleapis.com/fcm/send")
                                .header("Authorization" to "key=AAAAhrwxqtk:APA91bGY5yMA28eZ3go5wqtseuCwEjsIKW_bLrS7xlDxtF4U2ZlMOw-v1h06KlacPmqqDSy0MIxqu_trSidwhb7FnPX3OKXVb0vM0F-S4SkAvdplwUWC9OiBuKEnCKH5c4_wCOZMChIK")
                                .header(Headers.CONTENT_TYPE to "application/json")
                                .jsonBody("{\n" +
                                        "  \"to\":\n" +
                                        "  \"/topics/${obj.id.takeLast(3)}\"\n" +
                                        ",\n" +
                                        "  \"data\": {\n" +
                                        "    \"extra_information\": \"Collector Alloted\"\n" +
                                        "  },\n" +
                                        "  \"notification\": {\n" +
                                        "    \"title\": \"Collector Alloted\",\n" +
                                        "    \"text\": \"Mr RAjesh is on the way to pickup your order\",\n" +
                                        "    \"click_action\": \"SOMEACTIVITY\"\n" +
                                        "  }\n" +
                                        "}")
                                .responseString()

//do something with result

                            println(result.toString())


                        }
                }

                payamount->{
                         holder.btn.text = payamount
                        CoroutineScope(IO).launch {
                            val (ignoredRequest, ignoredResponse, result) = Fuel.post("https://fcm.googleapis.com/fcm/send")
                                .header("Authorization" to "key=AAAAhrwxqtk:APA91bGY5yMA28eZ3go5wqtseuCwEjsIKW_bLrS7xlDxtF4U2ZlMOw-v1h06KlacPmqqDSy0MIxqu_trSidwhb7FnPX3OKXVb0vM0F-S4SkAvdplwUWC9OiBuKEnCKH5c4_wCOZMChIK")
                                .header(Headers.CONTENT_TYPE to "application/json")
                                .jsonBody("{\n" +
                                        "  \"to\":\n" +
                                        "  \"/topics/${obj.id.takeLast(3)}\"\n" +
                                        ",\n" +
                                        "  \"data\": {\n" +
                                        "    \"extra_information\": \"Collector Alloted\"\n" +
                                        "  },\n" +
                                        "  \"notification\": {\n" +
                                        "    \"title\": \"Amount Transferred \uD83D\uDCB8 \",\n" +
                                        "    \"text\": \"An amount of \u20B9${obj.amount} has been transferred to you through you prefferd payment way. \",\n" +
                                        "    \"click_action\": \"SOMEACTIVITY\"\n" +
                                        "  }\n" +
                                        "}")
                                .responseString()

//do something with result

                            println(result.toString())


                        }

                    orDb.collection("user").document(uid).collection("orders")
                        .document(obj.id.takeLast(3)).update("status","Completed")
                    orDb.collection("all").document(obj.id.takeLast(3)).update("status","Completed")
                }
            }



        }
    }

    override fun getItemCount(): Int {
        return ordrList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}