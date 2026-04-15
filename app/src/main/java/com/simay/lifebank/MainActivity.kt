

package com.simay.lifebank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.theme.Elevation
import com.simay.lifebank.ui.theme.Radius
import com.simay.lifebank.ui.theme.YkbCanvas
import com.simay.lifebank.ui.theme.YkbPrimary
import com.simay.lifebank.ui.theme.YkbSurfaceCard
import com.simay.lifebank.ui.theme.YkbType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simay.lifebank.ui.navigation.Screen
import com.simay.lifebank.ui.navigation.bottomTabs
import com.simay.lifebank.ui.screens.AiAssistantScreen
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
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.Rose
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Teal
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbBorderHairline
import com.simay.lifebank.ui.theme.YkbNeutral300
import com.simay.lifebank.ui.theme.YkbNeutral900

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

// "benimdunyam" veya "asistanim" sekmesi açıkken aktif sayılan alt sayfalar
private val domainSubRoutes = setOf("evim", "aracim", "saglik", "seyahat", "ailem")

@Composable
fun LifeBankApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val baseRoute = currentRoute?.substringBefore("?")

    // null = kapalı, "benimdunyam" veya "asistanim" = açık tab
    var activePanel by remember { mutableStateOf<String?>(null) }

    val accentColor by animateColorAsState(
        targetValue = when (baseRoute) {
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

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Arka plan + NavHost (panel açıkken blur alır) ──────────────────
        val blurRadius by animateFloatAsState(
            targetValue = if (activePanel != null) 14f else 0f,
            animationSpec = tween(250),
            label = "blur"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (activePanel != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        Modifier.blur(blurRadius.dp)
                    else Modifier
                )
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
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(onNavigate = { route ->
                        activePanel = null
                        navController.navigate(route) { launchSingleTop = true }
                    })
                }
                composable(
                    route = "evim?intent={intent}&provider={provider}",
                    arguments = listOf(
                        navArgument("intent") { nullable = true; defaultValue = null },
                        navArgument("provider") { nullable = true; defaultValue = null }
                    )
                ) { backStackEntry ->
                    EvimScreen(
                        onBack = { navController.popBackStack() },
                        intent = backStackEntry.arguments?.getString("intent")
                    )
                }
                composable(Screen.Finans.route) {
                    FinansScreen(onBack = { navController.popBackStack() })
                }
                composable(
                    route = "seyahat?intent={intent}",
                    arguments = listOf(
                        navArgument("intent") { nullable = true; defaultValue = null }
                    )
                ) { backStackEntry ->
                    SeyahatScreen(
                        onBack = { navController.popBackStack() },
                        intent = backStackEntry.arguments?.getString("intent")
                    )
                }
                composable(
                    route = "aracim?intent={intent}",
                    arguments = listOf(
                        navArgument("intent") { nullable = true; defaultValue = null }
                    )
                ) { backStackEntry ->
                    AracimScreen(
                        onBack = { navController.popBackStack() },
                        intent = backStackEntry.arguments?.getString("intent")
                    )
                }
                composable(Screen.Saglik.route) {
                    SaglikScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.Ailem.route) {
                    AilemScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.AiAssistant.route) {
                    AiAssistantScreen(
                        onBack = { navController.popBackStack() },
                        onNavigate = { route ->
                            activePanel = null
                            navController.navigate(route) { launchSingleTop = true }
                        }
                    )
                }
            }
        }

        // ── Dim overlay — kapatmak için tıklanabilir ────────────────────────
        AnimatedVisibility(
            visible = activePanel != null,
            enter = fadeIn(tween(200)),
            exit  = fadeOut(tween(180))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.30f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { activePanel = null }
            )
        }

        // ── Alt bölge: sliding panel + nav bar ──────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = activePanel != null,
                enter = slideInVertically { it } + fadeIn(tween(220)),
                exit  = slideOutVertically { it } + fadeOut(tween(180))
            ) {
                QuickLaunchPanel(
                    onNavigate = { route ->
                        activePanel = null
                        navController.navigate(route) { launchSingleTop = true }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgBase)
            ) {
                GlassBottomBar(
                    currentRoute = baseRoute,
                    activePanel = activePanel,
                    onTabClick = { route ->
                        when (route) {
                            "home" -> {
                                activePanel = null
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                            "benimdunyam" -> {
                                activePanel = if (activePanel == route) null else route
                            }
                            else -> {
                                activePanel = null
                                navController.navigate(route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

// ─── Bottom Bar ───────────────────────────────────────────────────────────────

@Composable
private fun GlassBottomBar(
    currentRoute: String?,
    activePanel: String?,
    onTabClick: (String) -> Unit
) {
    var bouncingTab by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .navigationBarsPadding()
            .clip(RoundedCornerShape(22.dp))
            .background(YkbCanvas)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            bottomTabs.forEach { tab ->
                val isPanelOpen = activePanel == "benimdunyam"
                val panelActive = isPanelOpen || currentRoute in domainSubRoutes
                val isActive = when (tab.route) {
                    "benimdunyam" -> panelActive
                    else          -> !panelActive && currentRoute == tab.route
                }
                val isBouncing = bouncingTab == tab.route
                val scale by animateFloatAsState(
                    targetValue = if (isBouncing) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    ),
                    label = "bounce",
                    finishedListener = { bouncingTab = null }
                )

                val displayIcon = tab.icon
                val displayLabel = tab.label

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .then(
                            if (isActive) Modifier
                                .shadow(
                                    elevation = Elevation.card,
                                    shape = RoundedCornerShape(Radius.iconBg),
                                    ambientColor = Elevation.shadowColor,
                                    spotColor = Elevation.shadowColor
                                )
                                .clip(RoundedCornerShape(Radius.iconBg))
                                .background(YkbSurfaceCard)
                            else
                                Modifier.clip(RoundedCornerShape(Radius.iconBg))
                        )
                        .clickable {
                            bouncingTab = tab.route
                            onTabClick(tab.route)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = displayIcon,
                        contentDescription = displayLabel,
                        tint = if (isActive) YkbPrimary else Stone,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = displayLabel,
                        style = YkbType.Badge.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = if (isActive) YkbPrimary else Stone
                        )
                    )
                }
            }
        }
    }
}

// ─── Quick Launch Panel ────────────────────────────────────────────────────────

private data class DomainShortcut(
    val route: String,
    val label: String,
    val desc: String,
    val color: Color,
    @DrawableRes val illustrationRes: Int
)

private val domainShortcuts = listOf(
    DomainShortcut("evim",    "Evim",       "Evinizle ilgili\nher şey",        Sky,  R.drawable.ic_stitch_evim),
    DomainShortcut("aracim",  "Aracım",     "Aracınızın\nDeğerini İzleyin",    Moss, R.drawable.ic_stitch_aracim),
    DomainShortcut("seyahat", "Seyahatim",  "Seyahatinizi\nPlanlayın",         Lav,  R.drawable.ic_stitch_seyahat),
    DomainShortcut("saglik",  "Sağlığım",   "Sağlığınıza\nÖncelik Verin",      Rose, R.drawable.ic_stitch_saglik),
    DomainShortcut("ailem",   "Ailem",      "Ailenizle\nBağlantıda Kalın",     Teal,  R.drawable.ic_stitch_ailem),
)

@Composable
private fun QuickLaunchPanel(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(YkbCanvas)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 14.dp)
                .size(width = 36.dp, height = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(YkbNeutral300)
        )
        DomainGrid(onNavigate)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DomainGrid(onNavigate: (String) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        domainShortcuts.forEach { domain ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(148.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { onNavigate(domain.route) }
            ) {
                // Top-left: title + description
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = domain.label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = YkbNeutral900,
                        fontFamily = SansFont
                    )
                    Text(
                        text = domain.desc,
                        fontSize = 11.sp,
                        color = Stone,
                        fontFamily = SansFont,
                        lineHeight = 15.sp
                    )
                }
                // Bottom-right: Google Stitch illustration
                Image(
                    painter = painterResource(domain.illustrationRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .then(
                            if (domain.route == "ailem")
                                Modifier.size(148.dp)
                            else
                                Modifier.size(96.dp)
                        )
                        .align(
                            if (domain.route == "ailem") Alignment.CenterEnd
                            else Alignment.BottomEnd
                        )
                        .offset(
                            x = if (domain.route == "ailem") 4.dp else 8.dp,
                            y = if (domain.route == "ailem") 0.dp else 8.dp
                        )
                )
            }
        }
    }
    Spacer(Modifier.height(4.dp))
}

