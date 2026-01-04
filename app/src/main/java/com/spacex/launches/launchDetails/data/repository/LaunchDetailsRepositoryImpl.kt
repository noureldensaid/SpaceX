package com.spacex.launches.launchDetails.data.repository

import com.apollographql.apollo3.ApolloClient
import com.spacex.launches.LaunchDetailsQuery
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.data.remote.safeApiCall
import com.spacex.launches.launchDetails.data.mapper.LaunchDetailsModelMapper
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel
import com.spacex.launches.launchDetails.domain.repository.LaunchDetailsRepository
import javax.inject.Inject

class LaunchDetailsRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: LaunchDetailsModelMapper
) : LaunchDetailsRepository {


    override suspend fun getLaunchDetails(id: String): ResponseState<LaunchDetailsModel> {
        val result: ResponseState<LaunchDetailsQuery.Data> =
            safeApiCall<LaunchDetailsQuery.Data> {
                apolloClient.query(
                    LaunchDetailsQuery(
                        id = id
                    )
                ).execute()
            }

        return mapper.map(result)
    }
}