package com.trinoka.bharatnews.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.trinoka.bharatnews.R
import com.trinoka.bharatnews.data.db.AppDatabase
import com.trinoka.bharatnews.data.repository.NewsRepository
import com.trinoka.bharatnews.databinding.FragmentHomeBinding
import com.trinoka.bharatnews.ui.NewsViewModelProviderFactory
import com.trinoka.bharatnews.ui.adapter.FeaturedAdapter
import com.trinoka.bharatnews.ui.adapter.NewsAdapter
import com.trinoka.bharatnews.ui.adapter.TrendingAdapter
import com.trinoka.bharatnews.utils.PreferenceManager
import com.trinoka.bharatnews.utils.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var featuredAdapter: FeaturedAdapter
    private lateinit var preferenceManager: PreferenceManager

    private val categories = listOf(
        Pair("🔥 Top", "top"),
        Pair("🏏 Cricket", "sports"),
        Pair("🏛️ Politics", "politics"),
        Pair("💼 Business", "business"),
        Pair("🎬 Bollywood", "entertainment"),
        Pair("📱 Tech", "technology"),
        Pair("🌍 World", "world"),
        Pair("❤️ Health", "health"),
        Pair("🔬 Science", "science"),
        Pair("🌾 Agriculture", "environment")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        preferenceManager = PreferenceManager(requireContext())

        val newsRepository = NewsRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerViews()
        setupViewPager()
        setupCategoryChips()

        viewModel.articles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { articles ->
                        newsAdapter.differ.submitList(articles)
                        trendingAdapter.differ.submitList(articles.take(10))
                        featuredAdapter.differ.submitList(articles.take(5))
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    // Loading handled by data binding and swipeRefresh
                }
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadNews(viewModel.category.value ?: "top")
        }

        newsAdapter.setOnItemClickListener { navigateToDetail(it) }
        trendingAdapter.setOnItemClickListener { navigateToDetail(it) }
        featuredAdapter.setOnItemClickListener { navigateToDetail(it) }
    }

    private fun navigateToDetail(article: com.trinoka.bharatnews.data.model.Article) {
        val bundle = Bundle().apply {
            putParcelable("article", article)
        }
        findNavController().navigate(
            R.id.action_homeFragment_to_articleDetailFragment,
            bundle
        )
    }

    private fun setupRecyclerViews() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        trendingAdapter = TrendingAdapter()
        binding.rvTrending.apply {
            adapter = trendingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupViewPager() {
        featuredAdapter = FeaturedAdapter()
        binding.vpFeatured.adapter = featuredAdapter
        TabLayoutMediator(binding.tabIndicator, binding.vpFeatured) { _, _ -> }.attach()
    }

    private fun setupCategoryChips() {
        categories.forEach { (label, category) ->
            val chip = layoutInflater.inflate(R.layout.item_category_chip, binding.chipGroupCategories, false) as Chip
            chip.text = label
            chip.id = View.generateViewId()
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.loadNews(category)
                }
            }
            binding.chipGroupCategories.addView(chip)
            if (category == "top") chip.isChecked = true
        }
    }
}