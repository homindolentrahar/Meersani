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
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.util.Constants

class MoviesItemAdapter(
    private val type: String,
    private val onClick: (item: MoviesResult) -> Unit
) : ListAdapter<MoviesResult, RecyclerView.ViewHolder>(MoviesItemComparator) {

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
            else -> null
        }!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (type) {
            Constants.TYPE_PRIMARY_HOLDER -> (holder as PrimaryHolder).bind(item)
            Constants.TYPE_NORMAL_HOLDER -> (holder as NormalHolder).bind(item)
        }
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class PrimaryHolder(val binding: PrimaryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesResult) {
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.title
            binding.tvGenre.text = item.genres
            binding.tvRating.text = item.rating.toString()
        }
    }

    class NormalHolder(val binding: NormalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesResult) {
            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.title
            binding.tvGenre.text = item.genres
            binding.tvRating.text = item.rating.toString()
        }
    }

    companion object MoviesItemComparator : DiffUtil.ItemCallback<MoviesResult>() {
        override fun areItemsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean =
            oldItem == newItem
    }
}