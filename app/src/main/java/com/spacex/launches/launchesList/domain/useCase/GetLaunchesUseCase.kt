package com.spacex.launches.launchesList.domain.useCase

import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchesList.domain.model.LaunchPage
import com.spacex.launches.launchesList.domain.repository.LaunchesListRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetLaunchesUseCase @Inject constructor(
    private val repository: LaunchesListRepository
) {
    suspend operator fun invoke(
        cursor: String? = null,
        pageSize: Int = 20
    ): ResponseState<LaunchPage> {
        return repository.getLaunches(cursor, pageSize)
    }
}