package xyz.v.gaarb.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jaeger.library.StatusBarUtil
import xyz.v.gaarb.ui.activities.HomeActivity
import xyz.v.gaarb.R


class MainActivity : AppCompatActivity() {
    private val emailTV: EditText? = null
    private  var passwordTV:EditText? = null
    private val progressBar: ProgressBar? = null
    private var mAuth:FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setTransparent(this)
        mAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        val signinBtn:MaterialButton = findViewById(R.id.signin)
        val signupBtn:MaterialButton = findViewById(R.id.signup)

        signinBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser!=null)updateUI()
    }

    private fun updateUI() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}