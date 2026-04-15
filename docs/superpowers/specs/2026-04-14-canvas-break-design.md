# Canvas Break — HomeScreen Hero / Magazine Geçiş Tasarımı

**Onay tarihi:** 2026-04-14
**Seçilen alternatif:** Option 1 — Canvas Break (design-critic çıktısından)

---

## Problem

Navy hero panel (`YkbNavyDeep → YkbNavySoft`) ile altındaki `BenimDunyamMagazine` bölümü arasında üç katmanlı uyumsuzluk var:
1. Materyal kontrast sertliği — net bir ton köprüsü yok
2. Yüzey dili tutarsızlığı — hero glass morph kullanıyor, magazine tam doygun domain renkleri
3. `Color.White` literal kullanımı (token ihlali)

## Çözüm: Canvas Break

Navy hero `Radius.hero = 28dp` ile biter. Altında `YkbCanvas` (#F7F9FC) zemin, üstünde `YkbSurfaceCard` (beyaz) magazine kartlar. Domain rengi yalnızca **4dp sol accent bar** ve **icon/CTA tint** olarak görünür.

---

## Değişiklikler

### 1. `MagazineCoverCard` — `HomeScreen.kt`

**Yüzey:**
- `Color.White` → `YkbSurfaceCard` (token fix)
- Shadow: `12.dp` (mevcut) korunur — derinlik için yeterli
- `drawBehind` ile 4dp sol accent bar:
  ```kotlin
  .drawBehind {
      drawRect(color = accent, size = Size(4.dp.toPx(), size.height))
  }
  ```
  Sıra: `.clip(shape)` → `.background(YkbSurfaceCard)` → `.drawBehind { bar }` → `.border(...)`

### 2. `BenimDunyamMagazine` wrapper — `HomeScreen.kt`

Magazine pager'ı saran Column'a `YkbCanvas` arka plan + top/bottom padding:
```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(YkbCanvas)
        .padding(vertical = Spacing.lg)
) { ... }
```

Bu, hero'nun `Radius.hero` köşesiyle bittiği yerde net bir açık zemin oluşturur ve göz için "yeni bölüm" sinyali verir.

### 3. Section başlığı opsiyonel
Mevcut "Benim Dünyam" başlığı yoksa eklemek gerekmez — pager dot indicator yeterli affordance sağlıyor.

---

## Token Referansı

| Kullanım | Token | Değer |
|---|---|---|
| Magazine section bg | `YkbCanvas` | `#F7F9FC` |
| Kart yüzeyi | `YkbSurfaceCard` | `#FFFFFF` |
| Sol accent bar | `world.color` (domain accent) | per-domain |
| Kart kenarlık | `YkbBorderHairline` | mevcut |
| Kart shadow | `12.dp` (mevcut) | korunur |

---

## Etkilenen Dosyalar

- `app/src/main/java/com/simay/lifebank/ui/screens/HomeScreen.kt`
  - `MagazineCoverCard` modifier zinciri
  - `BenimDunyamMagazine` wrapper Column

Başka dosya dokunulmuyor.
