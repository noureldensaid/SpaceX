package com.spacex.launches.launchDetails.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun SiteDetailsSection(
    modifier: Modifier = Modifier,
    siteName: String,
) {
    Column(modifier = modifier) {
        Text(
            text = "📍 Site",
            fontSize = 22.sp,
            fontWeight = Bold,
        )
        Text(
            text = "name: $siteName",
            fontSize = 14.sp,
        )
    }
}


@Preview
@Composable
private fun SiteDetailsSectionPreview() {
    SiteDetailsSection(
        siteName = "Kennedy Space Center"
    )
}