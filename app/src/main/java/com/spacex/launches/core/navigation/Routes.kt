package com.spacex.launches.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LaunchesScreenRoute

@Serializable
data class LaunchDetailsScreenRoute(val id: String? = null)