package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.whatsappclone.activity.NumberActivity
import com.example.whatsappclone.activity.ProfileActivity
import com.example.whatsappclone.adapter.ViewPagerAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.example.whatsappclone.ui.CallFragment
import com.example.whatsappclone.ui.ChatFragment
import com.example.whatsappclone.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding!!.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            finish()
        }

        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding!!.viewPager.adapter = adapter

        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
        binding!!.profileBtn.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
            finish()
        }

    }
}