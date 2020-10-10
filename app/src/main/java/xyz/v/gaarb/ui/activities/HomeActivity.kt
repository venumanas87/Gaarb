package xyz.v.gaarb.ui.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.firebase.messaging.FirebaseMessaging
import com.jaeger.library.StatusBarUtil
import xyz.v.gaarb.R
import xyz.v.gaarb.models.OrderViewModel
import xyz.v.gaarb.ui.fragments.HomeFragment
import xyz.v.gaarb.ui.fragments.ProfileFragment


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        StatusBarUtil.setTransparent(this)
        loadFragment(HomeFragment())
        FirebaseMessaging.getInstance().subscribeToTopic("general")
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation)
        val orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getOrderList()
        navigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard ->{
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    }
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}