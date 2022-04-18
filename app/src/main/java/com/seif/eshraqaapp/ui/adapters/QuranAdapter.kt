package com.seif.eshraqaapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.databinding.DaysItemRowBinding
import com.seif.eshraqaapp.ui.fragments.QuranDaysFragmentDirections
import com.seif.eshraqaapp.utils.QuranDiffUtil


class QuranAdapter:RecyclerView.Adapter<QuranAdapter.MyViewHolder>() {
    private var quran = emptyList<Quran>()
    inner class MyViewHolder(private val binding: DaysItemRowBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, quran: List<Quran>){
            binding.txtDay.text = quran[position].dayName
            binding.txtScore.text = "${quran[position].score}/${quran[position].quran.size}"
            binding.txtDate.text = quran[position].date
            if(quran[position].isVacation)
                binding.imgVacation.visibility = View.VISIBLE
            else
                binding.imgVacation.visibility = View.GONE

            binding.itemRowDays.setOnClickListener {
                val action = QuranDaysFragmentDirections.actionQuranDaysFragmentToQuranFragment(quran[position])
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
            holder.bind(position, quran)
    }

    override fun getItemCount(): Int {
            return quran.size
    }
    fun addQuran(quran: List<Quran>){
        val diffUtilCallBack = QuranDiffUtil(this.quran, quran)
        val diffUtilResult =DiffUtil.calculateDiff(diffUtilCallBack)
        this.quran = quran
        diffUtilResult.dispatchUpdatesTo(this)
    }
}