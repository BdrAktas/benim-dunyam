package com.simay.lifebank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.AnimatedProgress
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassPill
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.ProactiveOffer
import com.simay.lifebank.ui.components.ProductCard
import com.simay.lifebank.ui.components.TabItem
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.BarkMid
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.util.formatTRY

private data class AccountTx(
    val t: String,
    val a: Int,
    val d: String,
    val tp: String
)

private data class Account(
    val name: String,
    val balance: Int,
    val currency: String,
    val iban: String,
    val trend: String,
    val txs: List<AccountTx>,
    val maturity: String? = null,
    val rate: String? = null
)

private data class CardTx(
    val t: String,
    val a: Int,
    val d: String
)

private data class Installment(
    val title: String,
    val total: Int,
    val remaining: Int,
    val monthly: Int,
    val months: String
)

private data class CreditCard(
    val name: String,
    val last4: String,
    val limit: Int,
    val used: Int,
    val brand: String,
    val installments: List<Installment>,
    val txs: List<CardTx>
)

private data class CashFlowEvent(
    val day: Int,
    val title: String,
    val amount: Int,
    val tp: String
)

@Composable
fun FinansScreen(onBack: () -> Unit) {
    var tab by remember { mutableStateOf("genel") }
    var expandedAcc by remember { mutableStateOf<Int?>(null) }
    var expandedCard by remember { mutableStateOf<Int?>(null) }

    val totalAssets = animateCountUp(172832)

    val accounts = remember {
        listOf(
            Account(
                name = "Vadesiz TL", balance = 47832, currency = "₺",
                iban = "TR42 \u00B7\u00B7\u00B7\u00B7 3456 78", trend = "+2.1%",
                txs = listOf(
                    AccountTx("Maaş", 42500, "10 Nis", "in"),
                    AccountTx("Migros", -847, "11 Nis", "out"),
                    AccountTx("Starbucks", -185, "10 Nis", "out")
                )
            ),
            Account(
                name = "Vadesiz USD", balance = 2150, currency = "$",
                iban = "TR42 \u00B7\u00B7\u00B7\u00B7 3456 79", trend = "+0.8%",
                txs = listOf(
                    AccountTx("Döviz Alım", 500, "5 Nis", "in")
                )
            ),
            Account(
                name = "Vadeli Mevduat", balance = 125000, currency = "₺",
                iban = "\u2014", trend = "+4.2%",
                maturity = "15 Haz", rate = "%38 net",
                txs = emptyList()
            )
        )
    }

    val cards = remember {
        listOf(
            CreditCard(
                name = "World Elite", last4 = "8742", limit = 75000, used = 23456,
                brand = "Mastercard",
                installments = listOf(
                    Installment("Teknosa Laptop", 24000, 18000, 2000, "9/12")
                ),
                txs = listOf(
                    CardTx("Migros", -847, "11 Nis"),
                    CardTx("Shell", -1250, "9 Nis"),
                    CardTx("Netflix", -99, "8 Nis")
                )
            ),
            CreditCard(
                name = "Play Digital", last4 = "3219", limit = 25000, used = 8120,
                brand = "Visa",
                installments = emptyList(),
                txs = listOf(
                    CardTx("Spotify", -59, "10 Nis")
                )
            )
        )
    }

    val cashflow = remember {
        listOf(
            CashFlowEvent(10, "Maaş", 42500, "in"),
            CashFlowEvent(13, "Doğalgaz", -847, "out"),
            CashFlowEvent(15, "Kira Geliri", 8500, "in"),
            CashFlowEvent(20, "Kart Borcu", -23456, "out"),
            CashFlowEvent(22, "Elektrik", -523, "out"),
            CashFlowEvent(28, "İnternet", -349, "out")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        DomainHeader(label = "Finansım", subtitle = "Genel Bakış", onBack = onBack)

        // Tabs
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            GlassTabs(
                tabs = listOf(
                    TabItem("genel", "Özet"),
                    TabItem("hesap", "Hesaplar"),
                    TabItem("kart", "Kartlar"),
                    TabItem("akis", "Nakit Akışı")
                ),
                activeId = tab,
                onTabChange = { tab = it }
            )
        }

        // Content
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {

            // ── OZET TAB ──
            if (tab == "genel") {
                // Total assets card
                GlassSurface(
                    animate = true,
                    intensity = GlassIntensity.Strong,
                    contentPadding = PaddingValues(18.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Text(
                        text = "TOPLAM VARLIK",
                        fontSize = 10.sp,
                        color = Pebble,
                        fontFamily = SansFont,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = formatTRY(totalAssets),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        color = Bark,
                        fontFamily = SerifFont,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Income / Expense / Savings row
                    Row(
                        modifier = Modifier.padding(top = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data class SummaryItem(val label: String, val value: String, val color: Color)
                        val summaryItems = listOf(
                            SummaryItem("Gelir", "+₺42.500", Moss),
                            SummaryItem("Gider", "-₺31.200", Terra),
                            SummaryItem("Tasarruf", "%27", Sky)
                        )
                        summaryItems.forEach { s ->
                            GlassSurface(
                                intensity = GlassIntensity.Subtle,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = s.label.uppercase(),
                                        fontSize = 9.sp,
                                        color = s.color,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = s.value,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = s.color,
                                        fontFamily = SansFont,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Savings goal progress
                GlassSurface(
                    animate = true,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "₺200.000 Tasarruf Hedefi",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Bark,
                            fontFamily = SansFont
                        )
                        Text(
                            text = "%${((172832f / 200000f) * 100).toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Honey,
                            fontFamily = SansFont
                        )
                    }
                    AnimatedProgress(
                        value = 172832f,
                        max = 200000f,
                        color = Honey,
                        height = 8.dp,
                        delayMs = 400
                    )
                }

                // e-Mevduat offer
                ProactiveOffer(
                    type = "savings",
                    emoji = "\uD83D\uDCB0",
                    title = "e-Mevduat Yenileme",
                    rate = "%38 net",
                    term = "32 gün",
                    earnings = 3850,
                    desc = "Vadeli mevduatınız 15 Haziran'da bitiyor. Aynı koşullarla yenileyin.",
                    cta = "Yenile",
                    color = Honey,
                    highlight = true,
                    socialProof = "Yapı Kredi müşterilerinin %68'i vadelerini yeniliyor",
                    savingsVs = "Vadesiz hesapta bıraksanız kazancınız ₺0 olurdu"
                )
                ProductCard(
                    emoji = "\uD83D\uDCC8",
                    title = "Yatırım Fonları",
                    desc = "Uzman portföy yönetimi ile riskinize uygun fonlar.",
                    cta = "İncele",
                    color = Honey
                )
                ProductCard(
                    emoji = "\uD83E\uDD47",
                    title = "Altın Birikim",
                    desc = "Gram altın fiyatından düzenli otomatik alım.",
                    cta = "Başla",
                    color = Honey
                )
            }

            // ── HESAPLAR TAB ──
            if (tab == "hesap") {
                accounts.forEachIndexed { ai, acc ->
                    GlassSurface(
                        animate = true,
                        onClick = {
                            expandedAcc = if (expandedAcc == ai) null else ai
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = acc.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Bark,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = acc.iban,
                                    fontSize = 11.sp,
                                    color = Pebble,
                                    fontFamily = SansFont
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${acc.currency}${formatNumber(acc.balance)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Bark,
                                    fontFamily = SerifFont
                                )
                                GlassPill(text = acc.trend, color = Moss)
                            }
                        }

                        // Maturity badge
                        if (acc.maturity != null) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Vade: ${acc.maturity} \u00B7 ${acc.rate}",
                                fontSize = 11.sp,
                                color = Honey,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = SansFont,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Honey.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        // Expanded transactions
                        AnimatedVisibility(
                            visible = expandedAcc == ai && acc.txs.isNotEmpty(),
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                HorizontalDivider(
                                    color = Color.White.copy(alpha = 0.3f),
                                    thickness = 1.dp
                                )
                                Spacer(Modifier.height(10.dp))
                                acc.txs.forEach { tx ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = tx.t,
                                                fontSize = 12.sp,
                                                color = Bark,
                                                fontFamily = SansFont
                                            )
                                            Text(
                                                text = tx.d,
                                                fontSize = 10.sp,
                                                color = Pebble,
                                                fontFamily = SansFont
                                            )
                                        }
                                        Text(
                                            text = "${if (tx.a > 0) "+" else ""}${formatTRY(tx.a)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tx.tp == "in") Moss else Bark,
                                            fontFamily = SansFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // YUVAM offer
                ProactiveOffer(
                    type = "savings",
                    emoji = "\uD83D\uDCB0",
                    title = "YUVAM TL Mevduat",
                    rate = "%42 net",
                    term = "90 gün",
                    earnings = 4100,
                    goal = 50000,
                    desc = "Vadesiz hesabınızdaki ₺47.832'yi değerlendirin. Yapı Kredi'ye özel yüksek faiz.",
                    cta = "Hedef Belirle",
                    color = Sky,
                    highlight = true,
                    savingsVs = "Vadesiz hesapta bu para 90 günde ₺0 kazanır, YUVAM'da ₺4.100",
                    authority = "Dijitale Özel"
                )
                ProductCard(
                    emoji = "\uD83D\uDD13",
                    title = "Esnek Vadeli Mevduat",
                    desc = "İstediğiniz zaman çekin, faiziniz kalsın.",
                    cta = "Aç",
                    color = Honey
                )
            }

            // ── KARTLAR TAB ──
            if (tab == "kart") {
                cards.forEachIndexed { ci, card ->
                    val pct = ((card.used.toFloat() / card.limit) * 100).toInt()

                    GlassSurface(
                        animate = true,
                        onClick = {
                            expandedCard = if (expandedCard == ci) null else ci
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        // Dark gradient header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Bark, BarkMid)
                                    )
                                )
                                .padding(horizontal = 18.dp, vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Yapı Kredi ${card.name}",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = card.brand,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = SansFont
                                )
                            }

                            // Card number
                            Text(
                                text = "\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 ${card.last4}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                letterSpacing = 3.sp,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            // Used / Limit row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "KULLANILAN",
                                        fontSize = 9.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = formatTRY(card.used),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "LİMİT",
                                        fontSize = 9.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = formatTRY(card.limit),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Usage bar
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = (pct / 100f).coerceIn(0f, 1f))
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(
                                            if (pct > 80) Color(0xFFFF9B8A)
                                            else Color.White.copy(alpha = 0.8f)
                                        )
                                )
                            }
                        }

                        // Expanded details
                        AnimatedVisibility(
                            visible = expandedCard == ci,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                // Installments
                                if (card.installments.isNotEmpty()) {
                                    Text(
                                        text = "Taksitler",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Bark,
                                        fontFamily = SansFont,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    card.installments.forEach { inst ->
                                        Column(modifier = Modifier.padding(bottom = 10.dp)) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = inst.title,
                                                    fontSize = 12.sp,
                                                    color = Bark,
                                                    fontFamily = SansFont
                                                )
                                                Text(
                                                    text = "${formatTRY(inst.monthly)}/ay",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Bark,
                                                    fontFamily = SansFont
                                                )
                                            }
                                            AnimatedProgress(
                                                value = (inst.total - inst.remaining).toFloat(),
                                                max = inst.total.toFloat(),
                                                color = Honey,
                                                height = 4.dp,
                                                delayMs = 200
                                            )
                                            Text(
                                                text = "${inst.months} taksit \u00B7 Kalan: ${formatTRY(inst.remaining)}",
                                                fontSize = 10.sp,
                                                color = Pebble,
                                                fontFamily = SansFont,
                                                modifier = Modifier.padding(top = 3.dp)
                                            )
                                        }
                                    }
                                }

                                // Recent transactions
                                Text(
                                    text = "Son Harcamalar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Bark,
                                    fontFamily = SansFont,
                                    modifier = Modifier.padding(
                                        top = if (card.installments.isNotEmpty()) 8.dp else 0.dp,
                                        bottom = 6.dp
                                    )
                                )
                                card.txs.forEach { tx ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = tx.t,
                                                fontSize = 12.sp,
                                                color = Bark,
                                                fontFamily = SansFont
                                            )
                                            Text(
                                                text = tx.d,
                                                fontSize = 10.sp,
                                                color = Pebble,
                                                fontFamily = SansFont
                                            )
                                        }
                                        Text(
                                            text = formatTRY(tx.a),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Bark,
                                            fontFamily = SansFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ProductCard(
                    emoji = "\uD83D\uDCCB",
                    title = "Findex Kredi Notu",
                    desc = "Kredi notunuzu öğrenin.",
                    cta = "Sorgula",
                    color = Honey
                )
                ProductCard(
                    emoji = "\u26A1",
                    title = "FAST Transfer",
                    desc = "7/24 anlık para transferi.",
                    cta = "Gönder",
                    color = Moss
                )
            }

            // ── NAKİT AKIŞI TAB ──
            if (tab == "akis") {
                Text(
                    text = "Nisan Nakit Akışı",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Bark,
                    fontFamily = SerifFont,
                    modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
                )

                var runningBalance = 47832
                cashflow.forEachIndexed { i, cf ->
                    runningBalance += cf.amount
                    val isPast = cf.day <= 11
                    val currentBalance = runningBalance

                    Row(
                        modifier = Modifier.padding(bottom = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Timeline icon column
                        Column(
                            modifier = Modifier.width(44.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GlassSurface(
                                intensity = GlassIntensity.Subtle,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .graphicsLayerAlpha(if (isPast) 0.5f else 1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (cf.tp == "in") "\uD83D\uDCE5" else "\uD83D\uDCE4",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            if (i < cashflow.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(1.5.dp)
                                        .height(40.dp)
                                        .padding(vertical = 3.dp)
                                        .background(Color.Black.copy(alpha = 0.06f))
                                )
                            }
                        }

                        // Event card
                        GlassSurface(
                            animate = true,
                            intensity = if (isPast) GlassIntensity.Subtle else GlassIntensity.Normal,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 6.dp)
                                .graphicsLayerAlpha(if (isPast) 0.55f else 1f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = cf.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = "${cf.day} Nis",
                                        fontSize = 11.sp,
                                        color = Pebble,
                                        fontFamily = SansFont
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${if (cf.amount > 0) "+" else ""}${formatTRY(cf.amount)}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (cf.tp == "in") Moss else Terra,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = "Bakiye: ${formatTRY(currentBalance)}",
                                        fontSize = 10.sp,
                                        color = Pebble,
                                        fontFamily = SansFont
                                    )
                                }
                            }
                        }
                    }
                }

                // Month-end estimate
                GlassSurface(
                    animate = true,
                    intensity = GlassIntensity.Subtle,
                    contentPadding = PaddingValues(14.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row {
                        Text(
                            text = "Ay Sonu Tahmini: ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont
                        )
                        Text(
                            text = formatTRY(64468),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Honey,
                            fontFamily = SansFont
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                ProactiveOffer(
                    emoji = "\uD83D\uDCB3",
                    title = "Bireysel İhtiyaç Kredisi",
                    limit = 75000,
                    rate = "%2.89",
                    term = "36 aya kadar",
                    desc = "Kredi notunuza göre ön onaylı. Anında hesabınıza.",
                    cta = "Hemen Kullan",
                    color = Moss,
                    highlight = true,
                    socialProof = "Bu ay 8.200 müşteri kullandı \u00B7 Ort. 4 dk onay süresi",
                    authority = "Ön Onaylı"
                )
                ProactiveOffer(
                    emoji = "\uD83D\uDCBC",
                    title = "Avans Hesap (KMH)",
                    limit = 50000,
                    rate = "%3.19",
                    term = "Süresiz",
                    desc = "İhtiyaç anında kullanın, kullandığınız kadar faiz ödeyin.",
                    cta = "Aktifleştir",
                    color = Lav
                )
            }
        }
    }
}

/**
 * Format a number using Turkish locale grouping (e.g. 47.832).
 */
private fun formatNumber(value: Int): String {
    return java.text.NumberFormat.getNumberInstance(java.util.Locale("tr", "TR")).format(value)
}

/**
 * Helper extension to apply graphicsLayer alpha in a modifier chain.
 */
private fun Modifier.graphicsLayerAlpha(alpha: Float): Modifier =
    this.graphicsLayer { this.alpha = alpha }
