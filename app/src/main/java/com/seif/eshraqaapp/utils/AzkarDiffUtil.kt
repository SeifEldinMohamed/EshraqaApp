package com.seif.eshraqaapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.models.Quran

class AzkarDiffUtil(
    private val oldList: List<Azkar>,
    private val newList: List<Azkar>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean { // compares content of the two lists to each other
        return oldList[oldItemPosition] == newList[newItemPosition]
    } // called when areItemsTheSame fun return true
}