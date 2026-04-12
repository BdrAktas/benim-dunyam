package com.simay.lifebank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SerifFont = FontFamily.Serif
val SansFont = FontFamily.SansSerif

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    titleLarge = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 0.2.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SansFont,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
    )
)
