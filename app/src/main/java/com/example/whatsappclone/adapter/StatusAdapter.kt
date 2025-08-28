package com.example.whatsappclone.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.StoryActivity
import com.example.whatsappclone.databinding.ChatUserItemLayoutBinding
import com.example.whatsappclone.databinding.StatusItemLayoutBinding
import com.example.whatsappclone.model.StatusModel
import com.example.whatsappclone.model.UserModel
import kotlinx.coroutines.time.withTimeoutOrNull

class StatusAdapter(var context: Context, var list: ArrayList<StatusModel>)  : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding: StatusItemLayoutBinding = StatusItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.status_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.adStatus)
        holder.binding.name.text = user.name
        holder.itemView.setOnClickListener{
            //val imageBitmap = holder.binding.adStatus.drawToBitmap()
            val intent = Intent(context, StoryActivity::class.java)
            intent.putExtra("imageUrl", user.imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}