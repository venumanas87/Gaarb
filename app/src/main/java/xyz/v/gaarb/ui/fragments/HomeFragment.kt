package xyz.v.gaarb.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jaeger.library.StatusBarUtil
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.GoalAdapter
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Goal
import xyz.v.gaarb.ui.activities.SellGarbActivity

class HomeFragment : Fragment() {
    var goal:Goal? = null
    val goalList:MutableList<Goal> = ArrayList()
    val goalAdapter:GoalAdapter? = GoalAdapter(goalList)
    var viewModel:UserViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView:View = inflater.inflate(R.layout.fragment_home, container, false)
        val headTV:TextView = rootView.findViewById(R.id.helo)
        val apTV:TextView = rootView.findViewById(R.id.ap)
        val db = FirebaseDatabase.getInstance().getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val progressBar:ProgressBar = rootView.findViewById(R.id.progress)
        StatusBarUtil.setTransparent(activity)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel!!.getUser().observe(this, Observer {
                headTV.text = "Hi, ${it.name.split(" ")[0]}!"
                apTV.text = it.ap
                progressBar.visibility = View.GONE
        }
        )
        // Inflate the layout for this fragment
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sellBtn = view.findViewById<MaterialButton>(R.id.sell_garb)

        ///////////Recyclerr view//////////
        val recyclerView:RecyclerView? = view.findViewById(R.id.recyclerView)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(view.context,RecyclerView.HORIZONTAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = GoalAdapter(goalList)

        /////////////////////////////////
        sellBtn.setOnClickListener {
            startActivity(Intent(activity, SellGarbActivity::class.java))
        }
        newGoal("Sell your garbage 1 time",1)
        newGoal("Plant a tree in your surrounding",1)
        newGoal("Sell 5 bottles of plastic with your garbage",5)
    }

    private fun newGoal(goalQ:String,tasks:Int){
        goal = Goal(goalQ,tasks)
        goalList.add(goal!!)
        goalAdapter?.notifyDataSetChanged()
    }


}