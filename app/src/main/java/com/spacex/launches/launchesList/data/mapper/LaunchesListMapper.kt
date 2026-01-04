package com.spacex.launches.launchesList.data.mapper

import com.spacex.launches.LaunchListQuery
import com.spacex.launches.core.base.BaseMapper
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.data.remote.ResponseState.Error
import com.spacex.launches.core.data.remote.ResponseState.Success
import com.spacex.launches.launchesList.domain.model.Launch
import com.spacex.launches.launchesList.domain.model.LaunchPage
import javax.inject.Inject

class LaunchesListMapper @Inject constructor() :
    BaseMapper<ResponseState<LaunchListQuery.Data>, ResponseState<LaunchPage>> {

    override fun map(from: ResponseState<LaunchListQuery.Data>): ResponseState<LaunchPage> {
        return when (from) {
            is Success -> {
                val data = from.data
                Success(
                    LaunchPage(
                        launches = data.launches.launches.map {
                            Launch(
                                id = it?.id.orEmpty(),
                                site = it?.site.orEmpty(),
                                missionName = it?.mission?.name.orEmpty(),
                                missionPatch = it?.mission?.missionPatch.orEmpty(),
                                rocketName = it?.rocket?.name.orEmpty(),

                                )
                        },
                        cursor = data.launches.cursor,
                        hasMore = data.launches.hasMore
                    )
                )
            }

            is Error -> Error(from.error, from.errorBody)
        }
    }
}