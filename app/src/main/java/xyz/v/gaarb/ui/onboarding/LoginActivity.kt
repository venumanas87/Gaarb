package xyz.v.gaarb.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import xyz.v.gaarb.ui.activities.HomeActivity
import xyz.v.gaarb.R

class LoginActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth? = null

    private var emailTV: EditText? = null
    private  var passwordTV: EditText? = null
    private var progressBar: ProgressBar? = null
    var signupBtn: MaterialButton?= null
    var signinBtn: MaterialButton?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        signupBtn = findViewById(R.id.signup)
        signinBtn = findViewById(R.id.signin)
        emailTV = findViewById(R.id.emailet)
        passwordTV = findViewById(R.id.passet)
        progressBar = findViewById(R.id.progress)
        val back: ImageView = findViewById(R.id.back)

        back.setOnClickListener {
            onBackPressed()
        }
        signinBtn?.setOnClickListener {
            loginUser()
        }

        signupBtn?.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }
    }

    private fun loginUser() {
        val email = emailTV?.text.toString()
        val pass = passwordTV?.text.toString()
        if (email.isEmpty()||pass.isEmpty()){
            Toast.makeText(this,"Fill details properly", Toast.LENGTH_SHORT).show()
        }else{
            progressBar!!.visibility = View.VISIBLE
            mAuth?.signInWithEmailAndPassword(email,pass)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    progressBar!!.visibility = View.GONE
                } else {
                    val error = task.exception?.message
                    Toast.makeText(this,error,Toast.LENGTH_LONG).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        }
    }
}