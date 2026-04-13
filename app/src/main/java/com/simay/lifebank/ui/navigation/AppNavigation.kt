package com.simay.lifebank.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

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
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab("home",        "Ana Sayfa",    Icons.Rounded.Home),
    BottomTab("finans",      "Finans",       Icons.Rounded.AccountBalanceWallet),
    BottomTab("benimdunyam", "Benim Dünyam", Icons.Rounded.GridView),
)
