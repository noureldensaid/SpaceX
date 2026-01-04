package com.spacex.launches.launchDetails.domain.useCase

import com.spacex.launches.launchDetails.domain.repository.LaunchDetailsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetLaunchDetailsUseCase @Inject constructor(
    private val repository: LaunchDetailsRepository
) {

    suspend operator fun invoke(launchId: String) = repository.getLaunchDetails(launchId)
}