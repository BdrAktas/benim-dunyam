# Benim Dünyam — Style Guide

> **Zorunlu kaynak.** Yeni bir ekran, component veya görsel değişiklik yaparken önce bu dosyayı oku. Burada tanımlı olmayan bir renk / tipografi / spacing değerini kodda **kullanma**; gerekirse önce bu dosyayı güncelle, sonra kodu yaz.

**Kaynak:** YKB (Yapı Kredi Mobil) tasarım sistemi + Benim Dünyam ekran analizi. Light + Dark tema destekli.

---

## 1. Tipografi

### 1.1 Font Aileleri

```kotlin
// Primary — tüm UI metni
val ubuntuFonts = FontFamily(
    Font(R.font.ubuntu_r),                              // Regular
    Font(R.font.ubuntu_b, weight = FontWeight.Bold),
    Font(R.font.ubuntu_m, weight = FontWeight.Medium),
    Font(R.font.ubuntu_l, weight = FontWeight.Light)
)

// Caption — küçük/özel metinler (12sp ve altı özel durumlar)
val captionFonts = FontFamily(
    Font(R.font.caption_regular),
    Font(R.font.caption_bold, weight = FontWeight.Bold)
)
```

**Kural:**
- Varsayılan `fontFamily = ubuntuFonts`. Başka font kullanma.
- `caption` stili `captionFonts`'a bağlı — değiştirme.
- Rakamlar (bakiye, tutar) `ubuntuFonts` ile + `tabular-nums` hissiyatı için monospace alignment gerektiğinde `Modifier` / `TextStyle` üzerinden yönet.

### 1.2 Tip Skalası

Material Typography + YKB custom extension'ları:

| Token | Size | Line Height | Weight | Kullanım |
|---|---|---|---|---|
| `h0` | 48 | 56 | Regular | En büyük display (landing hero) |
| `h1` | 40 | 48 | Regular | Ekran üstü büyük başlık |
| `h2` | 36 | 44 | Regular | Modül başlığı |
| `h3` | 32 | 40 | Regular | Bölüm başlığı |
| `h4` | 28 | 36 | Regular | Kart büyük başlık |
| `h5` | 24 | 28 | Regular | Kart başlık |
| `h5_half` | 22 | 26 | Regular | Ara başlık |
| `h6` | 20 | 24 | Regular | Liste başlığı |
| `h6_half` | 19 | 23 | Regular | Ara başlık |
| `h7` | 18 | 22 | Regular | Vurgulu body |
| `h7_half` | 17 | 21 | Regular | Kart içi başlık |
| `body1` / `p1` | 16 | 24 / 20 | Regular | Ana body |
| `p2` | 15 | 20 | Regular | Secondary body |
| `body2` / `p3` | 14 | 20 / 19 | Regular | Liste öğesi, açıklama |
| `p4` | 13 | 17 | Regular | Meta / küçük açıklama |
| `caption` / `p5` | 12 | 16 | Regular | Caption, etiket |
| `p5_half` | 11 | 15 | Regular | Küçük label |
| `overline` / `p6` | 10 | 14 / 12 | Regular | En küçük etiket, badge |
| `p7` | 8 | 12 | Regular | Mikro metin |
| `button` | 14 | 20 | Bold | Buton metni |
| `subtitle1` | 14 | 20 | Regular | Alt başlık |

### 1.3 Weight Varyantları — Extension Kullan

Her stil `Light / Regular / Medium / Semibold / Bold` olarak sunulur. **Yeni `TextStyle` kopyalama** — chain-extension kullan:

```kotlin
Text("Tutar", style = MaterialTheme.typography.h5.bold)
Text("İpucu", style = MaterialTheme.typography.p2.medium.color(YK.baseColorTextSecondary))
Text("Link", style = MaterialTheme.typography.p3.semiBold.links)
Text("Overline", style = MaterialTheme.typography.caption.light.lineHeight(18.sp))
```

Mevcut extension'lar:
`.bold`, `.semiBold`, `.medium`, `.light`, `.links` (underline), `.captionFont`, `.fontSize(x)`, `.lineHeight(x)`, `.color(x)`, `.fontWeight(x)`, `.decoration(x)`

### 1.4 Sabit Değerler (`TextSpacing`)

Size/lineHeight her yerde `TextSpacing.text_size_XX` üzerinden referans edilir (`.sp` suffix'i ile). Hard-coded `14.sp` kullanma — `TextSpacing.text_size_14`.

---

## 2. Renkler

### 2.1 Mimari

```
AtomicLightColors / AtomicDarkColors   (ham HEX, private)
         ↓
LightColor / DarkColor : YKColors      (semantic mapping, public)
         ↓
YK (CompositionLocal)                  (composable'dan çağrılır)
         ↓
Text(color = YK.baseColorTextPrimary)  ← KULLANIM
```

**Kural:** `Color(0xFF…)` kullanma. Daima `YK.baseColorXxx` üzerinden eriş. Eğer yeni bir semantic renk gerekiyorsa: `YKColors` interface'ine token ekle → Light + Dark implementations'da map'le.

### 2.2 Base Palet (Atomic)

Her renk **5 ton**; `4Main` ana ton, diğerleri light→dark skala. Light & Dark aynı atomic HEX'i paylaşır (renk tonları; sadece text/component/bg katmanları temaya göre değişir).

| Ramp | 1 (lightest) | 2 | 3 | 4 (main) | 5 (darkest) |
|---|---|---|---|---|---|
| **Blue** (Primary) | #8DD8FF | #4CBEF9 | #22A9F1 | **#0588DA** | #0072BC |
| **Cyan** (Secondary) | #B0F4FF | #98EAFF | #7EE0FF | #65D6FF | #33C1FF |
| **Yellow** (Warning) | #FFE797 | #FFDD6D | #FFD13A | **#FFC400** | #FFB300 |
| **Red** (Danger) | #FF9862 | #FF8354 | #FF6D46 | **#FF5738** | #EF4323 |
| **Green** (Success) | #BAF651 | #A9EB37 | #9BE220 | **#88D406** | #7CC402 |
| **Purple** | #E7A6FF | #D777F8 | #B952DC | #9136AF | #782B90 |
| **Gray** | #D8E0ED | #AABEDB | #94A8C5 | #7E91AC | #697C98 |

### 2.3 Semantic Token'lar (`YK.baseColor…`)

#### Text
| Token | Light | Dark | Kullanım |
|---|---|---|---|
| `baseColorTextPrimary` | #292929 | #FFFFFF | Başlık, ana metin |
| `baseColorTextSecondary` | #7E91AC | #C8C8D0 | İkincil, label |
| `baseColorTextPlaceholder` | #AABEDB | #8D8D93 | Input placeholder |
| `baseColorTextButton` | #FFFFFF | #FFFFFF | Primary buton metni |
| `baseColorTextButtonSecondary` | #0588DA | #FFFFFF | Secondary buton metni |
| `baseColorTextSuccessButton` | #0588DA | #FFFFFF | Success buton metni |
| `baseColorTextWhiteOnly` | #FFFFFF | #FFFFFF | Her iki temada beyaz |

#### Component
| Token | Light | Dark | Kullanım |
|---|---|---|---|
| `baseColorComponentBox` | #FFFFFF | #282829 | Kart yüzeyi |
| `baseColorComponentHeader` | #FFFFFF | #282829 | Header bg |
| `baseColorComponentBgPrimary` | #FFFFFF | #282829 | Primary yüzey |
| `baseColorComponentBgSecondary` | #F5F7FA | #1C1C1C | Sayfa bg / secondary yüzey |
| `baseColorComponentBgGroupHeader` | #F5F7FA | #282829 | Liste group header |
| `baseColorComponentBgIcon` | #F5F7FA | #404043 | Icon placeholder bg |
| `baseColorComponentBorder` | #D8E0ED | #646468 | Kart/input border |
| `baseColorComponentBorderSecondary` | #0588DA | #FFFFFF | Vurgulu border |
| `baseColorComponentListSelected` | #E5F5FF | #404043 | Liste seçili satır |
| `baseColorComponentButtonSelected` | #002C74 | #404043 | Buton selected state |
| `baseColorComponentBgButtonSuccess` | #FFFFFF | #0588DA | Success buton bg |
| `baseColorComponentBgButtonSecondary` | transparent | transparent | Secondary buton bg |
| `baseColorComponentBgToolbarSuccess` | #22A9F1 | #000000 | Success toolbar |

#### Background
| Token | Light | Dark |
|---|---|---|
| `baseColorBg` | #FFFFFF | #000000 |
| `baseColorBgOverlay` | #062D54 | #000000 |
| `baseColorBgScreenSuccess` | #0588DA | #000000 |
| `baseColorBgScreenAbout` | #0588DA | #282829 |
| `baseColorBgNoBg` | transparent | transparent |

#### Shadow / Special
| Token | Light | Dark |
|---|---|---|
| `baseColorShadowPrimary` | #D3DCEB | #D3DCEB |
| `baseColorLightClearDarkWhite` | transparent | #FFFFFF |
| `baseColorLightGroupHeaderBgDarkClear` | #F5F7FA | transparent |

### 2.4 Kullanım Kuralları

- **Statü renkleri:**
  - Başarılı / pozitif bakiye → `baseColorGreen4Main`
  - Hata / uyarı kırmızı → `baseColorRed4Main`
  - Uyarı sarı → `baseColorYellow4Main`
  - Link / CTA → `baseColorBlue4Main` veya `baseColorTextButtonSecondary`
- **Gradient:** Blue gradient `primary3 → primary4` (`#22A9F1 → #0588DA`). Başka custom gradient tanımlama.
- **Alpha overlay:** `Color.Black.copy(alpha = 0.XX)` yerine `baseColorBgOverlay` + alpha modifier.

---

## 3. Spacing & Layout

```kotlin
object Spacing {
    val xs  = 4.dp    // ikon-metin arası
    val sm  = 8.dp    // küçük gap
    val md  = 12.dp   // default gap
    val lg  = 16.dp   // sayfa padding, kart iç padding
    val xl  = 24.dp   // bölüm ayırıcı
    val xxl = 32.dp   // büyük ayırıcı
}
```

**Sabit kurallar:**
- Sayfa yatay padding: **16dp**
- Kart iç padding: **16dp**
- İkon grid gap: **16dp**
- Minimum dokunma hedefi: **44dp**

---

## 4. Border Radius

| Token | Değer | Kullanım |
|---|---|---|
| `Radius.card` | 16dp | Kart/hesap widget |
| `Radius.button` | 24dp | CTA buton |
| `Radius.iconBg` | 12dp | İkon arka planı |
| `Radius.badge` | 6dp | Badge / chip |
| `Radius.input` | 24dp | Arama / input |

---

## 5. Elevation & Shadow

```kotlin
// Standart kart
Modifier.shadow(2.dp, RoundedCornerShape(Radius.card))
// shadow color: 0x14000000 (alpha 0.08)

// Floating / öne çıkan
Modifier.shadow(4.dp, RoundedCornerShape(Radius.card))
// shadow color: 0x1A000000 (alpha 0.10)
```

Dark tema'da shadow yerine **border** (`baseColorComponentBorder`) tercih edilir.

---

## 6. Component Rehberi

### 6.1 Button

**Primary**
- Bg: `baseColorBlue4Main` (#0588DA)
- Text: `baseColorTextButton` (#FFFFFF, `button` style — 14/20 Bold)
- Radius: 24dp
- Min height: 48dp
- Padding: horizontal 24dp, vertical 12dp
- Pressed: `baseColorBlue5` (#0072BC)
- Disabled: alpha 0.4

**Secondary (outlined)**
- Bg: transparent (`baseColorComponentBgButtonSecondary`)
- Border: 1dp `baseColorBlue4Main`
- Text: `baseColorTextButtonSecondary`

**Success (success screen context)**
- Bg: `baseColorComponentBgButtonSuccess`
- Text: `baseColorTextSuccessButton`

### 6.2 Card

- Bg: `baseColorComponentBox`
- Radius: 16dp
- Padding: 16dp
- Shadow: 2dp (light) veya border (dark)
- Başlık: `h7_half.bold` veya `h6`
- Body: `p1` / `body1`

### 6.3 List Item

- Min height: 56dp
- Padding: horizontal 16dp, vertical 12dp
- Divider: 1dp `baseColorComponentBorder` (inset: 16dp)
- Selected state: `baseColorComponentListSelected` bg
- Primary text: `p1` `baseColorTextPrimary`
- Secondary text: `p3` `baseColorTextSecondary`
- Trailing icon: 24dp, `baseColorTextSecondary`

### 6.4 Input Field

- Bg: `baseColorComponentBgSecondary`
- Radius: 24dp
- Padding: horizontal 16dp, vertical 12dp
- Border (focused): 1dp `baseColorBlue4Main`
- Text: `body1` `baseColorTextPrimary`
- Placeholder: `body1` `baseColorTextPlaceholder`
- Label (üstte): `caption.medium` `baseColorTextSecondary`

### 6.5 Badge / Chip

- Radius: 6dp
- Padding: horizontal 8dp, vertical 2dp
- Typography: `overline.bold` (10/12)
- Renk eşleşmeleri:
  - "Yeni" → bg `baseColorRed4Main`, text white
  - "YENİ" (soft) → bg `baseColorYellow4Main`, text `baseColorTextPrimary`
  - Success → bg `baseColorGreen4Main`, text white
  - Info → bg `baseColorBlue1`, text `baseColorBlue5`

### 6.6 Header / Toolbar

- Bg: `baseColorComponentHeader` (standart) veya `baseColorBgScreenSuccess` (success flow)
- Height: 56dp
- Title: `h6.bold` — light tema: `baseColorTextPrimary`, success tema: `baseColorTextWhiteOnly`
- Back ikon: 24dp, `baseColorTextPrimary`

### 6.7 Tab / Segment

- Active bg: `baseColorComponentBox`
- Inactive bg: transparent
- Active text: `body2.bold` `baseColorTextPrimary`
- Inactive text: `body2` `baseColorTextSecondary`
- Indicator: 2dp `baseColorBlue4Main`

### 6.8 Divider

- 1dp `baseColorComponentBorder`
- Dark tema'da `baseColorGray5` varyantı

### 6.9 Progress / Slider

- Track: `baseColorGray1` (light) / `baseColorGray4` (dark)
- Fill: context'e göre `baseColorBlue4Main` (default), `baseColorPurple4` (step), `baseColorRed4Main` (%80+ kullanım)
- Height: 4dp, radius: 2dp

### 6.10 Icon

- Default size: 24dp
- Small: 16dp, Large: 32dp
- Tint: `baseColorTextSecondary` (pasif) / `baseColorBlue4Main` (aktif) / `baseColorTextPrimary` (content)

---

## 7. Illustration / Domain Accents

Benim Dünyam modüllerinin her biri yumuşak **mavi-beyaz izometrik illüstrasyon** ile temsil edilir (evim, aracım, seyahat, finans, ailem, sağlık). Kurallar:

- İllüstrasyon rengi palet içinde kalır: `baseColorBlue1..5` + `baseColorGray1..2` + beyaz vurgu.
- Accent bg pale ton: `#D6EEF8` (evim default), diğer modüller için `Blue1` / `Cyan1` / `Purple1` pale tonları.
- Hero bg: `baseColorComponentBgSecondary` (light) veya soft gradient.

---

## 8. Motion / Interaction

- Default duration: **200ms**
- Emphasized: **300ms**
- Easing: `FastOutSlowInEasing` (giriş) / `LinearOutSlowInEasing` (çıkış)
- Pressed scale: `0.98f`
- Sayım animasyonu (bakiye): 600ms `animateCountUp`

---

## 9. Dark Mode

Her component Light + Dark implementasyonu olmalı. Sadece `YK.baseColorXxx` kullanıldığı sürece tema geçişi otomatik.

**Denetim:**
- Hard-coded `Color.White`, `Color.Black`, `Color(0x…)` → **yasak** (GlassSurface efekt overlay hariç).
- `baseColorLightClearDarkWhite` light'ta şeffaf, dark'ta beyaz → split davranış gerekiyorsa bu token kullanılır.

---

## 10. Geliştirici Checklist

Yeni bir component yazarken:

- [ ] Tüm metinler `MaterialTheme.typography.xxx` + extension üzerinden
- [ ] Tüm renkler `YK.baseColorXxx` üzerinden (hard-coded hex yok)
- [ ] Tüm dp değerleri `Spacing` / `Radius` token'larından
- [ ] Padding: sayfa 16dp, kart iç 16dp
- [ ] Font Ubuntu (caption için caption fontları)
- [ ] Light + Dark tema'da test edildi
- [ ] Min dokunma alanı 44dp
- [ ] Hard-coded `Color(…)`, `14.sp`, `16.dp` literal'leri yok

Kurallara uymayan kod **review'dan geçmez**.

---

*Son güncelleme: 2026-04-12*
