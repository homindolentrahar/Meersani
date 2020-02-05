package com.homindolentrahar.meersani.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.databinding.NormalListItemBinding
import com.homindolentrahar.meersani.databinding.PrimaryListItemBinding
import com.homindolentrahar.meersani.databinding.VerticalListItemBinding
import com.homindolentrahar.meersani.model.SeriesResult
import com.homindolentrahar.meersani.util.Constants

class SeriesItemAdapter(
    private val type: String,
    private val onClick: (item: SeriesResult) -> Unit
) : ListAdapter<SeriesResult, RecyclerView.ViewHolder>(SeriesItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            Constants.TYPE_PRIMARY_HOLDER -> PrimaryHolder(
                PrimaryListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            Constants.TYPE_NORMAL_HOLDER -> NormalHolder(
                NormalListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            Constants.TYPE_PAGED_HOLDER -> PagedHolder(
                VerticalListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> null
        }!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (type) {
            Constants.TYPE_PRIMARY_HOLDER -> (holder as PrimaryHolder).bind(item)
            Constants.TYPE_NORMAL_HOLDER -> (holder as NormalHolder).bind(item)
            Constants.TYPE_PAGED_HOLDER -> (holder as PagedHolder).bind(item)
        }
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class PrimaryHolder(val binding: PrimaryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SeriesResult) {
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.name
            binding.tvGenre.text = item.genres
            binding.tvRating.text = item.rating.toString()
        }
    }

    class NormalHolder(val binding: NormalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SeriesResult) {
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.name
            binding.tvGenre.text = item.genres
            binding.tvRating.text = item.rating.toString()
        }
    }

    class PagedHolder(val binding: VerticalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SeriesResult) {
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.name
            binding.tvGenre.text = item.genres
            binding.tvRelease.text = Constants.getReleasedYear(item.firstAirDate)
            binding.tvRating.text = item.rating.toString()
        }
    }

    companion object SeriesItemComparator : DiffUtil.ItemCallback<SeriesResult>() {
        override fun areItemsTheSame(oldItem: SeriesResult, newItem: SeriesResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SeriesResult, newItem: SeriesResult): Boolean =
            oldItem == newItem
    }
}