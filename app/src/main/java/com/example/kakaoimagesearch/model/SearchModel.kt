package com.example.kakaoimagesearch.model

data class SearchModel (
    val sitename: String,
    val datetime: String,
    val image: String,
    var isLike: Boolean = false
)
