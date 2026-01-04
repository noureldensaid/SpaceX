package com.spacex.launches.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(FlowPreview::class)
@Composable
fun <T> PaginatedLazColumn(
    modifier: Modifier = Modifier,
    items: ImmutableList<T>,
    keySelector: (T) -> Any,
    listState: LazyListState,
    isAppending: Boolean,             // next-page spinner (footer)
    isEndReached: Boolean,
    hasFooter: Boolean = true,// show end message when true
    onLoadMore: () -> Unit,           // called when near end (debounced)
    itemContent: @Composable (item: T) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    prefetchDistance: Int = 3,       // how far from the end to trigger loadMore
) {
    // Derived state to determine when to load more items
    val shouldLoadMore = remember {
        derivedStateOf {
            val total = listState.layoutInfo.totalItemsCount
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisible >= (total - prefetchDistance) &&
                    !isEndReached &&
                    !isAppending
        }
    }

    // Launch a coroutine to load more items when shouldLoadMore becomes true
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }  // Ensure that we load more items only when needed
            .collect {
                onLoadMore()
            }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        itemsIndexed(
            items = items,
            key = { index, item -> keySelector(item) }
        ) { index, item ->
            itemContent(item)
            if (index == items.lastIndex) {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (hasFooter) {
            // Loading Footer
            item {
                AnimatedVisibility(isEndReached.not() && isAppending) {
                    LoadingFooter()
                }
            }

            //  End reached Footer
            item {
                AnimatedVisibility(isEndReached) {
                    EndReachedFooter()
                }
            }
        }
    }
}