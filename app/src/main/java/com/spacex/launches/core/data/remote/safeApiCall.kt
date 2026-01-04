package com.spacex.launches.core.data.remote


import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.spacex.launches.core.data.remote.dto.StatusJsonResponse
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

/**
 * Safe Apollo call that never throws to the caller (except Cancellation).
 * - Handles: HTTP errors, network errors, parsing errors
 * - Handles GraphQL errors even when HTTP is 200
 */
suspend inline fun <reified D : Operation.Data> safeApiCall(
    crossinline apolloCall: suspend () -> ApolloResponse<D>
): ResponseState<D> {
    return runCatching {
        coroutineContext.ensureActive()

        val response = apolloCall()

        // 1) GraphQL-level errors (can happen with HTTP 200)
        if (response.hasErrors()) {
            val message = response.errors
                ?.joinToString(separator = "\n") { it.message }
                ?.takeIf { it.isNotBlank() }

            return ResponseState.Error(NetworkError.CLIENT_API_ERROR,null)
        }

        // 2) Null data (possible even without errors in some cases)
        val data = response.data
        if (data == null) {
            return ResponseState.Error(NetworkError.UNKNOWN_ERROR,null)
        }

        ResponseState.Success(data)
    }.getOrElse { e ->
        when (e) {
            is CancellationException -> throw e

            // HTTP errors (non-2xx)
            is ApolloHttpException -> handleApolloHttpException(e)

            // Network errors (no internet, timeout, etc.)
            is ApolloNetworkException -> ResponseState.Error(
                NetworkError.NO_INTERNET_CONNECTION,
                StatusJsonResponse(-1, e.message)
            )

            // Response parsing / decoding errors
            is ApolloParseException -> ResponseState.Error(
                NetworkError.RESPONSE_PARSING_ERROR,
                StatusJsonResponse(-1, e.message)
            )

            // Fallback
            is ApolloException -> ResponseState.Error(
                NetworkError.UNKNOWN_ERROR,
                StatusJsonResponse(-1, e.message)
            )

            else -> ResponseState.Error(
                NetworkError.UNKNOWN_ERROR,
                StatusJsonResponse(-1, e.message)
            )
        }
    }
}

fun handleApolloHttpException(e: ApolloHttpException): ResponseState.Error {
    val statusCode = e.statusCode
    val body = e.body

    val parsedError = StatusJsonResponse(statusCode, body?.toString() ?: e.message)

    return when (statusCode) {
        401 -> ResponseState.Error(NetworkError.UNAUTHORIZED_ACCESS, parsedError)
        403 -> ResponseState.Error(NetworkError.FORBIDDEN_ACCESS, parsedError)
        404 -> ResponseState.Error(NetworkError.RESOURCE_NOT_FOUND, parsedError)
        408 -> ResponseState.Error(NetworkError.REQUEST_TIMEOUT_ERROR, parsedError)
        409 -> ResponseState.Error(NetworkError.DATA_CONFLICT, parsedError)
        in 500..599 -> ResponseState.Error(NetworkError.INTERNAL_SERVER_ERROR, parsedError)
        else -> ResponseState.Error(NetworkError.CLIENT_API_ERROR, parsedError)
    }
}