package com.example.torem.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.torem.Activity.DetailActivity
import com.example.torem.Activity.TravelPlanActivity
import com.example.torem.data.Places
import com.example.torem.data.TravelPlan
import com.example.torem.databinding.ItemTravelPlanBinding
import com.example.torem.databinding.ItemTravelSpotBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class TPAdapter(options: FirestoreRecyclerOptions<TravelPlan>) :
    FirestoreRecyclerAdapter<TravelPlan, TPAdapter.ListViewHolder>(options) {

     var places : Places?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemTravelPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: TravelPlan) {
        holder.name.text = model.nameTP
        Glide.with(holder.itemView.context)
            .load(model.cover)
            .into(holder.photo)
        holder.itemView.setOnClickListener {
            val context = holder.name.context
            val intent = Intent(context, TravelPlanActivity::class.java)
            intent.putExtra("documentID", model.nameTP)
            context.startActivity(intent)
        }
    }
    inner class ListViewHolder(private val binding:ItemTravelPlanBinding) : RecyclerView.ViewHolder(binding.root){
        var name = binding.tpTitle
        var photo = binding.tpImage
    }
}

