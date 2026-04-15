# AI Asistan Ekranı — Tasarım Spesifikasyonu

**Onay tarihi:** 2026-04-15

---

## Problem

Magazin kartlarındaki içerik (eventLine, metric) yapay zeka tarafından kişiselleştirilmiş olacak. Kullanıcının bu AI varlığını hissetmesi, etkileşime geçmesi ve sesli soru sorabilmesi için ayrı bir tam sayfa deneyim gerekiyor.

---

## Çözüm: AiAssistantScreen

Orb badge'e tıklanınca açılan tam sayfa ekran. İki katmanlı layout: üst yarı orb + gradient, alt yarı domain insights panel. Aralarında yüzen mikrofon FAB.

---

## Mimari

- **Yeni dosya:** `app/src/main/java/com/simay/lifebank/ui/screens/AiAssistantScreen.kt`
- **Route:** `Screen.AiAssistant("ai_assistant")` — `AppNavigation.kt`'e eklenir, bottom tab'da görünmez
- **Entry point:** `MagazineCoverCard` header'ındaki `AiOrbBadge` tıklanınca `onNavigate("ai_assistant")` çağrılır — kart `onClick`'inden bağımsız, ayrı tıklama alanı
- **Geçiş:** `slideInVertically + fadeIn`, geri: `slideOutVertically + fadeOut`
- **Çıkış:** Sol üst `←` geri butonu veya Android back gesture → HomeScreen

---

## Görsel Düzen

### Üst Katman — %45

- **Arka plan:** `Brush.verticalGradient(YkbNavyDeep, YkbNavySoft, Color(0xFF3D1A6B))`
  - Navy'den mora geçiş — orb renkleriyle uyumlu
  - Token notu: `0xFF3D1A6B` için `YkbNavyPurple` token önerilir, `Color.kt` + `STYLE.md` güncellenmeli
- **Orb:** `AiOrbBadge` — mevcut composable, `size = 160.dp` parametresiyle
- **Geri butonu:** Sol üst, `Icons.Rounded.ArrowBack`, beyaz tint, `48dp` tap target

### Orb FAB — İki Katman Kesişiminde

- `56dp` daire, `YkbSurfaceCard` fill, `Elevation.card` shadow
- İkon: `Icons.Rounded.Mic`, `YkbNavyDeep` tint
- Konumlandırma: üst katmanın alt kenarından `−28dp` (yarısı her katmanda)
- **Dinliyor durumu:** FAB `Terra` background, `Icons.Rounded.Stop` ikonu

### Alt Panel — %55

- `YkbSurfaceCard` background
- Üst köşeler `RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)`
- İç padding: `Spacing.lg`
- **Başlık:** `"Bugün sana özel"` — `YkbType.Heading2`, `Bark`
- **İçerik:** `AiInsightCard` listesi, dikey scroll
- **Transcript alanı:** Mikrofon aktifken başlığın altında `animateContentSize` ile kayarak açılır

---

## Bileşenler

### AiOrbBadge (mevcut — genişletilir)

```kotlin
@Composable
fun AiOrbBadge(modifier: Modifier = Modifier, size: Dp = 26.dp)
```

`size` parametresi eklenir. Animasyonlar aynı kalır.

### AiInsightCard

Her domain için bir kart. Mevcut `MagazineCoverCard` içeriğinden türetilir.

```
┌─────────────────────────────────────┐
│ ▌ [domain ikonu]  Domain Adı        │
│   Olay metni (eventLine)            │
│   ₺Metrik  metricLabel              │
│                    [Domain'e Git →] │
└─────────────────────────────────────┘
```

- Sol: `4dp` domain accent bar (`drawBehind`)
- Background: `YkbSurfaceCard`
- Border: `1dp YkbBorderHairline`
- Radius: `Radius.card`
- Tıklanınca: `onNavigate(domain.route)`

### Transcript Alanı

- Background: `YkbCanvas`
- Radius: `Radius.card`
- Kullanıcı sorusu: `YkbType.BodyMd`, `Bark`, sağa hizalı
- AI yanıtı: `YkbType.BodyMd`, `Stone`, sola hizalı
- Şimdilik: hardcoded mock yanıt — gerçek AI entegrasyonu sonraki sprint

---

## Mikrofon Durumları

| Durum | FAB | Orb | Transcript |
|---|---|---|---|
| Bekleme | Beyaz + navy mic ikonu | Normal pulse (1.1s) | Gizli |
| Dinliyor | `Terra` + stop ikonu | Hızlı pulse (0.5s), rotasyon 1.2s | Açık, kullanıcı metni yazıyor |
| Yanıt | Beyaz + mic ikonu | Normal pulse | Kullanıcı + AI yanıtı görünür |

Şimdilik ses tanıma gerçek değil — mikrofon tıklanınca mock bir soru/yanıt akışı gösterilir.

---

## Token Gereksinimleri

| Token | Değer | Neden |
|---|---|---|
| `YkbNavyPurple` | `#3D1A6B` | Gradient bitiş rengi — AI ekranına özel, navy-purple geçişi |

`Color.kt` + `STYLE.md` §10 akışı: `YKColors` interface → `LightColor` + `DarkColor` map → kullan.

---

## Etkilenen Dosyalar

| Dosya | Değişiklik |
|---|---|
| `ui/screens/AiAssistantScreen.kt` | Yeni dosya |
| `ui/navigation/AppNavigation.kt` | `Screen.AiAssistant` + NavHost composable |
| `ui/screens/HomeScreen.kt` | `AiOrbBadge` — `size` parametresi eklenir, `onOrbClick` callback eklenir |
| `ui/theme/Color.kt` | `YkbNavyPurple` token |

---

## Kapsam Dışı (Sonraki Sprint)

- Gerçek ses tanıma (SpeechRecognizer API)
- Gerçek AI backend entegrasyonu
- KVKK aydınlatma metni + rıza akışı
- Kişiselleştirme motoru (eventLine'ı gerçek kullanıcı verisinden üretme)
