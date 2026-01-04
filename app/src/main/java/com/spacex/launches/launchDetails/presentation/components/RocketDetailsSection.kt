package com.spacex.launches.launchDetails.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.spacex.launches.R

@Composable
fun RocketDetailsSection(
    modifier: Modifier = Modifier,
    rocketName: String,
    rocketType: String,
    rocketId: String
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.rocket),
            fontSize = 22.sp,
            fontWeight = Bold,
        )
        Text(
            text = stringResource(R.string.name, rocketName),
            fontSize = 14.sp,
        )
        Text(
            text = stringResource(R.string.type, rocketType),
            fontSize = 14.sp,
        )
        Text(
            text = stringResource(R.string.id, rocketId),
            fontSize = 14.sp,
        )
    }
}

@Preview
@Composable
private fun RocketDetailsSectionPreview() {
    RocketDetailsSection(
        rocketName = "Falcon 9",
        rocketType = "FT",
        rocketId = "falcon9"
    )
}