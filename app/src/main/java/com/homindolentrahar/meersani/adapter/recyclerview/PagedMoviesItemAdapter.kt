package com.homindolentrahar.meersani.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.databinding.VerticalListItemBinding
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.util.Constants

class PagedMoviesItemAdapter(private val onClick: (item: MoviesResult) -> Unit) :
    PagedListAdapter<MoviesResult, PagedMoviesItemAdapter.PagedMoviesItemHolder>(
        PagedMoviesItemComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedMoviesItemHolder =
        PagedMoviesItemHolder(
            VerticalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PagedMoviesItemHolder, position: Int) {
        val item = getItem(position) as MoviesResult
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class PagedMoviesItemHolder(val binding: VerticalListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesResult) {
            Glide.with(itemView.context).load(BuildConfig.IMAGE_BASE_URL + item.posterPath)
                .into(binding.imgPoster)
            binding.tvTitle.text = item.title
            binding.tvGenre.text = item.genres
            binding.tvRelease.text =  if (item.releaseDate != "") Constants.getReleasedYear(item.releaseDate) else "No Date"
            binding.tvRating.text = item.rating.toString()
        }
    }

    companion object PagedMoviesItemComparator : DiffUtil.ItemCallback<MoviesResult>() {
        override fun areItemsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean =
            oldItem == newItem
    }
}