package xyz.v.gaarb.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import xyz.v.gaarb.R


class SignupActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private  var passwordTV: EditText? = null
    private var progressBar: ProgressBar? = null
    var nameTV:EditText? = null
    var signupBtn:MaterialButton?= null
    var signinBtn:MaterialButton?= null

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth = FirebaseAuth.getInstance()
        signupBtn = findViewById(R.id.signup)
        emailTV = findViewById(R.id.emailet)
        nameTV = findViewById(R.id.nameet)
        passwordTV = findViewById(R.id.passet)
        progressBar = findViewById(R.id.progress)
        signupBtn?.setOnClickListener {
            RegisterUser()
        }
        signinBtn?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun RegisterUser() {
        val email:String = emailTV?.text.toString()
        val pass:String = passwordTV?.text.toString()
        val dbins = FirebaseDatabase.getInstance()
        val dbref = dbins.getReference("users")
        val name = nameTV?.text.toString()
        //val simplEmail = email.filter { it.isLetterOrDigit() }
        if (email.isEmpty()||pass.isEmpty()){
          Toast.makeText(this,"Fill details properly",Toast.LENGTH_SHORT).show()
        }else{
            progressBar?.visibility = View.VISIBLE
            mAuth!!.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val simplEmail = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        dbref.child(simplEmail).child("email").setValue(email)
                        dbref.child(simplEmail).child("name").setValue(name)
                        dbref.child(simplEmail).child("ap").setValue(0)
                        dbref.child(simplEmail).child("level").setValue("Bronze 2")
                        dbref.child(simplEmail).child("goalsC").setValue(0)
                        dbref.child(simplEmail).child("gSold").setValue(0)
                        Toast.makeText(
                            applicationContext,
                            "Registration successful!",
                            Toast.LENGTH_LONG
                        ).show()
                        progressBar!!.visibility = View.GONE
                        val intent =
                            Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(this,error,Toast.LENGTH_LONG).show()
                        progressBar!!.visibility = View.GONE
                    }
                }
        }
    }
}