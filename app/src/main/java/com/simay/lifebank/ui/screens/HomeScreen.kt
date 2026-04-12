package com.simay.lifebank.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.Rose
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbAccentPurple
import com.simay.lifebank.ui.util.formatTRY
import java.util.Calendar

private data class Contact(val name: String, val avatar: String, val color: Color)
private data class WorldCard(
    val id: String, val emoji: String, val label: String,
    val value: String, val sub: String,
    val alert: String, val alertColor: Color, val color: Color
)
private data class SmartFeedItem(
    val type: String, val emoji: String, val title: String,
    val sub: String, val amount: String? = null,
    val color: Color, val domain: String, val cta: String? = null
)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "G\u00fcnayd\u0131n"
        hour < 18 -> "Merhaba"
        else -> "\u0130yi ak\u015famlar"
    }
    val timeContext = when {
        hour < 12 -> "morning"
        hour < 18 -> "day"
        else -> "evening"
    }

    val balance = animateCountUp(47832)
    val cardSpent = animateCountUp(23456)
    val cardAvailable = 75000 - 23456
    val worldMiles = animateCountUp(14280)

    val contacts = listOf(
        Contact("Ahmet", "AY", Sky),
        Contact("Zeynep", "ZK", Rose),
        Contact("Mehmet", "MB", Moss),
        Contact("Elif", "ES", Lav),
    )

    val worlds = listOf(
        WorldCard("evim", "\uD83C\uDFE0", "Evim", "\u20BA3.24M", "ev de\u011feri",
            "Do\u011falgaz \u20BA847 \u00B7 son g\u00fcn!", Terra, Sky),
        WorldCard("aracim", "\uD83D\uDE97", "Arac\u0131m", "\u20BA1.2M", "ara\u00e7 de\u011feri",
            "Ya\u011f de\u011fi\u015fimi & fren balata kontrol\u00fc zaman\u0131", Honey, Moss),
        WorldCard("saglik", "\uD83C\uDF3F", "Sa\u011fl\u0131k", "15 May", "check-up",
            "Ac\u0131badem \u00B7 34 g\u00fcn kald\u0131", Rose, Rose),
        WorldCard("seyahat", "\u2708\uFE0F", "Seyahat", "Tokyo", "70 g\u00fcn kald\u0131",
            "JR Pass & sigorta al\u0131nmad\u0131", Honey, Lav),
        WorldCard("ailem", "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67", "Ailem", "\u20BA33.5K", "aile gideri",
            "Har\u00e7l\u0131k \u20BA500 \u00B7 Tatil fonu %63", Moss, Terra),
    )

    val smartFeed = listOf(
        SmartFeedItem("urgent", "\uD83D\uDD25", "Do\u011falgaz faturas\u0131", "\u0130GDA\u015e \u00B7 Son g\u00fcn!", "\u20BA847", Terra, "evim", "\u015eimdi \u00d6de"),
        SmartFeedItem("finance", "\u26A1", "Elektrik faturas\u0131", "22 Nis \u00B7 AYEDA\u015e", "\u20BA523", Honey, "evim"),
        SmartFeedItem("finance", "\uD83D\uDCB3", "Adios kart borcu", "20 Nis \u00B7 son \u00f6deme", "\u20BA23.456", Lav, "finans"),
        SmartFeedItem("life", "\uD83D\uDD27", "Ara\u00e7 bak\u0131m\u0131 gerekiyor", "Ya\u011f & fren balata \u00B7 48.200 km", null, Moss, "aracim"),
        SmartFeedItem("life", "\uD83E\uDDF3", "Tokyo haz\u0131rl\u0131klar\u0131", "JR Pass & seyahat sigortas\u0131 eksik", null, Lav, "seyahat"),
        SmartFeedItem("life", "\uD83C\uDFE5", "Check-up randevusu", "15 May\u0131s \u00B7 Ac\u0131badem \u00B7 34 g\u00fcn", null, Rose, "saglik"),
    )

    val navyDeep = Color(0xFF0A1F4A)
    val navyMid = Color(0xFF14306B)
    val navySoft = Color(0xFF1E4590)

    val totalBalance = balance + cardAvailable + 9830
    val totalStr = formatTRY(totalBalance)
    val (totalMain, totalDec) = remember(totalStr) {
        val comma = totalStr.lastIndexOf(',')
        if (comma >= 0) totalStr.substring(0, comma) to totalStr.substring(comma)
        else totalStr to ""
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fixed navy strip behind status bar — kayan içerik bu bandın arkasına girmez
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(navyDeep)
                .align(Alignment.TopCenter)
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // ═══ DARK NAVY HERO PANEL (greeting + total balance + cards) ═══

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Brush.verticalGradient(listOf(navyDeep, navyMid, navySoft)))
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            ) {
                // greeting row (avatar kaldırıldı)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(greeting, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), fontFamily = SansFont, letterSpacing = 0.3.sp)
                        Text("Simay", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = SansFont)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.14f))
                    ) {
                        Text("\uD83D\uDD14", fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                val tightText = androidx.compose.ui.text.TextStyle(
                    platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false),
                    lineHeightStyle = androidx.compose.ui.text.style.LineHeightStyle(
                        alignment = androidx.compose.ui.text.style.LineHeightStyle.Alignment.Center,
                        trim = androidx.compose.ui.text.style.LineHeightStyle.Trim.Both
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Hesabınızdaki Bakiye",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.75f),
                            fontFamily = SansFont, letterSpacing = 0.2.sp,
                            lineHeight = 13.sp,
                            style = tightText
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)) {
                                    append(totalMain)
                                }
                                if (totalDec.isNotEmpty()) {
                                    withStyle(SpanStyle(color = Color.White.copy(alpha = 0.45f), fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                                        append(totalDec)
                                    }
                                }
                            },
                            fontFamily = SansFont,
                            lineHeight = 34.sp,
                            style = tightText
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Tüm Hesaplarım \u203A",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = SansFont,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                            modifier = Modifier.clickable { onNavigate("finans") }
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        MaasShimmerCard(onParaEkle = { onNavigate("finans") })
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kartlar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White, fontFamily = SansFont)
                    Text("Ekle +", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.85f), fontFamily = SansFont)
                }

                Spacer(Modifier.height(12.dp))

                // Horizontal card scroll — Adios (YK mor) + Bonus (Garanti yeşil)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AccountGlassCard(
                        cardName = "Kullanılabilir Limit",
                        topLabel = "Adios Kart",
                        topEmoji = "\u2708\uFE0F",
                        amount = formatTRY(cardAvailable),
                        last4 = "8742",
                        statementDate = "20 Nis",
                        cardGradient = listOf(
                            Color(0xFF3A1A6B),
                            Color(0xFF5B2A9E),
                            Color(0xFF7B3FC9)
                        ),
                        brandBadge = null,
                        marqueeText = "15.000 TL puanınız var \u2708\uFE0F  Seyahatim+ ile puanlarınızı 2 kat değerinde kullanın",
                        onClick = { onNavigate("finans") }
                    )
                    AccountGlassCard(
                        cardName = "Kullanılabilir Limit",
                        topLabel = "Bonus Kart",
                        topEmoji = null,
                        amount = formatTRY(38450),
                        last4 = "4433",
                        statementDate = "02 May",
                        cardGradient = listOf(
                            Color(0xFF0F4A1E),
                            Color(0xFF1E7A33),
                            Color(0xFF2DA04A)
                        ),
                        brandBadge = "\uD83C\uDF40",
                        onClick = { onNavigate("finans") }
                    )
                }
            }
        }

        // ═══ BENİM DÜNYAM ═══
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text("Benim D\u00fcnyam", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Bark, fontFamily = SerifFont)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                worlds.forEach { w ->
                    GlassSurface(
                        animate = true, intensity = GlassIntensity.Normal, accent = w.color,
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                        onClick = { onNavigate(w.id) },
                        modifier = Modifier.width(160.dp).height(150.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                                    Text(w.emoji, fontSize = 22.sp)
                                    Text(w.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                                }
                                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                                    Text(w.value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SerifFont)
                                    Text(w.sub, fontSize = 10.sp, color = Pebble, fontFamily = SansFont)
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(w.alertColor.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
                                    .border(1.dp, w.alertColor.copy(alpha = 0.09f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(w.alertColor))
                                Text(w.alert, fontSize = 9.sp, color = w.alertColor, fontWeight = FontWeight.SemiBold, fontFamily = SansFont, lineHeight = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // ═══ AKILLI AKIŞ ═══
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(
                text = when (timeContext) {
                    "morning" -> "Bug\u00fcn seni neler bekliyor"
                    "evening" -> "Bug\u00fcn ilgilenmen gerekenler"
                    else -> "G\u00fcndemin"
                },
                fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Bark, fontFamily = SerifFont
            )
            Spacer(Modifier.height(10.dp))
            smartFeed.forEach { item ->
                val isUrgent = item.type == "urgent"
                GlassSurface(
                    animate = true,
                    intensity = if (isUrgent) GlassIntensity.Normal else GlassIntensity.Subtle,
                    accent = if (isUrgent) item.color else null,
                    glow = isUrgent,
                    borderLeftColor = item.color,
                    contentPadding = PaddingValues(horizontal = if (isUrgent) 16.dp else 14.dp, vertical = if (isUrgent) 14.dp else 11.dp),
                    onClick = { onNavigate(item.domain) },
                    modifier = Modifier.padding(bottom = if (isUrgent) 10.dp else 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(item.emoji, fontSize = if (isUrgent) 20.sp else 16.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(item.title, fontSize = if (isUrgent) 14.sp else 13.sp, fontWeight = if (isUrgent) FontWeight.Bold else FontWeight.SemiBold, color = Bark, fontFamily = SansFont)
                                if (item.amount != null) {
                                    Text(item.amount, fontSize = if (isUrgent) 16.sp else 13.sp, fontWeight = FontWeight.Bold, color = if (isUrgent) item.color else Bark, fontFamily = SerifFont)
                                }
                            }
                            Text(item.sub, fontSize = 11.sp, color = Stone, fontFamily = SansFont, modifier = Modifier.padding(top = 1.dp))
                        }
                    }
                    if (item.cta != null) {
                        Spacer(Modifier.height(10.dp))
                        GlassButton(text = item.cta, color = item.color, isFull = true, isSmall = true)
                    }
                }
            }
        }
    }
    }
}

@Composable
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
private fun AccountGlassCard(
    cardName: String,
    topLabel: String,
    topEmoji: String?,
    amount: String,
    last4: String,
    statementDate: String,
    cardGradient: List<Color>,
    brandBadge: String? = null,
    marqueeText: String? = null,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .width(168.dp)
            .height(190.dp)
            .clip(shape)
            .background(Brush.linearGradient(cardGradient))
            .border(1.dp, Color.White.copy(alpha = 0.18f), shape)
    ) {
        // Decorative sheen
        Box(
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopEnd)
                .graphicsLayer { translationX = 40f; translationY = -40f }
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.BottomStart)
                .graphicsLayer { translationX = -30f; translationY = 30f }
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: label + optional emoji, with last4 directly beneath
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        topLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = SansFont,
                        letterSpacing = 0.3.sp
                    )
                    if (topEmoji != null) {
                        Text(topEmoji, fontSize = 13.sp)
                    }
                    if (brandBadge != null) {
                        Text(brandBadge, fontSize = 18.sp)
                    }
                }
                Text(
                    "\u2022\u2022\u2022\u2022 $last4",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    fontFamily = SansFont,
                    letterSpacing = 1.sp
                )
            }

            // Middle: card name + amount
            Column {
                Text(
                    cardName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = SansFont
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    amount,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = SansFont
                )
            }

            // Bottom: statement date + optional marquee
            Column {
                Text(
                    "Hesap kesim: $statementDate",
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = SansFont
                )
                if (marqueeText != null) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        marqueeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFD166),
                        fontFamily = SansFont,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
            }
        }
    }
    @Suppress("UNUSED_EXPRESSION") onClick
}

@Composable
private fun MaasShimmerCard(onParaEkle: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    val t = rememberInfiniteTransition(label = "maasShimmer")
    val sweep by t.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "sweep"
    )
    val hue by t.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "hue"
    )

    val honey = Color(0xFFFFD166)
    val rose = Color(0xFFFF7A8A)
    val mint = Color(0xFF6DE5C9)
    val lav = Color(0xFFB79BFF)
    val hot1 = lerp(honey, mint, hue)
    val hot2 = lerp(rose, lav, hue)
    val hot3 = lerp(lav, honey, hue)

    val base = Color.White.copy(alpha = 0.28f)
    val palette = listOf(
        base, base, base, base, base, base,
        hot1, Color.White, hot2, hot3,
        base, base, base, base, base, base
    )
    val shift = ((sweep * palette.size).toInt()).coerceIn(0, palette.size - 1)
    val rotated = palette.drop(shift) + palette.take(shift)
    val borderBrush = Brush.sweepGradient(rotated)

    val fillBrush = Brush.linearGradient(
        listOf(
            hot1.copy(alpha = 0.14f),
            hot2.copy(alpha = 0.18f),
            hot3.copy(alpha = 0.14f)
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .clip(shape)
            .background(fillBrush)
            .border(1.5.dp, borderBrush, shape)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            "Maa\u015f hesab\u0131na \u00f6zel",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = SansFont,
            letterSpacing = 0.3.sp
        )
        Text(
            "%38 faiz",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = SansFont
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "Para Ekle +",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFFD166),
            fontFamily = SansFont,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
            modifier = Modifier.clickable { onParaEkle() }
        )
    }
}

