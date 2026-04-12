package com.simay.lifebank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// YKB tokens use a geometric sans-serif (Inter / SF Pro). Until a custom
// font is bundled, fall back to the platform sans-serif for both slots.
val SansFont = FontFamily.SansSerif
val SerifFont = FontFamily.SansSerif
val NumericFont = FontFamily.SansSerif

// ─────────────────────────────────────────────────────────────
// YKB Type Scale
// ─────────────────────────────────────────────────────────────
object YkbType {
    val Display = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    val Heading1 = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    )
    val Heading2 = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 22.sp
    )
    val Heading3 = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    )
    val BodyLg = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )
    val BodyMd = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
    val BodySm = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
    val NumericLg = TextStyle(
        fontFamily = NumericFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
    val NumericMd = TextStyle(
        fontFamily = NumericFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val Label = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
    val Badge = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.sp
    )
}

val AppTypography = Typography(
    displayLarge = YkbType.Display,
    displayMedium = YkbType.Heading1,
    headlineLarge = YkbType.Heading1,
    headlineMedium = YkbType.Heading2,
    titleLarge = YkbType.Heading2,
    titleMedium = YkbType.Heading3,
    bodyLarge = YkbType.BodyLg,
    bodyMedium = YkbType.BodyMd,
    bodySmall = YkbType.BodySm,
    labelLarge = YkbType.Label,
    labelMedium = YkbType.Label,
    labelSmall = YkbType.Badge
)
