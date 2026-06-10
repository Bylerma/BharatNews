package com.trinoka.bharatnews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trinoka.bharatnews.data.model.Article
import com.trinoka.bharatnews.databinding.ItemTrendingCardBinding

class TrendingAdapter : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    class TrendingViewHolder(val binding: ItemTrendingCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.articleId == newItem.articleId
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val binding = ItemTrendingCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrendingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root.context).load(article.imageUrl).into(ivTrendingImage)
            tvTrendingTitle.text = article.title
            tvTrendingSource.text = article.sourceName

            root.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}