package com.homindolentrahar.meersani.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.databinding.VerticalListItemBinding
import com.homindolentrahar.meersani.model.SeriesResult
import com.homindolentrahar.meersani.util.Constants

class PagedSeriesItemAdapter(private val onClick: (item: SeriesResult) -> Unit) :
    PagedListAdapter<SeriesResult, PagedSeriesItemAdapter.PagedSeriesItemHolder>(
        PagedSeriesItemComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedSeriesItemHolder =
        PagedSeriesItemHolder(
            VerticalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PagedSeriesItemHolder, position: Int) {
        val item = getItem(position) as SeriesResult
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class PagedSeriesItemHolder(val binding: VerticalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SeriesResult) {
            Glide.with(itemView.context).load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.name
            binding.tvGenre.text = item.genres
            binding.tvRelease.text = if (item.firstAirDate != "") Constants.getReleasedYear(item.firstAirDate) else "No Date"
            binding.tvRating.text = item.rating.toString()
        }
    }

    companion object PagedSeriesItemComparator : DiffUtil.ItemCallback<SeriesResult>() {
        override fun areItemsTheSame(oldItem: SeriesResult, newItem: SeriesResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SeriesResult, newItem: SeriesResult): Boolean =
            oldItem == newItem
    }
}