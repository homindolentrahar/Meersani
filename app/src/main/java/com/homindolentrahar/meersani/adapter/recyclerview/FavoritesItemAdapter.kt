package com.homindolentrahar.meersani.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homindolentrahar.meersani.databinding.NormalListItemBinding
import com.homindolentrahar.meersani.model.Favorites

class FavoritesItemAdapter(private val onClick: (item: Favorites) -> Unit) :
    ListAdapter<Favorites, FavoritesItemAdapter.FavoritesItemHolder>(FavoritesItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesItemHolder =
        FavoritesItemHolder(
            NormalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FavoritesItemHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class FavoritesItemHolder(val binding: NormalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Favorites) {
            Glide.with(itemView.context).load(item.posterPath).into(binding.imgPoster)
            binding.tvTitle.text = item.title
            binding.tvGenre.text = item.genres
            binding.tvRating.text = item.rating.toString()
        }
    }

    companion object FavoritesItemComparator : DiffUtil.ItemCallback<Favorites>() {
        override fun areItemsTheSame(oldItem: Favorites, newItem: Favorites): Boolean =
            oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: Favorites, newItem: Favorites): Boolean =
            oldItem == newItem
    }
}