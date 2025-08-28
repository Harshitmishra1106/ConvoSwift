package com.example.whatsappclone.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.MainActivity2
import com.example.whatsappclone.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)

        // get storedVerificationId from the intent
        val storedVerificationId= intent.getStringExtra("storedVerificationId")

        // fill otp and call the on click on button
        binding.button.setOnClickListener {
            val email = binding.emailID1.text.toString()
            val pass = binding.pass.text.toString()
            val repass = binding.repass.text.toString()

            if (email.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            }

            if (pass != repass) {
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                    .show()
            }

            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity2::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Registration Failed ", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.signIn.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity2::class.java))
            finish()
        }
    }
}