package com.simay.lifebank.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// YKB Mobile Design Tokens — spacing / radius / elevation

object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp

    val pagePadding = 16.dp
    val cardPadding = 16.dp
    val iconGridGap = 16.dp
}

object Radius {
    val card = 16.dp
    val button = 24.dp
    val iconBg = 12.dp
    val badge = 6.dp
    val input = 24.dp
}

object Elevation {
    val card = 2.dp
    val floating = 4.dp
    val shadowColor = Color(0x14000000)        // 0.08 alpha
    val floatingShadowColor = Color(0x1A000000) // 0.10 alpha
}
