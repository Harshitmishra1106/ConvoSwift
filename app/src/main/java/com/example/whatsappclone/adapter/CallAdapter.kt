package com.example.whatsappclone.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.CallUserItemLayoutBinding
import com.example.whatsappclone.model.NotesModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage


class CallAdapter(var context: Context, var list: ArrayList<NotesModel>) : RecyclerView.Adapter<CallAdapter.CallViewHolder>(){
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    inner class CallViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding: CallUserItemLayoutBinding = CallUserItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        return CallViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.call_user_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val user = list[position]
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        holder.binding.userName.text = user.name
        holder.binding.likeBtn.setOnClickListener{
            val updates = mapOf<String, Any>(
                "likes" to ServerValue.increment(1) // increment by 1
            )
            database.reference.child("Notes")
                .child((user.uid.toString()))
                .updateChildren(updates)
                .addOnSuccessListener {
                    Toast.makeText(this.context, "Looks to be liked by you", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        holder.binding.downloadBtn.setOnClickListener{
                downloadPdfWithDownloadManager(context, user.bookUrl!!, user.name.toString())
        }
    }

    private fun downloadPdfWithDownloadManager(context: Context, url: String, fileName: String){
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri)
            .setTitle(fileName)
            .setDescription("Downloading PDF...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName) // Save to Downloads folder

        try {
            downloadManager.enqueue(request)
            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}