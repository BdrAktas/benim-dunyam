package com.simay.lifebank.ui.screens

import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
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
import com.simay.lifebank.ui.theme.Stone
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
import com.simay.lifebank.ui.theme.YkbDomainParam
import com.simay.lifebank.ui.theme.YkbDomainSaglik
import com.simay.lifebank.ui.theme.YkbDomainSeyahat
import com.simay.lifebank.ui.theme.YkbNeutral100
import com.simay.lifebank.ui.theme.YkbNeutral300
import com.simay.lifebank.ui.theme.YkbNeutral500
import com.simay.lifebank.ui.theme.YkbNeutral900
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyMid
import com.simay.lifebank.ui.theme.YkbNavySoft
import com.simay.lifebank.ui.theme.YkbNeutral700
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
private data class CreditOverview(
    val totalLimit: Int,
    val used: Int
) {
    val available: Int get() = totalLimit - used
    val usageRatio: Float get() = if (totalLimit == 0) 0f else used.toFloat() / totalLimit
}

private data class CreditLimit(
    val id: String,
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val totalLimit: Int,
    val used: Int,
    val accent: Color
) {
    val available: Int get() = totalLimit - used
}

private data class PaymentSchedule(
    val dayLabel: String,   // "Bugün" / "Yarın" / "15 Nis"
    val amount: Int,        // TRY
    val title: String,      // "Su", "Elektrik", "Adios"
    val domain: String,
    val accent: Color,
    val urgent: Boolean = false  // highlight today / overdue
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
        // Evim — Doğalgaz son gün · 1 acil
        WorldCard("evim", Icons.Rounded.Home, "Evim",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Terra, color = Sky),
        // Aracım — HGS bakiye düşük · 1 acil
        WorldCard("aracim", Icons.Rounded.DirectionsCar, "Arac\u0131m",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Honey, color = Moss),
        // Sağlığım — Cepten ödeme geri ödeme fırsatı · 1 fırsat
        WorldCard("saglik", Icons.Rounded.MonitorHeart, "Sa\u011fl\u0131\u011f\u0131m",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Honey, color = Rose),
        // Seyahat — Sigorta eksik · 1 acil
        WorldCard("seyahat", Icons.Rounded.Flight, "Seyahatim",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Lav, color = Lav),
        // Ailem — Okul harcı yaklaşıyor · 1 acil
        WorldCard("ailem", Icons.Rounded.Groups, "Ailem",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Terra, color = Terra),
        // Param — Cash drag + vade fırsatı · 2 fırsat
        WorldCard("param", Icons.Rounded.AccountBalanceWallet, "Param",
            timeLabel = "", moneyLabel = "",
            alertCount = 2, alertColor = Honey, color = Honey),
    )

    val smartFeed = listOf(
        SmartFeedItem("urgent", "\uD83D\uDD25", "Do\u011falgaz faturas\u0131", "\u0130GDA\u015e \u00B7 Son g\u00fcn!", "\u20BA847", Terra, "evim", "\u015eimdi \u00d6de"),
        SmartFeedItem("finance", "\u26A1", "Elektrik faturas\u0131", "22 Nis \u00B7 AYEDA\u015e", "\u20BA523", Honey, "evim"),
        SmartFeedItem("finance", "\uD83D\uDCB3", "Adios kart borcu", "20 Nis \u00B7 son \u00f6deme", "\u20BA23.456", Lav, "finans"),
        SmartFeedItem("life", "\uD83D\uDD27", "Ara\u00e7 bak\u0131m\u0131 gerekiyor", "Ya\u011f & fren balata \u00B7 48.200 km", null, Moss, "aracim"),
        SmartFeedItem("life", "\uD83E\uDDF3", "Tokyo haz\u0131rl\u0131klar\u0131", "JR Pass al \u00B7 seyahat sigortas\u0131n\u0131 g\u00fcvenceye al", null, Lav, "seyahat"),
        SmartFeedItem("life", "\uD83C\uDFE5", "Check-up randevusu", "15 May\u0131s \u00B7 Ac\u0131badem \u00B7 34 g\u00fcn", null, Rose, "saglik"),
    )

    // 7 günlük ödeme takvimi — sadece parasal + tarihi olan olaylar
    val weeklySchedule = listOf(
        PaymentSchedule("Bug\u00fcn",  189,    "Su",       "evim",   Sky,   urgent = true),
        PaymentSchedule("Yar\u0131n",  523,    "Elektrik", "evim",   Honey),
        PaymentSchedule("15 Nis",      847,    "Do\u011falgaz", "evim", Terra),
        PaymentSchedule("18 Nis",      8920,   "Bonus Kart", "finans", Lav),
        PaymentSchedule("20 Nis",      23456,  "Adios Kart", "finans", Lav),
    )

    // Sana özel kredi limitleri — toplam + kırılım
    val creditLimits = listOf(
        CreditLimit(
            id = "ihtiyac",
            name = "\u0130htiya\u00e7 Kredisi",
            icon = Icons.Rounded.AccountBalanceWallet,
            totalLimit = 150000,
            used = 35000,
            accent = Moss
        ),
        CreditLimit(
            id = "tasit",
            name = "Ta\u015f\u0131t Kredisi \u00f6n onay",
            icon = Icons.Rounded.DirectionsCar,
            totalLimit = 120000,
            used = 0,
            accent = Sky
        ),
        CreditLimit(
            id = "konut",
            name = "Konut Kredisi \u00f6n onay",
            icon = Icons.Rounded.Home,
            totalLimit = 500000,
            used = 0,
            accent = Rose
        ),
        CreditLimit(
            id = "kmh",
            name = "KMH",
            icon = Icons.Rounded.AccountBalance,
            totalLimit = 25000,
            used = 0,
            accent = Honey
        ),
    )
    val creditOverview = CreditOverview(
        totalLimit = creditLimits.sumOf { it.totalLimit },
        used = creditLimits.sumOf { it.used }
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
                // greeting row (avatar + bell kaldırıldı)
                Text(
                    text = "$greeting, Simay",
                    style = YkbType.Display.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.1.sp
                    )
                )

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
            BenimDunyamMagazine(
                worlds = worlds,
                onNavigate = onNavigate
            )
        }

        Spacer(Modifier.height(Spacing.sectionGap - Spacing.md))

        // ═══ SANA ÖZEL KREDİ LİMİTLERİN ═══
        Column(modifier = Modifier.padding(horizontal = Spacing.lg)) {
            Text(
                text = "Sana \u00f6zel kredi limitlerin",
                style = YkbType.Heading2.copy(color = Bark)
            )
            Spacer(Modifier.height(Spacing.md))
            CreditLimitsSection(
                overview = creditOverview,
                limits = creditLimits,
                onLimitClick = { onNavigate("finans") },
                onSeeAll = { onNavigate("finans") }
            )
        }
    }
    }
}

// ═══════════════════════════════════════════════════════════════
// Sana özel kredi limitlerin — toplam + kırılım + detay satırı
// ═══════════════════════════════════════════════════════════════

@Composable
private fun CreditLimitsSection(
    overview: CreditOverview,
    limits: List<CreditLimit>,
    onLimitClick: (CreditLimit) -> Unit,
    onSeeAll: () -> Unit
) {
    val shape = RoundedCornerShape(28.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, YkbBorderHairline, shape)
            .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Hero: kullanılabilir limit
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Kullan\u0131labilir limitin",
                style = YkbType.BodySm.copy(color = Stone)
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = formatTRY(overview.available),
                    style = YkbType.NumericXl.copy(color = Moss, fontSize = 30.sp, lineHeight = 34.sp),
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = "/ ${formatTRY(overview.totalLimit)}",
                    style = YkbType.BodyMd.copy(color = Stone),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        // Progress bar — used vs available
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(YkbNeutral100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(overview.usageRatio.coerceIn(0f, 1f))
                    .background(Terra.copy(alpha = 0.7f))
            )
        }

        Text(
            text = "${formatTRY(overview.used)} kullan\u0131mda",
            style = YkbType.BodySm.copy(color = Stone)
        )

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(YkbBorderHairline)
        )

        // Kredi listesi
        limits.forEach { limit ->
            CreditLimitRow(limit = limit, onClick = { onLimitClick(limit) })
        }

        // Divider + all
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(YkbBorderHairline)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(role = Role.Button, onClick = onSeeAll)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "T\u00fcm\u00fcn\u00fc \u0130ncele",
                style = YkbType.BodyMd.copy(color = Sky, fontWeight = FontWeight.SemiBold)
            )
            Text("  \u2197", color = Sky, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CreditLimitRow(limit: CreditLimit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(limit.accent.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = limit.icon,
                contentDescription = null,
                tint = limit.accent,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = limit.name,
                style = YkbType.BodyMd.copy(color = Bark, fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
            Text(
                text = if (limit.used > 0) "${formatTRY(limit.used)} kullan\u0131mda"
                       else "Kullan\u0131labilir",
                style = YkbType.BodySm.copy(color = Stone)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatTRY(limit.available),
                style = YkbType.BodyMd.copy(color = Bark, fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Text(
                text = "haz\u0131r",
                style = YkbType.BodySm.copy(color = Moss, fontWeight = FontWeight.SemiBold)
            )
        }
        Text("\u203A", color = Stone, fontSize = 20.sp)
    }
}

// ═══════════════════════════════════════════════════════════════
// Weekly payment strip — 7 günlük nakit akışı agregasyonu (legacy)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun WeeklyPaymentStrip(
    schedules: List<PaymentSchedule>,
    onClick: (PaymentSchedule) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        contentPadding = PaddingValues(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        items(schedules.size) { idx ->
            val s = schedules[idx]
            PaymentChip(s, onClick = { onClick(s) })
        }
    }
}

@Composable
private fun PaymentChip(s: PaymentSchedule, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .clip(shape)
            .background(Color.White)
            .border(1.dp, YkbBorderHairline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .width(118.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(s.accent)
            )
            Text(
                text = s.dayLabel,
                style = YkbType.BodySm.copy(
                    color = if (s.urgent) Terra else Stone,
                    fontWeight = if (s.urgent) FontWeight.Bold else FontWeight.SemiBold
                )
            )
        }
        Text(
            text = formatTRY(s.amount),
            style = YkbType.NumericMd.copy(color = Bark),
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = s.title,
            style = YkbType.BodySm.copy(color = Stone),
            maxLines = 1,
            softWrap = false
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Smart feed — 3-tier components (legacy, feed kaldırıldı)
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
    "param" -> YkbDomainParam
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
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = Spacing.sm,
            modifier = Modifier.height(360.dp)
        ) { pageIndex ->
            val offset = (pagerState.currentPage - pageIndex + pagerState.currentPageOffsetFraction)
                .let { if (it.isNaN()) 0f else it }
            val absOff = kotlin.math.abs(offset).coerceIn(0f, 1f)
            val scale = androidx.compose.ui.util.lerp(0.90f, 1f, 1f - absOff)
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

private data class Pill(
    val text: String,
    val actionText: String? = null,
    val actionColor: Color = Terra
)

private data class CardContent(
    val emoji: String,
    val title: String,
    val pills: List<Pill>,
    val bigValue: String,
    val bigValueSuffix: String,
    val footnote: String,
    val cta: String
)

private fun cardContent(id: String): CardContent = when (id) {
    "evim" -> CardContent(
        emoji = "\uD83C\uDFE0",
        title = "Yakla\u015fan 3 fatura\n\u00f6demen var",
        pills = listOf(
            Pill("Su Bug\u00fcn", actionText = "\u00d6de", actionColor = Terra),
            Pill("Elektrik Yar\u0131n"),
            Pill("Do\u011falgaz Son 2 G\u00fcn")
        ),
        bigValue = "\u20BA6.990",
        bigValueSuffix = "",
        footnote = "Bu ay toplam ev giderin",
        cta = "Giderleri G\u00f6r"
    )
    "aracim" -> CardContent(
        emoji = "\uD83D\uDE97",
        title = "Kaskon 15 g\u00fcn\nsonra bitiyor",
        pills = listOf(Pill("Hasars\u0131zl\u0131k indirimi"), Pill("\u00dccretsiz Mini Onar\u0131m"), Pill("S\u0131n\u0131rl\u0131 S\u00fcre 2000 TL ek indirim")),
        bigValue = "\u20BA8.500",
        bigValueSuffix = "",
        footnote = "Tahmini yenileme primin",
        cta = "Detaylar\u0131 G\u00f6r"
    )
    "seyahat" -> CardContent(
        emoji = "\u2708\uFE0F",
        title = "Antalya tatiline\n18 g\u00fcn kald\u0131",
        pills = listOf(Pill("Sigorta eksik"), Pill("D\u00f6viz \u20BA14.200"), Pill("Pasaport ge\u00e7erli")),
        bigValue = "\u20BA28.500",
        bigValueSuffix = "",
        footnote = "Tahmini tatil b\u00fct\u00e7en",
        cta = "G\u00fcvenceye al"
    )
    "saglik" -> CardContent(
        emoji = "\u2764\uFE0F",
        title = "TSS limitinin\n%82'si sende",
        pills = listOf(Pill("Check-up 34 g\u00fcn sonra"), Pill("Bu ay \u20BA0 kulland\u0131n"), Pill("3 km'de 4 merkez")),
        bigValue = "%82",
        bigValueSuffix = "",
        footnote = "Y\u0131ll\u0131k limitinden kalan",
        cta = "Randevu al"
    )
    "ailem" -> CardContent(
        emoji = "\uD83D\uDC6A",
        title = "\u00dcniversite harc\u0131na\n18 g\u00fcn kald\u0131",
        pills = listOf(Pill("Har\u00e7 1 May"), Pill("\u0130lk Param \u20BA42K"), Pill("Devlet katk\u0131s\u0131 \u20BA6.200")),
        bigValue = "\u20BA8.500",
        bigValueSuffix = "",
        footnote = "Har\u00e7 i\u00e7in eksik tutar\u0131n",
        cta = "Transferi planla"
    )
    "param" -> CardContent(
        emoji = "\uD83D\uDCB0",
        title = "Vadesiz paran\neriyor",
        pills = listOf(Pill("e-Mevduat %52"), Pill("YUVAM TL %55"), Pill("Alt\u0131n \u20BA3.240/gr")),
        bigValue = "~\u20BA320",
        bigValueSuffix = "",
        footnote = "Son 30 g\u00fcnde eriyen miktar",
        cta = "3 se\u00e7ene\u011fi g\u00f6r"
    )
    else -> CardContent("", "", emptyList(), "", "", "", "Detay")
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun MagazineCoverCard(
    world: WorldCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val content = cardContent(world.id)
    val accent = world.color
    val shape = RoundedCornerShape(28.dp)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, YkbBorderHairline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Header: emoji badge + domain label | alert chip + menu dot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = world.icon,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = world.label,
                    style = YkbType.Heading2.copy(color = YkbNeutral900, fontWeight = FontWeight.SemiBold)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(YkbNeutral100)
            ) {
                Text("\u22EF", color = YkbNeutral500, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(Spacing.xs))

        // Title — big, dark, 2 lines
        Text(
            text = content.title,
            style = YkbType.Display.copy(
                color = YkbNeutral900,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        // Pills row — wraps to next line if not enough horizontal space
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            content.pills.forEach { p -> CardPill(p) }
        }


        Spacer(Modifier.weight(1f))

        // Footer: value block (left) + CTA (right), CTA top aligned with value top
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = content.bigValue,
                        style = YkbType.NumericXl.copy(color = accent, fontSize = 30.sp, lineHeight = 34.sp),
                        maxLines = 1,
                        softWrap = false
                    )
                    if (content.bigValueSuffix.isNotEmpty()) {
                        Text(
                            text = content.bigValueSuffix,
                            style = YkbType.BodyMd.copy(color = YkbNeutral500),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }
                Text(
                    text = content.footnote,
                    style = YkbType.BodySm.copy(color = YkbNeutral500)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(accent)
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Text(
                    text = content.cta,
                    style = YkbType.BodyMd.copy(color = Color.White, fontWeight = FontWeight.SemiBold),
                    maxLines = 1
                )
                Text(
                    text = "\u2197",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CardPill(pill: Pill) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(YkbNeutral100)
            .border(1.dp, YkbBorderHairline, RoundedCornerShape(Radius.pill))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = pill.text,
            style = YkbType.BodySm.copy(color = YkbNeutral700, fontWeight = FontWeight.Medium)
        )
        if (pill.actionText != null) {
            Text(
                text = pill.actionText,
                style = YkbType.BodySm.copy(
                    color = pill.actionColor,
                    fontWeight = FontWeight.Bold,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            )
        }
    }
}

// ═══ PER-DOMAIN CONTENT (hardcoded mock data for this iteration) ═══
// TODO: wire to real data sources — each card is a snapshot of that domain's state.

@Composable
private fun StatRow(label: String, value: String, valueBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = YkbType.BodySm.copy(color = Color.White.copy(alpha = 0.75f))
        )
        Text(
            text = value,
            style = YkbType.BodyMd.copy(
                color = Color.White,
                fontWeight = if (valueBold) FontWeight.Bold else FontWeight.SemiBold
            ),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PrimaryBlock(
    topLabel: String,
    primary: String,
    secondary: String,
    trendDelta: String? = null
) {
    Column {
        Text(
            text = topLabel,
            style = YkbType.BodySm.copy(color = Color.White.copy(alpha = 0.75f))
        )
        Spacer(Modifier.height(2.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Text(
                text = primary,
                style = YkbType.NumericLg.copy(color = Color.White)
            )
            if (trendDelta != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(Color.White.copy(alpha = 0.22f))
                        .padding(horizontal = Spacing.sm, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.TrendingUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = trendDelta,
                        style = YkbType.BodySm.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
        Text(
            text = secondary,
            style = YkbType.BodySm.copy(color = Color.White.copy(alpha = 0.85f))
        )
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.card))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) { content() }
}

@Composable
private fun EvimCoverContent() {
    // Primary: aylık ev harcaması anomaly (fatura + aidat + otomatik ödeme toplamı)
    // Glance cevabı: "Bu ay ev harcamam geçen aya göre fark ediyor mu?"
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Bu ay ev harcaman",
            primary = "\u20BA6.990",
            secondary = "Geçen ay \u20BA6.120 \u00B7 +%14 \u2022 anormal"
        )
        InfoCard {
            StatRow("Yaklaşan fatura", "Do\u011falgaz \u00B7 bugün \u00B7 \u20BA847", valueBold = true)
            StatRow("Sonraki kredi taksidi", "12 Nis \u00B7 \u20BA4.850")
            StatRow("DASK yenileme", "15 Ara \u00B7 247 gün")
            StatRow("Aidat", "Nisan \u00B7 \u20BA1.850 \u00B7 ödendi")
        }
    }
}

@Composable
private fun AracimCoverContent() {
    // Primary: kasko yenileme (takvim + yasa + fiyat beklentisi — lifestyle banking prototipi)
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Kasko yenileme",
            primary = "38 gün",
            secondary = "Tahmini \u20BA4.870"
        )
        InfoCard {
            StatRow("HGS bakiye", "\u20BA42 \u2022 2-3 geçiş kaldı", valueBold = true)
            StatRow("Trafik sigortası", "54 gün \u00B7 \u20BA1.320")
            StatRow("Muayene", "Kas 2026 \u00B7 7 ay")
            StatRow("Son ceza kontrolü", "2 gün önce \u00B7 yok")
        }
    }
}

@Composable
private fun SeyahatCoverContent() {
    // Primary: aktif seyahat niyeti (uçak bileti / havalimanı işlem sinyalinden türetilir).
    // Seyahat niyeti yoksa bu kart ana sayfaya gelmemeli — TODO: dinamik sıralama.
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Yaklaşan seyahat",
            primary = "Antalya 18 gün",
            secondary = "Sigortan yok \u00B7 2 dk hallet"
        )
        InfoCard {
            StatRow("Seyahat sigortası", "Güvenceye al", valueBold = true)
            StatRow("Döviz hedefin", "\u20AC840 / \u20AC2.000 \u00B7 %42")
            StatRow("Yurt dışı kart limiti", "\u20BA32K / \u20BA80K")
            StatRow("Pasaport", "2 yıl geçerli")
        }
    }
}

@Composable
private fun SaglikCoverContent() {
    // Primary: tasarruf framing (value hissi + churn önleme). TSS kullanımı = elde tutma.
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Bu yıl TSS tasarrufun",
            primary = "\u20BA1.820",
            secondary = "Yıllık limitin %82'si duruyor"
        )
        InfoCard {
            StatRow("Check-up hakkın", "Kullanılmadı \u00B7 34 gün", valueBold = true)
            StatRow("Poliçe yenileme", "3 Ağu \u00B7 113 gün")
            StatRow("Cepten harcama", "\u20BA640 \u00B7 geri al")
            StatRow("Yakındaki anlaşmalı", "3 km içinde 4 merkez")
        }
    }
}

@Composable
private fun AilemCoverContent() {
    // Primary: okul harcı (rotating — Mart-Eyl için en güçlü takvim olayı).
    // Diğer aylarda: "Devlet katkısı bu yıl +₺6.200" veya "İlk Param %71".
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Yaklaşan aile yükümlülüğü",
            primary = "18 gün \u00B7 \u20BA8.500",
            secondary = "Üniversite harcı \u00B7 hazır mısın?"
        )
        InfoCard {
            StatRow("Düzenli transfer", "Anne \u20BA2.500 \u00B7 1 May", valueBold = true)
            StatRow("Devlet katkısı (yıl)", "+\u20BA6.200")
            StatRow("BES birikim", "\u20BA24.8K")
            StatRow("Çocuk birikimi", "\u0130lk Param \u20BA42.3K \u00B7 %71")
        }
    }
}

@Composable
private fun ParamCoverContent() {
    // Primary: cash drag uyarısı (Revolut/Monzo pattern'i, enflasyon ortamı kritik).
    // "Net varlık" vanity metric — çıkarıldı.
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        PrimaryBlock(
            topLabel = "Vadesizde atıl",
            primary = "\u20BA80K",
            secondary = "30+ gün hareketsiz \u00B7 bu ay ~\u20BA320 eridi"
        )
        InfoCard {
            StatRow("En yakın vade", "5 gün \u00B7 +\u20BA4.120 \u2022 dönüştür?", valueBold = true)
            StatRow("Portföyün bu ay", "+%2.4 \u00B7 \u20BA5.240")
            StatRow("Findeks", "1.842 \u00B7 +12")
            StatRow("Altın hedefin", "\u20BA68K \u00B7 %82")
        }
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

