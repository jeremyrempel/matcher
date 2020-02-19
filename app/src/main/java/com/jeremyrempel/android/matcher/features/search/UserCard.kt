package com.jeremyrempel.android.matcher.features.search

data class UserCard(
    val name: String,
    val ageLocation: String,
    val matchPercentage: Int,
    val image: String,
    val isSelected: Boolean = false
)
