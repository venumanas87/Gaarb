package xyz.v.gaarb.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.v.gaarb.R
import xyz.v.gaarb.objects.Settings

class SettingsAdapter(private val listS:List<Settings>): RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    class SettingsViewHolder(view: View):RecyclerView.ViewHolder(view){
        val TV = view.findViewById<TextView>(R.id.title)
        val IV = view.findViewById<ImageView>(R.id.icon)
        val line:View = view.findViewById(R.id.line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.settings_option_card,parent,false)
        return SettingsViewHolder(card)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val obj = listS[position]
        holder.IV.setImageResource(obj.image)
        holder.TV.text = obj.title
        if (obj.title == "Durg"){
            holder.line.visibility = View.VISIBLE
        }
        holder.TV.setOnClickListener {
            it.context.startActivity(Intent(it.context,obj.act::class.java))
        }
    }

    override fun getItemCount(): Int {
        return listS.size
    }
}