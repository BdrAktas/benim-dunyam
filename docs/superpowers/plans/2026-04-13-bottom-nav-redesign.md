# Bottom Nav Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Emoji'leri Material Icons Rounded'a çevir, token ihlallerini düzelt, benimdunyam toggle affordance ekle.

**Architecture:** 2 dosya değişiyor. `AppNavigation.kt`'de `BottomTab.emoji: String` → `icon: ImageVector`. `MainActivity.kt`'de `GlassBottomBar` composable tüm hard-coded değerleri token'a çeker, `Icon()` kullanır, toggle logic'i ekler.

**Tech Stack:** Jetpack Compose, Material Icons Extended, mevcut YKB token sistemi (`YkbType`, `Spacing`, `Radius`, `Elevation`, `Color.kt`)

---

## Dosya Haritası

- **Modify:** `app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt`
  - `BottomTab.emoji: String` → `icon: ImageVector`
  - `bottomTabs` listesinde icon atamaları
- **Modify:** `app/src/main/java/com/simay/lifebank/MainActivity.kt`
  - `GlassBottomBar`: token fix + Icon() + toggle affordance
  - `QuickLaunchPanel` bg token fix

---

### Task 1: BottomTab veri modelini ImageVector'a çevir

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt`

- [ ] **Step 1: `AppNavigation.kt`'i aşağıdaki içerikle güncelle**

```kotlin
package com.simay.lifebank.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Evim : Screen("evim")
    data object Finans : Screen("finans")
    data object Seyahat : Screen("seyahat")
    data object Aracim : Screen("aracim")
    data object Saglik : Screen("saglik")
    data object Ailem : Screen("ailem")
}

data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab("home",        "Ana Sayfa",     Icons.Rounded.Home),
    BottomTab("finans",      "Finans",        Icons.Rounded.AccountBalanceWallet),
    BottomTab("benimdunyam", "Benim Dünyam",  Icons.Rounded.GridView),
)
```

- [ ] **Step 2: Compile kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | grep -E "error:|warning:|BUILD"
```

Beklenen: `error: Unresolved reference 'emoji'` — MainActivity.kt henüz güncellenmedi, bu beklenen durum.

---

### Task 2: GlassBottomBar — token fix + Icon + toggle affordance

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/MainActivity.kt` — `GlassBottomBar` composable (satır 313–386)

Token eşlemeleri:
| Eski (ihlal) | Yeni (token) |
|---|---|
| `Color(0xFFF2F4F7)` bar bg | `YkbCanvas` |
| `Color.White.copy(alpha = 0.5f)` pill | `YkbSurfaceCard` + `Elevation.card` shadow |
| `RoundedCornerShape(14.dp)` pill | `RoundedCornerShape(Radius.iconBg)` |
| `Text(tab.emoji)` + `16.sp` | `Icon(imageVector, size=22dp)` |
| `9.sp` label | `YkbType.Badge` (10sp) |
| `Bark` / `Honey` active color | `YkbPrimary` (tüm tab'lar için tutarlı) |
| `Pebble` inactive color | `Stone` (YkbNeutral500, daha okunabilir) |

Toggle affordance:
- `benimdunyam` tab'ı aktif (panel açık) iken ikon → `Icons.Rounded.KeyboardArrowDown`, label → `"Kapat"`

- [ ] **Step 1: Gerekli importları MainActivity.kt'ye ekle**

Mevcut import bloğuna ekle (alfabetik sıraya göre):
```kotlin
import androidx.compose.material.icons.rounded.KeyboardArrowDown
```

`YkbCanvas`, `YkbSurfaceCard`, `YkbPrimary`, `Stone`, `YkbType`, `Radius`, `Elevation` zaten import edilmiş veya theme paketinden geliyor — kontrol et, eksikse ekle.

- [ ] **Step 2: GlassBottomBar composable'ı tamamen değiştir**

Satır 313–386 arasındaki `GlassBottomBar` fonksiyonunu aşağıdakiyle değiştir:

```kotlin
@Composable
private fun GlassBottomBar(
    currentRoute: String?,
    activePanel: String?,
    onTabClick: (String) -> Unit
) {
    var bouncingTab by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .navigationBarsPadding()
            .clip(RoundedCornerShape(22.dp))
            .background(YkbCanvas)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            bottomTabs.forEach { tab ->
                val isPanelOpen = activePanel == "benimdunyam"
                val isActive = when (tab.route) {
                    "benimdunyam" -> isPanelOpen || currentRoute in domainSubRoutes
                    else          -> currentRoute == tab.route
                }
                val isBouncing = bouncingTab == tab.route
                val scale by animateFloatAsState(
                    targetValue = if (isBouncing) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    ),
                    label = "bounce",
                    finishedListener = { bouncingTab = null }
                )

                // Toggle affordance: panel açıkken ikon + label değişir
                val displayIcon = if (tab.route == "benimdunyam" && isPanelOpen)
                    Icons.Rounded.KeyboardArrowDown
                else
                    tab.icon
                val displayLabel = if (tab.route == "benimdunyam" && isPanelOpen)
                    "Kapat"
                else
                    tab.label

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .then(
                            if (isActive) Modifier
                                .shadow(
                                    elevation = Elevation.card,
                                    shape = RoundedCornerShape(Radius.iconBg),
                                    ambientColor = Elevation.shadowColor,
                                    spotColor = Elevation.shadowColor
                                )
                                .clip(RoundedCornerShape(Radius.iconBg))
                                .background(YkbSurfaceCard)
                            else
                                Modifier.clip(RoundedCornerShape(Radius.iconBg))
                        )
                        .clickable {
                            bouncingTab = tab.route
                            onTabClick(tab.route)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = displayIcon,
                        contentDescription = displayLabel,
                        tint = if (isActive) YkbPrimary else Stone,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = displayLabel,
                        style = YkbType.Badge.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = if (isActive) YkbPrimary else Stone
                        )
                    )
                }
            }
        }
    }
}
```

- [ ] **Step 3: QuickLaunchPanel bg token fix**

`QuickLaunchPanel` composable'daki `Color(0xFFF2F4F7)` → `YkbCanvas`:

```kotlin
// Satır ~408
.background(YkbCanvas)
```

- [ ] **Step 4: Compile + install**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew installDebug 2>&1 | tail -8
```

Beklenen: `BUILD SUCCESSFUL`, 2 cihaza yüklendi.

---

### Task 3: Kullanılmayan emoji import'larını temizle

- [ ] **Step 1: `tab.emoji` referansı kalmadıysa `AppNavigation.kt`'den eski `emoji` alanını sildiğini doğrula** (Task 1'de zaten yapıldı)

- [ ] **Step 2: `MainActivity.kt`'de kullanılmayan import var mı kontrol et**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | grep "Unused import"
```

Listelenen unused import'ları sil.

- [ ] **Step 3: Final build + commit**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug 2>&1 | tail -4
```

```bash
git add app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt \
        app/src/main/java/com/simay/lifebank/MainActivity.kt
git commit -m "feat(nav): emoji → Material Icons, token fix, benimdunyam toggle affordance"
```
