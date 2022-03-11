package com.seif.eshraqaapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.databinding.DaysItemRowBinding
import com.seif.eshraqaapp.databinding.SonnBottomSheetDialogBinding

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
                        "${prayerList[position].score}/${prayerList[position].prayers.size}"
                }
                AppSharedPref.readPrayerAndQadaa("prayerAndQadaa", false) -> {
                    // 0/10
                    binding.txtScore.text =
                        "${prayerList[position].score}/${prayerList[position].prayersAndQadaa.size}"
                }
                AppSharedPref.readPrayerAndSonn("prayerAndSonn", false) -> {
                    // 0/ 5+ {1..7}
                    var size = 0
                    prayerList[position].prayersAndSonn.forEach{
                        if (it.value)
                            size++
                    }
                    size+=5
                    binding.txtScore.text =
                        "${prayerList[position].score}/${size}"
                }
            }
            binding.itemRowDays.setOnClickListener {
                itemView.findNavController().navigate(R.id.action_prayerDaysFragment_to_prayerFragment)
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