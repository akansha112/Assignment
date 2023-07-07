package com.app.test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.test.R
import com.app.test.databinding.FilterLayputBinding
import com.app.test.model.FilterClass

class FilterAdapter(var context: Context, var list: ArrayList<FilterClass>) :RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    class ViewHolder(var binding: FilterLayputBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.filter_layput,parent,false))
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        holder.binding.heading.text = list[position].heading
        holder.binding.subHeading.text = list[position].subHeading
        holder.binding.image.setImageResource(list[position].icon)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}