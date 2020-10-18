package xyz.v.gaarb.adapters

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import xyz.v.gaarb.R
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Goal
import xyz.v.gaarb.ui.activities.SellGarbActivity

class GoalAdapter(private val goalList: List<Goal>): RecyclerView.Adapter<GoalAdapter.GoalHolder>() {


    class GoalHolder(view: View) : RecyclerView.ViewHolder(view) {

        var goalTV = view.findViewById<TextView>(R.id.tv1)
        var taskTV = view.findViewById<TextView>(R.id.tasksTV)
        var progressBar = view.findViewById<RoundCornerProgressBar>(R.id.progress)
        var doitBtn = view.findViewById<MaterialButton>(R.id.doitBtn)
        var mrl = view.findViewById<CardView>(R.id.grl)
        var progress:ProgressBar = view.findViewById(R.id.progress_circular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalHolder {
     val goalCard = LayoutInflater.from(parent.context)
         .inflate(R.layout.goals_card,parent,false)
        return GoalHolder(goalCard)
    }

    override fun onBindViewHolder(holder: GoalHolder, position: Int) {
         val listC = listOf<String>("#FFB9F6CA","#FFD9C3FF","#FFDCFFB3")
         val goal = goalList[position]
         holder.goalTV.text = goal.goalQ
         holder.setIsRecyclable(false)
         holder.mrl.backgroundTintList = ColorStateList.valueOf(Color.parseColor(listC[position]))
         val vm = ViewModelProvider(holder.progressBar.context as ViewModelStoreOwner).get(UserViewModel::class.java)
        /*holder.doitBtn.setOnClickListener {
            Toast.makeText(it.context,"Null Pointer exception",Toast.LENGTH_SHORT).show()
        }*/

        when(position){
            0->{

                vm.getUser().observe(holder.progressBar.context as LifecycleOwner, Observer {
                    holder.progress.visibility = View.GONE
                    holder.progressBar.max = goal.tasks!!.toFloat()
                    holder.progressBar.progress = it.tw.toFloat()
                    holder.taskTV.text = "${it.tw}/${goal.tasks}"
                    if (it.tw.toInt() >= goal.tasks!!){
                        holder.doitBtn.isEnabled = false
                        holder.doitBtn.text = "Done"
                        holder.taskTV.text = "${goal.tasks}/${goal.tasks}"
                    }
                })
                holder.doitBtn.setOnClickListener {
                    holder.doitBtn.context.startActivity(Intent(holder.doitBtn.context,SellGarbActivity::class.java))
                }
            }

            1->{
                vm.getUser().observe(holder.progressBar.context as LifecycleOwner, Observer {
                    holder.progress.visibility = View.GONE
                    holder.progressBar.max = goal.tasks!!.toFloat()
                    holder.progressBar.progress = it.vm.toFloat()
                    holder.taskTV.text = "${it.vm}/${goal.tasks}"
                    if (it.vm.toInt() >= goal.tasks!!){
                        holder.doitBtn.isEnabled = false
                        holder.doitBtn.text = "Done"
                        holder.taskTV.text = "${goal.tasks}/${goal.tasks}"
                    }
                })
                holder.doitBtn.setOnClickListener {
                   vm.promotevm()
                }
            }
            2->{
                holder.progress.visibility = View.GONE
                holder.taskTV.text = "0/${goal.tasks}"
            }

        }



    }

    override fun getItemCount(): Int {
       return goalList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}