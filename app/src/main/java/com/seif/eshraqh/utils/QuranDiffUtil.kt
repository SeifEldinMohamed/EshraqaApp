package com.seif.eshraqh.utils

import androidx.recyclerview.widget.DiffUtil
import com.seif.eshraqh.data.models.Quran

class QuranDiffUtil(
    private val oldList: List<Quran>,
    private val newList: List<Quran>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean { // compares content of the two lists to each other
        return oldList[oldItemPosition] == newList[newItemPosition]
    } // called when areItemsTheSame fun return true
}