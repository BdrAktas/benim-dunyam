# AI Asistan Ekranı Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Magazin kartlarındaki orb badge'e tıklanınca açılan tam sayfa AI asistan ekranını uçtan uca inşa et.

**Architecture:** İki katmanlı layout — üst %45 navy→mor gradient + 160dp orb, alt %55 beyaz panel + domain insight kartları. Aralarında yüzen mikrofon FAB. Navigasyon: `HomeScreen` orb tıklanınca `onNavigate("ai_assistant")` → yeni `AiAssistantScreen`. Mock mikrofon durumları (IDLE → LISTENING → ANSWERED) coroutine delay ile simüle edilir.

**Tech Stack:** Jetpack Compose, Kotlin, Jetpack Navigation. Tek modül `:app`. Build check: `./gradlew compileDebugKotlin`.

---

## Değişecek / Oluşacak Dosyalar

| Dosya | İşlem |
|---|---|
| `app/src/main/java/com/simay/lifebank/ui/theme/Color.kt` | `YkbNavyPurple` token ekle |
| `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt` | `AiOrbBadge` → `internal`, `size` parametresi; `MagazineCoverCard` → `onOrbClick`; `BenimDunyamMagazine` wire |
| `app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt` | `Screen.AiAssistant` route ekle |
| `app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt` | **Yeni dosya** — tam ekran uygulama |
| `app/src/main/java/com/simay/lifebank/MainActivity.kt` | import + NavHost composable kaydı |

---

## Task 1: `YkbNavyPurple` token — `Color.kt`

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/theme/Color.kt`

- [ ] **Step 1: Token ekle**

`Color.kt` satır 43'ün ardına (navy ramp bloğunun sonuna) ekle:

```kotlin
val YkbNavyCard = Color(0xFF0F1F3D)       // dark-matte card alternatifi (hero'dan bir ton açık)
val YkbNavyPurple = Color(0xFF3D1A6B)     // AI ekranı gradient bitiş — navy'den mora köprü
```

Yani mevcut `YkbNavyCard` satırının hemen altına tek satır ekle:

```kotlin
val YkbNavyPurple = Color(0xFF3D1A6B)     // AI ekranı gradient bitiş — navy'den mora köprü
```

- [ ] **Step 2: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/theme/Color.kt
git commit -m "feat(token): YkbNavyPurple — AI ekranı gradient bitiş rengi"
```

---

## Task 2: `AiOrbBadge` genişlet + `MagazineCoverCard` wire — `HomeScreen.kt`

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt`

- [ ] **Step 1: `AiOrbBadge` — `private` kaldır, `size` + `onClick` ekle**

Satır 1068–1109'daki mevcut composable'ı şu hale getir:

```kotlin
@Composable
internal fun AiOrbBadge(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 26.dp,
    onClick: (() -> Unit)? = null
) {
    val inf = rememberInfiniteTransition(label = "aiOrb")
    val rotation by inf.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Restart),
        label = "orbRot"
    )
    val pulse by inf.animateFloat(
        initialValue = 0.88f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "orbPulse"
    )
    val clickableModifier = if (onClick != null)
        Modifier.clickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onClick)
    else Modifier

    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer { scaleX = pulse; scaleY = pulse }
            .then(clickableModifier)
    ) {
        rotate(rotation) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF7B2FF7),
                        Color(0xFFE040FB),
                        Color(0xFF00D4FF),
                        Color(0xFF40C4FF),
                        Color(0xFF7B2FF7),
                    )
                )
            )
        }
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.58f), Color.Transparent),
                center = Offset(size.width * 0.30f, size.height * 0.22f),
                radius = size.minDimension * 0.36f
            )
        )
    }
}
```

Not: `size` parametresi `Canvas` içinde `Modifier.size(size)` olarak kullanılıyor; `drawCircle` canvas boyutuna göre otomatik ölçeklenir.

- [ ] **Step 2: `MagazineCoverCard` — `onOrbClick` parametresi ekle**

Satır 1113–1116 aralığındaki fonksiyon imzasını şu hale getir:

```kotlin
private fun MagazineCoverCard(
    world: WorldCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onOrbClick: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
```

- [ ] **Step 3: `MagazineCoverCard` gövdesinde `AiOrbBadge` çağrısını güncelle**

Satır 1181'deki `AiOrbBadge()` çağrısını şu hale getir:

```kotlin
AiOrbBadge(onClick = onOrbClick)
```

- [ ] **Step 4: `BenimDunyamMagazine` içindeki `MagazineCoverCard` çağrısını güncelle**

`BenimDunyamMagazine` içindeki `MagazineCoverCard(...)` çağrısını bul (satır ~984) ve `onOrbClick` parametresini ekle:

```kotlin
MagazineCoverCard(
    world = world,
    modifier = Modifier
        .fillMaxWidth()
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        },
    onClick = { onNavigate(world.ctaRoute ?: world.id) },
    onOrbClick = { onNavigate("ai_assistant") },
    onNavigate = onNavigate
)
```

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt
git commit -m "feat(orb): AiOrbBadge size+onClick param, MagazineCoverCard onOrbClick wire"
```

---

## Task 3: Route — `AppNavigation.kt`

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt`

- [ ] **Step 1: `Screen.AiAssistant` ekle**

`AppNavigation.kt`'deki `sealed class Screen` bloğuna `AiAssistant` ekle:

```kotlin
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Evim : Screen("evim")
    data object Finans : Screen("finans")
    data object Seyahat : Screen("seyahat")
    data object Aracim : Screen("aracim")
    data object Saglik : Screen("saglik")
    data object Ailem : Screen("ailem")
    data object AiAssistant : Screen("ai_assistant")
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
git add app/src/main/java/com/simay/lifebank/ui/navigation/AppNavigation.kt
git commit -m "feat(nav): Screen.AiAssistant route ekle"
```

---

## Task 4: `AiAssistantScreen.kt` — Yeni Dosya

**Files:**
- Create: `app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt`

- [ ] **Step 1: Dosyayı oluştur**

```kotlin
package com.simay.lifebank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Radius
import com.simay.lifebank.ui.theme.Spacing
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.theme.Terra
import com.simay.lifebank.ui.theme.YkbBorderHairline
import com.simay.lifebank.ui.theme.YkbCanvas
import com.simay.lifebank.ui.theme.YkbDomainAilem
import com.simay.lifebank.ui.theme.YkbDomainAracim
import com.simay.lifebank.ui.theme.YkbDomainEvim
import com.simay.lifebank.ui.theme.YkbDomainParam
import com.simay.lifebank.ui.theme.YkbDomainSaglik
import com.simay.lifebank.ui.theme.YkbDomainSeyahat
import com.simay.lifebank.ui.theme.YkbNavyDeep
import com.simay.lifebank.ui.theme.YkbNavyPurple
import com.simay.lifebank.ui.theme.YkbNavySoft
import com.simay.lifebank.ui.theme.YkbSurfaceCard
import com.simay.lifebank.ui.theme.YkbType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─── Durum makinesi ───────────────────────────────────────────────────────────
private enum class MicState { IDLE, LISTENING, ANSWERED }

// ─── Domain insight veri modeli ───────────────────────────────────────────────
private data class AiInsight(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val accent: Color,
    val route: String,
    val eventLine: String,
    val metric: String,
    val metricLabel: String
)

private val aiInsights = listOf(
    AiInsight("evim",    "Evim",       Icons.Rounded.Home,         YkbDomainEvim,    "evim",
        "Doğalgaz son gün, ödenmedi",    "₺847",    "aylık fatura tutarın"),
    AiInsight("aracim",  "Aracım",     Icons.Rounded.DirectionsCar, YkbDomainAracim,  "aracim",
        "Kaskon 15 gün sonra bitiyor",   "₺8.500",  "tahmini yenileme primin"),
    AiInsight("seyahat", "Seyahatim",  Icons.Rounded.Flight,        YkbDomainSeyahat, "seyahat",
        "Antalya tatiline 18 gün kaldı", "₺28.500", "tahmini tatil bütçen"),
    AiInsight("saglik",  "Sağlığım",   Icons.Rounded.MonitorHeart,  YkbDomainSaglik,  "saglik",
        "Check-up randevun 34 gün sonra","%82",     "yıllık sigortandan kalan"),
    AiInsight("ailem",   "Ailem",      Icons.Rounded.Groups,        YkbDomainAilem,   "ailem",
        "Üniversite harcına 18 gün kaldı","₺8.500", "harç için eksik tutarın"),
    AiInsight("param",   "Param",      Icons.Rounded.AccountBalance,YkbDomainParam,   "finans",
        "Sınırsız hesap ile her gün kazan","%52",   "e-Mevduat güncel faiz"),
)

// Mock soru-cevap — gerçek AI entegrasyonu sonraki sprint
private val mockQuestion = "Bu ay araç sigortam ne kadar?"
private val mockAnswer   = "Kaskonun bu ay ₺8.500 yenileme tutarı var. 15 gün içinde bitiyor. Teklif almak ister misin?"

// ─── Ana ekran ────────────────────────────────────────────────────────────────
@Composable
fun AiAssistantScreen(onBack: () -> Unit, onNavigate: (String) -> Unit) {
    var micState by remember { mutableStateOf(MicState.IDLE) }
    val scope = rememberCoroutineScope()

    val fabBg by animateColorAsState(
        targetValue = if (micState == MicState.LISTENING) Terra else YkbSurfaceCard,
        animationSpec = tween(300),
        label = "fabBg"
    )
    val fabIconTint by animateColorAsState(
        targetValue = if (micState == MicState.LISTENING) Color.White else YkbNavyDeep,
        animationSpec = tween(300),
        label = "fabIconTint"
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Üst gradient katman — %45 ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(
                    Brush.verticalGradient(
                        listOf(YkbNavyDeep, YkbNavySoft, YkbNavyPurple)
                    )
                )
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            AiOrbBadge(size = 160.dp)

            // Geri butonu — sol üst
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(Spacing.sm)
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(role = Role.Button, onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Geri",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // ── Alt beyaz panel — %55 ────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(YkbSurfaceCard)
                    .padding(
                        top = Spacing.xxl + Spacing.md, // FAB (28dp overlap) + nefes
                        start = Spacing.lg,
                        end = Spacing.lg,
                        bottom = Spacing.lg
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Bugün sana özel",
                    style = YkbType.Heading2.copy(color = Bark, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(Spacing.md))

                // Transcript — mikrofon aktifken açılır
                AnimatedVisibility(
                    visible = micState != MicState.IDLE,
                    enter = expandVertically() + fadeIn(tween(250)),
                    exit  = shrinkVertically() + fadeOut(tween(200))
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Radius.card))
                                .background(YkbCanvas)
                                .padding(Spacing.md),
                            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            if (micState == MicState.LISTENING) {
                                Text(
                                    text = "Dinliyorum...",
                                    style = YkbType.BodySm.copy(color = Stone),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                // Kullanıcı sorusu — sağa hizalı
                                Text(
                                    text = mockQuestion,
                                    style = YkbType.BodyMd.copy(
                                        color = Bark,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                                // AI yanıtı — sola hizalı
                                Text(
                                    text = mockAnswer,
                                    style = YkbType.BodyMd.copy(color = Stone),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(Modifier.height(Spacing.md))
                    }
                }

                // Domain insight kartları
                aiInsights.forEach { insight ->
                    AiInsightCard(
                        insight = insight,
                        onClick = { onNavigate(insight.route) }
                    )
                    Spacer(Modifier.height(Spacing.sm))
                }
            }

            // Mikrofon FAB — panelin üst kenarına 28dp overlap
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-28).dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(fabBg)
                    .clickable(role = Role.Button) {
                        when (micState) {
                            MicState.IDLE, MicState.ANSWERED -> {
                                micState = MicState.LISTENING
                                scope.launch {
                                    delay(2000L)
                                    micState = MicState.ANSWERED
                                }
                            }
                            MicState.LISTENING -> micState = MicState.IDLE
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (micState == MicState.LISTENING)
                        Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = if (micState == MicState.LISTENING)
                        "Durdur" else "Sesli Soru Sor",
                    tint = fabIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

// ─── Domain Insight Card ──────────────────────────────────────────────────────
@Composable
private fun AiInsightCard(insight: AiInsight, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.card))
            .background(YkbSurfaceCard)
            .border(1.dp, YkbBorderHairline, RoundedCornerShape(Radius.card))
            .drawBehind {
                drawRect(
                    color = insight.accent,
                    size = Size(4.dp.toPx(), size.height)
                )
            }
            .clickable(role = Role.Button, onClick = onClick)
            .padding(
                start = Spacing.md + 4.dp,
                end = Spacing.md,
                top = Spacing.md,
                bottom = Spacing.md
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        // Domain ikon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(Radius.iconBg))
                .background(insight.accent.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = insight.icon,
                contentDescription = null,
                tint = insight.accent,
                modifier = Modifier.size(18.dp)
            )
        }

        // Olay metni
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = insight.label,
                style = YkbType.BodySm.copy(color = Stone)
            )
            Text(
                text = insight.eventLine,
                style = YkbType.BodyMd.copy(
                    color = Bark,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 2
            )
        }

        // Metrik
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = insight.metric,
                style = YkbType.BodyMd.copy(
                    color = insight.accent,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = insight.metricLabel,
                style = YkbType.BodySm.copy(color = Stone),
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Stone,
            modifier = Modifier.size(16.dp)
        )
    }
}
```

- [ ] **Step 2: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | grep -E "^e:|error:|BUILD"
```

Beklenen: `BUILD SUCCESSFUL` — hata varsa `^e:` satırları görünür, düzelt.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt
git commit -m "feat(ai): AiAssistantScreen — orb + domain insights + mock mikrofon"
```

---

## Task 5: `MainActivity.kt` — NavHost kaydı

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/MainActivity.kt`

- [ ] **Step 1: Import ekle**

`MainActivity.kt`'nin import bloğuna ekle (satır ~86, diğer screen importlarının yanına):

```kotlin
import com.simay.lifebank.ui.screens.AiAssistantScreen
```

- [ ] **Step 2: NavHost'a composable ekle**

`MainActivity.kt`'deki `NavHost { ... }` bloğunda, `composable(Screen.Ailem.route)` bloğunun hemen ardına ekle:

```kotlin
composable(Screen.AiAssistant.route) {
    AiAssistantScreen(
        onBack = { navController.popBackStack() },
        onNavigate = { route ->
            activePanel = null
            navController.navigate(route) { launchSingleTop = true }
        }
    )
}
```

- [ ] **Step 3: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | grep -E "^e:|error:|BUILD"
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/MainActivity.kt
git commit -m "feat(nav): AiAssistantScreen NavHost kaydı"
```

---

## Task 6: Build + Install + Kontrol

- [ ] **Step 1: Full build ve install**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew installDebug 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL` + `Installed on 1 device.`

- [ ] **Step 2: Emülatörde kontrol listesi**

- [ ] Ana sayfada magazin kartı üstünde orb badge görünüyor
- [ ] Orb'a tıklanınca yeni tam sayfa açılıyor (nav bar kaybolmuyor — bottom bar HomeScreen'de kaldı)
- [ ] Üst yarı: navy→mor gradient + 160dp dönen orb
- [ ] Sol üst: `←` geri butonu çalışıyor, HomeScreen'e döner
- [ ] Alt yarı: "Bugün sana özel" başlığı + 6 domain insight kartı
- [ ] Her insight kartına tıklanınca doğru domain ekranına gider
- [ ] Mikrofon FAB iki katmanın kesişiminde yüzüyor
- [ ] FAB tıklanınca kırmızıya döner, "Dinliyorum..." gösterir
- [ ] 2 saniye sonra mock soru + yanıt transcript'te belirir
- [ ] FAB tekrar beyaza döner

- [ ] **Step 3: Final commit**

```bash
git add -A
git commit -m "chore: AI asistan ekranı — tüm görevler tamamlandı"
```
