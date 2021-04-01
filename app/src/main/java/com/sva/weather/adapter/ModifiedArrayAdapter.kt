package com.sva.weather.adapter

import android.widget.ArrayAdapter
import java.util.Arrays

import android.content.Context


class ModifiedArrayAdapter(
    context: Context?, textViewResourceId: Int,
    private val mData: List<String>
) : ArrayAdapter<String?>(context!!, textViewResourceId, mData) {
    override fun getItemId(position: Int): Long {
        val data = getItem(position)
        val index = mData.indexOf(data)
        return if (index > 0) index.toLong() else 0
    }
}