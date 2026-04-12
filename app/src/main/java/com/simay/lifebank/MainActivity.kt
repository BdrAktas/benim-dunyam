package com.simay.lifebank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.simay.lifebank.ui.navigation.Screen
import com.simay.lifebank.ui.navigation.bottomTabs
import com.simay.lifebank.ui.screens.AilemScreen
import com.simay.lifebank.ui.screens.AracimScreen
import com.simay.lifebank.ui.screens.EvimScreen
import com.simay.lifebank.ui.screens.FinansScreen
import com.simay.lifebank.ui.screens.HomeScreen
import com.simay.lifebank.ui.screens.SaglikScreen
import com.simay.lifebank.ui.screens.SeyahatScreen
import com.simay.lifebank.ui.theme.AccentAilem
import com.simay.lifebank.ui.theme.AccentAracim
import com.simay.lifebank.ui.theme.AccentDefault
import com.simay.lifebank.ui.theme.AccentEvim
import com.simay.lifebank.ui.theme.AccentFinans
import com.simay.lifebank.ui.theme.AccentSaglik
import com.simay.lifebank.ui.theme.AccentSeyahat
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.BgBase
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.LifeBankTheme
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.Sky

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeBankTheme {
                LifeBankApp()
            }
        }
    }
}

@Composable
fun LifeBankApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val accentColor by animateColorAsState(
        targetValue = when (currentRoute) {
            Screen.Evim.route -> AccentEvim
            Screen.Finans.route -> AccentFinans
            Screen.Seyahat.route -> AccentSeyahat
            Screen.Aracim.route -> AccentAracim
            Screen.Saglik.route -> AccentSaglik
            Screen.Ailem.route -> AccentAilem
            else -> AccentDefault
        },
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "accent_transition"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(BgBase)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.16f), Color.Transparent),
                        center = Offset(size.width * 0.2f, size.height * 0.2f),
                        radius = size.maxDimension * 0.5f
                    ),
                    radius = size.maxDimension
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFD4E4F0).copy(alpha = 0.25f), Color.Transparent),
                        center = Offset(size.width * 0.8f, size.height * 0.6f),
                        radius = size.maxDimension * 0.4f
                    ),
                    radius = size.maxDimension
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE8D5F0).copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.85f),
                        radius = size.maxDimension * 0.35f
                    ),
                    radius = size.maxDimension
                )
            }
    ) {
        // Content
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                })
            }
            composable(Screen.Evim.route) {
                EvimScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Finans.route) {
                FinansScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Seyahat.route) {
                SeyahatScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Aracim.route) {
                AracimScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Saglik.route) {
                SaglikScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Ailem.route) {
                AilemScreen(onBack = { navController.popBackStack() })
            }
        }

        // Floating glass bottom nav
        GlassBottomBar(
            currentRoute = currentRoute,
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun GlassBottomBar(
    currentRoute: String?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var bouncingTab by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .navigationBarsPadding()
            .clip(RoundedCornerShape(22.dp))
            .drawBehind {
                drawRect(Color.White.copy(alpha = 0.42f))
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        start = Offset.Zero,
                        end = Offset(size.width * 0.6f, size.height * 0.5f)
                    ),
                    size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.5f)
                )
            }
            .border(1.dp, Color.White.copy(alpha = 0.55f), RoundedCornerShape(22.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            bottomTabs.forEach { tab ->
                val isActive = currentRoute == tab.route
                val domainColor = when (tab.route) {
                    "evim" -> Sky
                    "finans" -> Honey
                    "seyahat" -> Lav
                    else -> Bark
                }
                val isBouncing = bouncingTab == tab.route
                val bounceScale by animateFloatAsState(
                    targetValue = if (isBouncing) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    ),
                    label = "bounce",
                    finishedListener = { bouncingTab = null }
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = bounceScale
                            scaleY = bounceScale
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (isActive) Modifier
                                .background(Color.White.copy(alpha = 0.5f))
                            else Modifier
                        )
                        .clickable {
                            bouncingTab = tab.route
                            if (tab.route == "home") {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(tab.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tab.emoji,
                        fontSize = 16.sp,
                        modifier = if (!isActive)
                            Modifier.graphicsLayer { alpha = 0.6f }
                        else Modifier
                    )
                    Text(
                        text = tab.label,
                        fontSize = 9.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = if (isActive) domainColor else Pebble,
                        fontFamily = SansFont
                    )
                }
            }
        }
    }
}
