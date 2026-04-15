package com.simay.lifebank.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbType
import com.simay.lifebank.ui.util.formatTRY
import kotlin.math.abs

// ── Veri Modelleri ────────────────────────────────────────────────────────────

data class ExpenseCategory(
    val label: String,
    val amount: Int,
    val transactionCount: Int,
    val percent: Int,
    val color: Color
)

data class ExpenseMerchant(
    val name: String,
    val category: String,
    val amount: Int,
    val transactionCount: Int
)

data class ExpenseSummary(
    val totalAmount: Int,
    val changePercent: Int,   // pozitif = artış (Terra), negatif = düşüş (Moss)
    val topCategory: String,
    val topAmount: Int,
    val categories: List<ExpenseCategory>,
    val merchants: List<ExpenseMerchant>
)

// ── Ana Composable ─────────────────────────────────────────────────────────────

@Composable
fun GiderlerTab(
    summary: ExpenseSummary,
    accent: Color
) {
    var innerTab by remember { mutableStateOf("kategoriler") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ExpenseSummaryCard(summary = summary, accent = accent)

        GlassTabs(
            tabs = listOf(
                TabItem("kategoriler", "Kategoriler"),
                TabItem("isyerleri", "İşyerleri")
            ),
            activeId = innerTab,
            onTabChange = { innerTab = it }
        )

        if (innerTab == "kategoriler") {
            summary.categories.forEachIndexed { i, cat ->
                ExpenseCategoryRow(category = cat, index = i)
            }
        } else {
            summary.merchants.forEach { merchant ->
                ExpenseMerchantRow(merchant = merchant, accent = accent)
            }
        }
    }
}

// ── Özet Kart ─────────────────────────────────────────────────────────────────

@Composable
private fun ExpenseSummaryCard(summary: ExpenseSummary, accent: Color) {
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Strong,
        accent = accent,
        glow = true,
        contentPadding = PaddingValues(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Bu ay toplam",
                style = YkbType.BodySm,
                color = Stone
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = formatTRY(summary.totalAmount),
                style = YkbType.NumericXl,
                color = accent
            )
            Spacer(Modifier.height(4.dp))
            val positive = summary.changePercent >= 0
            val changeColor = if (positive) Terra else Moss
            val arrow = if (positive) "↑" else "↓"
            Text(
                text = "$arrow%${abs(summary.changePercent)} geçen aya göre",
                style = YkbType.BodySm,
                color = changeColor
            )
            Spacer(Modifier.height(8.dp))
            AnimatedProgress(
                value = summary.totalAmount.toFloat(),
                max = summary.totalAmount * 1.5f,
                color = accent,
                height = 5.dp
            )
            Spacer(Modifier.height(6.dp))
            Row {
                Text(
                    text = "En yüksek: ",
                    style = YkbType.BodySm,
                    color = Stone
                )
                Text(
                    text = summary.topCategory,
                    style = YkbType.BodySm.copy(fontWeight = FontWeight.Bold),
                    color = Bark
                )
                Text(
                    text = " · ${formatTRY(summary.topAmount)}",
                    style = YkbType.BodySm,
                    color = Stone
                )
            }
        }
    }
}

// ── Kategori Satırı ───────────────────────────────────────────────────────────

@Composable
private fun ExpenseCategoryRow(category: ExpenseCategory, index: Int) {
    GlassSurface(
        animate = true,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(category.color)
                    )
                    Text(
                        text = category.label,
                        style = YkbType.BodyMd.copy(fontWeight = FontWeight.SemiBold),
                        color = Bark
                    )
                }
                Text(
                    text = formatTRY(category.amount),
                    style = YkbType.BodyMd.copy(fontWeight = FontWeight.Bold),
                    color = Bark
                )
            }
            AnimatedProgress(
                value = category.percent.toFloat(),
                max = 100f,
                color = category.color,
                height = 5.dp,
                delayMs = index * 100
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${category.transactionCount} işlem",
                    style = YkbType.BodySm,
                    color = Stone
                )
                Text(
                    text = "%${category.percent}",
                    style = YkbType.BodySm,
                    color = Stone
                )
            }
        }
    }
}

// ── İşyeri Satırı ─────────────────────────────────────────────────────────────

@Composable
private fun ExpenseMerchantRow(merchant: ExpenseMerchant, accent: Color) {
    GlassSurface(
        animate = true,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = merchant.name.first().toString(),
                        style = YkbType.BodySm.copy(fontWeight = FontWeight.Bold),
                        color = accent
                    )
                }
                Column {
                    Text(
                        text = merchant.name,
                        style = YkbType.BodyMd.copy(fontWeight = FontWeight.SemiBold),
                        color = Bark
                    )
                    Text(
                        text = "${merchant.transactionCount} işlem",
                        style = YkbType.BodySm,
                        color = Stone
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatTRY(merchant.amount),
                    style = YkbType.BodyMd.copy(fontWeight = FontWeight.Bold),
                    color = Bark
                )
                Spacer(Modifier.height(4.dp))
                GlassPill(text = merchant.category, color = accent)
            }
        }
    }
}
