package com.simay.lifebank.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.YkbBorderHairline
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.Rose
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt

enum class GlassIntensity { Normal, Strong, Subtle }

@Composable
fun animateCountUp(target: Int, duration: Int = 900): Int {
    var value by remember { mutableIntStateOf(0) }
    LaunchedEffect(target) {
        val startTime = System.currentTimeMillis()
        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            val progress = (elapsed.toFloat() / duration).coerceAtMost(1f)
            val eased = 1f - (1f - progress).pow(3)
            value = (target * eased).roundToInt()
            if (progress >= 1f) break
            delay(16)
        }
    }
    return value
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    intensity: GlassIntensity = GlassIntensity.Normal,
    accent: Color? = null,
    glow: Boolean = false,
    animate: Boolean = false,
    borderLeftColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "press_scale"
    )

    var visible by remember { mutableStateOf(!animate) }
    LaunchedEffect(Unit) {
        if (animate) {
            delay(50)
            visible = true
        }
    }
    val animAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(450, easing = FastOutSlowInEasing),
        label = "entrance_alpha"
    )
    val animOffsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 24f,
        animationSpec = tween(450, easing = FastOutSlowInEasing),
        label = "entrance_offset"
    )

    // S1.1 — home DNA ile hizalı: 28dp radius + daha opak surface'ler.
    val shape = RoundedCornerShape(28.dp)

    val cardBg = when (intensity) {
        GlassIntensity.Strong -> Color.White
        GlassIntensity.Normal -> Color.White
        GlassIntensity.Subtle -> Color(0xFFFBFAF8) // çok hafif warm tint, neredeyse beyaz
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = animAlpha
                translationY = animOffsetY
            }
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clip(shape)
            .background(cardBg)
            .drawBehind {
                // Specular highlight sadece Strong intensity'de — ambiance taşıması gereken
                // hero özet kartlar için. Normal/Subtle'da beyaz DNA temiz tutulur.
                if (intensity == GlassIntensity.Strong) {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                            start = Offset.Zero,
                            end = Offset(size.width * 0.6f, size.height * 0.5f)
                        ),
                        size = Size(size.width, size.height * 0.5f)
                    )
                }
                if (glow && accent != null) {
                    drawRect(accent.copy(alpha = 0.04f))
                }
                if (borderLeftColor != null) {
                    drawRect(
                        color = borderLeftColor,
                        size = Size(3.dp.toPx(), size.height)
                    )
                }
            }
            .border(1.dp, YkbBorderHairline, shape) // home ile aynı hairline
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ) else Modifier
            )
            .padding(contentPadding)
    ) {
        Column { content() }
    }
}

@Composable
fun GlassPill(
    text: String,
    color: Color = Moss
) {
    Text(
        text = text.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        fontFamily = SansFont,
        letterSpacing = 0.3.sp,
        modifier = Modifier
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .border(1.dp, color.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

@Composable
fun GlassButton(
    text: String,
    color: Color = Moss,
    isFull: Boolean = false,
    isSmall: Boolean = false,
    isOutline: Boolean = false,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "btn_press"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(if (isFull) Modifier.fillMaxWidth() else Modifier)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation = if (!isOutline && !isPressed) 4.dp else 0.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(if (isOutline) color.copy(alpha = 0.06f) else color)
            .then(
                if (isOutline) Modifier.border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal = if (isSmall) 16.dp else 24.dp,
                vertical = if (isSmall) 8.dp else 12.dp
            )
    ) {
        Text(
            text = text,
            fontSize = if (isSmall) 12.sp else 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOutline) color else Color.White,
            fontFamily = SansFont,
            letterSpacing = 0.2.sp
        )
    }
}

data class TabItem(val id: String, val label: String)

@Composable
fun GlassTabs(
    tabs: List<TabItem>,
    activeId: String,
    onTabChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.3f))
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEach { tab ->
            val isActive = tab.id == activeId
            val bgColor by animateColorAsState(
                targetValue = if (isActive) Color.White.copy(alpha = 0.7f) else Color.Transparent,
                label = "tab_bg"
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable { onTabChange(tab.id) }
                    .padding(vertical = 9.dp, horizontal = 6.dp)
            ) {
                Text(
                    text = tab.label,
                    fontSize = 12.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) Bark else Pebble,
                    fontFamily = SansFont,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AnimatedProgress(
    value: Float,
    max: Float,
    color: Color = Moss,
    height: Dp = 6.dp,
    delayMs: Int = 0
) {
    var targetWidth by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(value, max) {
        delay(100L + delayMs)
        targetWidth = (value / max).coerceIn(0f, 1f)
    }
    val animatedWidth by animateFloatAsState(
        targetValue = targetWidth,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height))
            .background(Color.Black.copy(alpha = 0.06f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedWidth.coerceAtLeast(0.001f))
                .clip(RoundedCornerShape(height))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color, color.copy(alpha = 0.8f))
                    )
                )
        )
    }
}

private data class ConfettiParticle(
    val color: Color,
    val startX: Float,
    val delay: Long,
    val size: Float,
    val duration: Int,
    val isCircle: Boolean
)

@Composable
fun ConfettiEffect(active: Boolean) {
    if (!active) return
    val colors = listOf(Moss, Sky, Honey, Lav, Terra, Rose)
    val particles = remember {
        List(30) { i ->
            ConfettiParticle(
                color = colors[i % colors.size],
                startX = (10 + Math.random() * 80).toFloat(),
                delay = (Math.random() * 500).toLong(),
                size = (4 + Math.random() * 6).toFloat(),
                duration = (1500 + Math.random() * 1000).toInt(),
                isCircle = Math.random() > 0.5
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            var progress by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(active) {
                delay(particle.delay)
                val startTime = System.currentTimeMillis()
                while (true) {
                    val elapsed = System.currentTimeMillis() - startTime
                    progress = (elapsed.toFloat() / particle.duration).coerceAtMost(1f)
                    if (progress >= 1f) break
                    delay(16)
                }
            }
            Box(
                modifier = Modifier
                    .offset(
                        x = (particle.startX * 3.5f).dp,
                        y = (-20 + progress * 700).dp
                    )
                    .size(particle.size.dp)
                    .graphicsLayer {
                        alpha = 1f - progress
                        rotationZ = progress * 720f
                    }
                    .background(
                        particle.color,
                        if (particle.isCircle) CircleShape else RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
