package com.simay.lifebank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra

/**
 * Paylaşımlı checklist satırı — ilaç, belge, çanta, bakım listelerinde kullanılır.
 *
 * @param title       Ana metin (işaretlenince üstü çizilir)
 * @param subtitle    İkincil metin (doz, not) — boşsa gösterilmez
 * @param trailing    Sağ taraf etiketi (saat, tahmini tutar)
 * @param warning     Alt uyarı satırı (Terra rengi, bold) — boşsa gösterilmez
 * @param checked     İşaretli mi?
 * @param onCheck     Tıklanınca çağrılır
 * @param checkColor  İşaretlenince soluk çizgi + kutu dolgu rengi (default: Moss)
 * @param pendingColor İşaretlenmemişken sol çizgi rengi — null ise çizgi yok
 */
@Composable
fun ChecklistRow(
    title: String,
    subtitle: String? = null,
    trailing: String? = null,
    warning: String? = null,
    checked: Boolean,
    onCheck: () -> Unit,
    checkColor: Color = Moss,
    pendingColor: Color? = null,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        animate = true,
        onClick = onCheck,
        borderLeftColor = if (checked) checkColor else pendingColor,
        intensity = if (checked) GlassIntensity.Subtle else GlassIntensity.Normal,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = modifier.alpha(if (checked) 0.5f else 1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checkbox kutusu
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 1.5.dp,
                        color = if (checked) checkColor else Pebble,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .background(
                        color = if (checked) checkColor.copy(alpha = 0.12f) else Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                if (checked) {
                    Text(
                        text = "\u2713",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = checkColor
                    )
                }
            }

            // İçerik
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Bark,
                        fontFamily = SansFont,
                        textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(1f)
                    )
                    if (trailing != null) {
                        Text(
                            text = trailing,
                            fontSize = 12.sp,
                            color = Stone,
                            fontFamily = SansFont
                        )
                    }
                }
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Stone,
                        fontFamily = SansFont,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Uyarı satırı (örn. "⚠ 8 gün kaldı")
        if (!warning.isNullOrBlank()) {
            Text(
                text = warning,
                fontSize = 11.sp,
                color = Terra,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SansFont,
                modifier = Modifier.padding(start = 32.dp, top = 6.dp)
            )
        }
    }
}
