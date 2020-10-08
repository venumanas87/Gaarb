package xyz.v.gaarb.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.w3c.dom.Text
import xyz.v.gaarb.ui.onboarding.MainActivity
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.SettingsAdapter
import xyz.v.gaarb.models.OrderViewModel
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Settings
import xyz.v.gaarb.ui.activities.AdminActiviy
import xyz.v.gaarb.ui.activities.BuyGarbageActivity
import xyz.v.gaarb.ui.activities.OrdersActivity
import xyz.v.gaarb.ui.activities.ReminderActivity
import xyz.v.gaarb.ui.onboarding.LoginActivity


class ProfileFragment : Fragment() {

    var settings:Settings? = null
    var sList:MutableList<Settings> = ArrayList()
    var settingAdapter:SettingsAdapter = SettingsAdapter(sList)
    var viewModel: UserViewModel? = null
    var orderViewModel:OrderViewModel? = null
    var p = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View? = inflater.inflate(R.layout.fragment_profile, container, false)
        activity?.window?.statusBarColor = Color.parseColor("#E6FFED")
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel!!.getOrderList()
        val nameTV = rootView?.findViewById<TextView>(R.id.name)
        /////////////
        nameTV?.setOnClickListener {
            p++
            if (p==3)
          startActivity(Intent(activity,AdminActiviy::class.java))
        }

        ///////////
        val apTV:TextView? = rootView?.findViewById(R.id.ap)
        val gcTV:TextView? = rootView?.findViewById(R.id.gc)
        val gsTV:TextView? = rootView?.findViewById(R.id.gs)
        val levelTV:TextView? = rootView?.findViewById(R.id.level)
        val loadBand:RelativeLayout? = rootView?.findViewById(R.id.loadBand)
        viewModel!!.getUser().observe(this, Observer {
            nameTV?.text = it.name
            apTV?.text = it.ap
            gcTV?.text = it.goalsC
            gsTV?.text = it.gSold
            levelTV?.text = it.level
            loadBand?.visibility = View.GONE
        }
        )


        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ///////////Recyclerr view//////////
        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context,
            RecyclerView.VERTICAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = SettingsAdapter(sList)
        ///////////////////////////////////////
        newSet("My Orders",R.drawable.ic_baseline_list_alt_24) { startOrder() }
        newSet("Points System",R.drawable.nuevo_sol){ startOrder() }
        newSet("Buy Garbage",R.drawable.ic_baseline_monetization_on_24){ startBuyGarb() }
        newSet("Set Reminder",R.drawable.bronze_2){ startReminder() }
        newSet("Sign Out",R.drawable.ic_baseline_play_for_work_24){ signOut() }
        newSet("About",R.drawable.ic_baseline_access_alarm_24){ startOrder() }
        /////////////////////////////////
    }
    private fun returnTOMain() {
        activity?.finish()
        startActivity(Intent(activity, MainActivity::class.java))
    }
    fun newSet(tit:String,image:Int,func:()->(Unit)){
        settings = Settings(image,tit) { func() }
        sList.add(settings!!)
        settingAdapter.notifyDataSetChanged()
    }
    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        FirebaseMessaging.getInstance().deleteToken()
        startActivity(Intent(activity,MainActivity::class.java))
        activity?.finish()
    }
    private fun startOrder(){
        activity?.startActivity(Intent(activity,OrdersActivity::class.java))
        activity?.overridePendingTransition(R.anim.screen_slide_in_from_right,R.anim.screen_slide_out_to_left)
    }
    private fun startBuyGarb(){
        activity?.startActivity(Intent(activity,BuyGarbageActivity::class.java))
        activity?.overridePendingTransition(R.anim.screen_slide_in_from_right,R.anim.screen_slide_out_to_left)
    }
    private fun startReminder(){
        activity?.startActivity(Intent(activity,ReminderActivity::class.java))
        activity?.overridePendingTransition(R.anim.screen_slide_in_from_right,R.anim.screen_slide_out_to_left)
    }

    override fun onResume() {
        super.onResume()
        p=0
    }

}