package com.simay.lifebank.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
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
        hour < 18 -> "Merhabalar"
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // ═══ HEADER ═══
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(greeting, fontSize = 12.sp, color = Stone, fontFamily = SansFont, letterSpacing = 0.3.sp)
                    Text("Bedir", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Bark, fontFamily = SerifFont)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                ) {
                    Text("B", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                }
            }

            // ═══ HESAP KARTI ═══
            GlassSurface(
                animate = true,
                intensity = GlassIntensity.Strong,
                accent = Moss,
                glow = true,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            "VADES\u0130Z TL HESABI", fontSize = 10.sp, color = Stone,
                            fontFamily = SansFont, letterSpacing = 0.8.sp, fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            formatTRY(balance), fontSize = 28.sp, fontWeight = FontWeight.Medium,
                            color = Bark, fontFamily = SerifFont, modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .widthIn(max = 120.dp)
                            .background(Color.White.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "\u2B50 MAA\u015e AYRICALI\u011eI", fontSize = 8.sp, color = Stone,
                            fontFamily = SansFont, letterSpacing = 0.5.sp, fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(3.dp))
                        Text("EFT \u00fccretsiz", fontSize = 9.sp, color = Moss, fontWeight = FontWeight.Bold, fontFamily = SansFont)
                        Text("Faiz +%2", fontSize = 9.sp, color = Moss, fontWeight = FontWeight.Bold, fontFamily = SansFont)
                        Text("Kredi avantaj\u0131", fontSize = 9.sp, color = Moss, fontWeight = FontWeight.Bold, fontFamily = SansFont)
                    }
                }

                Text(
                    "HIZLI TRANSFER", fontSize = 10.sp, color = Stone,
                    fontFamily = SansFont, fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp, modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    contacts.forEach { c ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(c.color, c.color.copy(alpha = 0.53f))))
                            ) {
                                Text(c.avatar, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = SansFont)
                            }
                            Spacer(Modifier.height(3.dp))
                            Text(c.name, fontSize = 9.sp, color = Stone, fontFamily = SansFont)
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.4f))
                                .border(1.5.dp, Color.Black.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Text("+", fontSize = 16.sp, color = Pebble)
                        }
                        Spacer(Modifier.height(3.dp))
                        Text("Yeni", fontSize = 9.sp, color = Pebble, fontFamily = SansFont)
                    }
                }
            }

            // ═══ ADİOS KART ═══
            val purpleDark = Color(0xFF1A0A3E)
            val purpleMid = Color(0xFF2D1B69)
            val purpleAccent = Color(0xFF8B5CF6)
            val purpleLight = Color(0xFFC084FC)

            GlassSurface(
                animate = true,
                intensity = GlassIntensity.Normal,
                contentPadding = PaddingValues(0.dp),
                onClick = { onNavigate("finans") },
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(listOf(purpleDark, purpleMid, purpleDark)))
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .graphicsLayer { translationX = 20f; translationY = -30f }
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.04f))
                    )
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Adios \u2022\u2022\u2022\u2022 8742", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontFamily = SansFont)
                            Text("VISA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f), fontFamily = SansFont)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text("BU AY HARCAMA", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f), fontFamily = SansFont)
                                Text(formatTRY(cardSpent), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = SansFont)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("KULLANILAB\u0130L\u0130R L\u0130M\u0130T", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f), fontFamily = SansFont)
                                Text(formatTRY(cardAvailable), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = SerifFont)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(23456f / 75000f).height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Brush.horizontalGradient(listOf(purpleAccent, purpleLight)))
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("\u2708\uFE0F", fontSize = 14.sp)
                        Column {
                            Text("${String.format("%,d", worldMiles).replace(',', '.')} mil", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                            Text("Adios mil", fontSize = 9.sp, color = Pebble, fontFamily = SansFont)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("\uD83C\uDF0D", fontSize = 14.sp)
                        Column {
                            Text("3x mil", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Lav, fontFamily = SansFont)
                            Text("yurt d\u0131\u015f\u0131", fontSize = 9.sp, color = Pebble, fontFamily = SansFont)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("\uD83D\uDCC5", fontSize = 14.sp)
                        Column {
                            Text("20 Nis", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                            Text("ekstre kesim", fontSize = 9.sp, color = Pebble, fontFamily = SansFont)
                        }
                    }
                }
            }

            // ═══ MİL TEKLİFİ ═══
            GlassSurface(
                animate = true,
                intensity = GlassIntensity.Subtle,
                borderLeftColor = purpleAccent,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                onClick = { onNavigate("seyahat") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("\u2708\uFE0F", fontSize = 16.sp)
                    Row(modifier = Modifier.weight(1f)) {
                        Text("14.280 miliniz var", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                        Text(" \u00B7 Tokyo u\u00e7u\u015funda kullan\u0131n, ", fontSize = 11.sp, color = Stone, fontFamily = SansFont)
                        Text("\u20BA12.200 tasarruf", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = purpleAccent, fontFamily = SansFont)
                    }
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
