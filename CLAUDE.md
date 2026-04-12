# Project Rules for Claude

## Design System — ZORUNLU

Yeni bir Composable, ekran veya UI değişikliği yaparken **önce `STYLE.md`** oku. Bu dosyada tanımlı olmayan hiçbir renk/tipografi/spacing değerini kodda kullanma.

- Renk: sadece `YK.baseColorXxx` (veya `LightColor` / `DarkColor` mapping). `Color(0xFF…)` **yasak** (glass/overlay efektleri hariç, gerekçelendir).
- Tipografi: sadece `MaterialTheme.typography.xxx` + extension'lar (`.bold`, `.semiBold`, `.medium`, `.light`, `.color()`, `.links`). Font ailesi Ubuntu (`ubuntuFonts`), caption için `captionFonts`.
- Size/line-height: `TextSpacing.text_size_XX` sabitleri; literal `14.sp` yok.
- Spacing/Radius: `Spacing.*` / `Radius.*` token'ları.
- Light + Dark tema'da doğru görünmesini sağla.

Yeni bir semantic renk gerekirse: önce `YKColors` interface'ine ekle → `LightColor` + `DarkColor` implementations'da map'le → `STYLE.md`'yi güncelle → sonra kullan.

Checklist: `STYLE.md` §10.
