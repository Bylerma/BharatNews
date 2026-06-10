package com.trinoka.bharatnews.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.trinoka.bharatnews.R
import com.trinoka.bharatnews.data.db.AppDatabase
import com.trinoka.bharatnews.data.repository.NewsRepository
import com.trinoka.bharatnews.databinding.FragmentArticleDetailBinding
import com.trinoka.bharatnews.ui.bookmarks.BookmarksViewModel
import com.trinoka.bharatnews.utils.PreferenceManager
import com.trinoka.bharatnews.utils.toBookmarkEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    private lateinit var binding: FragmentArticleDetailBinding
    private val args: ArticleDetailFragmentArgs by navArgs()
    private lateinit var viewModel: BookmarksViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleDetailBinding.bind(view)
        preferenceManager = PreferenceManager(requireContext())

        val article = args.article
        binding.article = article

        val repository = NewsRepository(AppDatabase(requireContext()))
        viewModel = BookmarksViewModel(repository)

        Glide.with(this).load(article.imageUrl).into(binding.ivArticleImage)

        binding.fabBookmark.setOnClickListener {
            viewModel.saveArticle(article.toBookmarkEntity())
            Snackbar.make(view, "Article bookmarked successfully", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnReadMore.setOnClickListener {
            openInCustomTab(article.link)
        }

        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        applyTextSize()
    }

    private fun applyTextSize() {
        lifecycleScope.launch {
            val size = preferenceManager.textSizeFlow.first()
            binding.tvTitle.textSize = size + 6
            binding.tvSource.textSize = size - 2
            binding.tvDescription.textSize = size
            binding.tvContent.textSize = size
        }
    }

    private fun openInCustomTab(url: String) {
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.primary)
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorPrimary)
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(defaultColors)
            .build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
}