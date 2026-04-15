package com.simay.lifebank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.simay.lifebank.ui.components.AiOrbBadge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.simay.lifebank.ui.theme.YkbAccentPurple
import com.simay.lifebank.ui.theme.YkbIridescentRose
import com.simay.lifebank.ui.theme.YkbPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Radius
import com.simay.lifebank.ui.theme.Spacing
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbBorderHairline
import com.simay.lifebank.ui.theme.YkbCanvas
import com.simay.lifebank.ui.theme.YkbDomainAilem
import com.simay.lifebank.ui.theme.YkbDomainAracim
import com.simay.lifebank.ui.theme.YkbDomainEvim
import com.simay.lifebank.ui.theme.YkbDomainParam
import com.simay.lifebank.ui.theme.YkbDomainSaglik
import com.simay.lifebank.ui.theme.YkbDomainSeyahat
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyPurple
import com.simay.lifebank.ui.theme.YkbNavySoft
import com.simay.lifebank.ui.theme.YkbSurfaceCard
import com.simay.lifebank.ui.theme.YkbType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
private fun AmbientLayer() {
    val inf = rememberInfiniteTransition(label = "ambient")

    val drift1 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift1"
    )
    val drift2 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(13000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift2"
    )
    val drift3 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(19000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift3"
    )
    val drift4 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift4"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w    = size.width
        val h    = size.height
        val cx   = w / 2f
        val cy   = h / 2f
        val xAmp = w * 0.30f
        val yAmp = h * 0.20f

        val phase1 = 0f
        val phase2 = (2.0 * PI / 3.0).toFloat()   // 120°
        val phase3 = (4.0 * PI / 3.0).toFloat()   // 240°
        val phase4 = (PI / 3.0).toFloat()          //  60°

        // Blob 1 — YkbPrimary, en büyük, 8s
        val b1x = cx + cos(drift1 + phase1) * xAmp
        val b1y = cy + sin(drift1 + phase1) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbPrimary.copy(alpha = 0.22f), Color.Transparent),
                center = Offset(b1x, b1y),
                radius = w * 0.70f
            ),
            radius = w * 0.70f,
            center = Offset(b1x, b1y)
        )

        // Blob 2 — YkbNavyPurple, 13s
        val b2x = cx + cos(drift2 + phase2) * xAmp
        val b2y = cy + sin(drift2 + phase2) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbNavyPurple.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(b2x, b2y),
                radius = w * 0.60f
            ),
            radius = w * 0.60f,
            center = Offset(b2x, b2y)
        )

        // Blob 3 — YkbAccentPurple, 19s
        val b3x = cx + cos(drift3 + phase3) * xAmp
        val b3y = cy + sin(drift3 + phase3) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbAccentPurple.copy(alpha = 0.15f), Color.Transparent),
                center = Offset(b3x, b3y),
                radius = w * 0.50f
            ),
            radius = w * 0.50f,
            center = Offset(b3x, b3y)
        )

        // Blob 4 — YkbIridescentRose, 11s
        val b4x = cx + cos(drift4 + phase4) * xAmp
        val b4y = cy + sin(drift4 + phase4) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbIridescentRose.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(b4x, b4y),
                radius = w * 0.40f
            ),
            radius = w * 0.40f,
            center = Offset(b4x, b4y)
        )
    }
}

private enum class MicState { IDLE, LISTENING, ANSWERED }

private data class AiInsight(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val accent: Color,
    val route: String,
    val eventLine: String,
    val metric: String,
    val metricLabel: String
)

private val aiInsights = listOf(
    AiInsight("evim",    "Evim",       Icons.Rounded.Home,          YkbDomainEvim,    "evim",
        "Doğalgaz son gün, ödenmedi",     "₺847",    "aylık fatura tutarın"),
    AiInsight("aracim",  "Aracım",     Icons.Rounded.DirectionsCar,  YkbDomainAracim,  "aracim",
        "Kaskon 15 gün sonra bitiyor",    "₺8.500",  "tahmini yenileme primin"),
    AiInsight("seyahat", "Seyahatim",  Icons.Rounded.Flight,         YkbDomainSeyahat, "seyahat",
        "Antalya tatiline 18 gün kaldı",  "₺28.500", "tahmini tatil bütçen"),
    AiInsight("saglik",  "Sağlığım",   Icons.Rounded.MonitorHeart,   YkbDomainSaglik,  "saglik",
        "Check-up randevun 34 gün sonra", "%82",      "yıllık sigortandan kalan"),
    AiInsight("ailem",   "Ailem",      Icons.Rounded.Groups,         YkbDomainAilem,   "ailem",
        "Üniversite harcına 18 gün kaldı","₺8.500",  "harç için eksik tutarın"),
    AiInsight("param",   "Param",      Icons.Rounded.AccountBalance, YkbDomainParam,   "finans",
        "Sınırsız hesap ile her gün kazan","%52",     "e-Mevduat güncel faiz"),
)

private const val mockQuestion = "Bu ay araç sigortam ne kadar?"
private const val mockAnswer   = "Kaskonun bu ay ₺8.500 yenileme tutarı var. 15 gün içinde bitiyor. Teklif almak ister misin?"

@Composable
fun AiAssistantScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    var micState by remember { mutableStateOf(MicState.IDLE) }
    val scope = rememberCoroutineScope()

    val fabBg by animateColorAsState(
        targetValue = if (micState == MicState.LISTENING) Terra else YkbSurfaceCard,
        animationSpec = tween(300),
        label = "fabBg"
    )
    val fabIconTint by animateColorAsState(
        targetValue = if (micState == MicState.LISTENING) Color.White else YkbNavyDeep,
        animationSpec = tween(300),
        label = "fabIconTint"
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Üst gradient katman — %45 ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(
                    Brush.verticalGradient(listOf(YkbNavyDeep, YkbNavySoft, YkbNavyPurple))
                )
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            AmbientLayer()

            AiOrbBadge(size = 160.dp)

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(Spacing.sm)
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(role = Role.Button, onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Geri",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // ── Alt beyaz panel — %55 ────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth().weight(0.55f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(YkbSurfaceCard)
                    .padding(
                        top = Spacing.xxl + Spacing.md,
                        start = Spacing.lg,
                        end = Spacing.lg,
                        bottom = Spacing.lg
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Bugün sana özel",
                    style = YkbType.Heading2.copy(color = Bark, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(Spacing.md))

                AnimatedVisibility(
                    visible = micState != MicState.IDLE,
                    enter = expandVertically() + fadeIn(tween(250)),
                    exit  = shrinkVertically() + fadeOut(tween(200))
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Radius.card))
                                .background(YkbCanvas)
                                .padding(Spacing.md),
                            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            if (micState == MicState.LISTENING) {
                                Text(
                                    text = "Dinliyorum...",
                                    style = YkbType.BodySm.copy(color = Stone),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    text = mockQuestion,
                                    style = YkbType.BodyMd.copy(
                                        color = Bark,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                                Text(
                                    text = mockAnswer,
                                    style = YkbType.BodyMd.copy(color = Stone),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(Modifier.height(Spacing.md))
                    }
                }

                aiInsights.forEach { insight ->
                    AiInsightCard(insight = insight, onClick = { onNavigate(insight.route) })
                    Spacer(Modifier.height(Spacing.sm))
                }
            }

            // Mikrofon FAB — panelin üst kenarına 28dp overlap
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-28).dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(fabBg)
                    .clickable(role = Role.Button) {
                        when (micState) {
                            MicState.IDLE, MicState.ANSWERED -> {
                                micState = MicState.LISTENING
                                scope.launch {
                                    delay(2000L)
                                    micState = MicState.ANSWERED
                                }
                            }
                            MicState.LISTENING -> micState = MicState.IDLE
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (micState == MicState.LISTENING) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = if (micState == MicState.LISTENING) "Durdur" else "Sesli Soru Sor",
                    tint = fabIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun AiInsightCard(insight: AiInsight, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.card))
            .background(YkbSurfaceCard)
            .border(1.dp, YkbBorderHairline, RoundedCornerShape(Radius.card))
            .drawBehind {
                drawRect(color = insight.accent, size = Size(4.dp.toPx(), size.height))
            }
            .clickable(role = Role.Button, onClick = onClick)
            .padding(
                start = Spacing.md + 4.dp,
                end = Spacing.md,
                top = Spacing.md,
                bottom = Spacing.md
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(Radius.iconBg))
                .background(insight.accent.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = insight.icon,
                contentDescription = null,
                tint = insight.accent,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = insight.label, style = YkbType.BodySm.copy(color = Stone))
            Text(
                text = insight.eventLine,
                style = YkbType.BodyMd.copy(color = Bark, fontWeight = FontWeight.SemiBold),
                maxLines = 2
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = insight.metric,
                style = YkbType.BodyMd.copy(color = insight.accent, fontWeight = FontWeight.Bold)
            )
            Text(
                text = insight.metricLabel,
                style = YkbType.BodySm.copy(color = Stone),
                maxLines = 1
            )
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Stone,
            modifier = Modifier.size(16.dp)
        )
    }
}
