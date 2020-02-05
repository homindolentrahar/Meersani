package com.homindolentrahar.meersani.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.databinding.CastListItemBinding
import com.homindolentrahar.meersani.model.CastResult

class CastItemAdapter :
    ListAdapter<CastResult, CastItemAdapter.CastItemHolder>(CastItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemHolder =
        CastItemHolder(
            CastListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CastItemHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CastItemHolder(val binding: CastListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastResult) {
            Glide.with(itemView.context).load(BuildConfig.IMAGE_BASE_URL + item.profilePath)
                .into(binding.imgPoster)
            binding.tvCastName.text = item.name
        }
    }

    companion object CastItemComparator : DiffUtil.ItemCallback<CastResult>() {
        override fun areItemsTheSame(oldItem: CastResult, newItem: CastResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CastResult, newItem: CastResult): Boolean =
            oldItem == newItem
    }
}