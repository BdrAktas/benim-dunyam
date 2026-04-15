package com.simay.lifebank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassPill
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.InfoBadge
import com.simay.lifebank.ui.components.ProductCard
import com.simay.lifebank.ui.components.TabItem
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import java.text.NumberFormat
import java.util.Locale

private data class MaintenanceItem(
    val title: String,
    val interval: String,
    val next: String,
    val urgent: Boolean
)

private fun formatKm(value: Int): String {
    return NumberFormat.getNumberInstance(Locale("tr", "TR")).format(value)
}

@Composable
fun AracimScreen(onBack: () -> Unit, intent: String? = null) {
    var tab by remember { mutableStateOf("genel") }
    LaunchedEffect(intent) {
        when (intent) {
            "kasko_quote" -> tab = "genel"
            "muayene_book" -> tab = "bakim"
        }
    }
    var expandedM by remember { mutableStateOf<Int?>(null) }
    val km = animateCountUp(target = 48250, duration = 1000)

    val maintenance = remember {
        listOf(
            MaintenanceItem("Yağ & Filtre", "10.000 km", "48.200 km", urgent = true),
            MaintenanceItem("Fren Balata", "20.000 km", "48.000 km", urgent = true),
            MaintenanceItem("Lastik Rotasyonu", "15.000 km", "60.000 km", urgent = false),
            MaintenanceItem("Akü Kontrolü", "2 yılda 1", "Mar 2026", urgent = true)
        )
    }

    val aracimSummary = remember {
        ExpenseSummary(
            totalAmount = 3605,
            changePercent = 3,
            topCategory = "Yakıt",
            topAmount = 3240,
            categories = listOf(
                ExpenseCategory("Yakıt",   3240, 8, 90, Moss),
                ExpenseCategory("OGS/HGS",  245, 3,  7, Sky),
                ExpenseCategory("Otopark",  120, 4,  3, Lav)
            ),
            merchants = listOf(
                ExpenseMerchant("Shell",      "Yakıt",   1840, 3),
                ExpenseMerchant("BP",         "Yakıt",    980, 3),
                ExpenseMerchant("Opet",       "Yakıt",    420, 2),
                ExpenseMerchant("OGS Geçiş",  "OGS/HGS",  245, 3),
                ExpenseMerchant("İSPARK",     "Otopark",  120, 4)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        DomainHeader(
            label = "Aracım",
            subtitle = "BMW 320i \u00B7 34 ABC 742",
            accent = Moss,
            onBack = onBack
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
            GlassTabs(
                tabs = listOf(
                    TabItem("genel", "Genel"),
                    TabItem("bakim", "Bakım"),
                    TabItem("giderler", "Giderlerim")
                ),
                activeId = tab,
                onTabChange = { tab = it }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {

            // ---- TAB: Genel ----
            if (tab == "genel") {
                // BMW main card
                GlassSurface(
                    animate = true,
                    intensity = GlassIntensity.Strong,
                    accent = Moss,
                    glow = true,
                    contentPadding = PaddingValues(18.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "BMW 320i",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Bark,
                                fontFamily = SerifFont
                            )
                            Text(
                                text = "34 ABC 742 \u00B7 2021",
                                fontSize = 12.sp,
                                color = Stone,
                                fontFamily = SansFont
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "${formatKm(km)} km",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Moss,
                                fontFamily = SerifFont
                            )
                        }
                        Text("\uD83D\uDE97", fontSize = 40.sp)
                    }
                }

                // Araç Muayenesi
                GlassSurface(
                    animate = true,
                    borderLeftColor = Honey,
                    contentPadding = PaddingValues(14.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Araç Muayenesi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Bark,
                                fontFamily = SansFont
                            )
                            Text(
                                text = "15 Ağu 2026 \u00B7 126 gün",
                                fontSize = 12.sp,
                                color = Stone,
                                fontFamily = SansFont
                            )
                        }
                        GlassButton(
                            text = "Randevu Al",
                            color = Honey,
                            isSmall = true
                        )
                    }
                }

                // Insurance items
                data class InsuranceItem(
                    val type: String,
                    val provider: String,
                    val expiry: String,
                    val price: String
                )

                val insurances = listOf(
                    InsuranceItem("Kasko", "AXA", "Oca 2027", "₺12.400/yıl"),
                    InsuranceItem("Trafik", "AXA", "Ağu 2026", "₺3.800/yıl")
                )

                insurances.forEach { ins ->
                    GlassSurface(
                        animate = true,
                        borderLeftColor = Moss,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = ins.type,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Bark,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = "${ins.provider} \u00B7 ${ins.expiry}",
                                    fontSize = 11.sp,
                                    color = Stone,
                                    fontFamily = SansFont
                                )
                            }
                            GlassPill(text = "Aktif", color = Moss)
                        }
                    }
                }

                // Kasko — Value proposition
                GlassSurface(
                    animate = true,
                    accent = Moss,
                    glow = true,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .border(1.5.dp, Moss.copy(alpha = 0.19f), RoundedCornerShape(24.dp))
                ) {
                    // Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Moss.copy(alpha = 0.06f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("\uD83D\uDEE1\uFE0F", fontSize = 14.sp)
                            Text(
                                text = "KASKO YENİLEME ZAMANI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Moss,
                                fontFamily = SansFont,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Vade: Oca 2027",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Stone,
                            fontFamily = SansFont,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                        // Content title
                        Text(
                            text = "Dijital Kasko ile neler kazanırsınız?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont
                        )

                        Spacer(Modifier.height(10.dp))

                        // Benefit items
                        data class BenefitItem(
                            val icon: String,
                            val title: String,
                            val desc: String
                        )

                        val benefits = listOf(
                            BenefitItem("\uD83D\uDD27", "Mini Onarım", "Küçük çizik ve eziklerde hasarsız onarım, primsiz"),
                            BenefitItem("\uD83D\uDE97", "İkame Araç", "Kaza sonrası tamirde araçsız kalmayın, 15 güne kadar"),
                            BenefitItem("\uD83C\uDD7F\uFE0F", "Otopark Hasarı", "Park halindeyken oluşan hasarlarda tam koruma"),
                            BenefitItem("\uD83D\uDCF1", "Anında Hasar İhbarı", "Uygulama üzerinden fotoğraf çek, dosya aç, 2 dk")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            benefits.forEach { benefit ->
                                GlassSurface(
                                    intensity = GlassIntensity.Subtle,
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(benefit.icon, fontSize = 16.sp)
                                        Column {
                                            Text(
                                                text = benefit.title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Bark,
                                                fontFamily = SansFont
                                            )
                                            Text(
                                                text = benefit.desc,
                                                fontSize = 10.sp,
                                                color = Stone,
                                                fontFamily = SansFont
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Social proof
                        InfoBadge(
                            emoji = "\uD83D\uDC65",
                            text = "Dijital Kasko müşterilerinin %94'ü yeniliyor — memnuniyet skoru 4.7/5"
                        )

                        Spacer(Modifier.height(8.dp))

                        // Comparison
                        InfoBadge(
                            emoji = "\uD83D\uDCCA",
                            text = "Online yenileme ile %12 indirim — şubeden daha avantajlı",
                            bgColor = Moss.copy(alpha = 0.04f),
                            borderColor = Moss.copy(alpha = 0.1f),
                            textColor = Moss,
                            textWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(10.dp))

                        GlassButton(
                            text = "Dijital Kasko Teklifi Al",
                            color = Moss,
                            isFull = true
                        )
                    }
                }

                // HGS ProductCard
                ProductCard(
                    emoji = "\uD83D\uDEE3\uFE0F",
                    title = "HGS Yükleme",
                    desc = "Bakiyenizi anında yükleyin.",
                    cta = "Yükle",
                    color = Lav
                )
            }

            // ---- TAB: Bakım ----
            if (tab == "bakim") {
                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    Text(
                        text = "Mevcut km: ",
                        fontSize = 11.sp,
                        color = Stone,
                        fontFamily = SansFont
                    )
                    Text(
                        text = formatKm(km),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Bark,
                        fontFamily = SansFont
                    )
                }

                maintenance.forEachIndexed { i, m ->
                    GlassSurface(
                        animate = true,
                        onClick = { expandedM = if (expandedM == i) null else i },
                        borderLeftColor = if (m.urgent) Terra else Moss,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = m.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Bark,
                                    fontFamily = SansFont
                                )
                                Text(
                                    text = "Sonraki: ${m.next}",
                                    fontSize = 11.sp,
                                    color = Stone,
                                    fontFamily = SansFont
                                )
                            }
                            GlassPill(
                                text = if (m.urgent) "Yakın" else "Güncel",
                                color = if (m.urgent) Terra else Moss
                            )
                        }
                        AnimatedVisibility(
                            visible = expandedM == i,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                GlassButton(
                                    text = "Bakım Randevusu Al",
                                    color = Moss,
                                    isSmall = true,
                                    isFull = true
                                )
                            }
                        }
                    }
                }
            }

            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = aracimSummary, accent = Moss)
            }
        }
    }
}
