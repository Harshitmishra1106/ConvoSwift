package com.example.whatsappclone.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.CallUserItemLayoutBinding
import com.example.whatsappclone.model.UserModel


class CallAdapter(var context: Context, var list: ArrayList<UserModel>) : RecyclerView.Adapter<CallAdapter.CallViewHolder>(){

    inner class CallViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding: CallUserItemLayoutBinding = CallUserItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        return CallViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.call_user_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.userImage)
        holder.binding.userName.text = user.name
        holder.binding.phone.text = user.number
        holder.binding.videoCall.setOnClickListener{
            Toast.makeText(this.context, "Video call facility to be added soon", Toast.LENGTH_SHORT).show()
        }
        holder.binding.phoneCall.setOnClickListener{
            Toast.makeText(this.context, "Phone call facility to be added soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}