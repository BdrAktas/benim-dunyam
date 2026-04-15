package com.simay.lifebank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.AnimatedProgress
import com.simay.lifebank.ui.components.ConfettiEffect
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassPill
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.InfoBadge
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class Bill(
    val id: Int,
    val title: String,
    val emoji: String,
    val amount: Int,
    val due: String,
    val provider: String,
    val autopay: Boolean,
    val color: Color
)

private data class NeighborhoodComparison(
    val loc: String,
    val avg: String,
    val trend: String,
    val mine: Boolean = false
)

private data class EnergyCategory(
    val label: String,
    val emoji: String,
    val cons: Int,
    val unit: String,
    val cost: Int,
    val change: String,
    val avg: Int,
    val color: Color,
    val tip: String
)

private data class InsurancePolicy(
    val title: String,
    val provider: String,
    val expiry: String,
    val coverage: String,
    val premium: String
)

@Composable
fun EvimScreen(onBack: () -> Unit, intent: String? = null) {
    var tab by remember { mutableStateOf("fatura") }
    LaunchedEffect(intent) {
        when (intent) {
            "pay_bill" -> tab = "fatura"
            "renew_dask" -> tab = "sigorta"
        }
    }
    var paidBills by remember { mutableStateOf(mapOf<Int, Boolean>()) }
    var showConfetti by remember { mutableStateOf(false) }
    var dismissing by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

    val bills = remember {
        listOf(
            Bill(1, "Doğalgaz", "\uD83D\uDD25", 847, "13 Nis", "İGDAŞ", false, Terra),
            Bill(2, "Elektrik", "\u26A1", 523, "22 Nis", "AYEDAŞ", true, Honey),
            Bill(3, "Su", "\uD83D\uDCA7", 189, "25 Nis", "İSKİ", true, Sky),
            Bill(4, "İnternet", "\uD83D\uDCE1", 349, "28 Nis", "Türk Telekom", true, Lav),
            Bill(5, "Site Aidatı", "\uD83C\uDFE2", 2100, "1 May", "Yönetim", false, Stone)
        )
    }

    val payBill: (Int) -> Unit = { id ->
        dismissing = id
        showConfetti = true
        scope.launch {
            delay(500)
            paidBills = paidBills + (id to true)
            dismissing = null
        }
        scope.launch {
            delay(2500)
            showConfetti = false
        }
    }

    val totalBillAmount = bills.sumOf { it.amount }
    val totalAmt = animateCountUp(totalBillAmount, 800)
    val currentValue = animateCountUp(3240000, 1200)
    val equity = animateCountUp(2560000, 1000)

    val neighborhoods = remember {
        listOf(
            NeighborhoodComparison("İdealtepe", "₺29.455/m²", "+%18", mine = true),
            NeighborhoodComparison("Maltepe merkez", "₺32.100/m²", "+%22"),
            NeighborhoodComparison("Kartal", "₺27.800/m²", "+%15")
        )
    }

    val energyCategories = remember {
        listOf(
            EnergyCategory("Elektrik", "\u26A1", 280, "kWh", 523, "-6%", 320, Honey, "LED aydınlatma ile yıllık ~₺800 tasarruf"),
            EnergyCategory("Doğalgaz", "\uD83D\uDD25", 380, "m³", 847, "-19%", 400, Terra, "Kombi bakımı ile %15 verim artışı"),
            EnergyCategory("Su", "\uD83D\uDCA7", 14, "m³", 189, "-3%", 15, Sky, "Tasarruflu duş başlığı ile %40 tasarruf")
        )
    }

    val insurancePolicies = remember {
        listOf(
            InsurancePolicy("DASK", "Yapı Kredi Sigorta", "15 Nis 2027", "₺640K", "₺820/yıl"),
            InsurancePolicy("Konut Sigortası", "Allianz", "3 Ağu 2026", "₺2.4M", "₺4.200/yıl")
        )
    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            DomainHeader(label = "Evim", subtitle = "İdealtepe, Maltepe", accent = Sky, onBack = onBack)

            // ── Güvence Durumun ──
            GuvenceDurumu(
                items = listOf(
                    GuvenceItem("DASK'ın Var", "Güvendesin"),
                    GuvenceItem("Konut Sigortan Var", "Güvendesin")
                )
            )

            // Tabs
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                GlassTabs(
                    tabs = listOf(
                        TabItem("fatura", "Faturalar"),
                        TabItem("deger", "Ev Değeri"),
                        TabItem("enerji", "Enerji"),
                        TabItem("sigorta", "Sigortalar")
                    ),
                    activeId = tab,
                    onTabChange = { tab = it }
                )
            }

            // Content
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {

                // ── FATURA TAB ──
                if (tab == "fatura") {
                    // Total amount card
                    GlassSurface(
                        animate = true,
                        intensity = GlassIntensity.Strong,
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                        modifier = Modifier.padding(bottom = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "NİSAN TOPLAMI",
                                    fontSize = 10.sp,
                                    color = Pebble,
                                    fontFamily = SansFont,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = formatTRY(totalAmt),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Bark,
                                    fontFamily = SerifFont,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            GlassPill(text = "\u2193 %6", color = Moss)
                        }
                    }

                    // Bills list
                    bills.forEach { bill ->
                        val paid = paidBills[bill.id] == true
                        val isDismissing = dismissing == bill.id

                        AnimatedVisibility(
                            visible = !isDismissing,
                            exit = slideOutHorizontally(
                                targetOffsetX = { it * 2 },
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500)) +
                                    shrinkVertically(animationSpec = tween(500))
                        ) {
                            GlassSurface(
                                animate = true,
                                intensity = if (paid) GlassIntensity.Subtle else GlassIntensity.Normal,
                                borderLeftColor = if (paid) Pebble else bill.color,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .then(
                                        if (paid) Modifier.graphicsLayerAlpha(0.4f)
                                        else Modifier
                                    )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(bill.emoji, fontSize = 20.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = bill.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Bark,
                                                fontFamily = SansFont,
                                                textDecoration = if (paid) TextDecoration.LineThrough else TextDecoration.None
                                            )
                                            Text(
                                                text = formatTRY(bill.amount),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (paid) Pebble else Bark,
                                                fontFamily = SansFont
                                            )
                                        }
                                        Spacer(Modifier.height(3.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${bill.provider} \u00B7 ${bill.due}",
                                                fontSize = 11.sp,
                                                color = Stone,
                                                fontFamily = SansFont
                                            )
                                            if (bill.autopay && !paid) {
                                                GlassPill(text = "Otomatik", color = Moss)
                                            }
                                            if (paid) {
                                                GlassPill(text = "Ödendi \u2713", color = Moss)
                                            }
                                        }
                                    }
                                }
                                if (!paid && !bill.autopay) {
                                    Spacer(Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.padding(start = 32.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        GlassButton(
                                            text = "Öde",
                                            color = Moss,
                                            isSmall = true,
                                            onClick = { payBill(bill.id) }
                                        )
                                        GlassButton(
                                            text = "Otomatik Kur",
                                            color = Sky,
                                            isSmall = true,
                                            isOutline = true
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // "Eviniz İçin" section
                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Sky)
                        )
                        Text(
                            text = "Eviniz İçin",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SerifFont
                        )
                    }

                    ProactiveOffer(
                        emoji = "\uD83D\uDCB3",
                        title = "Avans Hesap",
                        limit = 50000,
                        rate = "%3.19",
                        term = "Süresiz",
                        desc = "Fatura ödeme sıkışıklığı yaşamayın. Hazır limitinizi kullanın.",
                        cta = "Başvur",
                        color = Moss,
                        highlight = true,
                        socialProof = "Bu ay 12.400 müşteri fatura ödemesinde kullandı",
                        authority = "Ön Onaylı"
                    )
                    ProductCard(
                        emoji = "\uD83D\uDEE1\uFE0F",
                        title = "DASK Deprem Sigortası",
                        desc = "Online başvuru ile anında poliçe.",
                        cta = "Başvur",
                        color = Sky
                    )
                    ProductCard(
                        emoji = "\u26A1",
                        title = "Garantili Otomatik Fatura",
                        desc = "Tüm faturalarınızı otomatikleştirin.",
                        cta = "Kur",
                        color = Honey
                    )
                }

                // ── EV DEĞERİ TAB ──
                if (tab == "deger") {
                    // House value card
                    GlassSurface(
                        animate = true,
                        intensity = GlassIntensity.Strong,
                        accent = Sky,
                        glow = true,
                        contentPadding = PaddingValues(20.dp),
                        modifier = Modifier.padding(bottom = 14.dp)
                    ) {
                        Text(
                            text = "TAHMİNİ GÜNCEL DEĞER",
                            fontSize = 10.sp,
                            color = Stone,
                            fontFamily = SansFont,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = formatTRY(currentValue),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium,
                            color = Bark,
                            fontFamily = SerifFont,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            GlassPill(text = "+%75", color = Moss)
                            Text(
                                text = "Mart 2022'den beri",
                                fontSize = 11.sp,
                                color = Stone,
                                fontFamily = SansFont
                            )
                        }
                    }

                    // Equity / Remaining Loan row
                    Row(
                        modifier = Modifier.padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GlassSurface(
                            animate = true,
                            contentPadding = PaddingValues(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "ÖZ VARLIK",
                                fontSize = 10.sp,
                                color = Pebble,
                                fontFamily = SansFont,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = formatTRY(equity),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Moss,
                                fontFamily = SerifFont,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        GlassSurface(
                            animate = true,
                            contentPadding = PaddingValues(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "KALAN KREDİ",
                                fontSize = 10.sp,
                                color = Pebble,
                                fontFamily = SansFont,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = formatTRY(680000),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Terra,
                                fontFamily = SerifFont,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Loan-to-value
                    GlassSurface(
                        animate = true,
                        contentPadding = PaddingValues(14.dp),
                        modifier = Modifier.padding(bottom = 14.dp)
                    ) {
                        Text(
                            text = "Kredi / Değer Oranı",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        AnimatedProgress(
                            value = 79f,
                            max = 100f,
                            color = Moss,
                            height = 20.dp,
                            delayMs = 300
                        )
                        Text(
                            text = "%79 sizin \u00B7 %21 banka",
                            fontSize = 10.sp,
                            color = Stone,
                            fontFamily = SansFont,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // DASK Protection Gap card
                    GlassSurface(
                        animate = true,
                        accent = Terra,
                        glow = true,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(bottom = 14.dp)
                            .border(1.5.dp, Terra.copy(alpha = 0.19f), RoundedCornerShape(24.dp))
                    ) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Terra.copy(alpha = 0.06f))
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("\uD83D\uDEE1\uFE0F", fontSize = 14.sp)
                            Text(
                                text = "KORUMA AÇIĞI TESPİT EDİLDİ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Terra,
                                fontFamily = SansFont,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Content
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            Text(
                                text = "DASK Teminat Durumunuz",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Bark,
                                fontFamily = SansFont,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            // Visual gap bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Mevcut DASK teminatı",
                                    fontSize = 10.sp,
                                    color = Stone,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = "Evinizin değeri",
                                    fontSize = 10.sp,
                                    color = Stone,
                                    fontFamily = SansFont
                                )
                            }
                            Spacer(Modifier.height(4.dp))

                            // Protection gap visual bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black.copy(alpha = 0.04f))
                            ) {
                                // DASK coverage (20%)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.2f)
                                        .height(28.dp)
                                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                                        .background(Moss.copy(alpha = 0.19f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "₺640K",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Moss,
                                        fontFamily = SansFont
                                    )
                                }
                                // Gap (80%)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(28.dp)
                                        .align(Alignment.CenterEnd)
                                        .background(Terra.copy(alpha = 0.05f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "₺2.6M koruma açığı",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Terra,
                                        fontFamily = SansFont,
                                        modifier = Modifier
                                            .background(
                                                Color.White.copy(alpha = 0.7f),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "₺640.000",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Moss,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = "₺3.240.000",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Bark,
                                    fontFamily = SansFont
                                )
                            }

                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Evinizin güncel değeri ₺3.24M ama DASK teminatınız ₺640K. Olası bir depremde ₺2.6M'lik kısım güvence dışında kalır.",
                                fontSize = 11.sp,
                                color = BarkMid,
                                fontFamily = SansFont,
                                lineHeight = 16.5.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            InfoBadge(
                                emoji = "\uD83D\uDC65",
                                text = "İdealtepe'de konut sahiplerinin %73'ü DASK güncelledi"
                            )

                            Spacer(Modifier.height(10.dp))
                            GlassButton(
                                text = "Teminatı Güncelle \u2014 ₺45/ay",
                                color = Terra,
                                isFull = true
                            )
                            Text(
                                text = "Günlük maliyeti bir çay parasından az",
                                fontSize = 10.sp,
                                color = Pebble,
                                fontFamily = SansFont,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Neighborhood comparisons
                    neighborhoods.forEach { area ->
                        GlassSurface(
                            animate = true,
                            intensity = if (area.mine) GlassIntensity.Normal else GlassIntensity.Subtle,
                            borderLeftColor = if (area.mine) Sky else null,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = area.loc,
                                        fontSize = 13.sp,
                                        fontWeight = if (area.mine) FontWeight.Bold else FontWeight.Medium,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = area.avg,
                                        fontSize = 11.sp,
                                        color = Stone,
                                        fontFamily = SansFont
                                    )
                                }
                                GlassPill(text = area.trend, color = Moss)
                            }
                        }
                    }

                    // Konut Kredisi offer
                    Spacer(Modifier.height(12.dp))
                    ProactiveOffer(
                        emoji = "\uD83D\uDD11",
                        title = "Konut Kredisi",
                        limit = 2500000,
                        rate = "%2.59",
                        term = "120 aya kadar",
                        desc = "İdealtepe'deki evinizin değerine göre hazırlandı.",
                        cta = "Başvur",
                        color = Moss,
                        highlight = true,
                        socialProof = "Maltepe'de bu ay 340 konut kredisi kullanıldı",
                        savingsVs = "Kira öder gibi ev sahibi olun \u2014 aylık taksit ₺18.200",
                        authority = "Ön Onaylı"
                    )
                }

                // ── ENERJİ TAB ──
                if (tab == "enerji") {
                    energyCategories.forEachIndexed { ci, cat ->
                        val belowAvg = cat.cons <= cat.avg
                        val progressMax = (maxOf(cat.cons, cat.avg) * 1.2f)

                        GlassSurface(
                            animate = true,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            // Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(cat.emoji, fontSize = 18.sp)
                                    Text(
                                        text = cat.label,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${cat.cons} ${cat.unit}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                    GlassPill(text = cat.change, color = Moss)
                                }
                            }

                            // Bottom section
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.02f))
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Mahalle ort: ${cat.avg} ${cat.unit}",
                                        fontSize = 11.sp,
                                        color = Stone,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = if (belowAvg) "\u2713 Altında" else "\u26A0 Üstünde",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (belowAvg) Moss else Terra,
                                        fontFamily = SansFont
                                    )
                                }
                                AnimatedProgress(
                                    value = cat.cons.toFloat(),
                                    max = progressMax,
                                    color = if (belowAvg) Moss else Terra,
                                    height = 5.dp,
                                    delayMs = ci * 200
                                )
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("\uD83D\uDCA1", fontSize = 11.sp)
                                    Text(
                                        text = cat.tip,
                                        fontSize = 11.sp,
                                        color = BarkMid,
                                        fontFamily = SansFont
                                    )
                                }
                            }
                        }
                    }
                }

                // ── SİGORTA TAB ──
                if (tab == "sigorta") {
                    insurancePolicies.forEach { ins ->
                        GlassSurface(
                            animate = true,
                            borderLeftColor = Moss,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = ins.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = ins.provider,
                                        fontSize = 11.sp,
                                        color = Stone,
                                        fontFamily = SansFont
                                    )
                                }
                                GlassPill(text = "Aktif", color = Moss)
                            }

                            // Detail rows
                            listOf(
                                "Teminat" to ins.coverage,
                                "Prim" to ins.premium,
                                "Vade" to ins.expiry
                            ).forEach { (key, value) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 12.sp,
                                        color = Stone,
                                        fontFamily = SansFont
                                    )
                                    Text(
                                        text = value,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Bark,
                                        fontFamily = SansFont
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlassButton(text = "Yenile", color = Moss, isSmall = true)
                                GlassButton(
                                    text = "PDF",
                                    color = Sky,
                                    isSmall = true,
                                    isOutline = true
                                )
                            }
                        }
                    }

                    ProductCard(
                        emoji = "\uD83C\uDFE0",
                        title = "Güvenli Evim Sigortası",
                        desc = "Kapsamlı konut güvencesi.",
                        cta = "Teklif Al",
                        color = Sky
                    )
                }
            }
        }

        // Confetti overlay
        ConfettiEffect(active = showConfetti)
    }
}

/**
 * Helper extension to apply graphicsLayer alpha in a modifier chain.
 */
private fun Modifier.graphicsLayerAlpha(alpha: Float): Modifier =
    this.graphicsLayer { this.alpha = alpha }

// ═══ Güvence Durumun ═══
private data class GuvenceItem(val title: String, val status: String)

@androidx.compose.runtime.Composable
private fun GuvenceDurumu(items: List<GuvenceItem>) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        androidx.compose.material3.Text(
            text = "Güvence Durumun",
            style = com.simay.lifebank.ui.theme.YkbType.Heading2.copy(
                color = com.simay.lifebank.ui.theme.Bark
            )
        )
        androidx.compose.foundation.layout.Spacer(
            Modifier.height(com.simay.lifebank.ui.theme.Spacing.md)
        )
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(
                    com.simay.lifebank.ui.theme.Radius.card
                ))
                .background(com.simay.lifebank.ui.theme.YkbSurfaceCard)
                .border(
                    com.simay.lifebank.ui.theme.Elevation.hairline,
                    com.simay.lifebank.ui.theme.YkbBorderHairline,
                    androidx.compose.foundation.shape.RoundedCornerShape(
                        com.simay.lifebank.ui.theme.Radius.card
                    )
                )
        ) {
            items.forEachIndexed { idx, item ->
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = com.simay.lifebank.ui.theme.Spacing.lg,
                            vertical = com.simay.lifebank.ui.theme.Spacing.md
                        ),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                            com.simay.lifebank.ui.theme.Spacing.md
                        )
                    ) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(
                                    com.simay.lifebank.ui.theme.YkbSuccess.copy(alpha = 0.12f)
                                ),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Rounded.Shield,
                                contentDescription = null,
                                tint = com.simay.lifebank.ui.theme.YkbSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        androidx.compose.material3.Text(
                            text = item.title,
                            style = com.simay.lifebank.ui.theme.YkbType.Heading3.copy(
                                color = com.simay.lifebank.ui.theme.Bark
                            )
                        )
                    }
                    // Güvendesin chip
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(
                                com.simay.lifebank.ui.theme.Radius.pill
                            ))
                            .background(
                                com.simay.lifebank.ui.theme.YkbSuccess.copy(alpha = 0.14f)
                            )
                            .padding(
                                horizontal = com.simay.lifebank.ui.theme.Spacing.sm,
                                vertical = 4.dp
                            )
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = com.simay.lifebank.ui.theme.YkbSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                        androidx.compose.material3.Text(
                            text = item.status,
                            style = com.simay.lifebank.ui.theme.YkbType.BodySm.copy(
                                color = com.simay.lifebank.ui.theme.YkbSuccess,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        )
                    }
                }
                if (idx < items.lastIndex) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(com.simay.lifebank.ui.theme.Elevation.hairline)
                            .background(com.simay.lifebank.ui.theme.YkbBorderHairline)
                    )
                }
            }
        }
    }
}
