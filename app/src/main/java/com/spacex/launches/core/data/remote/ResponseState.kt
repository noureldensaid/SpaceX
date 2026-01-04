package com.spacex.launches.core.data.remote

import com.spacex.launches.core.base.BaseError
import com.spacex.launches.core.data.remote.dto.StatusJsonResponse

sealed interface ResponseState<out D> {
    data class Success<out D>(val data: D) : ResponseState<D>
    data class Error(val error: BaseError, val errorBody: StatusJsonResponse?) : ResponseState<Nothing>
}