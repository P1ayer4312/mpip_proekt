package com.example.proekt_mpip_181190

import androidx.annotation.DrawableRes

data class RecipeCardData(
    val title: String,
    val author: String,
    val imageLink: String,
    @DrawableRes val thumbnail: Int?
)
