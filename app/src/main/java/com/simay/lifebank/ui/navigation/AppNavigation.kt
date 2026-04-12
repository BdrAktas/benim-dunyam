package com.simay.lifebank.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Evim : Screen("evim")
    data object Finans : Screen("finans")
    data object Seyahat : Screen("seyahat")
    data object Aracim : Screen("aracim")
    data object Saglik : Screen("saglik")
    data object Ailem : Screen("ailem")
}

data class BottomTab(
    val route: String,
    val label: String,
    val emoji: String
)

val bottomTabs = listOf(
    BottomTab("home", "Ana Sayfa", "✦"),
    BottomTab("evim", "Ev", "\uD83C\uDFE0"),
    BottomTab("finans", "Finans", "\uD83D\uDCB0"),
    BottomTab("seyahat", "Seyahat", "✈\uFE0F"),
)
