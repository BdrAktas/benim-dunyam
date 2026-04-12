package com.simay.lifebank.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = YkbPrimary,
    onPrimary = YkbWhite,
    primaryContainer = YkbPrimaryPale,
    onPrimaryContainer = YkbNeutral900,
    secondary = YkbAccentPurple,
    onSecondary = YkbWhite,
    tertiary = YkbAccentOrange,
    onTertiary = YkbWhite,
    background = YkbBgApp,
    onBackground = YkbNeutral900,
    surface = YkbBgCard,
    onSurface = YkbNeutral900,
    surfaceVariant = YkbNeutral100,
    onSurfaceVariant = YkbNeutral700,
    outline = YkbNeutral300,
    error = YkbAccentRed,
    onError = YkbWhite
)

@Composable
fun LifeBankTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
