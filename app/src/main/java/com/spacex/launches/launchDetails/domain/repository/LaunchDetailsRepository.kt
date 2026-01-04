package com.spacex.launches.launchDetails.domain.repository

import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel

interface LaunchDetailsRepository {
    suspend fun getLaunchDetails(id: String): ResponseState<LaunchDetailsModel>
}