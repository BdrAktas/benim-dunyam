# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

Gradle 8.14.3, Kotlin 1.9.22, Compose BOM 2024.02.00. minSdk 26, target/compileSdk 34. Java 17.

The project requires the Android Studio JBR; the system `java` will fail with "Unable to locate a Java Runtime". Set before every Gradle invocation:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew compileDebugKotlin   # fastest Kotlin-only compile check
./gradlew assembleDebug        # full debug APK
./gradlew installDebug         # install on connected device/emulator
./gradlew lint                 # Android lint
./gradlew test                 # unit tests (none exist yet)
```tasarımcı

There is one module (`:app`). No test suites exist — no "run a single test" workflow to document.

## Architecture

Single-activity Jetpack Compose app. Everything hangs off `app/src/main/java/com/simay/lifebank/`.

- **`MainActivity.kt`** — hosts `LifeBankApp()`. Owns the `NavHost`, the `accentColor` transition (animates background per route, 600ms `FastOutSlowInEasing`), the full-screen decorative radial-gradient background, and the fixed bottom nav (`GlassBottomBar`). `enableEdgeToEdge()` is on; each screen manages its own `statusBarsPadding()`.
- **`ui/navigation/AppNavigation.kt`** — `Screen` sealed class + `bottomTabs` list. Seven routes: home, evim, finans, seyahat, aracim, saglik, ailem.
- **`ui/screens/`** — one file per route. `HomeScreen` is the most complex (navy hero panel, horizontal card scroller, Benim Dünyam grid, smart feed). Sub-screens follow a lighter pattern: `DomainHeader` → content column.
- **`ui/components/`** — shared UI primitives. `GlassComponents.kt` is the core building block: `GlassSurface` (frosted card with accent border), `GlassButton`, `GlassIntensity` enum (Subtle/Normal/Strong), `animateCountUp` helper. `CommonComponents.kt` has `DomainHeader`.
- **`ui/theme/`** — the design token layer. `Color.kt` (YKB palette + legacy aliases + domain accents), `Type.kt` (`YkbType` scale wired into `AppTypography`), `Tokens.kt` (`Spacing`, `Radius`, `Elevation`), `Theme.kt` (`LifeBankTheme`).
- **`ui/util/Formatters.kt`** — `formatTRY(amount)` Turkish Lira formatter; used everywhere money is shown.

## Conventions Worth Knowing

**Design system is mandatory — read `STYLE.md` before any UI work.** Rules (from the previous CLAUDE.md, still in force):

- Colors: only `YK.baseColor*` tokens (or `LightColor`/`DarkColor` maps). `Color(0xFF…)` is forbidden outside glass/overlay effects (and must be justified).
- Typography: only `MaterialTheme.typography.*` + extensions (`.bold`, `.semiBold`, `.medium`, `.light`, `.color()`, `.links`). Font family is Ubuntu (`ubuntuFonts`), caption uses `captionFonts`. No literal `14.sp`; use `TextSpacing.text_size_XX`.
- Spacing / radius: `Spacing.*` / `Radius.*` tokens only.
- Must render correctly in Light **and** Dark themes.
- New semantic color → add to `YKColors` interface → map in `LightColor` + `DarkColor` → update `STYLE.md` → then use. Checklist in `STYLE.md` §10.

**Domain-per-color convention**: each of the six sub-domains has an `Accent*` pale color (`AccentEvim`, `AccentAracim`, `AccentSaglik`, `AccentSeyahat`, `AccentAilem`, `AccentFinans`). Changing route animates the background gradient in MainActivity to that accent. Don't introduce a new domain color ad-hoc — extend the pattern.

**Glass morphism**: the aesthetic is frosted white surfaces over tinted backgrounds. `GlassSurface` with `GlassIntensity` is the default card primitive; don't roll new semi-transparent boxes when this composable fits.

**Locale is Turkish**: all UI strings, domain names (Ev, Finans, Seyahat, Aracım, Sağlık, Ailem), currency formatting (`formatTRY`). Match the tone when adding copy.

**Status bar**: each screen draws its own top inset. `HomeScreen` extends the navy hero gradient behind the status bar via a fixed `windowInsetsTopHeight(WindowInsets.statusBars)` strip + `statusBarsPadding()` on the scroll container — so scrolled content is clipped below the clock. Other screens apply `.statusBarsPadding()` at the root. Don't put `statusBarsPadding` on the `NavHost`; it breaks the hero-bleed pattern.
