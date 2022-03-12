package com.seif.eshraqaapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.databinding.DaysItemRowBinding
import com.seif.eshraqaapp.ui.fragments.PrayerDaysFragmentDirections

class PrayerAdapter : RecyclerView.Adapter<PrayerAdapter.MyViewHolder>() {
    var prayerList = emptyList<Prayer>()

    inner class MyViewHolder(private val binding: DaysItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, prayerList: List<Prayer>) {
            binding.txtDate.text =
                "${prayerList[position].currentDay} / ${prayerList[position].currentMonth} / ${prayerList[position].currentYear} "
           binding.txtDay.text = prayerList[position].dayName

            when {
                AppSharedPref.readPrayerOnly("prayerOnly", false) -> {
                    // 0/5
                    binding.txtScore.text =
                        "${prayerList[position].totalScore}/5"
                }
                AppSharedPref.readPrayerAndQadaa("prayerAndQadaa", false) -> {
                    // 0/10
                    binding.txtScore.text =
                        "${prayerList[position].totalScore}/10"
                }
                AppSharedPref.readPrayerAndSonn("prayerAndSonn", false) -> {

                    binding.txtScore.text =
                        "${prayerList[position].totalScore}/${prayerList[position].sonnHashMap.size + 5}"
                }
            }
            binding.itemRowDays.setOnClickListener {
                val action = PrayerDaysFragmentDirections.actionPrayerDaysFragmentToPrayerFragment(prayerList[position])
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
        holder.bind(position, prayerList)
    }

    override fun getItemCount(): Int {
        return prayerList.size
    }

    fun addPrayerList(prayers: List<Prayer>) {
        this.prayerList = prayers
        notifyDataSetChanged()
    }
}