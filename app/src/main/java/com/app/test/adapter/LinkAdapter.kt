package com.app.test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.test.R
import com.app.test.databinding.LinkLayoutBinding
import com.app.test.model.TopLink
import com.bumptech.glide.Glide

class LinkAdapter(var context: Context, var list: List<TopLink>):RecyclerView.Adapter<LinkAdapter.ViewHolder>() {
    class ViewHolder(var binding: LinkLayoutBinding) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.link_layout,parent,false))
    }

    override fun onBindViewHolder(holder: LinkAdapter.ViewHolder, position: Int) {
        holder.binding.sample.text = list[position].title
        holder.binding.date.text = list[position].times_ago
        holder.binding.linkName.text = list[position].web_link
        holder.binding.sampleCount.text = list[position].total_clicks.toString()
        if (list[position].original_image!=null)
            Glide.with(context).load(list[position].original_image).error(R.mipmap.amazon_icon).into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}