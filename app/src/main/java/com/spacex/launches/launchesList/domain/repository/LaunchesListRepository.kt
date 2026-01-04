package com.spacex.launches.launchesList.domain.repository

import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchesList.domain.model.LaunchPage

interface LaunchesListRepository {
    suspend fun getLaunches(cursor: String?, pageSize: Int): ResponseState<LaunchPage>
}