package com.spacex.launches.launchesList.presentation.model

sealed interface LaunchesScreenEvents {
    data object LoadLaunches : LaunchesScreenEvents
    data object LoadNextPage : LaunchesScreenEvents
    data class NavigateToDetails(val id: String) : LaunchesScreenEvents
}