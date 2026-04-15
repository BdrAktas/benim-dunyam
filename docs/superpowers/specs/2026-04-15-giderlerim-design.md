# Giderlerim — Design Spec

**Date:** 2026-04-15  
**Status:** Approved  
**Scope:** 5 domain screen (Evim, Aracım, Seyahatim, Sağlığım, Ailem)

---

## Problem

Domain ekranlarında kullanıcı o yaşam alanına ait harcamalarını göremez. Aracım'daki "Maliyet" sekmesi yetersiz; diğer 4 domain'de hiç gider görünümü yok.

---

## Çözüm

Her domain ekranına **"Giderlerim"** sekmesi eklenir. Aracım'da "Maliyet" bu sekmeye dönüşür. Paylaşılan `GiderlerTab` composable, domain parametresiyle çalışır.

---

## Sekme Değişiklikleri

| Domain | Mevcut GlassTabs | Sonrası |
|--------|-----------------|---------|
| Evim | Faturalar \| Ev Değeri \| Enerji \| Sigortalar | + Giderlerim |
| Aracım | Genel \| Bakım \| **Maliyet** | Maliyet → **Giderlerim** |
| Seyahatim | Gün Planı \| Belgeler \| Çanta | + Giderlerim |
| Sağlığım | Takvim \| İlaçlar \| Kapsam | + Giderlerim |
| Ailem | Bütçe \| Hedefler \| Çocuk | + Giderlerim |

---

## Ekran Yapısı (Giderlerim sekmesi açıkken)

```
DomainHeader               ← değişmez
GlassTabs                  ← "Giderlerim" eklendi / "Maliyet" yeniden adlandırıldı

── Giderlerim içeriği ──────────────────────────────────────

1. ÖZET KART (GlassSurface, intensity=Strong, accent, glow=true)
   - "Bu ay toplam" label  → YkbType.BodySm, Stone
   - Tutar ₺X.XXX         → YkbType.NumericXl, domainAccent
   - Değişim satırı        → YkbType.BodySm; artış Terra ↑%12, düşüş Moss ↓%8
   - AnimatedProgress bar  → domainAccent, 0→usageRatio animasyonlu
   - "En yüksek: X · ₺YYY" → YkbType.BodySm, Stone; X Bold Bark

2. SEGMENTLİ KONTROL (GlassTabs, 2 sekme)
   - TabItem("kategoriler", "Kategoriler")
   - TabItem("isyerleri", "İşyerleri")
   - Aktif sekme: domainAccent bg tonu

3a. KATEGORİ LİSTESİ (tab == "kategoriler")
   Her kategori → GlassSurface(contentPadding=14dp):
   - Row: renk noktası (8dp) + kategori adı + tutar (sağ)
   - AnimatedProgress + işlem sayısı + yüzde metni
   - Sıralama: tutara göre azalan

3b. İŞYERİ LİSTESİ (tab == "isyerleri")
   Her işyeri → GlassSurface(contentPadding=14dp):
   - Row: ⬤ baş harf daire (24dp, domainAccent %12 bg) + isim + kategori GlassPill
   - Tutar bold + işlem sayısı
   - Sıralama: tutara göre azalan
```

---

## Veri Modeli

```kotlin
data class ExpenseCategory(
    val label: String,
    val amount: Int,
    val transactionCount: Int,
    val percent: Int,      // toplam içindeki yüzde
    val color: Color       // domain accent, kartlar arası alpha farklılaşır
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
```

---

## Mock Veriler (Domain başına)

### Evim
```
Toplam: ₺4.280  ↑%8 geçen aya göre
Kategoriler:
  Fatura         ₺2.840  %66  12 işlem
  Aidat          ₺850    %20   1 işlem
  İnternet       ₺350     %8   1 işlem
  Tadilat/Bakim  ₺240     %6   2 işlem
İşyerleri:
  İGDAŞ          Fatura       ₺980   2 işlem
  ENERJİSA       Fatura       ₺720   1 işlem
  İSKİ           Fatura       ₺340   2 işlem
  Yönetim Ofisi  Aidat        ₺850   1 işlem
  Turkcell       İnternet     ₺350   1 işlem
```

### Aracım  *(Maliyet sekmesinin mevcut verisi genişletilir)*
```
Toplam: ₺3.605  ↑%3 geçen aya göre
Kategoriler:
  Yakıt          ₺3.240  %90   8 işlem
  OGS/HGS        ₺245     %7   3 işlem
  Otopark        ₺120     %3   4 işlem
İşyerleri:
  Shell          Yakıt        ₺1.840  3 işlem
  BP             Yakıt        ₺980    3 işlem
  Opet           Yakıt        ₺420    2 işlem
  OGS Geçiş      OGS/HGS      ₺245    3 işlem
  İSPARK         Otopark      ₺120    4 işlem
```

### Seyahatim
```
Toplam: ₺12.450  ↑%34 geçen aya göre
Kategoriler:
  Konaklama      ₺6.800  %55   2 işlem
  Uçak/Ulaşım    ₺3.200  %26   3 işlem
  Yemek          ₺1.650  %13   9 işlem
  Aktivite       ₺800     %6   4 işlem
İşyerleri:
  Hilton Antalya Konaklama     ₺6.800  1 işlem
  Türk Hava Y.   Uçak         ₺2.400  2 işlem
  Lokantalar     Yemek        ₺1.650  9 işlem
  Pegasus        Uçak         ₺800    1 işlem
  Tekne Turu     Aktivite     ₺800    1 işlem
```

### Sağlığım
```
Toplam: ₺2.180  ↓%5 geçen aya göre
Kategoriler:
  Eczane         ₺840    %39   6 işlem
  Doktor         ₺720    %33   3 işlem
  Diş Hekimi     ₺480    %22   2 işlem
  Spor           ₺140     %6   4 işlem
İşyerleri:
  Eczacıbaşı     Eczane       ₺840    6 işlem
  Acıbadem Klnk. Doktor       ₺720    2 işlem
  Diş Kliniği    Diş Hekimi   ₺480    2 işlem
  Fit Life       Spor         ₺140    4 işlem
```

### Ailem
```
Toplam: ₺7.920  ↑%15 geçen aya göre
Kategoriler:
  Eğitim/Okul    ₺3.800  %48   2 işlem
  Market         ₺2.640  %33  14 işlem
  Giyim          ₺980    %12   4 işlem
  Oyuncak/Kitap  ₺500     %6   3 işlem
İşyerleri:
  Özel Okul      Eğitim       ₺3.800  1 işlem
  Migros         Market       ₺1.640  8 işlem
  CarrefourSA    Market       ₺1.000  6 işlem
  LC Waikiki     Giyim        ₺980    4 işlem
  Toyzzshop      Oyuncak      ₺500    3 işlem
```

---

## Bileşen Mimarisi

### Yeni: `GiderlerTab` (paylaşılan)
**Dosya:** `app/src/main/java/com/simay/lifebank/ui/components/GiderlerTab.kt`

```kotlin
@Composable
fun GiderlerTab(
    summary: ExpenseSummary,
    accent: Color
)
```

İçerir: `ExpenseSummaryCard` + iç `GlassTabs` (Kategoriler/İşyerleri) + liste.

### Yeni: `ExpenseSummaryCard` (private in GiderlerTab.kt)
Özet kart. `GlassSurface(intensity=Strong, accent, glow=true)` wrapper.

### Yeni: `ExpenseCategoryRow` (private in GiderlerTab.kt)
Tek kategori satırı. `GlassSurface` + `AnimatedProgress`.

### Yeni: `ExpenseMerchantRow` (private in GiderlerTab.kt)
Tek işyeri satırı. Baş harf daire + `GlassPill` kategori badge.

---

## Design Tokens

Yeni token gerekmez. Kullanılanlar:
- `YkbType.NumericXl`, `YkbType.BodySm`, `YkbType.BodyMd`
- `Spacing.md`, `Spacing.lg`, `Spacing.sm`, `Spacing.xs`
- `Radius.card`, `Radius.badge`
- `GlassSurface`, `GlassPill`, `AnimatedProgress`, `GlassTabs` — hepsi mevcut
- `Terra` (artış), `Moss` (düşüş), `Stone`, `Bark` — mevcut
- Domain accent'ler: `Sky`, `Moss`, `Lav`, `Rose`, `Teal` — mevcut

---

## Domain Accent Eşlemesi

| Domain | Accent | Accent Token |
|--------|--------|--------------|
| Evim | Sky | `Sky` |
| Aracım | Moss | `Moss` |
| Seyahatim | Lav | `Lav` |
| Sağlığım | Rose | `Rose` |
| Ailem | Teal | `Teal` |

---

## Dosya Değişiklikleri

| Dosya | İşlem |
|-------|-------|
| `ui/components/GiderlerTab.kt` | **Yeni** — `GiderlerTab` + private alt bileşenler |
| `ui/screens/EvimScreen.kt` | `GlassTabs`'a `TabItem("giderler","Giderlerim")` + `if (tab=="giderler")` bloğu |
| `ui/screens/AracimScreen.kt` | `"maliyet"` → `"giderler"`, label `"Maliyet"` → `"Giderlerim"`, içerik `GiderlerTab` |
| `ui/screens/SeyahatScreen.kt` | `GlassTabs`'a `TabItem("giderler","Giderlerim")` + `if (tab=="giderler")` bloğu |
| `ui/screens/SaglikScreen.kt` | `GlassTabs`'a `TabItem("giderler","Giderlerim")` + `if (tab=="giderler")` bloğu |
| `ui/screens/AilemScreen.kt` | `GlassTabs`'a `TabItem("giderler","Giderlerim")` + `if (tab=="giderler")` bloğu |

---

## Visual Spec Özeti (Design-Critic)

- **Özet kart:** `GlassSurface(Strong, glow)` — görsel ağırlık, ekranda birincil odak
- **Segmentli kontrol:** `GlassTabs` iç bileşen olarak — mevcut pattern tutarlılığı
- **Liste:** Dikey, `GlassSurface` kartlar — mobil bankacılık için taranan liste, grid değil
- **Değişim rengi:** `Terra` (artış) / `Moss` (düşüş) — anlam taşıyan, domain accent değil
- **İşyeri baş harf dairesi:** domain accent %12 bg — logosuz ama tanınabilir
- **Tap target:** her kart min 56dp yükseklik — 48dp min karşılanır

---

## PM Notu

- Veri kaynağı: Mock; gerçek implementasyonda YKB kart hareketleri + MCC kategorizasyonu
- KVKK: Harcama analizi özelliği açılışında açık rıza alınmalı (MVP dışı flag)
- "Diğer" kategorisi: Kategorilenemeyen işlemler görünür, gizlenmez
- Domain çakışması (ör. araç sigortası hem Aracım hem Finans): primary domain ataması backend'de, manuel düzeltme 2. iterasyon
