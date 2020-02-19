package com.jeremyrempel.android.matcher.features.search.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchResponse(
    val data: List<Data>?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        val match: Int,
        val username: String,
        val age: Int,
        val city_name: String,
        val state_name: String,
        val photo: Photo
    )

    @JsonClass(generateAdapter = true)
    data class Photo(
        val full_paths: PhotoPaths
    )

    @JsonClass(generateAdapter = true)
    data class PhotoPaths(val large: String)
}
