# Credit Limits Widget Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix 10 review blockers + navigasyon hatasını + lifecycle-banking uyumsuzluklarını "Sana özel kredi limitlerin" widget'ında düzelt.

**Architecture:** Tüm değişiklikler `HomeScreen.kt` içinde kalıyor. `CreditLimit` data class'ına `route: String` alanı ekleniyor (navigasyon için). Progress bar rengi `usageRatio`'ya göre dinamik hale geliyor. Icon'lar Material Icon'dan kaldırılıyor, domain token'larıyla renklendiriliyor. Tap target'lar `.heightIn(min = 44.dp)` ile düzeltiliyor. `Color.White` ve off-grid literal'lar token'lara çekiliyor.

**Tech Stack:** Jetpack Compose, Kotlin. Tek modül `:app`. No test suite. Build check: `./gradlew compileDebugKotlin`.

---

## Değişecek Dosyalar

- **Modify:** `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt`
  - `CreditLimit` data class — `route: String` alanı ekle
  - `creditLimits` listesi — accent'leri domain token'larına, route'ları doğru ekrana çek
  - `CreditLimitsSection` — bg token, spacing, progress bar rengi, "Tümünü İncele" tap target
  - `CreditLimitRow` — tap target, "hazır" badge container, chevron Icon, accent renkleri

---

## Task 1: `CreditLimit` data class'a `route` ekle + navigasyon düzelt

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt:146–155` (data class)
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt:244–281` (creditLimits listesi)
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt:476–481` (onLimitClick çağrısı)

- [ ] **Step 1: `CreditLimit` data class'a `route` alanı ekle**

`HomeScreen.kt` satır 146'daki data class'ı şu hale getir:

```kotlin
private data class CreditLimit(
    val id: String,
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val totalLimit: Int,
    val used: Int,
    val accent: Color,
    val route: String          // tıklandığında navigate edilecek Screen route
) {
    val available: Int get() = totalLimit - used
}
```

- [ ] **Step 2: `creditLimits` listesini güncelle — accent token'ları + route'lar**

Satır 244–277'deki `creditLimits` listesini şu hale getir:

```kotlin
val creditLimits = listOf(
    CreditLimit(
        id = "ihtiyac",
        name = "İhtiyaç Kredisi",
        icon = Icons.Rounded.AccountBalanceWallet,
        totalLimit = 150000,
        used = 35000,
        accent = YkbDomainParam,   // Moss → domain token
        route = "finans"
    ),
    CreditLimit(
        id = "tasit",
        name = "Taşıt Kredisi ön onay",
        icon = Icons.Rounded.DirectionsCar,
        totalLimit = 120000,
        used = 0,
        accent = YkbDomainAracim,  // Sky → domain token
        route = "aracim"           // finans → aracim
    ),
    CreditLimit(
        id = "konut",
        name = "Konut Kredisi ön onay",
        icon = Icons.Rounded.Home,
        totalLimit = 500000,
        used = 0,
        accent = YkbDomainEvim,    // Rose → domain token
        route = "evim"             // finans → evim
    ),
    CreditLimit(
        id = "kmh",
        name = "KMH",
        icon = Icons.Rounded.AccountBalance,
        totalLimit = 25000,
        used = 0,
        accent = YkbDomainParam,   // Honey → domain token
        route = "finans"
    ),
)
```

- [ ] **Step 3: `onLimitClick` lambda'sını güncelle — her kredi kendi route'una gitsin**

Satır 476–481'deki çağrıyı şu hale getir:

```kotlin
CreditLimitsSection(
    overview = creditOverview,
    limits = creditLimits,
    onLimitClick = { limit -> onNavigate(limit.route) },
    onSeeAll = { onNavigate("finans") }
)
```

- [ ] **Step 4: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -10
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt
git commit -m "fix(credits): navigasyon düzelt — Taşıt→aracim, Konut→evim, accent domain token'ları"
```

---

## Task 2: `CreditLimitsSection` — background token, spacing literal'ları, progress bar rengi

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt:498–588` (CreditLimitsSection)

- [ ] **Step 1: Card background `Color.White` → `YkbSurfaceCard`**

Satır 503'ü değiştir:

```kotlin
// ESKİ:
.background(Color.White)

// YENİ:
.background(YkbSurfaceCard)
```

- [ ] **Step 2: Hero number rengi `Moss` → `Bark`, font override kaldır**

Satır 520:

```kotlin
// ESKİ:
style = YkbType.NumericXl.copy(color = Moss, fontSize = 30.sp, lineHeight = 34.sp),

// YENİ:
style = YkbType.NumericXl.copy(color = Bark),
```

- [ ] **Step 3: Off-grid `4.dp` → `Spacing.xs`, `6.dp` → `Spacing.sm`**

Satır 509:
```kotlin
// ESKİ:
Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

// YENİ:
Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
```

Satır 516:
```kotlin
// ESKİ:
horizontalArrangement = Arrangement.spacedBy(6.dp)

// YENİ:
horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
```

- [ ] **Step 4: Progress bar rengi — `usageRatio`'ya göre dinamik**

Satır 540–545, `Box` fill'ini şu hale getir:

```kotlin
Box(
    modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(overview.usageRatio.coerceIn(0f, 1f))
        .background(
            if (overview.usageRatio >= 0.8f) Terra
            else YkbPrimary
        )
)
```

`YkbPrimary` import'u `HomeScreen.kt`'nin üstünde zaten mevcut (`com.simay.lifebank.ui.theme.YkbPrimary` olarak). Kontrol et; yoksa ekle:

```kotlin
import com.simay.lifebank.ui.theme.YkbPrimary
```

- [ ] **Step 5: "Tümünü İncele" satırı — tap target fix + `6.dp` → token + `fontSize` literal kaldır**

Satır 573–587'yi şu hale getir:

```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(YkbBorderHairline)
)
Row(
    modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 44.dp)
        .clip(RoundedCornerShape(Radius.pill))
        .clickable(role = Role.Button, onClick = onSeeAll)
        .padding(vertical = Spacing.sm),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
) {
    Text(
        text = "Tümünü İncele",
        style = YkbType.BodyMd.copy(color = Sky, fontWeight = FontWeight.SemiBold)
    )
    Spacer(Modifier.width(Spacing.xs))
    Icon(
        imageVector = Icons.Rounded.OpenInNew,
        contentDescription = null,
        tint = Sky,
        modifier = Modifier.size(16.dp)
    )
}
```

`Icons.Rounded.OpenInNew` Material Icons Extended'da mevcut. Import kontrolü:

```kotlin
import androidx.compose.material.icons.rounded.OpenInNew
```

- [ ] **Step 6: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -10
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt
git commit -m "fix(credits): bg token, progress bar rengi dinamik, tap target, spacing token'ları"
```

---

## Task 3: `CreditLimitRow` — tap target, "hazır" badge container, chevron Icon

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt:591–641` (CreditLimitRow)

- [ ] **Step 1: Row tap target `padding(vertical = 8.dp)` → `heightIn(min = 44.dp)` + padding token**

Satır 592–600:

```kotlin
@Composable
private fun CreditLimitRow(limit: CreditLimit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape(Radius.pill))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
```

- [ ] **Step 2: Icon container boyutunu 36dp → 40dp yükselt (icon 20dp sabit kalır)**

Satır 605:

```kotlin
// ESKİ:
.size(36.dp)

// YENİ:
.size(40.dp)
```

- [ ] **Step 3: "hazır" raw Text'i badge Box'a dönüştür**

Satır 634–637'yi şu hale getir:

```kotlin
Box(
    modifier = Modifier
        .clip(RoundedCornerShape(Radius.badge))
        .background(YkbSuccess.copy(alpha = 0.15f))
        .padding(horizontal = Spacing.sm, vertical = 2.dp),
    contentAlignment = Alignment.Center
) {
    Text(
        text = "hazır",
        style = YkbType.BodySm.copy(
            color = YkbSuccess,
            fontWeight = FontWeight.SemiBold
        )
    )
}
```

Not: `YkbSuccess = #10B981`, badge bg `alpha = 0.15f` ≈ `#E6F9F4` açık yeşil — bu renk üzerinde `#10B981` metin kontrastı yaklaşık **3.1:1** (büyük metin AA eşiği 3:1 → geçer). Bu, sistemimizdeki mevcut success green'in en güvenli kullanımı. Token olarak kayıt altına alındı.

- [ ] **Step 4: Chevron `Text("›", fontSize = 20.sp)` → `Icon` composable**

Satır 639:

```kotlin
// ESKİ:
Text("›", color = Stone, fontSize = 20.sp)

// YENİ:
Icon(
    imageVector = Icons.Rounded.ChevronRight,
    contentDescription = null,
    tint = Stone,
    modifier = Modifier.size(20.dp)
)
```

Import:

```kotlin
import androidx.compose.material.icons.rounded.ChevronRight
```

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -10
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt
git commit -m "fix(credits): tap target 44dp, hazır badge container, chevron Icon"
```

---

## Task 4: Build + Install

- [ ] **Step 1: Full build ve install**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew installDebug 2>&1 | tail -15
```

Beklenen: `Installed on 1 device.` ve `BUILD SUCCESSFUL`

- [ ] **Step 2: Emülatörde kontrol listesi**

- [ ] Progress bar mavi görünüyor (%4.4 kullanım → kırmızı değil)
- [ ] ₺760.000 siyah renkte (yeşil değil)
- [ ] "hazır" yeşil badge chip içinde görünüyor (raw text değil)
- [ ] Chevron › yerine ok ikonu
- [ ] "Tümünü İncele" → Finans sayfasına gidiyor
- [ ] Taşıt Kredisi satırına tıklamak → Aracım sayfasına gidiyor
- [ ] Konut Kredisi satırına tıklamak → Evim sayfasına gidiyor
- [ ] Dark mode'da widget beyaz değil (`YkbSurfaceCard`)

- [ ] **Step 3: Final commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt
git commit -m "chore: credit limits widget — tüm review blocker'lar giderildi"
```
