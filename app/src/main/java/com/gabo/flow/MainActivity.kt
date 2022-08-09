package com.gabo.flow

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabo.flow.adapter.ItemsAdapter
import com.gabo.flow.databinding.ActivityMainBinding
import com.gabo.flow.model.Content
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
        binding.sRL.isRefreshing = true
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getList().collect {
                    when (it) {
                        is Result.Success -> {
                            adapter.submitList(it.list)
                            binding.sRL.isRefreshing = false
                        }
                        is Result.Error -> {
                            Toast.makeText(this@MainActivity, it.errorMSg, Toast.LENGTH_SHORT)
                                .show()
                            binding.sRL.isRefreshing = false
                        }
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
                setupObservers()
            }
        }
    }
}