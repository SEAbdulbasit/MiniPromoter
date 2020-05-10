package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.CampaignMessageItemBinding
import com.example.minipromoter.databinding.KeywordItemBinding
import com.example.minipromoter.models.Keywords


class KeywordsAdapter(private val onClickListener: KeywordsClickListner) :
    ListAdapter<Keywords, KeywordsAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: KeywordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Keywords) {
            binding.model = model
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<KeywordItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.keyword_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(client)
        }
        holder.bind(client)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Keywords>() {
        override fun areItemsTheSame(
            oldItem: Keywords,
            newItem: Keywords
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Keywords,
            newItem: Keywords
        ): Boolean {
            return oldItem.campaignId == newItem.campaignId
        }
    }
}

class KeywordsClickListner(val clickListener: (model: Keywords) -> Unit) {
    fun onClick(client: Keywords) = clickListener(client)
}
