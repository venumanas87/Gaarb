package xyz.v.gaarb.adapters

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import xyz.v.gaarb.R
import xyz.v.gaarb.objects.Goal

class GoalAdapter(private val goalList: List<Goal>): RecyclerView.Adapter<GoalAdapter.GoalHolder>() {
    class GoalHolder(view: View) : RecyclerView.ViewHolder(view) {
        var goalTV = view.findViewById<TextView>(R.id.tv1)
        var taskTV = view.findViewById<TextView>(R.id.tasksTV)
        var progressBar = view.findViewById<RoundCornerProgressBar>(R.id.progress)
        var doitBtn = view.findViewById<MaterialButton>(R.id.doitBtn)
        var mrl = view.findViewById<CardView>(R.id.grl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalHolder {
     val goalCard = LayoutInflater.from(parent.context)
         .inflate(R.layout.goals_card,parent,false)
        return GoalHolder(goalCard)
    }

    override fun onBindViewHolder(holder: GoalHolder, position: Int) {
        var listC = listOf<String>("#FFB9F6CA","#FFD9C3FF","#FFDCFFB3")
         val goal = goalList[position]
         holder.goalTV.text = goal.goalQ
          holder.setIsRecyclable(false)
        holder.taskTV.text = "0/${goal.tasks}"

          holder.mrl.backgroundTintList = ColorStateList.valueOf(Color.parseColor(listC[position]))

        /*val anim = ObjectAnimator.ofInt(holder.progressBar,"progress",20).setDuration(500)
        anim.interpolator = DecelerateInterpolator()
        anim.start()*/
        holder.doitBtn.setOnClickListener {
            Toast.makeText(it.context,"Null Pointer exception",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
       return goalList.size
    }
}