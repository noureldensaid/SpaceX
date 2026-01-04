package com.spacex.launches.launchesList.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.spacex.launches.LaunchListQuery
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.data.remote.safeApiCall
import com.spacex.launches.launchesList.data.mapper.LaunchesListMapper
import com.spacex.launches.launchesList.domain.model.LaunchPage
import com.spacex.launches.launchesList.domain.repository.LaunchesListRepository
import javax.inject.Inject

class LaunchesListRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: LaunchesListMapper
) : LaunchesListRepository {

    override suspend fun getLaunches(
        cursor: String?,
        pageSize: Int
    ): ResponseState<LaunchPage> {

        val result: ResponseState<LaunchListQuery.Data> =
            safeApiCall<LaunchListQuery.Data> {
                apolloClient.query(
                    LaunchListQuery(
                        after = Optional.presentIfNotNull(cursor),
                        pageSize = Optional.presentIfNotNull(pageSize),
                    )
                ).execute()
            }

        return mapper.map(result)
    }
}

