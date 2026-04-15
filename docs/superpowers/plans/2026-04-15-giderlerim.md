# Giderlerim Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Her domain ekranına (Evim, Aracım, Seyahatim, Sağlığım, Ailem) o alana ait harcamaları gösteren "Giderlerim" sekmesi ekle; paylaşılan `GiderlerTab` composable ile mock veri göster.

**Architecture:** Yeni `GiderlerTab.kt` bileşeni `ExpenseSummary` veri modelini alır, özet kart + iç GlassTabs (Kategoriler/İşyerleri) + liste döner. Her domain ekranı kendi mock verisini tanımlar ve `GiderlerTab(summary, accent)` çağırır. Aracım'da mevcut "Maliyet" sekmesi "Giderlerim"e dönüştürülür.

**Tech Stack:** Jetpack Compose, Kotlin, `:app` tek modülü. Derleme kontrolü: `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" && ./gradlew compileDebugKotlin`.

---

## Dosya Değişiklikleri

| Dosya | İşlem |
|-------|-------|
| `app/src/main/java/com/simay/lifebank/ui/components/GiderlerTab.kt` | **Yeni** — veri modelleri + `GiderlerTab` + private alt bileşenler |
| `app/src/main/java/com/simay/lifebank/ui/screens/AracimScreen.kt` | `"maliyet"` → `"giderler"`, `"Maliyet"` → `"Giderlerim"`, içerik `GiderlerTab` |
| `app/src/main/java/com/simay/lifebank/ui/screens/EvimScreen.kt` | `TabItem("giderler","Giderlerim")` ekle + `if (tab=="giderler")` bloğu |
| `app/src/main/java/com/simay/lifebank/ui/screens/SeyahatScreen.kt` | `TabItem("giderler","Giderlerim")` ekle + `if (tab=="giderler")` bloğu |
| `app/src/main/java/com/simay/lifebank/ui/screens/SaglikScreen.kt` | `TabItem("giderler","Giderlerim")` ekle + `if (tab=="giderler")` bloğu |
| `app/src/main/java/com/simay/lifebank/ui/screens/AilemScreen.kt` | `TabItem("giderler","Giderlerim")` ekle + `if (tab=="giderler")` bloğu |

---

## Task 1: GiderlerTab.kt — Veri modelleri ve paylaşılan bileşen

**Files:**
- Create: `app/src/main/java/com/simay/lifebank/ui/components/GiderlerTab.kt`

- [ ] **Step 1: Dosyayı oluştur**

Tam içerik aşağıdadır — kopyala-yapıştır:

```kotlin
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
```

- [ ] **Step 2: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/components/GiderlerTab.kt
git commit -m "feat(giderler): GiderlerTab bileşeni ve veri modelleri"
```

---

## Task 2: AracimScreen.kt — Maliyet → Giderlerim

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/AracimScreen.kt`

Mevcut "Maliyet" sekmesinin içeriği tamamen kaldırılır, yerine `GiderlerTab` çağrısı ve `ExpenseSummary` mock verisi eklenir.

- [ ] **Step 1: Import'ları ekle**

`AracimScreen.kt` import bloğuna şu satırları ekle (var `import com.simay.lifebank.ui.components.AnimatedProgress` satırının hemen altına):

```kotlin
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
```

- [ ] **Step 2: Tab listesinde "maliyet" → "giderler" olarak değiştir**

Dosyada şu bloğu bul:

```kotlin
                    TabItem("genel", "Genel"),
                    TabItem("bakim", "Bakım"),
                    TabItem("maliyet", "Maliyet")
```

Şununla değiştir:

```kotlin
                    TabItem("genel", "Genel"),
                    TabItem("bakim", "Bakım"),
                    TabItem("giderler", "Giderlerim")
```

- [ ] **Step 3: `CostItem` data class ve `costs` listesini kaldır, `ExpenseSummary` mock verisi ekle**

Dosyanın üstündeki şu kodu kaldır (satır 68–73 civarı):

```kotlin
private data class CostItem(
    val label: String,
    val amount: Int,
    val color: Color,
    val percent: Int
)
```

Ardından `AracimScreen` composable içindeki şu `remember` bloğunu kaldır:

```kotlin
    val costs = remember {
        listOf(
            CostItem("Yakıt", 3240, Terra, 89),
            CostItem("OGS/HGS", 245, Sky, 7),
            CostItem("Otopark", 120, Lav, 4)
        )
    }
```

Yerine `val km = animateCountUp(...)` satırının hemen altına şunu ekle:

```kotlin
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
```

- [ ] **Step 4: "Maliyet" tab içeriğini `GiderlerTab` ile değiştir**

Dosyada şu bloğu bul ve tamamını değiştir:

```kotlin
            // ---- TAB: Maliyet ----
            if (tab == "maliyet") {
                costs.forEachIndexed { i, c ->
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
                                text = c.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Bark,
                                fontFamily = SansFont
                            )
                            Text(
                                text = formatTRY(c.amount),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Bark,
                                fontFamily = SansFont
                            )
                        }
                        AnimatedProgress(
                            value = c.percent.toFloat(),
                            max = 100f,
                            color = c.color,
                            height = 5.dp,
                            delayMs = i * 150
                        )
                    }
                }

                ProactiveOffer(
                    emoji = "\uD83D\uDE97",
                    title = "Taşıt Kredisi",
                    limit = 450000,
                    rate = "%2.79",
                    term = "60 aya kadar",
                    desc = "Araç değişikliği düşünüyorsanız, size özel hazırlandı.",
                    cta = "Başvur",
                    color = Honey,
                    highlight = true,
                    socialProof = "Bu ay 3.800 araç kredisi kullanıldı",
                    savingsVs = "Aylık taksit ₺9.800 — araç kiralama maliyetinden düşük",
                    authority = "Ön Onaylı"
                )
            }
```

Şununla değiştir:

```kotlin
            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = aracimSummary, accent = Moss)
            }
```

- [ ] **Step 5: Artık kullanılmayan import'ları kaldır**

`AracimScreen.kt`'de `CostItem` ve `costs` kaldırıldıktan sonra `ProactiveOffer` import'u hâlâ kullanılıyorsa bırak, kullanılmıyorsa kaldır. Kontrol et:

```kotlin
import com.simay.lifebank.ui.components.ProactiveOffer
```

`ProactiveOffer` başka bir tab'da da kullanılmıyorsa bu import satırını kaldır.

- [ ] **Step 6: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/AracimScreen.kt
git commit -m "feat(giderler): AracimScreen — Maliyet sekmesi Giderlerim'e dönüştürüldü"
```

---

## Task 3: EvimScreen.kt — Giderlerim sekmesi ekle

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/EvimScreen.kt`

- [ ] **Step 1: Import'ları ekle**

`EvimScreen.kt` import bloğuna şu satırları ekle:

```kotlin
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
```

- [ ] **Step 2: Mock veri ekle**

`EvimScreen` composable içinde, `var tab by remember { mutableStateOf("fatura") }` satırının hemen altına şunu ekle:

```kotlin
    val evimSummary = remember {
        ExpenseSummary(
            totalAmount = 4280,
            changePercent = 8,
            topCategory = "Fatura",
            topAmount = 2840,
            categories = listOf(
                ExpenseCategory("Fatura",        2840, 12, 66, Sky),
                ExpenseCategory("Aidat",          850,  1, 20, Sky.copy(alpha = 0.7f)),
                ExpenseCategory("İnternet",        350,  1,  8, Sky.copy(alpha = 0.5f)),
                ExpenseCategory("Tadilat/Bakım",   240,  2,  6, Sky.copy(alpha = 0.35f))
            ),
            merchants = listOf(
                ExpenseMerchant("İGDAŞ",        "Fatura",   980, 2),
                ExpenseMerchant("ENERJİSA",     "Fatura",   720, 1),
                ExpenseMerchant("İSKİ",         "Fatura",   340, 2),
                ExpenseMerchant("Yönetim Ofisi","Aidat",    850, 1),
                ExpenseMerchant("Turkcell",     "İnternet", 350, 1)
            )
        )
    }
```

- [ ] **Step 3: GlassTabs listesine "Giderlerim" ekle**

Dosyada şu bloğu bul:

```kotlin
                    TabItem("fatura", "Faturalar"),
                    TabItem("deger", "Ev Değeri"),
                    TabItem("enerji", "Enerji"),
                    TabItem("sigorta", "Sigortalar")
```

Şununla değiştir:

```kotlin
                    TabItem("fatura", "Faturalar"),
                    TabItem("deger", "Ev Değeri"),
                    TabItem("enerji", "Enerji"),
                    TabItem("sigorta", "Sigortalar"),
                    TabItem("giderler", "Giderlerim")
```

- [ ] **Step 4: Giderlerim tab içeriğini ekle**

`EvimScreen` içindeki tab render bloğunun sonuna (son `if (tab == "sigorta") { ... }` bloğunun hemen ardından) şunu ekle:

```kotlin
            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = evimSummary, accent = Sky)
            }
```

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/EvimScreen.kt
git commit -m "feat(giderler): EvimScreen — Giderlerim sekmesi eklendi"
```

---

## Task 4: SeyahatScreen.kt — Giderlerim sekmesi ekle

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/SeyahatScreen.kt`

- [ ] **Step 1: Import'ları ekle**

`SeyahatScreen.kt` import bloğuna şu satırları ekle:

```kotlin
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
```

- [ ] **Step 2: Mock veri ekle**

`SeyahatScreen` composable içinde, `var tab by remember { mutableStateOf("plan") }` satırının hemen altına şunu ekle:

```kotlin
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
```

- [ ] **Step 3: GlassTabs listesine "Giderlerim" ekle**

Dosyada şu bloğu bul:

```kotlin
                    TabItem("plan", "Gün Planı"),
                    TabItem("docs", "Belgeler"),
                    TabItem("packing", "Çanta")
```

Şununla değiştir:

```kotlin
                    TabItem("plan", "Gün Planı"),
                    TabItem("docs", "Belgeler"),
                    TabItem("packing", "Çanta"),
                    TabItem("giderler", "Giderlerim")
```

- [ ] **Step 4: Giderlerim tab içeriğini ekle**

`SeyahatScreen` içindeki son tab bloğunun hemen ardından şunu ekle:

```kotlin
            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = seyahatSummary, accent = Lav)
            }
```

`Lav` zaten import edilmiş — kontrol et, yoksa şunu ekle: `import com.simay.lifebank.ui.theme.Lav`

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/SeyahatScreen.kt
git commit -m "feat(giderler): SeyahatScreen — Giderlerim sekmesi eklendi"
```

---

## Task 5: SaglikScreen.kt — Giderlerim sekmesi ekle

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/SaglikScreen.kt`

- [ ] **Step 1: Import'ları ekle**

`SaglikScreen.kt` import bloğuna şu satırları ekle:

```kotlin
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
import com.simay.lifebank.ui.theme.Rose
```

Not: `Rose` import'u yoksa ekle; zaten varsa atla.

- [ ] **Step 2: Mock veri ekle**

`SaglikScreen` composable içinde, `var tab by remember { mutableStateOf("takvim") }` satırının hemen altına şunu ekle:

```kotlin
    val saglikSummary = remember {
        ExpenseSummary(
            totalAmount = 2180,
            changePercent = -5,
            topCategory = "Eczane",
            topAmount = 840,
            categories = listOf(
                ExpenseCategory("Eczane",     840, 6, 39, Rose),
                ExpenseCategory("Doktor",     720, 3, 33, Rose.copy(alpha = 0.7f)),
                ExpenseCategory("Diş Hekimi", 480, 2, 22, Rose.copy(alpha = 0.5f)),
                ExpenseCategory("Spor",       140, 4,  6, Rose.copy(alpha = 0.35f))
            ),
            merchants = listOf(
                ExpenseMerchant("Eczacıbaşı",    "Eczane",     840, 6),
                ExpenseMerchant("Acıbadem Klnk.", "Doktor",     720, 2),
                ExpenseMerchant("Diş Kliniği",   "Diş Hekimi", 480, 2),
                ExpenseMerchant("Fit Life",      "Spor",       140, 4)
            )
        )
    }
```

- [ ] **Step 3: GlassTabs listesine "Giderlerim" ekle**

Dosyada şu bloğu bul:

```kotlin
                    TabItem(id = "takvim", label = "Takvim"),
                    TabItem(id = "ilac", label = "İlaçlar"),
                    TabItem(id = "kapsam", label = "Kapsam")
```

Şununla değiştir:

```kotlin
                    TabItem(id = "takvim", label = "Takvim"),
                    TabItem(id = "ilac", label = "İlaçlar"),
                    TabItem(id = "kapsam", label = "Kapsam"),
                    TabItem(id = "giderler", label = "Giderlerim")
```

- [ ] **Step 4: Giderlerim tab içeriğini ekle**

`SaglikScreen` içindeki son tab bloğunun hemen ardından şunu ekle:

```kotlin
            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = saglikSummary, accent = Rose)
            }
```

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/SaglikScreen.kt
git commit -m "feat(giderler): SaglikScreen — Giderlerim sekmesi eklendi"
```

---

## Task 6: AilemScreen.kt — Giderlerim sekmesi ekle

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/AilemScreen.kt`

- [ ] **Step 1: Import'ları ekle**

`AilemScreen.kt` import bloğuna şu satırları ekle:

```kotlin
import com.simay.lifebank.ui.components.ExpenseCategory
import com.simay.lifebank.ui.components.ExpenseMerchant
import com.simay.lifebank.ui.components.ExpenseSummary
import com.simay.lifebank.ui.components.GiderlerTab
import com.simay.lifebank.ui.theme.Teal
```

Not: `Teal` zaten import edilmişse tekrar ekleme.

- [ ] **Step 2: Mock veri ekle**

`AilemScreen` composable içinde, `var tab by remember { mutableStateOf("butce") }` satırının hemen altına şunu ekle:

```kotlin
    val ailemSummary = remember {
        ExpenseSummary(
            totalAmount = 7920,
            changePercent = 15,
            topCategory = "Eğitim/Okul",
            topAmount = 3800,
            categories = listOf(
                ExpenseCategory("Eğitim/Okul",  3800,  2, 48, Teal),
                ExpenseCategory("Market",       2640, 14, 33, Teal.copy(alpha = 0.7f)),
                ExpenseCategory("Giyim",         980,  4, 12, Teal.copy(alpha = 0.5f)),
                ExpenseCategory("Oyuncak/Kitap", 500,  3,  6, Teal.copy(alpha = 0.35f))
            ),
            merchants = listOf(
                ExpenseMerchant("Özel Okul",   "Eğitim",  3800, 1),
                ExpenseMerchant("Migros",      "Market",  1640, 8),
                ExpenseMerchant("CarrefourSA", "Market",  1000, 6),
                ExpenseMerchant("LC Waikiki",  "Giyim",    980, 4),
                ExpenseMerchant("Toyzzshop",   "Oyuncak",  500, 3)
            )
        )
    }
```

- [ ] **Step 3: GlassTabs listesine "Giderlerim" ekle**

Dosyada şu bloğu bul:

```kotlin
                    TabItem(id = "butce", label = "Bütçe"),
                    TabItem(id = "hedef", label = "Hedefler"),
                    TabItem(id = "cocuk", label = "Çocuk")
```

Şununla değiştir:

```kotlin
                    TabItem(id = "butce", label = "Bütçe"),
                    TabItem(id = "hedef", label = "Hedefler"),
                    TabItem(id = "cocuk", label = "Çocuk"),
                    TabItem(id = "giderler", label = "Giderlerim")
```

- [ ] **Step 4: Giderlerim tab içeriğini ekle**

`AilemScreen` içindeki son tab bloğunun hemen ardından şunu ekle:

```kotlin
            // ---- TAB: Giderlerim ----
            if (tab == "giderler") {
                GiderlerTab(summary = ailemSummary, accent = Teal)
            }
```

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/AilemScreen.kt
git commit -m "feat(giderler): AilemScreen — Giderlerim sekmesi eklendi"
```

---

## Task 7: Full Build

- [ ] **Step 1: APK derle**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug 2>&1 | tail -8
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 2: Commit plan dosyası**

```bash
git add docs/superpowers/plans/2026-04-15-giderlerim.md
git commit -m "docs(plan): Giderlerim implementation plan"
```
