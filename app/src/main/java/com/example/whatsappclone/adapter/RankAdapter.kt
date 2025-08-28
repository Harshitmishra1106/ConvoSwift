package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.CallUserItemLayoutBinding
import com.example.whatsappclone.databinding.RankItemBinding
import com.example.whatsappclone.model.NotesModel

class RankAdapter(var context: Context, var list: ArrayList<NotesModel>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    inner class RankViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding: RankItemBinding = RankItemBinding.bind(view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAdapter.RankViewHolder {
        return RankViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rank_item,parent,false))
    }

    override fun onBindViewHolder(holder: RankAdapter.RankViewHolder, position: Int) {
        val user = list[position]
        var pos = position
        pos += 1
        holder.binding.sNo.text = (pos).toString()
        holder.binding.bkName.text = user.name.toString()
        holder.binding.tLikes.text = user.likes.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}