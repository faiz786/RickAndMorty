package com.example.rickandmorty.data.entities

import java.util.*

data class News(
    val id: Long,
    val title: String,
    val newsAbstract: String,
    val publishDate: Date,
    val category: String,
    val author: String,
    val source: String,
    val url: String,
    val images: List<NewsImage>
)