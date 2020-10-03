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
import org.w3c.dom.Text
import xyz.v.gaarb.ui.onboarding.MainActivity
import xyz.v.gaarb.R
import xyz.v.gaarb.adapters.SettingsAdapter
import xyz.v.gaarb.models.UserViewModel
import xyz.v.gaarb.objects.Settings
import xyz.v.gaarb.ui.activities.OrdersActivity


class ProfileFragment : Fragment() {

    var settings:Settings? = null
    var sList:MutableList<Settings> = ArrayList()
    var settingAdapter:SettingsAdapter = SettingsAdapter(sList)
    var viewModel: UserViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View? = inflater.inflate(R.layout.fragment_profile, container, false)
        activity?.window?.statusBarColor = Color.parseColor("#E6FFED")
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val nameTV = rootView?.findViewById<TextView>(R.id.name)
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
        newSet("My Orders",R.drawable.ic_baseline_list_alt_24)
        newSet("Points System",R.drawable.nuevo_sol)
        newSet("Buy Garbage",R.drawable.ic_baseline_monetization_on_24)
        newSet("Durg",R.drawable.bronze_2)
        newSet("Sign Out",R.drawable.ic_baseline_play_for_work_24)
        newSet("About",R.drawable.gold_1)
        /////////////////////////////////
    }
    private fun returnTOMain() {
        activity?.finish()
        startActivity(Intent(activity, MainActivity::class.java))
    }
    fun newSet(tit:String,image:Int){
        settings = Settings(image,tit,OrdersActivity())
        sList.add(settings!!)
        settingAdapter.notifyDataSetChanged()
    }


}