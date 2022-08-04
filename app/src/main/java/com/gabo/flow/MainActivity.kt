package com.gabo.flow

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabo.flow.adapter.ItemsAdapter
import com.gabo.flow.databinding.ActivityMainBinding
import com.gabo.flow.model.Content
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainVM by viewModels()
    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter().also {
            binding.rvItems.adapter = it
            binding.rvItems.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.viewState.collect {
                binding.sRL.isRefreshing = viewModel.viewState.value.isLoading
                viewModel.viewState.value.isLoading.also { isLoading ->
                    binding.ctvEN.isVisible = !isLoading
                    binding.ctvRU.isVisible = !isLoading
                    binding.ctvKA.isVisible = !isLoading
                }
                if (it.isSuccessful == true) {
                    adapter.submitList(viewModel.viewState.value.list!!.content)
                } else {
                    viewModel.viewState.value.errorMsg?.let {msg ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupClickListeners() {
        with(binding) {
            ctvKA.isChecked = true
            ctvEN.setOnClickListener {
                adapter.setLanguage(Content.Language.EN)
                adapter.notifyDataSetChanged()
                ctvEN.isChecked = true
                ctvRU.isChecked = false
                ctvKA.isChecked = false
            }
            ctvKA.setOnClickListener {
                adapter.setLanguage(Content.Language.KA)
                adapter.notifyDataSetChanged()
                ctvKA.isChecked = true
                ctvEN.isChecked = false
                ctvRU.isChecked = false
            }
            ctvRU.setOnClickListener {
                adapter.setLanguage(Content.Language.RU)
                adapter.notifyDataSetChanged()
                ctvRU.isChecked = true
                ctvKA.isChecked = false
                ctvEN.isChecked = false
            }
            sRL.setOnRefreshListener {
                adapter.submitList(emptyList())
                viewModel.getList()
            }
        }
    }
}