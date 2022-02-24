package com.seif.eshraqaapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqaapp.DaysFragmentDirections
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.databinding.DaysItemRowBinding

class AzkarAdapter: RecyclerView.Adapter<AzkarAdapter.MyViewHolder>() {
     var azkar = emptyList<Azkar>()
   inner class MyViewHolder(private val binding: DaysItemRowBinding):RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("SetTextI18n")
            fun bind(position: Int, azkar: List<Azkar>){
                binding.txtDate.text = azkar[position].date
                binding.txtDay.text = azkar[position].dayName
                binding.txtScore.text = "${azkar[position].score} / ${azkar[position].azkar.size}"
                binding.itemRowDays.setOnClickListener {
                 val action = DaysFragmentDirections.actionDaysFragmentToAzkarFragment(azkar[position])
                    itemView.findNavController().navigate(action)
                }
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
        holder.bind(position, azkar)
    }

    override fun getItemCount(): Int {
        return azkar.size
    }

    fun addAzkar(days:List<Azkar>){
        this.azkar = days
        notifyDataSetChanged()
    }

}