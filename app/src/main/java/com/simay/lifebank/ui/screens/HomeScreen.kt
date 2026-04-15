package com.simay.lifebank.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
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
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
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
import com.simay.lifebank.ui.components.AiOrbBadge
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
import com.simay.lifebank.ui.theme.Teal
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbAccentHighlight
import com.simay.lifebank.ui.theme.YkbBorderHairline
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.simay.lifebank.R
import com.simay.lifebank.ui.theme.YkbCardGold1
import com.simay.lifebank.ui.theme.YkbCardGold2
import com.simay.lifebank.ui.theme.YkbCardGold3
import com.simay.lifebank.ui.theme.YkbCardGreen1
import com.simay.lifebank.ui.theme.YkbCardGreen2
import com.simay.lifebank.ui.theme.YkbCardGreen3
import com.simay.lifebank.ui.theme.YkbCardPurple1
import com.simay.lifebank.ui.theme.YkbCardPurple2
import com.simay.lifebank.ui.theme.YkbCardPurple3
import com.simay.lifebank.ui.theme.YkbDomainAracim
import com.simay.lifebank.ui.theme.YkbDomainEvim
import com.simay.lifebank.ui.theme.YkbDomainParam
import com.simay.lifebank.ui.theme.YkbNeutral100
import com.simay.lifebank.ui.theme.YkbNeutral300
import com.simay.lifebank.ui.theme.YkbNeutral500
import com.simay.lifebank.ui.theme.YkbNeutral900
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyMid
import com.simay.lifebank.ui.theme.YkbNavySoft
import com.simay.lifebank.ui.theme.YkbCanvas
import com.simay.lifebank.ui.theme.YkbPrimary
import com.simay.lifebank.ui.theme.YkbSuccess
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
    val color: Color,        // domain accent (sol bar + icon tint)
    val ctaRoute: String? = null,  // intent-carrying route; null → fallback to id
    @field:DrawableRes val illustrationRes: Int? = null  // Stitch doodle; null → icon fallback
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
    @field:DrawableRes val illustrationRes: Int? = null,
    val totalLimit: Int,
    val used: Int,
    val accent: Color,
    val route: String
) {
    val available: Int get() = totalLimit - used
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "G\u00fcnayd\u0131n"
        hour < 18 -> "Merhaba"
        else -> "\u0130yi ak\u015famlar"
    }
    val balance = animateCountUp(47832)
    val cardAvailable = 75000 - 23456

    val worlds = listOf(
        // Evim — Doğalgaz son gün · 1 acil
        WorldCard("evim", Icons.Rounded.Home, "Evim",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Terra, color = Sky,
            ctaRoute = "evim?intent=pay_bill",
            illustrationRes = R.drawable.ic_stitch_evim),
        // Aracım — HGS bakiye düşük · 1 acil
        WorldCard("aracim", Icons.Rounded.DirectionsCar, "Arac\u0131m",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Honey, color = Moss,
            ctaRoute = "aracim?intent=kasko_quote",
            illustrationRes = R.drawable.ic_stitch_aracim),
        // Sağlığım — Cepten ödeme geri ödeme fırsatı · 1 fırsat
        WorldCard("saglik", Icons.Rounded.MonitorHeart, "Sa\u011fl\u0131\u011f\u0131m",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Honey, color = Rose,
            illustrationRes = R.drawable.ic_stitch_saglik),
        // Seyahat — Sigorta eksik · 1 acil
        WorldCard("seyahat", Icons.Rounded.Flight, "Seyahatim",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Lav, color = Lav,
            ctaRoute = "seyahat?intent=travel_insurance_quote",
            illustrationRes = R.drawable.ic_stitch_seyahat),
        // Ailem — Okul harcı yaklaşıyor · 1 acil
        WorldCard("ailem", Icons.Rounded.Groups, "Ailem",
            timeLabel = "", moneyLabel = "",
            alertCount = 1, alertColor = Teal, color = Teal,
            illustrationRes = R.drawable.ic_stitch_ailem),
        // Param — Cash drag + vade fırsatı · 2 fırsat
        WorldCard("param", Icons.Rounded.AccountBalanceWallet, "Param",
            timeLabel = "", moneyLabel = "",
            alertCount = 2, alertColor = Honey, color = Honey,
            illustrationRes = R.drawable.ic_stitch_param),
    )

    // Sana özel kredi limitleri — toplam + kırılım
    val creditLimits = listOf(
        CreditLimit(
            id = "ihtiyac",
            name = "İhtiyaç Kredisi",
            icon = Icons.Rounded.AccountBalanceWallet,
            // illustrationRes = R.drawable.ic_stitch_ihtiyac,  // TODO: wallet doodle eklenince aktif et
            totalLimit = 150000,
            used = 35000,
            accent = YkbPrimary,            // KMH'dan ayrışması için mavi
            route = "finans"
        ),
        CreditLimit(
            id = "tasit",
            name = "Taşıt Kredisi ön onay",
            icon = Icons.Rounded.DirectionsCar,
            illustrationRes = R.drawable.ic_stitch_aracim,
            totalLimit = 120000,
            used = 0,
            accent = YkbDomainAracim,
            route = "aracim"
        ),
        CreditLimit(
            id = "konut",
            name = "Konut Kredisi ön onay",
            icon = Icons.Rounded.Home,
            illustrationRes = R.drawable.ic_stitch_evim,
            totalLimit = 500000,
            used = 0,
            accent = YkbDomainEvim,
            route = "evim"
        ),
        CreditLimit(
            id = "kmh",
            name = "KMH",
            icon = Icons.Rounded.AccountBalance,
            illustrationRes = R.drawable.ic_stitch_param,
            totalLimit = 25000,
            used = 0,
            accent = YkbDomainParam,
            route = "finans"
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
                        text = "Tüm Kartlarım",
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
                    AccountGlassCard(
                        cardName = "Kullanılabilir Limit",
                        topLabel = "Platinum Plus",
                        topEmoji = null,
                        amount = formatTRY(62500),
                        last4 = "9981",
                        statementDate = "15 May",
                        cardGradient = listOf(YkbCardGold1, YkbCardGold2, YkbCardGold3),
                        logoRes = R.drawable.ic_vb_logo,
                        onClick = { onNavigate("finans") }
                    )
                    AddCardGhostCard(onClick = { onNavigate("finans") })
                }
            }
        }

        // ═══ BENİM DÜNYAM — magazine stack (editorial pager kapakları) ═══
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(YkbCanvas)
                .padding(vertical = Spacing.lg)
        ) {
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
                onLimitClick = { limit -> onNavigate(limit.route) },
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
            .background(YkbSurfaceCard)
            .border(1.dp, YkbBorderHairline, shape)
            .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Hero: kullanılabilir limit
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = "Kullan\u0131labilir limitin",
                style = YkbType.BodySm.copy(color = Stone)
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Text(
                    text = formatTRY(overview.available),
                    style = YkbType.NumericXl.copy(color = Bark),
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
                    .background(
                        if (overview.usageRatio >= 0.8f) Terra else YkbPrimary
                    )
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
                .heightIn(min = 44.dp)
                .clip(RoundedCornerShape(Radius.pill))
                .clickable(role = Role.Button, onClick = onSeeAll)
                .padding(vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tümünü İncele",
                style = YkbType.BodyMd.copy(color = Sky, fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.width(Spacing.xs))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = Sky,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CreditLimitRow(limit: CreditLimit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape(Radius.pill))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        if (limit.illustrationRes != null) {
            Image(
                painter = painterResource(limit.illustrationRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(Radius.iconBg))
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(Radius.iconBg))
                    .background(limit.accent.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = limit.icon,
                    contentDescription = null,
                    tint = limit.accent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = limit.name,
                style = YkbType.BodyMd.copy(color = Bark, fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
            Text(
                text = if (limit.used > 0) "${formatTRY(limit.used)} kullanımda"
                       else "Kullanılabilir",
                style = YkbType.BodySm.copy(color = Stone)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatTRY(limit.available),
                style = YkbType.BodyMd.copy(color = Bark, fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.badge))
                    .background(YkbSuccess.copy(alpha = 0.15f))
                    .padding(horizontal = Spacing.sm, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "hazır",
                    style = YkbType.BodySm.copy(
                        color = YkbSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Stone,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Smart feed — 3-tier components (legacy, feed kaldırıldı)
// ═══════════════════════════════════════════════════════════════

// ═══════════════════════════════════════════════════════════════
// BENIM DUNYAM — magazine stack
// HorizontalPager + scale/alpha transforms for peek depth + big
// editorial cover typography per domain.
// ═══════════════════════════════════════════════════════════════

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
            modifier = Modifier.height(280.dp)
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
                onClick = { onNavigate(world.ctaRoute ?: world.id) },
                onOrbClick = { onNavigate("ai_assistant") }
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


private data class CardContent(
    val eventLine: String,    // tek cümle — en yüksek öncelikli olay
    val metric: String,       // ilgili sayısal değer (accent renkte büyük)
    val metricLabel: String,  // metriğin bağlamı (stone, küçük)
    val cta: String           // tek net aksiyon
)

private fun cardContent(id: String): CardContent = when (id) {
    "evim" -> CardContent(
        eventLine = "Doğalgaz son gün,\nödenmedi",
        metric = "₺847",
        metricLabel = "aylık fatura tutarın",
        cta = "Evim'e Git"
    )
    "aracim" -> CardContent(
        eventLine = "Kaskon 15 gün\nsonra bitiyor",
        metric = "₺8.500",
        metricLabel = "tahmini yenileme primin",
        cta = "Aracım'a Git"
    )
    "seyahat" -> CardContent(
        eventLine = "Antalya tatiline\n18 gün kaldı",
        metric = "₺28.500",
        metricLabel = "tahmini tatil bütçen",
        cta = "Seyahatim'e Git"
    )
    "saglik" -> CardContent(
        eventLine = "Check-up randevun\n34 gün sonra",
        metric = "%82",
        metricLabel = "yıllık sigortandan kalan",
        cta = "Sağlığım'a Git"
    )
    "ailem" -> CardContent(
        eventLine = "Üniversite harcına\n18 gün kaldı",
        metric = "₺8.500",
        metricLabel = "harç için eksik tutarın",
        cta = "Ailem'e Git"
    )
    "param" -> CardContent(
        eventLine = "Sınırsız hesap ile\nher gün kazan",
        metric = "%52",
        metricLabel = "e-Mevduat güncel faiz",
        cta = "Param'a Git"
    )
    else -> CardContent("", "", "", "Keşfet")
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun MagazineCoverCard(
    world: WorldCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onOrbClick: () -> Unit
) {
    val content = cardContent(world.id)
    val accent = world.color
    val shape = RoundedCornerShape(28.dp)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.18f)
            )
            .clip(shape)
            .background(YkbSurfaceCard)
            .drawBehind {
                drawRect(color = accent, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height))
            }
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
                if (world.illustrationRes != null) {
                    Image(
                        painter = painterResource(world.illustrationRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(Radius.iconBg))
                    )
                } else {
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
                }
                Text(
                    text = world.label,
                    style = YkbType.Heading2.copy(color = YkbNeutral900, fontWeight = FontWeight.SemiBold)
                )
            }
            AiOrbBadge(onClick = onOrbClick)
        }

        // Tek olay cümlesi — primary scan
        Text(
            text = content.eventLine,
            style = YkbType.Display.copy(
                color = YkbNeutral900,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.weight(1f))

        // Metrik + label
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = content.metric,
                style = YkbType.NumericXl.copy(color = accent, fontSize = 30.sp, lineHeight = 34.sp),
                maxLines = 1,
                softWrap = false
            )
            Text(
                text = content.metricLabel,
                style = YkbType.BodySm.copy(color = YkbNeutral500)
            )
        }

        // Sağa yaslı navigasyon CTA — pale tint fill, tap target 44dp
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(accent.copy(alpha = 0.12f))
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm)
            ) {
                Text(
                    text = content.cta,
                    style = YkbType.BodySm.copy(color = accent, fontWeight = FontWeight.SemiBold),
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
private fun AddCardGhostCard(onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(190.dp)
            .clip(shape)
            .background(YkbSurfaceCard)
            .border(1.dp, YkbBorderHairline, shape)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Yeni kart ekle",
                tint = YkbNeutral500,
                modifier = Modifier.size(24.dp)
            )
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
    @DrawableRes logoRes: Int? = null,
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
            // Top: label + optional emoji/logo, with last4 directly beneath
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
                    if (topEmoji != null) Text(topEmoji, fontSize = 13.sp)
                    if (brandBadge != null) Text(brandBadge, fontSize = 18.sp)
                    if (logoRes != null) {
                        Image(
                            painter = painterResource(logoRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
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
            animation = tween(2400, easing = LinearEasing),
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

