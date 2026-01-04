package com.spacex.launches.launchDetails.domain.model

data class LaunchDetailsModel(
    val id: String,
    val site: String,
    val missionName: String,
    val missionPatchUrl: String?,
    val rocketId: String?,
    val rocketName: String,
    val rocketType: String,
)