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

// Contextual
val YkbStepPurple = Color(0xFF9B59B6)
val YkbWorldpayPurple = Color(0xFF6B21A8)
val YkbSuccess = Color(0xFF10B981)
val YkbInfoIcon = YkbPrimary

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

// Domain accent backgrounds (pale tints aligned with YKB accents)
val AccentEvim = YkbPrimaryPale
val AccentAracim = Color(0xFFD1FADF)
val AccentSaglik = Color(0xFFFCE1E4)
val AccentSeyahat = Color(0xFFEDE4FE)
val AccentAilem = Color(0xFFFDE3CC)
val AccentFinans = Color(0xFFFEF3C7)
val AccentDefault = YkbNeutral100

// App background
val BgBase = YkbBgApp
