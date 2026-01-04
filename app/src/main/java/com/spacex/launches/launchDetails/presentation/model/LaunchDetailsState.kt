package com.spacex.launches.launchDetails.presentation.model

import androidx.compose.runtime.Stable
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel

@Stable
data class LaunchDetailsState(
    val launchDetails: LaunchDetailsModel? = null,
    val isLoading: Boolean = false,
)
