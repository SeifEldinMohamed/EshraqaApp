package com.seif.eshraqh.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqh.data.models.Azkar
import com.seif.eshraqh.databinding.DaysItemRowBinding
import com.seif.eshraqh.ui.fragments.AzkarDaysFragmentDirections
import com.seif.eshraqh.utils.AzkarDiffUtil

class AzkarAdapter: RecyclerView.Adapter<AzkarAdapter.MyViewHolder>() {
     var azkar = emptyList<Azkar>()
   inner class MyViewHolder(private val binding: DaysItemRowBinding):RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("SetTextI18n")
            fun bind(position: Int, azkar: List<Azkar>){
                binding.txtDate.text = azkar[position].date
                binding.txtDay.text = azkar[position].dayName
                binding.txtScore.text = "${azkar[position].score} / ${azkar[position].azkar.size}"
                binding.itemRowDays.setOnClickListener {
                 val action = AzkarDaysFragmentDirections.actionDaysFragmentToAzkarFragment(azkar[position])
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
            val diffUtilCallBack = AzkarDiffUtil(this.azkar, days)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallBack)
            this.azkar = days
            diffUtilResult.dispatchUpdatesTo(this)
    }
}