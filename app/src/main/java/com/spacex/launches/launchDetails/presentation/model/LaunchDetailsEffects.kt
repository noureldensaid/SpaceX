package com.spacex.launches.launchDetails.presentation.model

sealed interface LaunchDetailsEffects {
    data class ShowErrorMessage(val message: String) : LaunchDetailsEffects
}