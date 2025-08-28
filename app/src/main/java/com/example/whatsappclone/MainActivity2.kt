package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebSettings.TextSize
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.whatsappclone.activity.MainActivity
import com.example.whatsappclone.activity.ProfileActivity
import com.example.whatsappclone.adapter.ViewPagerAdapter
import com.example.whatsappclone.databinding.ActivityMain2Binding
import com.example.whatsappclone.ui.CallFragment
import com.example.whatsappclone.ui.ChatFragment
import com.example.whatsappclone.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class MainActivity2 : AppCompatActivity() {
    private var binding : ActivityMain2Binding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding!!.viewPager.adapter = adapter

        for (i in 0 until binding!!.tabs.tabCount) {
            val tab = binding!!.tabs.getTabAt(i)
            val textView = LayoutInflater.from(this).inflate(R.layout.tab_item, null) as TextView
            textView.text = ViewPagerAdapter.TAB_TITLES[i]
            tab?.customView = textView
        }

        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
        binding!!.profileBtn.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        binding!!.leaderboard.setOnClickListener{
            startActivity(Intent(this,RankActivity::class.java))
        }

    }
}