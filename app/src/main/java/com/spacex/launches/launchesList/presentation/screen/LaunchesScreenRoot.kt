package com.spacex.launches.launchesList.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacex.launches.core.presentation.components.PaginatedLazColumn
import com.spacex.launches.launchesList.domain.model.Launch
import com.spacex.launches.launchesList.presentation.components.LaunchItem
import com.spacex.launches.launchesList.presentation.model.LaunchesScreenEvents
import com.spacex.launches.launchesList.presentation.model.LaunchesScreenState
import kotlinx.collections.immutable.toPersistentList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreenRoot(
    modifier: Modifier = Modifier,
    state: LaunchesScreenState,
    onEvent: (event: LaunchesScreenEvents) -> Unit = {}
) {

    val listState = rememberLazyListState()


    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val isCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction > 0.5 } }

    val topAppBarElementColor = if (isCollapsed.value) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val collapsedTextSize = 22
    val expandedTextSize = 28

    val topAppBarTextSize =
        (collapsedTextSize + (expandedTextSize - collapsedTextSize) * (1 - scrollBehavior.state.collapsedFraction)).sp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SpaceX Launches", fontSize = topAppBarTextSize) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = topAppBarElementColor,
                    actionIconContentColor = topAppBarElementColor
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        PaginatedLazColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            items = state.launches.toPersistentList(),
            keySelector = { it.id },
            listState = listState,
            isAppending = state.isLoadingMore,
            isEndReached = state.endReached,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            onLoadMore = {
                onEvent(LaunchesScreenEvents.LoadNextPage)
            },
            itemContent = { launch ->
                LaunchItem(
                    modifier = Modifier.height(100.dp),
                    launchModel = launch
                )
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LaunchesScreenRootPreview() {
    val mockState = LaunchesScreenState(
        launches = listOf(
            Launch(
                id = "1",
                rocketName = "Falcon 9",
                missionName = "Starlink 7 • Batch of 60 satellites",
                missionPatch = "null",
                site = "CCAFS SLC-40"
            ),
            Launch(
                id = "2",
                rocketName = "Falcon Heavy",
                missionName = "Arabsat-6A (Very long mission name to test wrapping in UI)",
                missionPatch = "https://images2.imgbox.com/94/f2/NNY2oKPs_o.png",
                site = "KSC LC-39A"
            ),
            Launch(
                id = "3",
                rocketName = "Falcon 1",
                missionName = "RatSat",
                missionPatch = "https://images2.imgbox.com/3d/86/cnu0pan8_o.png",
                site = "Kwajalein Atoll"
            ),
            Launch(
                id = "4",
                rocketName = "Falcon 07",
                missionName = "Demo Mission",
                missionPatch = "null",
                site = "VAFB SLC-4E"
            ),
            Launch(
                id = "5",
                rocketName = "Falcon 9",
                missionName = "Starlink 7 • Batch of 60 satellites",
                missionPatch = "https://images2.imgbox.com/d2/3b/bQaWi0m0_o.png",
                site = "CCAFS SLC-40"
            ),
            Launch(
                id = "6",
                rocketName = "Falcon 9",
                missionName = "Crew-1",
                missionPatch = "https://images2.imgbox.com/df/7d/0zB3Yb6D_o.png",
                site = "KSC LC-39A"
            ),
            Launch(
                id = "7",
                rocketName = "Falcon 9",
                missionName = "GPS III SV04",
                missionPatch = "null",
                site = "CCAFS SLC-40"
            ),
            Launch(
                id = "8",
                rocketName = "Falcon Heavy",
                missionName = "Psyche",
                missionPatch = "null",
                site = "KSC LC-39A"
            )
        )
    )

    LaunchesScreenRoot(
        state = mockState,
        onEvent = {}
    )
}