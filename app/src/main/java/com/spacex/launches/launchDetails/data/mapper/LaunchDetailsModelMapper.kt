package com.spacex.launches.launchDetails.data.mapper

import com.spacex.launches.LaunchDetailsQuery
import com.spacex.launches.core.base.BaseMapper
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel
import javax.inject.Inject

class LaunchDetailsModelMapper @Inject constructor() : BaseMapper<
        ResponseState<LaunchDetailsQuery.Data>, ResponseState<LaunchDetailsModel>> {

    override fun map(from: ResponseState<LaunchDetailsQuery.Data>): ResponseState<LaunchDetailsModel> {
        when (from) {
            is ResponseState.Error -> return ResponseState.Error(from.error, from.errorBody)
            is ResponseState.Success -> {
                val data = from.data.launch
                return ResponseState.Success(
                    LaunchDetailsModel(
                        id = data?.id.orEmpty(),
                        site = data?.site.orEmpty(),
                        missionName = data?.mission?.name.orEmpty(),
                        missionPatchUrl = data?.mission?.missionPatch,
                        rocketId = data?.rocket?.id,
                        rocketName = data?.rocket?.name.orEmpty(),
                        rocketType = data?.rocket?.type.orEmpty(),
                    )
                )
            }
        }
    }
}