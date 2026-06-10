package com.trinoka.bharatnews.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.trinoka.bharatnews.R
import com.trinoka.bharatnews.data.db.AppDatabase
import com.trinoka.bharatnews.data.repository.NewsRepository
import com.trinoka.bharatnews.databinding.FragmentBookmarksBinding
import com.trinoka.bharatnews.ui.NewsViewModelProviderFactory
import com.trinoka.bharatnews.ui.adapter.NewsAdapter
import com.trinoka.bharatnews.utils.toArticle

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private lateinit var binding: FragmentBookmarksBinding
    lateinit var viewModel: BookmarksViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarksBinding.bind(view)

        val newsRepository = NewsRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BookmarksViewModel::class.java)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_bookmarksFragment_to_articleDetailFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                val bookmarkEntity = com.trinoka.bharatnews.data.model.BookmarkEntity(
                    article_id = article.articleId,
                    title = article.title,
                    description = article.description,
                    link = article.link,
                    image_url = article.imageUrl,
                    source_name = article.sourceName,
                    pubDate = article.pubDate
                )
                viewModel.deleteArticle(bookmarkEntity)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(bookmarkEntity)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles.map { it.toArticle() })
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}