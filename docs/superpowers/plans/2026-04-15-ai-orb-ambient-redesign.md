# AI Orb Ambient Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Halo'yu orb Canvas'ından ayırarak üst paneli dolduran özgür-hareket eden bir `AmbientLayer` ekle.

**Architecture:** `AiOrbBadge.kt`'den Layer 1 (dış halo) kaldırılır. `AiAssistantScreen.kt`'ye `AmbientLayer` private composable eklenir — `fillMaxSize()` Canvas içinde 4 bağımsız blob, farklı periyotlar ve faz offset'leriyle eliptik yörüngede döner. Blob'lar Canvas kenarlarına clip'lenmez → sınırsız hissi.

**Tech Stack:** Jetpack Compose, Kotlin, `androidx.compose.foundation.Canvas`, `kotlin.math`. Tek modül `:app`. Build check: `./gradlew compileDebugKotlin`.

---

## Değişecek Dosyalar

| Dosya | İşlem |
|---|---|
| `app/src/main/java/com/simay/lifebank/ui/components/AiOrbBadge.kt` | Layer 1 (dış halo) kaldır |
| `app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt` | `AmbientLayer` ekle + üst Box'a wire et + gerekli import'lar |

---

## Task 1: `AiOrbBadge.kt` — Layer 1 kaldır

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/components/AiOrbBadge.kt:106–123`

- [ ] **Step 1: Layer 1 bloğunu sil**

`AiOrbBadge.kt` satır 106–123 arasındaki şu bloğu **tamamen sil**:

```kotlin
            // ── Katman 1: Dış halo — clip dışında, orb sınırından taşar ────
            val haloSlip   = r * 0.30f
            val haloCx     = cx + (cos(hueRad) * haloSlip).toFloat()
            val haloCy     = cy + (sin(hueRad) * haloSlip).toFloat()
            val haloRadius = r * orbBreathe * 2.2f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        YkbPrimary.copy(alpha = 0.18f),
                        YkbNavyPurple.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(haloCx, haloCy),
                    radius = haloRadius
                ),
                radius = haloRadius,
                center = Offset(haloCx, haloCy)
            )
```

Satır 125'teki `// Katman 2–8 orb çemberine clip'lenir` yorumu ve `val orbClip = ...` satırı yerinde kalır.

- [ ] **Step 2: `orbBreathe` ve `orbHueDrift` animasyonları artık kullanılmıyor — kaldır**

`AiOrbBadge.kt` satır 57–74'teki şu iki `animateFloat` bloğunu **sil**:

```kotlin
    // Halo nefes — büyüklük nabzı
    val orbBreathe by inf.animateFloat(
        initialValue = 0.88f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(3200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "orbBreathe"
    )
```

ve

```kotlin
    // Halo merkezi kayması — 14s (orbDrift'ten farklı → faz kilidi yok)
    val orbHueDrift by inf.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "orbHueDrift"
    )
```

- [ ] **Step 3: `orbBreathe` ve `orbHueDrift`'e bağlı `hueRad` hesaplamasını kaldır**

Canvas bloğu içinde `val driftRad = ...` satırının hemen altındaki şu satırı sil:

```kotlin
            val hueRad   = orbHueDrift * PI / 180.0
```

- [ ] **Step 4: Artık kullanılmayan import'ları temizle**

`AiOrbBadge.kt` import bloğunda `FastOutSlowInEasing` hâlâ `orbGlow` tarafından kullanılıyor — **kaldırma**. `LinearEasing` hâlâ `orbDrift` ve `orbShimmer`'da kullanılıyor — **kaldırma**. Hiçbir import değişmez; bu adım yalnızca teyit içindir.

- [ ] **Step 5: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/components/AiOrbBadge.kt
git commit -m "refactor(orb): Layer 1 dış halo kaldırıldı — AmbientLayer'a taşınacak"
```

---

## Task 2: `AiAssistantScreen.kt` — `AmbientLayer` ekle ve wire et

**Files:**
- Modify: `app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt`

- [ ] **Step 1: Eksik import'ları ekle**

`AiAssistantScreen.kt`'nin import bloğuna şu satırları ekle (mevcut import'ların hemen altına, alfabetik sıraya dikkat etmek zorunda değilsin — sadece ekle):

```kotlin
import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.geometry.Offset
import com.simay.lifebank.ui.theme.YkbAccentPurple
import com.simay.lifebank.ui.theme.YkbIridescentRose
import com.simay.lifebank.ui.theme.YkbPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
```

Not: `YkbNavyPurple` zaten import'lanmış — tekrar ekleme.

- [ ] **Step 2: `AmbientLayer` composable'ı ekle**

`AiAssistantScreen.kt`'de `private enum class MicState` satırından **hemen önce** şu composable'ı ekle:

```kotlin
@Composable
private fun AmbientLayer() {
    val inf = rememberInfiniteTransition(label = "ambient")

    val drift1 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift1"
    )
    val drift2 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(13000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift2"
    )
    val drift3 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(19000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift3"
    )
    val drift4 by inf.animateFloat(
        initialValue = 0f, targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift4"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w    = size.width
        val h    = size.height
        val cx   = w / 2f
        val cy   = h / 2f
        val xAmp = w * 0.30f
        val yAmp = h * 0.20f

        val phase1 = 0f
        val phase2 = (2.0 * PI / 3.0).toFloat()   // 120°
        val phase3 = (4.0 * PI / 3.0).toFloat()   // 240°
        val phase4 = (PI / 3.0).toFloat()          //  60°

        // Blob 1 — YkbPrimary, en büyük, 8s
        val b1x = cx + cos(drift1 + phase1) * xAmp
        val b1y = cy + sin(drift1 + phase1) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbPrimary.copy(alpha = 0.22f), Color.Transparent),
                center = Offset(b1x, b1y),
                radius = w * 0.70f
            ),
            radius = w * 0.70f,
            center = Offset(b1x, b1y)
        )

        // Blob 2 — YkbNavyPurple, 13s
        val b2x = cx + cos(drift2 + phase2) * xAmp
        val b2y = cy + sin(drift2 + phase2) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbNavyPurple.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(b2x, b2y),
                radius = w * 0.60f
            ),
            radius = w * 0.60f,
            center = Offset(b2x, b2y)
        )

        // Blob 3 — YkbAccentPurple, 19s
        val b3x = cx + cos(drift3 + phase3) * xAmp
        val b3y = cy + sin(drift3 + phase3) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbAccentPurple.copy(alpha = 0.15f), Color.Transparent),
                center = Offset(b3x, b3y),
                radius = w * 0.50f
            ),
            radius = w * 0.50f,
            center = Offset(b3x, b3y)
        )

        // Blob 4 — YkbIridescentRose, 11s
        val b4x = cx + cos(drift4 + phase4) * xAmp
        val b4y = cy + sin(drift4 + phase4) * yAmp
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(YkbIridescentRose.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(b4x, b4y),
                radius = w * 0.40f
            ),
            radius = w * 0.40f,
            center = Offset(b4x, b4y)
        )
    }
}
```

- [ ] **Step 3: Üst Box'a `AmbientLayer` ekle**

`AiAssistantScreen.kt` içinde üst paneli oluşturan `Box` bloğunu bul. Şu hale getir:

```kotlin
        // ── Üst gradient katman — %45 ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(
                    Brush.verticalGradient(listOf(YkbNavyDeep, YkbNavySoft, YkbNavyPurple))
                )
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            AmbientLayer()

            AiOrbBadge(size = 160.dp)

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
```

`AmbientLayer()` satırı `AiOrbBadge`'den **önce** gelmelidir — arka planda kalır.

- [ ] **Step 4: Derleme kontrolü**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin 2>&1 | tail -5
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt
git commit -m "feat(ai): AmbientLayer — 4 bağımsız blob üst paneli özgürce dolduruyor"
```

---

## Task 3: Full Build

- [ ] **Step 1: APK derle**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug 2>&1 | tail -8
```

Beklenen: `BUILD SUCCESSFUL`

- [ ] **Step 2: Emülatörde kontrol listesi**

- [ ] Üst panelde 4 blob görünüyor, birbirinden bağımsız hareket ediyor
- [ ] Bloblar panelin kenarlarına kadar uzanıyor (sınırda clip yok)
- [ ] Orb merkezde sabit, etrafında özgür ışık hareketi var
- [ ] Yatay hareket dikey hareketten belirgin biçimde daha geniş
- [ ] Geri butonu ve orb'un üzerinde beliriyor (z-order doğru)
