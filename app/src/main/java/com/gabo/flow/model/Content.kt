package com.gabo.flow.model

import com.google.gson.annotations.SerializedName


data class Content(
    val content: List<ItemModel>,
) {
    data class ItemModel(
        var language: Language,
        val category: String,
        val cover: String,
        @SerializedName("created_at")
        val createdAt: Long,
        val descriptionEN: String,
        val descriptionKA: String,
        val descriptionRU: String,
        val id: String,
        val isLast: Boolean,
        @SerializedName("publish_date")
        val publishDate: String?,
        val published: Int,
        val titleEN: String,
        val titleKA: String,
        val titleRU: String,
        @SerializedName("updated_at")
        val updatedAt: Long
    )

    enum class Language {
        KA,
        EN,
        RU
    }
}