package com.spacex.launches.launchesList.domain.model

data class LaunchPage(
    val launches: List<Launch>,
    val cursor: String?,
    val hasMore: Boolean
)