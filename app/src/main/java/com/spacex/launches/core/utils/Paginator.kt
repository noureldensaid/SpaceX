package com.spacex.launches.core.utils

import com.spacex.launches.core.data.remote.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Paginator<T, Page>(
    private val initialLoadSize: Int,
    private val pageSize: Int,
    private val initialCursor: String? = null,

    private val initialLoad: suspend (pageSize: Int) -> ResponseState<Page>,
    private val loadMore: suspend (cursor: String?, pageSize: Int) -> ResponseState<Page>,

    private val getItems: (Page) -> List<T>,
    private val getNextCursor: (Page) -> String?,
    private val getHasMore: (Page) -> Boolean,

    private val isEndReached: (Boolean) -> Unit,
    private val distinctByKey: ((T) -> Any)? = null
) {
    private val _items =
        MutableStateFlow<ResponseState<List<T>>>(ResponseState.Success(emptyList()))
    val items: Flow<ResponseState<List<T>>> = _items.asStateFlow()

    private var cursor: String? = initialCursor
    private var isLoading = false
    private var hasMoreItems = true
    private var loadedOnce = false

    private fun mergeDistinct(current: List<T>, incoming: List<T>): List<T> {
        val merged = current + incoming
        return distinctByKey?.let { key -> merged.distinctBy(key) } ?: merged.distinct()
    }

    suspend fun loadInitial(force: Boolean = false) {
        val current = (_items.value as? ResponseState.Success<List<T>>)?.data.orEmpty()
        if (!force && loadedOnce && current.isNotEmpty()) return
        if (isLoading) return

        isLoading = true
        cursor = initialCursor
        hasMoreItems = true

        when (val result = initialLoad(initialLoadSize)) {
            is ResponseState.Success -> {
                val page = result.data
                val items = getItems(page)
                _items.value = ResponseState.Success(
                    distinctByKey?.let { items.distinctBy(it) } ?: items.distinct()
                )

                cursor = getNextCursor(page)
                hasMoreItems = getHasMore(page) && items.isNotEmpty()
                loadedOnce = true
                isEndReached(!hasMoreItems)
            }

            is ResponseState.Error -> _items.value = result
        }

        isLoading = false
    }

    suspend fun loadNextPage() {
        if (isLoading || !hasMoreItems) return

        isLoading = true

        when (val result = loadMore(cursor, pageSize)) {
            is ResponseState.Success -> {
                val page = result.data
                val incoming = getItems(page)

                val current = (_items.value as? ResponseState.Success<List<T>>)?.data.orEmpty()
                _items.value = ResponseState.Success(mergeDistinct(current, incoming))

                cursor = getNextCursor(page)
                hasMoreItems = getHasMore(page) && incoming.isNotEmpty()
                isEndReached(!hasMoreItems)
            }

            is ResponseState.Error -> _items.value = result
        }

        isLoading = false
    }

    suspend fun refresh() {
        reset()
        loadInitial(force = true)
    }

    fun reset() {
        cursor = initialCursor
        isLoading = false
        hasMoreItems = true
        loadedOnce = false
        _items.value = ResponseState.Success(emptyList())
    }
}