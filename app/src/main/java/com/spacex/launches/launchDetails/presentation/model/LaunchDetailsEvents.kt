package com.spacex.launches.launchDetails.presentation.model

sealed interface LaunchDetailsEvents {
    data object GetLaunchDetails : LaunchDetailsEvents
    data object NavigateBack : LaunchDetailsEvents
}