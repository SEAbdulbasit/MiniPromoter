package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.CampainItemBinding
import com.example.minipromoter.models.Campaign
import java.text.DateFormat
import java.util.*


class CampainAdapter(private val onClickListener: CampainOnClickListener) :
    ListAdapter<Campaign, CampainAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: CampainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Campaign) {

            binding.model = client

            val date = Date(client.campaignCreationDate)
            val df = DateFormat.getDateTimeInstance()
            binding.date.text = df.format(date)

            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CampainItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.campain_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var client = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(client)
        }
        holder.bind(client)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Campaign>() {
        override fun areItemsTheSame(
            oldItem: Campaign,
            newItem: Campaign
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Campaign,
            newItem: Campaign
        ): Boolean {
            return oldItem.campaignId == newItem.campaignId
        }
    }
}

class CampainOnClickListener(val clickListener: (client: Campaign) -> Unit) {
    fun onClick(client: Campaign) = clickListener(client)
}