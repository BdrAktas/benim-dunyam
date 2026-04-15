package com.simay.lifebank.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────
// YKB Mobile Design Tokens (Nisan 2026)
// ─────────────────────────────────────────────────────────────

// Primary / Marka
val YkbPrimary = Color(0xFF1E9BD7)
val YkbPrimaryDark = Color(0xFF1578A8)
val YkbPrimaryLight = Color(0xFF5BB8E8)
val YkbPrimaryPale = Color(0xFFD6EEF8)

// Accent
val YkbAccentRed = Color(0xFFE8302A)
val YkbAccentOrange = Color(0xFFF5821F)
val YkbAccentPurple = Color(0xFF8B5CF6)
val YkbAccentBlueLink = YkbPrimary

// Neutral
val YkbNeutral900 = Color(0xFF111827)
val YkbNeutral700 = Color(0xFF374151)
val YkbNeutral500 = Color(0xFF6B7280)
val YkbNeutral300 = Color(0xFFD1D5DB)
val YkbNeutral100 = Color(0xFFF3F4F6)
val YkbWhite = Color(0xFFFFFFFF)

// Background
val YkbBgApp = Color(0xFFF0F4F8)
val YkbBgCard = YkbWhite
val YkbBgHeader = YkbPrimary

// Canvas / Surface — banking-grade cool neutrals (Revolut / Things 3 pattern)
val YkbCanvas = Color(0xFFF7F9FC)         // cool near-white page canvas (replaces warm YkbBgApp)
val YkbSurfaceCard = YkbWhite             // card bg on light canvas
val YkbBorderHairline = Color(0xFFEAEEF4) // 0.5–1dp card borders

// Navy hero ramp (tepe paneli ve status bar bleed için)
val YkbNavyDeep = Color(0xFF0A1F4A)
val YkbNavyMid = Color(0xFF14306B)
val YkbNavySoft = Color(0xFF1E4590)
val YkbNavyCard = Color(0xFF0F1F3D)       // dark-matte card alternatifi (hero'dan bir ton açık)
val YkbNavyPurple = Color(0xFF3D1A6B)     // AI ekranı gradient bitiş — navy'den mora köprü

// Magazine-stack — full-saturation domain backgrounds
// White text on each of these passes WCAG AA ≥ 4.5:1:
//   Evim    4.92:1   Aracim  5.36:1   Saglik  7.27:1
//   Seyahat 9.24:1   Ailem   5.27:1
val YkbDomainEvim = Color(0xFF1578A8)
val YkbDomainAracim = Color(0xFF1E7A33)
val YkbDomainSaglik = Color(0xFF9B2C4F)
val YkbDomainSeyahat = Color(0xFF5B2A9E)
val YkbDomainAilem = Color(0xFFB54A2E)
val YkbDomainParam = Color(0xFF7A5200)   // deep amber — altın/mevduat/yatırım; white AAA ~7.2:1

// Kart brand gradient'leri (Adios / Bonus)
val YkbCardPurple1 = Color(0xFF3A1A6B)
val YkbCardPurple2 = Color(0xFF5B2A9E)
val YkbCardPurple3 = Color(0xFF7B3FC9)
val YkbCardGreen1 = Color(0xFF0F4A1E)
val YkbCardGreen2 = Color(0xFF1E7A33)
val YkbCardGreen3 = Color(0xFF2DA04A)
// Premium gold — Vakıfbank Platinum
val YkbCardGold1 = Color(0xFF4A2800)
val YkbCardGold2 = Color(0xFF7A4800)
val YkbCardGold3 = Color(0xFFAA6A00)

// Contextual
val YkbStepPurple = Color(0xFF9B59B6)
val YkbWorldpayPurple = Color(0xFF6B21A8)
val YkbSuccess = Color(0xFF10B981)
val YkbInfoIcon = YkbPrimary
val YkbAccentHighlight = Color(0xFFFFD166) // marquee / tease highlight

// ─────────────────────────────────────────────────────────────
// Legacy aliases → remapped to YKB tokens so existing usages
// across screens/components re-skin consistently.
// ─────────────────────────────────────────────────────────────

// Glass system (kept — used by GlassComponents)
val GlassWhite = Color.White.copy(alpha = 0.42f)
val GlassDark = Color.White.copy(alpha = 0.22f)
val GlassUltra = Color.White.copy(alpha = 0.62f)
val GlassBorder = Color.White.copy(alpha = 0.5f)

// Text tones
val Bark = YkbNeutral900
val BarkMid = YkbNeutral700
val Stone = YkbNeutral500
val Pebble = YkbNeutral300

// Brand accents remapped to YKB palette
val Moss = YkbSuccess
val Terra = YkbAccentRed
val Sky = YkbPrimary
val Lav = YkbAccentPurple
val Honey = YkbAccentOrange
val Rose = YkbWorldpayPurple
val Teal = Color(0xFF0891B2)  // Ailem domain — sıcak teal, aile hissini yansıtır

// Domain accent backgrounds (pale tints aligned with YKB accents)
val AccentEvim = YkbPrimaryPale
val AccentAracim = Color(0xFFD1FADF)
val AccentSaglik = Color(0xFFFCE1E4)
val AccentSeyahat = Color(0xFFEDE4FE)
val AccentAilem = Color(0xFFCCEEF6)  // teal pale tint
val AccentFinans = Color(0xFFFEF3C7)
val AccentDefault = YkbNeutral100

// App background — serin nötr kanvas (navy hero ile aynı sıcaklıkta)
val BgBase = YkbCanvas

// AI Orb — dekoratif token, semantic interface'e eklenmez
val YkbIridescentRose = Color(0xFFC084FC)  // orb üçüncü blob; YkbAccentPurple→YkbPrimaryLight köprüsü
