package xyz.v.gaarb.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import xyz.v.gaarb.R
import xyz.v.gaarb.ui.fragments.LocationFragment
import xyz.v.gaarb.ui.fragments.WeightFragment
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

class SellGarbActivity : AppCompatActivity(), CoroutineScope {
    var job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    var nxtBtn:MaterialButton? = null
   // var backBtn:MaterialButton? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var PERMISSION_ID = 44
    val locationFragment = LocationFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lastLocation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_garb)
         loadFragment(LocationFragment())

        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onStart() {
        super.onStart()
        val CONT = "continue".toUpperCase()
        val NXT = "next".toUpperCase()
        val landET = findViewById<EditText>(R.id.landmark_et)
        val phoneET = findViewById<EditText>(R.id.phone_et)
        val wtET = findViewById<EditText>(R.id.wtET)
        val weightFragment = WeightFragment()
        val db = FirebaseDatabase.getInstance().getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        nxtBtn = findViewById(R.id.nxt)
      //  backBtn = findViewById(R.id.back)
        nxtBtn?.setOnClickListener {
            if (!(landET.text.isEmpty()||phoneET.text.isEmpty())) {
                loadFragment(WeightFragment())
                nxtBtn?.visibility = View.GONE
                db.child(uid).child("landmark").setValue(landET.text.toString()).addOnFailureListener {
                    Toast.makeText(this,"Please check your connection",Toast.LENGTH_SHORT).show()
                }
                    .addOnCanceledListener {

                    }
                db.child(uid).child("phone").setValue(phoneET.text.toString())
            }else{
                Toast.makeText(this,"Fill all the details properly",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()
    }


    @SuppressLint("MissingPermission")
    private fun lastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                       val LAT = location.latitude
                       val  LON = location.longitude
                        job = launch(Dispatchers.IO) {geo(LAT,LON)  }

                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
        }
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private val isLocationEnabled: Boolean
        private get() {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lastLocation()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            lastLocation()
        }
    }

    private fun geo(lat: Double, lon: Double) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dbins = FirebaseDatabase.getInstance()
        val dbref = dbins.getReference("users")
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.isNotEmpty()) {
                dbref.child(uid).child("location").setValue(addresses[0].getAddressLine(0).toString())
                dbref.child(uid).child("state").setValue(addresses[0].adminArea.toString())
            }else{
                Log.d(TAG, "geo: NO adress")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }



}