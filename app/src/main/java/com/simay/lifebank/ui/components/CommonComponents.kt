package com.simay.lifebank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.BarkMid
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.util.formatTRY

// S1.2 — home DNA ile uyumlu: koyu başlık, YkbNeutral100 doldurulmuş çember back button,
// opsiyonel domain accent chip (hangi domain'deyim hissini kaybetmemek için).
@Composable
fun DomainHeader(
    label: String,
    subtitle: String? = null,
    accent: Color? = null,
    onBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(com.simay.lifebank.ui.theme.YkbNeutral100)
                .clickable(onClick = onBack)
        ) {
            Text(
                text = "\u2039",
                fontSize = 22.sp,
                color = com.simay.lifebank.ui.theme.YkbNeutral900,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.width(12.dp))
        // Optional accent dot — hangi domain'de olduğunu hızlı işaretler
        if (accent != null) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.12f))
            )
            Spacer(Modifier.width(10.dp))
        }
        Column {
            Text(
                text = label,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = com.simay.lifebank.ui.theme.YkbNeutral900,
                fontFamily = SansFont
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    emoji: String,
    title: String,
    desc: String,
    cta: String? = null,
    color: Color = Sky,
    onClick: () -> Unit = {}
) {
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Subtle,
        borderLeftColor = color,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        onClick = onClick,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(emoji, fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
                Text(
                    desc, fontSize = 11.sp, color = Stone, fontFamily = SansFont,
                    lineHeight = 16.sp, modifier = Modifier.padding(top = 2.dp)
                )
                if (cta != null) {
                    Spacer(Modifier.height(8.dp))
                    GlassButton(text = cta, color = color, isSmall = true)
                }
            }
        }
    }
}

@Composable
fun ProactiveOffer(
    emoji: String,
    title: String,
    limit: Int = 0,
    rate: String = "",
    term: String = "",
    desc: String = "",
    cta: String? = null,
    color: Color = Moss,
    highlight: Boolean = false,
    type: String = "credit",
    earnings: Int = 0,
    goal: Int = 0,
    socialProof: String? = null,
    savingsVs: String? = null,
    authority: String? = null,
    onClick: () -> Unit = {}
) {
    val animLimit = animateCountUp(limit, 1000)
    val animEarnings = animateCountUp(earnings, 1000)
    val isSavings = type == "savings"

    GlassSurface(
        animate = true,
        intensity = if (highlight) GlassIntensity.Strong else GlassIntensity.Normal,
        accent = color,
        glow = highlight,
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        // Top banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(color.copy(alpha = 0.08f), color.copy(alpha = 0.03f))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(emoji, fontSize = 14.sp)
                Text(
                    text = if (isSavings) "PARANIZI DE\u011eERLEND\u0130R\u0130N" else "S\u0130Z\u0130N \u0130\u00c7\u0130N HAZIRLANDI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontFamily = SansFont,
                    letterSpacing = 0.5.sp
                )
            }
            if (authority != null) {
                Text(
                    text = authority,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Stone,
                    fontFamily = SansFont,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        // Content
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Bark, fontFamily = SansFont)
            Spacer(Modifier.height(6.dp))

            if (isSavings) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "FA\u0130Z ORANI", fontSize = 10.sp, color = Stone,
                        fontFamily = SansFont, letterSpacing = 0.5.sp
                    )
                    Text(
                        rate, fontSize = 26.sp, fontWeight = FontWeight.Bold,
                        color = color, fontFamily = SerifFont
                    )
                }
                Spacer(Modifier.height(4.dp))

                if (earnings > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Moss.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\uD83D\uDCB8", fontSize = 12.sp)
                        Text(
                            "Tahmini kazan\u00e7: ${formatTRY(animEarnings)}",
                            fontSize = 12.sp, color = Moss,
                            fontWeight = FontWeight.Bold, fontFamily = SansFont
                        )
                        if (term.isNotEmpty()) {
                            Text("/ $term", fontSize = 10.sp, color = Stone, fontFamily = SansFont)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                if (goal > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\uD83C\uDFAF", fontSize = 12.sp)
                        Text(
                            "${formatTRY(goal)} hedef belirleyin, otomatik biriktirin",
                            fontSize = 12.sp, color = Bark, fontFamily = SansFont
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "HAZIR L\u0130M\u0130T\u0130N\u0130Z", fontSize = 10.sp, color = Stone,
                        fontFamily = SansFont, letterSpacing = 0.5.sp
                    )
                    Text(
                        formatTRY(animLimit), fontSize = 24.sp,
                        fontWeight = FontWeight.Bold, color = color, fontFamily = SerifFont
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            if (socialProof != null) {
                InfoBadge(emoji = "\uD83D\uDC65", text = socialProof, bgColor = Color.White.copy(alpha = 0.35f))
                Spacer(Modifier.height(8.dp))
            }

            if (savingsVs != null) {
                InfoBadge(
                    emoji = "\uD83D\uDCCA", text = savingsVs,
                    bgColor = Moss.copy(alpha = 0.04f),
                    borderColor = Moss.copy(alpha = 0.1f),
                    textColor = Moss, textWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
            }

            // Terms pills
            if (!isSavings) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (rate.isNotEmpty()) TermPill("Faiz $rate")
                    if (term.isNotEmpty()) TermPill(term)
                }
                if (rate.isNotEmpty() || term.isNotEmpty()) Spacer(Modifier.height(10.dp))
            }
            if (isSavings && term.isNotEmpty() && earnings == 0) {
                TermPill(term)
                Spacer(Modifier.height(10.dp))
            }

            if (desc.isNotEmpty()) {
                Text(desc, fontSize = 11.sp, color = Stone, fontFamily = SansFont, lineHeight = 16.sp)
                Spacer(Modifier.height(10.dp))
            }

            GlassButton(
                text = cta ?: if (isSavings) "Hedef Belirle" else "Hemen Kullan",
                color = color,
                isFull = true
            )
        }
    }
}

@Composable
fun InfoBadge(
    emoji: String,
    text: String,
    bgColor: Color = Color.White.copy(alpha = 0.35f),
    borderColor: Color = Color.White.copy(alpha = 0.3f),
    textColor: Color = BarkMid,
    textWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(emoji, fontSize = 11.sp)
        Text(text, fontSize = 11.sp, color = textColor, fontFamily = SansFont, fontWeight = textWeight)
    }
}

@Composable
fun TermPill(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Bark,
        fontFamily = SansFont,
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
