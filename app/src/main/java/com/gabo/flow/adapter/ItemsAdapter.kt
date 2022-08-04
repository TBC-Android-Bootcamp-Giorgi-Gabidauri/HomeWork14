package com.gabo.flow.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabo.flow.R
import com.gabo.flow.databinding.ItemViewBinding
import com.gabo.flow.model.Content

class ItemsAdapter(
) :
    RecyclerView.Adapter<ItemsAdapter.ItemVH>() {
    private var list: List<Content.ItemModel> = emptyList()

    init {
        setLanguage(Content.Language.KA)
    }

    fun setLanguage(language: Content.Language) {
        list.forEach { it.language = language }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Content.ItemModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ItemVH(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: Content.ItemModel,
        ) {
            with(binding) {
                model.language?.let {
                    when (model.language) {
                        Content.Language.EN -> {
                            tvDescription.text = model.descriptionEN
                            tvTitle.text = model.titleEN
                        }

                        Content.Language.KA -> {
                            tvDescription.text = model.descriptionKA
                            tvTitle.text = model.titleKA
                        }
                        Content.Language.RU -> {
                            tvDescription.text = model.descriptionRU
                            tvTitle.text = model.titleRU
                        }
                    }
                }
                tvPublishDate.text = model.publishDate
                Glide.with(ivCover.context).load(model.cover)
                    .placeholder(R.drawable.ic_launcher_background).into(ivCover)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(binding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}