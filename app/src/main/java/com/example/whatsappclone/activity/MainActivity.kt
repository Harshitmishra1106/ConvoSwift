package com.example.whatsappclone.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone.MainActivity2
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity2::class.java))
            finish()
        }
        binding.button.setOnClickListener {
            if (binding.emailId.text!!.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email-id", Toast.LENGTH_SHORT).show()
            }
            if (binding.password.text!!.isEmpty()) {
                //login
                Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
            } else {
                login()
            }
        }
        binding.signup.setOnClickListener{
            startActivity(Intent(applicationContext, OTPActivity::class.java))
            finish()
        }
    }
    private fun login() {
        val email = binding.emailId.text.toString()
        val pass = binding.password.text.toString()

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this, "Logged In Successfully\nNow please update your profile", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity2::class.java))
                finish()
                Log.d("WhatsAppClone" , "onVerificationCompleted Success")
            }else{
                Toast.makeText(this, "Logging In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}