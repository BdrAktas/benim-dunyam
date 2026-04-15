# AI Orb Ambient Redesign — Design Spec

**Date:** 2026-04-15  
**Status:** Approved

## Problem

`AiOrbBadge` is a 160dp Canvas. The outer halo (Layer 1) is drawn within this Canvas and can only shift `r × 0.30f` from center. The result: halos look cramped instead of free-floating. The upper panel (`weight(0.45f)`) is much larger but entirely unused for ambient glow.

## Solution: Ambient Layer + Orb Separation

Split the visual into two independent composables:

```
AiAssistantScreen — Üst Panel (weight 0.45f)
├── AmbientLayer()        ← NEW: fillMaxSize Canvas, 4 independent drifting blobs
└── AiOrbBadge(160dp)     ← UNCHANGED structure, Layer 1 halo removed
```

## AmbientLayer

A new private `@Composable` in `AiAssistantScreen.kt`. Fills the top panel with a `Canvas(Modifier.fillMaxSize())`.

### 4 Independent Blobs

Each blob has its own `infiniteRepeatable` animation with a distinct period and starting phase, so they are never in sync (no phase-lock).

| Blob | Color Token | Alpha | Period | Phase Offset | Gradient Radius |
|------|-------------|-------|--------|--------------|-----------------|
| 1 | `YkbPrimary` | 0.22f | 8 000ms | 0° | panel_w × 0.70 |
| 2 | `YkbNavyPurple` | 0.18f | 13 000ms | 120° | panel_w × 0.60 |
| 3 | `YkbAccentPurple` | 0.15f | 19 000ms | 240° | panel_w × 0.50 |
| 4 | `YkbIridescentRose` | 0.12f | 11 000ms | 60° | panel_w × 0.40 |

### Orbit Shape

Elliptical — X amplitude = `panel_w × 0.30f`, Y amplitude = `panel_h × 0.20f`.  
X > Y intentionally: left-right travel is more pronounced → "freely moving horizontally" perception.

Each blob center:
```
cx = panel_w/2 + cos(phase + drift) × X_amplitude
cy = panel_h/2 + sin(phase + drift) × Y_amplitude
```

Each `drift` animates `0f → 2π` (full orbit) over its period.

### Rendering

`drawCircle` with `Brush.radialGradient` — center at blob position, `Color.Transparent` at edge.  
No `clipPath` — blobs are allowed to bleed off canvas edges for a boundless feel.

## AiOrbBadge Changes

- **Remove:** Layer 1 (outer halo) — now handled by `AmbientLayer`
- **Keep:** Layers 2–8 unchanged (base sphere, core glow, 3 blobs, glass highlight, thinking shimmer)
- Canvas size stays 160dp

## Token Usage

All ambient colors use existing tokens from `Color.kt`:
- `YkbPrimary`, `YkbNavyPurple`, `YkbAccentPurple`, `YkbIridescentRose`

No new tokens required.

## Animation Feel

- Range: 8s–19s per orbit → slow, calm, no anxiety
- Phase offsets mean blobs are always at different positions → visual variety without code complexity
- Elliptical orbit → more horizontal travel → matches user description "sağa sola yukarı aşağı rahat hareket"

## Files Changed

| File | Change |
|------|--------|
| `app/.../ui/screens/AiAssistantScreen.kt` | Add `AmbientLayer` composable; wire into top Box |
| `app/.../ui/components/AiOrbBadge.kt` | Remove Layer 1 (outer halo) |
