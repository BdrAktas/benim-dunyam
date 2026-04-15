package com.simay.lifebank.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.AnimatedProgress
import com.simay.lifebank.ui.components.ChecklistRow
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassPill
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.InfoBadge
import com.simay.lifebank.ui.components.ProactiveOffer
import com.simay.lifebank.ui.components.TabItem
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

private data class DayItem(
    val time: String,
    val title: String,
    val note: String,
    val type: String,
    val est: Int? = null
)

private data class DayPlan(
    val day: Int,
    val date: String,
    val title: String,
    val items: List<DayItem>
)

private data class DocItem(
    val title: String,
    val status: String,
    val note: String
)

private data class PackCategory(
    val cat: String,
    val icon: String,
    val items: List<String>
)

@Composable
fun SeyahatScreen(onBack: () -> Unit, intent: String? = null) {
    var tab by remember { mutableStateOf("plan") }
    val seyahatSummary = remember {
        ExpenseSummary(
            totalAmount = 12450,
            changePercent = 34,
            topCategory = "Konaklama",
            topAmount = 6800,
            categories = listOf(
                ExpenseCategory("Konaklama",   6800, 2, 55, Lav),
                ExpenseCategory("Uçak/Ulaşım", 3200, 3, 26, Lav.copy(alpha = 0.7f)),
                ExpenseCategory("Yemek",       1650, 9, 13, Lav.copy(alpha = 0.5f)),
                ExpenseCategory("Aktivite",     800, 4,  6, Lav.copy(alpha = 0.35f))
            ),
            merchants = listOf(
                ExpenseMerchant("Hilton Antalya", "Konaklama",    6800, 1),
                ExpenseMerchant("Türk Hava Y.",   "Uçak",         2400, 2),
                ExpenseMerchant("Lokantalar",     "Yemek",        1650, 9),
                ExpenseMerchant("Pegasus",        "Uçak",          800, 1),
                ExpenseMerchant("Tekne Turu",     "Aktivite",      800, 1)
            )
        )
    }
    LaunchedEffect(intent) {
        when (intent) {
            "travel_insurance_quote" -> tab = "plan"
            "currency_buy" -> tab = "plan"
            "doc_checklist" -> tab = "docs"
        }
    }
    var activeDay by remember { mutableStateOf(0) }
    var checked by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var checkedPack by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    val days = remember {
        listOf(
            DayPlan(
                day = 1, date = "20 Haz", title = "Varış & Shinjuku",
                items = listOf(
                    DayItem("06:30", "Narita'ya iniş", "THY TK198", "transport"),
                    DayItem("10:00", "Otele check-in", "Granbell", "hotel"),
                    DayItem("12:00", "Ramen öğle", "Fuunji", "food", est = 1500),
                    DayItem("14:00", "Meiji Jingu", "Ücretsiz", "sight"),
                    DayItem("19:00", "Izakaya akşam", "Omoide Yokocho", "food", est = 3000)
                )
            ),
            DayPlan(
                day = 2, date = "21 Haz", title = "Asakusa & Akihabara",
                items = listOf(
                    DayItem("08:00", "Senso-ji", "Erken gidin", "sight"),
                    DayItem("10:00", "Nakamise", "Hediyelik", "shopping", est = 5000),
                    DayItem("14:00", "Akihabara", "Elektronik", "sight"),
                    DayItem("17:00", "Tokyo Skytree", "¥2100", "sight", est = 800)
                )
            ),
            DayPlan(
                day = 3, date = "22 Haz", title = "Tsukiji & Odaiba",
                items = listOf(
                    DayItem("07:00", "Tsukiji Market", "Sushi kahvaltı", "food", est = 2000),
                    DayItem("10:00", "teamLab", "Online bilet!", "sight", est = 1600),
                    DayItem("16:00", "Onsen", "Oedo Monogatari", "activity", est = 1400)
                )
            )
        )
    }

    val typeColors = mapOf(
        "transport" to Sky,
        "hotel" to Lav,
        "food" to Honey,
        "sight" to Moss,
        "shopping" to Terra,
        "activity" to Rose
    )
    val typeLabels = mapOf(
        "transport" to "Ulaşım",
        "hotel" to "Otel",
        "food" to "Yemek",
        "sight" to "Gezi",
        "shopping" to "Alışveriş",
        "activity" to "Aktivite"
    )

    val docs = remember {
        listOf(
            DocItem("Pasaport", "ready", "6+ ay \u2713"),
            DocItem("Vize", "ready", "Muafiyet"),
            DocItem("Uçak Bileti", "ready", "THY TK198"),
            DocItem("Otel", "ready", "Granbell 10 gece"),
            DocItem("JR Pass", "pending", "₺6.200"),
            DocItem("Seyahat Sigortası", "pending", "Yapı Kredi'den al"),
            DocItem("eSIM", "pending", "Airalo ₺380")
        )
    }

    val packing = remember {
        listOf(
            PackCategory("Giyim", "\uD83D\uDC55", listOf("Yağmurluk", "Yürüyüş ayakkabısı", "4x tişört", "2x pantolon")),
            PackCategory("Elektronik", "\uD83D\uDCF1", listOf("Şarj", "Adaptör", "Powerbank", "Kamera")),
            PackCategory("Belge", "\uD83D\uDCC4", listOf("Pasaport", "Kredi kartı", "Nakit JPY", "eSIM")),
            PackCategory("Diğer", "\uD83C\uDF92", listOf("İlaçlar", "Güneş kremi", "Kalem"))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        DomainHeader(label = "Seyahat", subtitle = "Tokyo \u00B7 70 gün", accent = Lav, onBack = onBack)

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
            // Header card: Tokyo trip info
            GlassSurface(
                animate = true,
                intensity = GlassIntensity.Subtle,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tokyo, Japonya",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Bark,
                            fontFamily = SerifFont
                        )
                        Text(
                            text = "20 \u2013 30 Haz 2026",
                            fontSize = 11.sp,
                            color = Stone,
                            fontFamily = SansFont
                        )
                    }
                    GlassSurface(
                        intensity = GlassIntensity.Strong,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "GÜN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Lav,
                                fontFamily = SansFont
                            )
                            Text(
                                text = "70",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Lav,
                                fontFamily = SansFont
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                AnimatedProgress(value = 32000f, max = 85000f, color = Lav, height = 5.dp, delayMs = 300)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "₺32.000 / ₺85.000 bütçe",
                    fontSize = 10.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
            }

            GlassTabs(
                tabs = listOf(
                    TabItem("plan", "Gün Planı"),
                    TabItem("docs", "Belgeler"),
                    TabItem("packing", "Çanta"),
                    TabItem("giderler", "Giderlerim")
                ),
                activeId = tab,
                onTabChange = { tab = it }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            // ---- TAB: Gün Planı ----
            if (tab == "plan") {
                // Day buttons
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    itemsIndexed(days) { i, d ->
                        val doneCount = d.items.indices.count { j -> checked["$i-$j"] == true }
                        GlassButton(
                            text = "G${d.day} \u00B7 $doneCount/${d.items.size}",
                            color = if (activeDay == i) Lav else Pebble,
                            isOutline = activeDay != i,
                            isSmall = true,
                            onClick = { activeDay = i }
                        )
                    }
                }

                // Timeline items
                val currentDay = days[activeDay]
                currentDay.items.forEachIndexed { idx, item ->
                    val key = "$activeDay-$idx"
                    val ck = checked[key] == true
                    val col = typeColors[item.type] ?: Stone

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        // Time column with line
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(46.dp)
                        ) {
                            Text(
                                text = item.time,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ck) Pebble else Bark,
                                fontFamily = SansFont
                            )
                            if (idx < currentDay.items.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(1.5.dp)
                                        .weight(1f)
                                        .padding(vertical = 4.dp)
                                        .background(Color.Black.copy(alpha = 0.06f))
                                )
                            }
                        }

                        // Activity card
                        GlassSurface(
                            animate = true,
                            onClick = {
                                checked = checked.toMutableMap().apply {
                                    this[key] = !(this[key] ?: false)
                                }
                            },
                            intensity = if (ck) GlassIntensity.Subtle else GlassIntensity.Normal,
                            borderLeftColor = col,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                                .alpha(if (ck) 0.5f else 1f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Checkbox — plan tab'a özgü küçük boyut
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .background(
                                                if (ck) Moss.copy(alpha = 0.12f) else Color.Transparent,
                                                RoundedCornerShape(5.dp)
                                            )
                                            .border(1.5.dp, if (ck) Moss else Pebble, RoundedCornerShape(5.dp))
                                    ) {
                                        if (ck) Text("\u2713", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Moss)
                                    }
                                    Text(
                                        text = item.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Bark,
                                        fontFamily = SansFont,
                                        textDecoration = if (ck) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                }
                                if (item.est != null) {
                                    Text(
                                        text = "~¥${item.est}",
                                        fontSize = 11.sp,
                                        color = Stone,
                                        fontFamily = SansFont
                                    )
                                }
                            }
                            Text(
                                text = item.note,
                                fontSize = 11.sp,
                                color = Stone,
                                fontFamily = SansFont,
                                modifier = Modifier.padding(start = 26.dp, top = 3.dp)
                            )
                            Box(modifier = Modifier.padding(start = 26.dp, top = 4.dp)) {
                                GlassPill(
                                    text = typeLabels[item.type] ?: "",
                                    color = col
                                )
                            }
                        }
                    }
                }
            }

            // ---- TAB: Belgeler ----
            if (tab == "docs") {
                val readyCount = docs.count { it.status == "ready" }

                AnimatedProgress(
                    value = readyCount.toFloat(),
                    max = docs.size.toFloat(),
                    color = Moss,
                    height = 5.dp,
                    delayMs = 200
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$readyCount/${docs.size} hazır",
                    fontSize = 11.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
                Spacer(Modifier.height(10.dp))

                docs.forEach { doc ->
                    GlassSurface(
                        animate = true,
                        intensity = if (doc.status == "ready") GlassIntensity.Subtle else GlassIntensity.Normal,
                        borderLeftColor = if (doc.status == "ready") Moss else Honey,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .alpha(if (doc.status == "ready") 0.65f else 1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = doc.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Bark,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = doc.note,
                                    fontSize = 11.sp,
                                    color = Stone,
                                    fontFamily = SansFont
                                )
                            }
                            if (doc.status == "pending") {
                                GlassButton(
                                    text = "İşlem",
                                    color = Lav,
                                    isSmall = true,
                                    isOutline = true
                                )
                            }
                            if (doc.status == "ready") {
                                GlassPill(text = "Hazır", color = Moss)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ProactiveOffer: JPY savings
                ProactiveOffer(
                    type = "savings",
                    emoji = "\uD83D\uDCB1",
                    title = "Döviz Birikim — JPY",
                    rate = "Otomatik alım",
                    term = "Aylık",
                    goal = 85000,
                    desc = "Tokyo seyahatiniz için hedef belirleyin, her ay otomatik JPY alımı yapılsın. Kur ortalaması ile avantaj.",
                    cta = "Planla",
                    color = Lav,
                    highlight = true,
                    socialProof = "Japonya seyahati planlayan 2.100 müşteri kullanıyor",
                    savingsVs = "Düzenli alım ile kur ortalaması %8 daha avantajlı"
                )

                // Travel health insurance risk card
                GlassSurface(
                    animate = true,
                    accent = Lav,
                    glow = true,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .border(1.5.dp, Lav.copy(alpha = 0.19f), RoundedCornerShape(24.dp))
                ) {
                    // Header banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Lav.copy(alpha = 0.06f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("✈️", fontSize = 14.sp)
                        Text(
                            text = "SEYAHAT GÜVENCESİ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Lav,
                            fontFamily = SansFont,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                        Text(
                            text = "Tokyo Seyahatinizi Güvenceye Alın",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "En sık yaşanan seyahat sorunlarına karşı koruma",
                            fontSize = 11.sp,
                            color = Stone,
                            fontFamily = SansFont
                        )
                        Spacer(Modifier.height(12.dp))

                        // Risk items: 4 detailed cards
                        data class RiskItem(
                            val icon: String,
                            val title: String,
                            val desc: String,
                            val coverage: String
                        )
                        val riskItems = listOf(
                            RiskItem("🧳", "Bagaj Kaybı / Gecikmesi", "Bagajınız gelmezse acil ihtiyaçlar + kayıp eşya tazminatı", "₺15.000'e kadar"),
                            RiskItem("❌", "Uçuş İptali / Rötar", "İptal veya 4+ saat rötar durumunda konaklama + yeni bilet", "₺10.000'e kadar"),
                            RiskItem("📋", "Vize Reddi", "Vize başvurusu reddedilirse ödediğiniz masraflar iade", "₺5.000'e kadar"),
                            RiskItem("🏥", "Acil Sağlık", "Yurt dışında acil tedavi, ilaç ve ambulans masrafları", "₺250.000'e kadar")
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            riskItems.forEach { risk ->
                                GlassSurface(
                                    intensity = GlassIntensity.Subtle,
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(risk.icon, fontSize = 16.sp)
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = risk.title,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Bark,
                                                    fontFamily = SansFont
                                                )
                                                Text(
                                                    text = risk.coverage,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Lav,
                                                    fontFamily = SansFont,
                                                    modifier = Modifier
                                                        .background(
                                                            Lav.copy(alpha = 0.12f),
                                                            RoundedCornerShape(5.dp)
                                                        )
                                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(Modifier.height(2.dp))
                                            Text(
                                                text = risk.desc,
                                                fontSize = 10.sp,
                                                color = Stone,
                                                fontFamily = SansFont
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Shield badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Moss.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("\uD83D\uDEE1\uFE0F", fontSize = 12.sp)
                            Text(
                                text = "10 günlük tam koruma: ₺890",
                                fontSize = 13.sp,
                                color = Moss,
                                fontWeight = FontWeight.Bold,
                                fontFamily = SansFont
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "günlük ₺89",
                                fontSize = 10.sp,
                                color = Stone,
                                fontFamily = SansFont
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        InfoBadge(
                            emoji = "\uD83D\uDC65",
                            text = "Japonya'ya giden müşterilerimizin %91'i seyahat sigortası alıyor"
                        )

                        Spacer(Modifier.height(8.dp))

                        InfoBadge(
                            emoji = "\uD83D\uDCCA",
                            text = "Geçen yıl 480 müşterimiz bagaj gecikmesi tazminatı aldı",
                            bgColor = Moss.copy(alpha = 0.04f),
                            borderColor = Moss.copy(alpha = 0.1f),
                            textColor = Moss,
                            textWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(10.dp))

                        GlassButton(
                            text = "Poliçe Al — ₺890",
                            color = Lav,
                            isFull = true
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Seyahat öncesi veya sonrası iptal edebilirsiniz",
                            fontSize = 10.sp,
                            color = Pebble,
                            fontFamily = SansFont,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = seyahatSummary, accent = Lav)
            }

            // ---- TAB: Çanta ----
            if (tab == "packing") {
                val totalItems = packing.sumOf { it.items.size }
                val checkedCount = checkedPack.values.count { it }

                AnimatedProgress(
                    value = checkedCount.toFloat(),
                    max = totalItems.toFloat(),
                    color = Moss,
                    height = 5.dp,
                    delayMs = 200
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$checkedCount/$totalItems tamamlandı",
                    fontSize = 11.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
                Spacer(Modifier.height(14.dp))

                packing.forEachIndexed { ci, cat ->
                    // Category header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(cat.icon, fontSize = 14.sp)
                        Text(
                            text = cat.cat,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont
                        )
                    }

                    cat.items.forEachIndexed { ii, item ->
                        val key = "p-$ci-$ii"
                        ChecklistRow(
                            title = item,
                            checked = checkedPack[key] == true,
                            onCheck = {
                                checkedPack = checkedPack.toMutableMap().apply {
                                    this[key] = !(this[key] ?: false)
                                }
                            },
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

