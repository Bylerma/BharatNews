package com.trinoka.bharatnews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trinoka.bharatnews.data.model.Article
import com.trinoka.bharatnews.databinding.ItemFeaturedCardBinding

class FeaturedAdapter : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>() {

    class FeaturedViewHolder(val binding: ItemFeaturedCardBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val binding = ItemFeaturedCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeaturedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            Glide.with(root.context).load(article.imageUrl).into(ivFeaturedImage)
            tvFeaturedTitle.text = article.title
            tvFeaturedSource.text = "${article.sourceName} · ${article.pubDate}"

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