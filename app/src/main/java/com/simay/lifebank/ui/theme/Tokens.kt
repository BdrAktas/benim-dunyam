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
    val sectionGap = 24.dp   // majör bölümler arası (Benim Dünyam ↔ Smart feed)
}

object Radius {
    val card = 16.dp
    val button = 24.dp
    val iconBg = 12.dp
    val badge = 6.dp
    val pill = 8.dp          // alert chip / smart feed amount pill
    val input = 24.dp
    val hero = 28.dp         // navy hero bottom-corner radius
}

object OrbSize {
    val small = 26.dp   // magazin kartı badge
    val large = 160.dp  // AI asistan ekranı hero
}

object Elevation {
    val hairline = 0.5.dp
    val card = 2.dp
    val floating = 4.dp
    val shadowColor = Color(0x14000000)        // 0.08 alpha
    val floatingShadowColor = Color(0x1A000000) // 0.10 alpha
}
