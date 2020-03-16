package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.CampainItemBinding
import com.example.minipromoter.models.Campain


class CampainAdapter(val onClickListener: CampainOnClickListener) :
    ListAdapter<Campain, CampainAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: CampainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Campain) {
            binding.model = client
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

    companion object DiffCallBack : DiffUtil.ItemCallback<Campain>() {
        override fun areItemsTheSame(
            oldItem: Campain,
            newItem: Campain
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Campain,
            newItem: Campain
        ): Boolean {
            return oldItem.campainId == newItem.campainId
        }
    }
}

class CampainOnClickListener(val clickListener: (client: Campain) -> Unit) {
    fun onClick(client: Campain) = clickListener(client)
}