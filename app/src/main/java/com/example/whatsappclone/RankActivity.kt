package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.adapter.RankAdapter
import com.example.whatsappclone.databinding.ActivityRankBinding
import com.example.whatsappclone.model.NotesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RankActivity : AppCompatActivity() {
    private var binding: ActivityRankBinding? = null
    lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var userList: ArrayList<NotesModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userList = ArrayList()
        database.reference.child("Notes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(NotesModel::class.java)
                        userList.add(user!!)

                    }
                    userList.sortByDescending { it.likes!!.toInt() }
                    binding!!.rankRecyclerView.adapter = RankAdapter(this@RankActivity,userList)
                    checkEmptyState()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RankActivity, "$error", Toast.LENGTH_SHORT).show()
                }
            })
        binding!!.backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun checkEmptyState() {
        if (userList.isEmpty()) {
            binding!!.rankRecyclerView.visibility = View.GONE
            binding!!.emptyView.visibility = View.VISIBLE
        } else {
            binding!!.rankRecyclerView.visibility = View.VISIBLE
            binding!!.emptyView.visibility = View.GONE
        }
    }
}