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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.orders_card.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.v.gaarb.R
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Orders
import java.io.File

class BuyGarbAdapter(private val ordrList:List<Orders>):RecyclerView.Adapter<BuyGarbAdapter.mvh>() {

    class mvh(view: View):RecyclerView.ViewHolder(view){
        val idTV: TextView = view.findViewById<TextView>(R.id.orderId)
        val dateTV: TextView = view.findViewById<TextView>(R.id.idTV)
        val typeTV: TextView = view.findViewById<TextView>(R.id.add)
        val wtTV:TextView = view.findViewById(R.id.wtTV)
        val amtTV:TextView = view.findViewById(R.id.amtTV)
        val statusTV:TextView = view.findViewById(R.id.status)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mvh {
        val ordersCard = LayoutInflater.from(parent.context).inflate(R.layout.buy_garb_card,parent,false)
        return mvh(ordersCard)
    }

    override fun onBindViewHolder(holder: mvh, position: Int) {
        val obj = ordrList[position]
        holder.typeTV.text = obj.type
        holder.dateTV.text = obj.dateTime
        val amount = obj.amount.toFloat() + 10
        holder.amtTV.text = "\u20B9$amount"
        holder.wtTV.text = obj.weight

    }

    override fun getItemCount(): Int {
        return ordrList.size
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}