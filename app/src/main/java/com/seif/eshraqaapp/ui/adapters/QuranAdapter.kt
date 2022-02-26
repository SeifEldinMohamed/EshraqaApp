package com.seif.eshraqaapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.databinding.DaysItemRowBinding

class QuranAdapter:RecyclerView.Adapter<QuranAdapter.MyViewHolder>() {
    private var quran = emptyList<Quran>()
    inner class MyViewHolder(private val binding:DaysItemRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int, quran: List<Quran>){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                DaysItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(position, quran)
    }

    override fun getItemCount(): Int {
            return quran.size
    }
    fun addQuran(quran: List<Quran>){
        this.quran = quran
    }
}