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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.ProductCard
import com.simay.lifebank.ui.components.TabItem
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.Rose
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.util.formatTRY

private data class Appointment(
    val title: String,
    val provider: String,
    val date: String,
    val time: String = "",
    val past: Boolean = false,
    val cost: Int = 0,
    val covered: Int = 0
)

private data class Medication(
    val name: String,
    val dose: String,
    val time: String,
    val remaining: Int
)

private data class CoverageCategory(
    val cat: String,
    val limit: Int,
    val used: Int
)

@Composable
fun SaglikScreen(onBack: () -> Unit) {
    var tab by remember { mutableStateOf("takvim") }
    val medChecked = remember { mutableStateMapOf<String, Boolean>() }
    val covered = animateCountUp(target = 9284, duration = 800)

    val appointments = remember {
        listOf(
            Appointment(title = "Check-up", provider = "Acıbadem", date = "15 May 2026", time = "10:00", past = false),
            Appointment(title = "Diş Kontrolü", provider = "DentGroup", date = "22 Haz 2026", time = "14:30", past = false),
            Appointment(title = "Diş Tedavisi", provider = "DentGroup", date = "20 Mar 2026", past = true, cost = 2800, covered = 1960),
            Appointment(title = "Genel Check-up", provider = "Acıbadem", date = "1 Mar 2026", past = true, cost = 4200, covered = 3780)
        )
    }

    val meds = remember {
        listOf(
            Medication(name = "Vitamin D3", dose = "1000 IU", time = "Sabah", remaining = 18),
            Medication(name = "Omega 3", dose = "1000 mg", time = "Öğle", remaining = 24),
            Medication(name = "Magnezyum", dose = "400 mg", time = "Akşam", remaining = 8)
        )
    }

    val coverage = remember {
        listOf(
            CoverageCategory(cat = "Muayene", limit = 10000, used = 6000),
            CoverageCategory(cat = "Diş", limit = 5000, used = 2800),
            CoverageCategory(cat = "İlaç", limit = 3000, used = 1680),
            CoverageCategory(cat = "Lab", limit = 5000, used = 1200)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        DomainHeader(label = "Sağlık", subtitle = "Allianz Tamamlayıcı", onBack = onBack)

        // Tabs
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            GlassTabs(
                tabs = listOf(
                    TabItem(id = "takvim", label = "Takvim"),
                    TabItem(id = "ilac", label = "İlaçlar"),
                    TabItem(id = "kapsam", label = "Kapsam")
                ),
                activeId = tab,
                onTabChange = { tab = it }
            )
        }

        // Content
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            when (tab) {
                "takvim" -> TakvimTab(appointments)
                "ilac" -> IlacTab(meds, medChecked)
                "kapsam" -> KapsamTab(coverage, covered)
            }
        }
    }
}

@Composable
private fun TakvimTab(appointments: List<Appointment>) {
    // Yaklaşan header
    Text(
        text = "Yaklaşan",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Bark,
        fontFamily = SerifFont,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    // Upcoming appointments
    appointments.filter { !it.past }.forEach { a ->
        GlassSurface(
            animate = true,
            borderLeftColor = Rose,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = a.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Bark,
                        fontFamily = SansFont
                    )
                    Text(
                        text = a.provider,
                        fontSize = 12.sp,
                        color = Stone,
                        fontFamily = SansFont
                    )
                    Text(
                        text = "${a.date} · ${a.time}",
                        fontSize = 12.sp,
                        color = Rose,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = SansFont,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    GlassButton(text = "Hatırlat", color = Rose, isSmall = true)
                    GlassButton(text = "İptal", color = Pebble, isSmall = true, isOutline = true)
                }
            }
        }
    }

    // Geçmiş header
    Text(
        text = "Geçmiş",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Bark,
        fontFamily = SerifFont,
        modifier = Modifier.padding(top = 14.dp, bottom = 10.dp)
    )

    // Past appointments
    appointments.filter { it.past }.forEach { a ->
        GlassSurface(
            animate = true,
            intensity = GlassIntensity.Subtle,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            modifier = Modifier
                .padding(bottom = 6.dp)
                .alpha(0.7f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = a.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Bark,
                        fontFamily = SansFont
                    )
                    Text(
                        text = a.date,
                        fontSize = 11.sp,
                        color = Stone,
                        fontFamily = SansFont
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatTRY(a.cost),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Bark,
                        fontFamily = SansFont
                    )
                    Text(
                        text = "Karşılanan: ${formatTRY(a.covered)}",
                        fontSize = 10.sp,
                        color = Moss,
                        fontFamily = SansFont
                    )
                }
            }
        }
    }

    ProductCard(
        emoji = "🏥",
        title = "Tamamlayıcı Sağlık",
        desc = "SGK'yı tamamlayan güvence.",
        cta = "Teklif Al",
        color = Rose
    )
}

@Composable
private fun IlacTab(
    meds: List<Medication>,
    medChecked: MutableMap<String, Boolean>
) {
    meds.forEachIndexed { i, med ->
        val key = "m-$i"
        val done = medChecked[key] == true

        GlassSurface(
            animate = true,
            onClick = { medChecked[key] = !done },
            borderLeftColor = if (done) Moss else Rose,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .alpha(if (done) 0.5f else 1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Checkbox
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(22.dp)
                        .border(
                            width = 1.5.dp,
                            color = if (done) Moss else Pebble,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(
                            color = if (done) Moss.copy(alpha = 0.12f) else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    if (done) {
                        Text(
                            text = "✓",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Moss
                        )
                    }
                }

                // Medication info
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = med.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Bark,
                            fontFamily = SansFont,
                            textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Text(
                            text = med.time,
                            fontSize = 12.sp,
                            color = Stone,
                            fontFamily = SansFont
                        )
                    }
                    Text(
                        text = med.dose,
                        fontSize = 11.sp,
                        color = Stone,
                        fontFamily = SansFont,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Remaining days warning
            if (med.remaining <= 10) {
                Text(
                    text = "⚠ ${med.remaining} gün kaldı",
                    fontSize = 11.sp,
                    color = Terra,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = SansFont,
                    modifier = Modifier.padding(start = 34.dp, top = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun KapsamTab(coverage: List<CoverageCategory>, covered: Int) {
    // Summary card
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Strong,
        contentPadding = PaddingValues(18.dp),
        modifier = Modifier.padding(bottom = 14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "KULLANILAN",
                    fontSize = 10.sp,
                    color = Pebble,
                    fontFamily = SansFont,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatTRY(12480),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Bark,
                    fontFamily = SerifFont
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "KARŞILANAN",
                    fontSize = 10.sp,
                    color = Pebble,
                    fontFamily = SansFont,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatTRY(covered),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Moss,
                    fontFamily = SerifFont
                )
            }
        }
        AnimatedProgress(
            value = 9284f,
            max = 12480f,
            color = Moss,
            height = 8.dp,
            delayMs = 300
        )
    }

    // Coverage categories
    coverage.forEachIndexed { i, c ->
        val ratio = c.used.toFloat() / c.limit.toFloat()
        GlassSurface(
            animate = true,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = c.cat,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Bark,
                    fontFamily = SansFont
                )
                Text(
                    text = "${formatTRY(c.used)} / ${formatTRY(c.limit)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Bark,
                    fontFamily = SansFont
                )
            }
            AnimatedProgress(
                value = c.used.toFloat(),
                max = c.limit.toFloat(),
                color = if (ratio > 0.8f) Terra else Moss,
                height = 5.dp,
                delayMs = i * 150
            )
        }
    }

    ProductCard(
        emoji = "🧩",
        title = "Modüler Sağlık",
        desc = "İhtiyacınıza göre modül seçin.",
        cta = "Paket Oluştur",
        color = Lav
    )
}
