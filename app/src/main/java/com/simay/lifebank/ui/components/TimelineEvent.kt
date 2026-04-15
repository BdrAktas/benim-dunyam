package com.simay.lifebank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.simay.lifebank.ui.theme.YkbBorderHairline

/**
 * Yeniden kullanılabilir timeline satırı.
 *
 * Kullanım alanları:
 * - FinansScreen nakit akışı (emoji marker + olay kartı)
 * - SeyahatScreen gün planı (saat marker + aktivite kartı)
 * - AracimScreen bakım listesi (durum marker + bakım kartı)
 *
 * @param marker       Sol sütundaki marker composable (emoji kutu, saat etiketi, vb.)
 * @param showLine     Son eleman değilse true — marker altına dikey çizgi çizer
 * @param lineHeight   Dikey çizgi yüksekliği (varsayılan 40.dp)
 * @param lineColor    Dikey çizgi rengi (varsayılan YkbBorderHairline)
 * @param markerWidth  Sol sütun genişliği (varsayılan 44.dp)
 * @param modifier     Dış modifier
 * @param content      Sağ taraftaki içerik composable
 */
@Composable
fun TimelineEvent(
    marker: @Composable () -> Unit,
    showLine: Boolean = false,
    lineHeight: Dp = 40.dp,
    lineColor: Color = YkbBorderHairline,
    markerWidth: Dp = 44.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sol sütun: marker + opsiyonel dikey çizgi
        Column(
            modifier = Modifier.width(markerWidth),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            marker()
            if (showLine) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(lineHeight)
                        .padding(vertical = 3.dp)
                        .background(lineColor)
                )
            }
        }

        // Sağ içerik
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}
