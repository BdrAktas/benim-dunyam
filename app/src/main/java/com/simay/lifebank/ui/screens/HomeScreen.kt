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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
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
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Elevation
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Radius
import com.simay.lifebank.ui.theme.Rose
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Spacing
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbAccentHighlight
import com.simay.lifebank.ui.theme.YkbBorderHairline
import com.simay.lifebank.ui.theme.YkbCardGreen1
import com.simay.lifebank.ui.theme.YkbCardGreen2
import com.simay.lifebank.ui.theme.YkbCardGreen3
import com.simay.lifebank.ui.theme.YkbCardPurple1
import com.simay.lifebank.ui.theme.YkbCardPurple2
import com.simay.lifebank.ui.theme.YkbCardPurple3
import com.simay.lifebank.ui.theme.YkbDomainAilem
import com.simay.lifebank.ui.theme.YkbDomainAracim
import com.simay.lifebank.ui.theme.YkbDomainEvim
import com.simay.lifebank.ui.theme.YkbDomainSaglik
import com.simay.lifebank.ui.theme.YkbDomainSeyahat
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyMid
import com.simay.lifebank.ui.theme.YkbNavySoft
import com.simay.lifebank.ui.theme.YkbNeutral500
import com.simay.lifebank.ui.theme.YkbNeutral700
import com.simay.lifebank.ui.theme.YkbNeutral900
import com.simay.lifebank.ui.theme.YkbSurfaceCard
import com.simay.lifebank.ui.theme.YkbType
import com.simay.lifebank.ui.util.formatTRY
import java.util.Calendar

// Tek kontrat: (zaman vektörü + para vektörü) + sessiz alert badge.
// "alert" tam metni artık kartta gösterilmiyor — detay Bugünün Gündemi feed'inde.
private data class WorldCard(
    val id: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val timeLabel: String,   // Yakın olay + gün sayısı — primary scan
    val moneyLabel: String,  // YK ürününe bağlı para göstergesi — secondary
    val alertCount: Int,     // 0 ise badge gösterilmez
    val alertColor: Color,   // badge tint (Terra = acil, Honey = hatırlatma, domain = info)
    val color: Color         // domain accent (sol bar + icon tint)
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
    val cardAvailable = 75000 - 23456

    val worlds = listOf(
        // Evim — en yakın fatura + bu ay toplam. Feed'de 2 item (doğalgaz acil + elektrik).
        WorldCard("evim", Icons.Rounded.Home, "Evim",
            timeLabel = "3 fatura \u00B7 7 g\u00fcn",
            moneyLabel = "Toplam \u20BA2.140",
            alertCount = 2, alertColor = Terra, color = Sky),
        // Aracım — kasko yenilemesi + tahmini prim. Feed'de 1 bakım hatırlatması.
        WorldCard("aracim", Icons.Rounded.DirectionsCar, "Arac\u0131m",
            timeLabel = "Kasko 38 g\u00fcn",
            moneyLabel = "Tahmini \u20BA4.870",
            alertCount = 1, alertColor = Honey, color = Moss),
        // Sağlığım — check-up randevusu + TSS poliçe limit kullanımı.
        WorldCard("saglik", Icons.Rounded.MonitorHeart, "Sa\u011fl\u0131\u011f\u0131m",
            timeLabel = "Check-up \u00B7 34 g\u00fcn",
            moneyLabel = "TSS limit %18",
            alertCount = 0, alertColor = Rose, color = Rose),
        // Seyahatim — aktif proje + fon ilerlemesi. 1 info (sigorta eksik).
        WorldCard("seyahat", Icons.Rounded.Flight, "Seyahatim",
            timeLabel = "Tokyo \u00B7 70 g\u00fcn",
            moneyLabel = "Fon %63 \u00B7 \u20BA38K/\u20BA60K",
            alertCount = 1, alertColor = Lav, color = Lav),
        // Ailem — aktif düzenli talimat + İlk Param bakiye. Proaktif tetikleyici yok → badge yok.
        WorldCard("ailem", Icons.Rounded.Groups, "Ailem",
            timeLabel = "BES katk\u0131 \u00B7 bu ay",
            moneyLabel = "\u0130lk Param \u20BA42.3K",
            alertCount = 0, alertColor = Moss, color = Terra),
    )

    val smartFeed = listOf(
        SmartFeedItem("urgent", "\uD83D\uDD25", "Do\u011falgaz faturas\u0131", "\u0130GDA\u015e \u00B7 Son g\u00fcn!", "\u20BA847", Terra, "evim", "\u015eimdi \u00d6de"),
        SmartFeedItem("finance", "\u26A1", "Elektrik faturas\u0131", "22 Nis \u00B7 AYEDA\u015e", "\u20BA523", Honey, "evim"),
        SmartFeedItem("finance", "\uD83D\uDCB3", "Adios kart borcu", "20 Nis \u00B7 son \u00f6deme", "\u20BA23.456", Lav, "finans"),
        SmartFeedItem("life", "\uD83D\uDD27", "Ara\u00e7 bak\u0131m\u0131 gerekiyor", "Ya\u011f & fren balata \u00B7 48.200 km", null, Moss, "aracim"),
        SmartFeedItem("life", "\uD83E\uDDF3", "Tokyo haz\u0131rl\u0131klar\u0131", "JR Pass & seyahat sigortas\u0131 eksik", null, Lav, "seyahat"),
        SmartFeedItem("life", "\uD83C\uDFE5", "Check-up randevusu", "15 May\u0131s \u00B7 Ac\u0131badem \u00B7 34 g\u00fcn", null, Rose, "saglik"),
    )

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
                .background(YkbNavyDeep)
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
                .clip(RoundedCornerShape(bottomStart = Radius.hero, bottomEnd = Radius.hero))
                .background(Brush.verticalGradient(listOf(YkbNavyDeep, YkbNavyMid, YkbNavySoft)))
        ) {
            Column(
                modifier = Modifier.padding(Spacing.lg)
            ) {
                // greeting row (avatar kaldırıldı)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$greeting, Simay",
                        style = YkbType.Display.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.1.sp
                        )
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.14f))
                    ) {
                        Text("\uD83D\uDD14", fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(Spacing.lg))

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
                            text = "Hesabınızdaki Bakiye",
                            style = YkbType.BodyMd.copy(
                                color = Color.White.copy(alpha = 0.75f),
                                letterSpacing = 0.2.sp
                            ).merge(tightText)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.White, fontSize = YkbType.NumericXl.fontSize, fontWeight = FontWeight.Bold)) {
                                    append(totalMain)
                                }
                                if (totalDec.isNotEmpty()) {
                                    withStyle(SpanStyle(color = Color.White.copy(alpha = 0.45f), fontSize = YkbType.Heading2.fontSize, fontWeight = FontWeight.Bold)) {
                                        append(totalDec)
                                    }
                                }
                            },
                            style = YkbType.NumericXl.copy(color = Color.White).merge(tightText)
                        )
                        Spacer(Modifier.height(Spacing.sm))
                        Text(
                            text = "Tüm Hesaplarım \u203A",
                            style = YkbType.BodySm.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                            ),
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

                Spacer(Modifier.height(Spacing.lg))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kartlar",
                        style = YkbType.Heading3.copy(color = Color.White)
                    )
                    Text(
                        text = "Ekle +",
                        style = YkbType.BodySm.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable { onNavigate("finans") }
                    )
                }

                Spacer(Modifier.height(Spacing.md))

                // Horizontal card scroll — Adios (YK mor) + Bonus (Garanti yeşil)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    AccountGlassCard(
                        cardName = "Kullanılabilir Limit",
                        topLabel = "Adios Kart",
                        topEmoji = "\u2708\uFE0F",
                        amount = formatTRY(cardAvailable),
                        last4 = "8742",
                        statementDate = "20 Nis",
                        cardGradient = listOf(YkbCardPurple1, YkbCardPurple2, YkbCardPurple3),
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
                        cardGradient = listOf(YkbCardGreen1, YkbCardGreen2, YkbCardGreen3),
                        brandBadge = "\uD83C\uDF40",
                        onClick = { onNavigate("finans") }
                    )
                }
            }
        }

        // ═══ BENİM DÜNYAM — magazine stack (editorial pager kapakları) ═══
        Column(modifier = Modifier.padding(vertical = Spacing.md)) {
            Text(
                text = "Benim D\u00fcnyam",
                style = YkbType.Heading2.copy(color = Bark),
                modifier = Modifier.padding(horizontal = Spacing.lg)
            )
            Spacer(Modifier.height(Spacing.md))

            BenimDunyamMagazine(
                worlds = worlds,
                onNavigate = onNavigate
            )
        }

        Spacer(Modifier.height(Spacing.sectionGap - Spacing.md))

        // ═══ BUGÜNÜN GÜNDEMİ — 3-tier smart feed ═══
        Column(modifier = Modifier.padding(horizontal = Spacing.lg)) {
            Text(
                text = when (timeContext) {
                    "morning" -> "Bug\u00fcn seni neler bekliyor"
                    "evening" -> "Bug\u00fcn ilgilenmen gerekenler"
                    else -> "G\u00fcndemin"
                },
                style = YkbType.Heading2.copy(color = Bark)
            )
            Spacer(Modifier.height(Spacing.md))

            // Tier-1 urgent (pinned to top, only first one rendered as filled card)
            val urgent = smartFeed.firstOrNull { it.type == "urgent" }
            val rest = smartFeed.filter { it !== urgent }
            if (urgent != null) {
                SmartFeedUrgent(item = urgent, onClick = { onNavigate(urgent.domain) })
                Spacer(Modifier.height(Spacing.md))
            }

            // Tier-2 billable (has amount) — outlined card with accent bar
            val billable = rest.filter { it.amount != null }
            val info = rest.filter { it.amount == null }

            billable.forEachIndexed { idx, item ->
                if (idx > 0) Spacer(Modifier.height(Spacing.sm))
                SmartFeedBillable(item = item, onClick = { onNavigate(item.domain) })
            }

            if (billable.isNotEmpty() && info.isNotEmpty()) {
                Spacer(Modifier.height(Spacing.md))
            }

            // Tier-3 info — flat list rows with divider
            info.forEachIndexed { idx, item ->
                SmartFeedInfo(
                    item = item,
                    showDivider = idx < info.size - 1,
                    onClick = { onNavigate(item.domain) }
                )
            }
        }
    }
    }
}

// ═══════════════════════════════════════════════════════════════
// Smart feed — 3-tier components
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SmartFeedUrgent(item: SmartFeedItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(Radius.card)
    val tint = item.color
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(tint)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(Spacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 20.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = YkbType.Heading3.copy(color = Color.White)
                )
                Text(
                    text = item.sub,
                    style = YkbType.BodySm.copy(color = Color.White.copy(alpha = 0.9f))
                )
            }
            if (item.amount != null) {
                Text(
                    text = item.amount,
                    style = YkbType.NumericMd.copy(color = Color.White)
                )
            }
        }
        if (item.cta != null) {
            Spacer(Modifier.height(Spacing.md))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(Radius.button))
                    .background(Color.White)
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(vertical = Spacing.md),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.cta,
                    style = YkbType.Heading3.copy(color = tint, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun SmartFeedBillable(item: SmartFeedItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(Radius.card)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(YkbSurfaceCard)
            .border(Elevation.hairline, YkbBorderHairline, shape)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(3.dp)
                .align(Alignment.CenterStart)
                .background(item.color)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.md, bottom = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(Radius.iconBg))
                    .background(item.color.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 16.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = YkbType.Heading3.copy(color = Bark)
                )
                Text(
                    text = item.sub,
                    style = YkbType.BodySm.copy(color = YkbNeutral500)
                )
            }
            if (item.amount != null) {
                Text(
                    text = item.amount,
                    style = YkbType.NumericMd.copy(color = item.color)
                )
            }
        }
    }
}

@Composable
private fun SmartFeedInfo(item: SmartFeedItem, showDivider: Boolean, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 14.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = YkbType.Heading3.copy(color = Bark)
                )
                Text(
                    text = item.sub,
                    style = YkbType.BodySm.copy(color = YkbNeutral500)
                )
            }
            Text(
                text = "\u203A",
                style = YkbType.Heading2.copy(color = YkbNeutral500)
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Elevation.hairline)
                    .background(YkbBorderHairline)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// BENIM DUNYAM — magazine stack
// HorizontalPager + scale/alpha transforms for peek depth + big
// editorial cover typography per domain.
// ═══════════════════════════════════════════════════════════════

private fun domainBg(id: String): Color = when (id) {
    "evim" -> YkbDomainEvim
    "aracim" -> YkbDomainAracim
    "saglik" -> YkbDomainSaglik
    "seyahat" -> YkbDomainSeyahat
    "ailem" -> YkbDomainAilem
    else -> YkbNeutral700
}

// Extract teaser number + unit from timeLabel
private fun extractTeaser(timeLabel: String): Pair<String, String> {
    val numRegex = Regex("(\\d+)\\s*(\\w+)")
    val match = numRegex.find(timeLabel)
    return if (match != null) {
        match.groupValues[1] to match.groupValues[2]
    } else {
        // fallback — no numeric (e.g. "BES katkı · bu ay")
        "—" to "bu ay"
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun BenimDunyamMagazine(
    worlds: List<WorldCard>,
    onNavigate: (String) -> Unit
) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { worlds.size })

    Column {
        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = Spacing.sm,
            modifier = Modifier.height(340.dp)
        ) { pageIndex ->
            val offset = (pagerState.currentPage - pageIndex + pagerState.currentPageOffsetFraction)
                .let { if (it.isNaN()) 0f else it }
            val absOff = kotlin.math.abs(offset).coerceIn(0f, 1f)
            val scale = androidx.compose.ui.util.lerp(0.86f, 1f, 1f - absOff)
            val alpha = androidx.compose.ui.util.lerp(0.55f, 1f, 1f - absOff)

            val world = worlds[pageIndex]
            MagazineCoverCard(
                world = world,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                onClick = { onNavigate(world.id) }
            )
        }

        Spacer(Modifier.height(Spacing.md))

        // Page indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(worlds.size) { i ->
                val isActive = i == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isActive) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) YkbNeutral900 else YkbBorderHairline
                        )
                )
            }
        }
    }
}

@Composable
private fun MagazineCoverCard(
    world: WorldCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(Radius.card)
    val (teaserNum, teaserUnit) = extractTeaser(world.timeLabel)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .background(domainBg(world.id))
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        // Decorative corner halo
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .graphicsLayer { translationX = 60f; translationY = -40f }
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )

        // Top: domain name + alert chip
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(Spacing.xl),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = world.label,
                style = YkbType.Display.copy(color = Color.White)
            )
            if (world.alertCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(Color.White.copy(alpha = 0.24f))
                        .padding(horizontal = Spacing.sm, vertical = 4.dp)
                ) {
                    Text(
                        text = "${world.alertCount} acil",
                        style = YkbType.Badge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Big icon top-right (below header)
        Icon(
            imageVector = world.icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(48.dp)
                .padding(end = Spacing.xl)
        )

        // Center-left: big teaser number + unit
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = Spacing.xl),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Text(
                text = teaserNum,
                style = YkbType.NumericXl.copy(color = Color.White)
            )
            Text(
                text = teaserUnit,
                style = YkbType.BodyMd.copy(color = Color.White.copy(alpha = 0.8f)),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Bottom-left: moneyLabel
        Text(
            text = world.moneyLabel,
            style = YkbType.BodyMd.copy(color = Color.White),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.xl)
        )
    }
}

@Composable
private fun AlertBadge(count: Int, tint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(tint.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(tint)
        )
        Text(
            text = count.toString(),
            style = YkbType.Badge.copy(color = tint, fontWeight = FontWeight.Bold)
        )
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

            // Bottom: statement date + marquee slot (reserved on both cards)
            Column {
                Text(
                    "Hesap kesim: $statementDate",
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = SansFont
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    marqueeText ?: " ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = YkbAccentHighlight,
                    fontFamily = SansFont,
                    maxLines = 1,
                    modifier = if (marqueeText != null) Modifier.basicMarquee() else Modifier
                )
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

    val honey = YkbAccentHighlight
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
            color = YkbAccentHighlight,
            fontFamily = SansFont,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
            modifier = Modifier.clickable { onParaEkle() }
        )
    }
}

