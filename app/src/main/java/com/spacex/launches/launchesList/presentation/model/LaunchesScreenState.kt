package com.spacex.launches.launchesList.presentation.model

import androidx.compose.runtime.Stable
import com.spacex.launches.launchesList.domain.model.Launch

@Stable
data class LaunchesScreenState(
    val launches: List<Launch> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val cursor: String? = null,
    val hasMore: Boolean = true
)
