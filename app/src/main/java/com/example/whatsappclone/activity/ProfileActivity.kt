package com.example.whatsappclone.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.whatsappclone.MainActivity2
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityProfileBinding
import com.example.whatsappclone.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private lateinit var selectedImg:Uri
    private lateinit var dialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = AlertDialog.Builder(this)
                 .setMessage("Updating Profile")
                 .setCancelable(false)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth  = FirebaseAuth.getInstance()

        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.logo,null)
        selectedImg = Uri.parse(drawable?.toBitmap().toString())

        database.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(auth.currentUser?.displayName!=null){
                        Toast.makeText(applicationContext, "Your profile is already up to date\nPress continue button to return to chat", Toast.LENGTH_SHORT).show()
                        binding.userImage.setOnClickListener{
                            Toast.makeText(applicationContext, "Cannot update it", Toast.LENGTH_SHORT).show()
                        }
                        binding.continueBtn.setOnClickListener{
                            startActivity(Intent(applicationContext,MainActivity2::class.java))
                            finish()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Please enter your details", Toast.LENGTH_SHORT).show()
                }

            })
        binding.userImage.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
        binding.continueBtn.setOnClickListener{
            if(binding.userName.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
            }else if(binding.userImage ==null){
                Toast.makeText(this, "Please Select Your Image", Toast.LENGTH_SHORT).show()
            }else if(binding.phoneNo.text!!.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
            }else uploadData()
        }
        binding.logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUri: String) {
        val user = UserModel(auth.uid.toString(),binding.userName.text.toString(),
            binding.phoneNo.text.toString(),auth.currentUser?.email.toString(),imgUri)
        database.reference.child("users")
            .child((auth.uid.toString()))
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity2::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!=null){
            if(data.data!=null){
                selectedImg = data.data!!
                binding.userImage.setImageURI(selectedImg)
            }
        }
    }
}