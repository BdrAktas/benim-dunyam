package com.simay.lifebank.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.simay.lifebank.ui.theme.OrbSize
import com.simay.lifebank.ui.theme.YkbAccentPurple
import com.simay.lifebank.ui.theme.YkbIridescentRose
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyMid
import com.simay.lifebank.ui.theme.YkbNavyPurple
import com.simay.lifebank.ui.theme.YkbPrimaryLight
import com.simay.lifebank.ui.theme.YkbWhite
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * AI Orb badge — design-system native, 8 katmanlı Canvas animasyonu.
 *
 * @param size      [OrbSize.small] (26dp) veya [OrbSize.large] (160dp). Default small.
 * @param isThinking true → hızlı shimmer sweep aktif (AI yanıt üretirken).
 * @param onClick   null → tap target eklenmez; non-null → Box 48dp minimum hedef alır.
 */
@Composable
fun AiOrbBadge(
    modifier: Modifier = Modifier,
    size: Dp = OrbSize.small,
    isThinking: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val inf = rememberInfiniteTransition(label = "aiOrb")

    // Blob yörüngesi — 9s; yavaş olduğu için bireysel bloblar seçilemiyor
    val orbDrift by inf.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "orbDrift"
    )
    // Çekirdek parlaklık nabzı
    val orbGlow by inf.animateFloat(
        initialValue = 0.55f, targetValue = 0.80f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "orbGlow"
    )
    // Düşünme shimmer — her zaman animasyonda; yalnızca isThinking=true olunca çizilir
    val orbShimmer by inf.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "orbShimmer"
    )

    val tapTarget = if (onClick != null) maxOf(size, 48.dp) else size
    val clickMod  = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier

    Box(
        modifier = modifier
            .size(tapTarget)
            .then(clickMod),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val s   = this.size
            val r   = s.minDimension / 2f
            val cx  = s.width  / 2f
            val cy  = s.height / 2f

            val driftRad = orbDrift    * PI / 180.0

            // Katman 2–8 orb çemberine clip'lenir
            val orbClip = Path().apply {
                addOval(Rect(cx - r, cy - r, cx + r, cy + r))
            }

            clipPath(orbClip) {

                // ── Katman 2: Taban küre — koyu navy radial ──────────────────
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(YkbNavyMid, YkbNavyDeep),
                        center = Offset(cx, cy),
                        radius = r
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 3: Çekirdek parlaklık nabzı ───────────────────────
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(YkbNavyPurple.copy(alpha = orbGlow), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = r * 0.55f
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 4: Mor blob — faz 0° ──────────────────────────────
                val b1cx = cx + (cos(driftRad) * r * 0.30).toFloat()
                val b1cy = cy + (sin(driftRad) * r * 0.30).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(YkbAccentPurple.copy(alpha = 0.75f), Color.Transparent),
                        center = Offset(b1cx, b1cy),
                        radius = r * 0.62f
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 5: Buz mavisi blob — faz +120° ────────────────────
                val rad2  = driftRad + 2.0 * PI / 3.0
                val b2cx  = cx + (cos(rad2) * r * 0.30).toFloat()
                val b2cy  = cy + (sin(rad2) * r * 0.30).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(YkbPrimaryLight.copy(alpha = 0.65f), Color.Transparent),
                        center = Offset(b2cx, b2cy),
                        radius = r * 0.58f
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 6: Gül rengi blob — faz +240° (en sessiz) ─────────
                val rad3  = driftRad + 4.0 * PI / 3.0
                val b3cx  = cx + (cos(rad3) * r * 0.30).toFloat()
                val b3cy  = cy + (sin(rad3) * r * 0.30).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(YkbIridescentRose.copy(alpha = 0.55f), Color.Transparent),
                        center = Offset(b3cx, b3cy),
                        radius = r * 0.50f
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 7: Cam parlaması — sol üst köşe, sabit ────────────
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            YkbWhite.copy(alpha = 0.55f),
                            YkbWhite.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(s.width * 0.32f, s.height * 0.20f),
                        radius = r * 0.28f
                    ),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // ── Katman 8: Düşünme shimmer — sadece isThinking=true ────────
                if (isThinking) {
                    rotate(orbShimmer * 360f, pivot = Offset(cx, cy)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    YkbWhite.copy(alpha = 0.35f),
                                    Color.Transparent
                                ),
                                center = Offset(cx, cy)
                            ),
                            radius = r,
                            center = Offset(cx, cy)
                        )
                    }
                }
            }
        }
    }
}
