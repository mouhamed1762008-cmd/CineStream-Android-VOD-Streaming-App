# CineStream — Premium Android VOD Streaming Platform

A production-grade, immersive Android Video On Demand (VOD) streaming application crafted with Jetpack Compose, Material Design 3, Media3 ExoPlayer, and Room Database persistence, delivering a cinematic entertainment experience.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following key decisions were confirmed during the clarification phase:
> - **Brand Identity**: CineStream — Sleek, cinematic VOD platform with high visual fidelity.
> - **Color Scheme**: Deep Black (`#080A10`), Secondary Surface (`#11141D`), and Vibrant Electric Purple (`#8B5CF6` / `#A78BFA`) accent theme.
> - **Video Streaming Engine**: Android Media3 ExoPlayer streaming verified open-source HD cinematics (Blender Foundation productions such as *Tears of Steel*, *Sintel*, *Cosmos Laundromat*, and *Big Buck Bunny*) with fallback streams and multi-bitrate support.

---

## 1. Overview & Core Concept

**CineStream** is designed as a top-tier Android streaming client inspired by leading entertainment platforms (Netflix, Disney+, Prime Video) while maintaining its own distinct visual identity. The app delivers:
- Personalized onboarding where users enter and store their profile name.
- Rich content discovery across movies, series, genres, trending feeds, and curated recommendations.
- Interactive details view with season/episode breakdown for series, full cast credits, and related recommendations.
- Custom-built full-screen Media3 ExoPlayer video playback with adaptive gesture/touch controls, 10s skip, speed adjustment, subtitle/audio toggles, and Next Episode auto-chaining.
- Persistent user data via Room & DataStore (Watchlist, Continue Watching with exact playback timestamps, and user profile preferences).

---

## 2. User Experience & Visual Design

### Key User Flows

1. **Onboarding Flow**:
   - First launch displays a cinematic gradient backdrop with CineStream typography and branding.
   - User enters their name in a styled M3 text field and taps "Get Started".
   - Stored in persistent DataStore/Room; transitions smoothly to the Home Screen.

2. **Browsing & Discovery Flow**:
   - **Home**: Dynamic top hero banner with "Play Now" and "More Info", followed by horizontal carousels: *Continue Watching*, *Trending Now*, *Popular Movies*, *Popular Series*, *Action & Adventure*, *Comedy*, and *Drama*.
   - **Movies Screen**: Featured cinematic spotlight, 9 interactive genre pill filters (*Action*, *Adventure*, *Comedy*, *Drama*, *Horror*, *Sci-Fi*, *Romance*, *Thriller*, *Animation*), and sorting dropdown (*Popularity*, *Rating*, *Release Year*).
   - **Series Screen**: Dedicated series discovery, season badges, episode counts, and genre filtering.
   - **Search Screen**: Instant query matching against titles, synopsis, genres, and cast members, augmented with clickable "Trending Searches" and recent search query chips.

3. **Details & Content Inspection Flow**:
   - Immersive hero backdrop with deep vertical gradient fading into app background.
   - Floating poster card, rating badge, maturity rating, duration, and genre badges.
   - Action row: Big "Play" button, "+ Watchlist" toggle with animated state change, and Share intent trigger.
   - Series tab: Interactive Season selector tabs updating the episode list with thumbnails, durations, and direct episode play triggers.
   - "More Like This" recommendation grid.

4. **Cinematic Player Flow**:
   - Clean, full-bleed landscape/portrait video playback powered by Media3 ExoPlayer.
   - Tap overlay reveals transport controls: Play/Pause, ±10s seek buttons, interactive timeline scrubber, elapsed and remaining time codes.
   - Top bar with title, back button, PiP (Picture-in-Picture) trigger, and settings (playback speed 0.5x–2.0x, audio & subtitles modal).
   - Series episode jumper: "Next Episode" button and overlay countdown.
   - Bottom navigation automatically hides during playback.

5. **Profile & Library Management Flow**:
   - User avatar card, greeting, and "Edit Profile" dialog to change user name.
   - Watchlist gallery with remove and play actions.
   - "Continue Watching" cards showing progress bars and remaining time.
   - Settings switches (Stream Quality, Autoplay Previews, App Theme, Notifications).

### Visual Identity & Theme Tokens

- **Atmosphere**: Deep cinema darkness with subtle glassmorphic surfaces and high-contrast typography.
- **Color Palette**:
  - `Background`: `#080A10` (true deep black)
  - `Surface / Card`: `#11141D` (dark navy slate)
  - `Surface Variant / Sheet`: `#1A1E2B` (elevated container)
  - `Primary Accent`: `#8B5CF6` (electric purple)
  - `Primary Variant`: `#A78BFA` (soft lavender purple)
  - `Accent Glow`: `#7C3AED` (deep violet)
  - `Rating Gold`: `#FBBF24`
  - `Text Primary`: `#F8FAFC` (pure crisp white)
  - `Text Secondary`: `#94A3B8` (slate gray)
- **Typography**: Bold display headers (headlineLarge / titleMedium) paired with clean geometric body text.
- **Edge-to-Edge**: Full system bar integration with transparent navigation and status bars respecting `WindowInsets`.

---

## 3. Key Product Decisions & Trade-Offs

1. **State Persistence (Room + DataStore)**:
   - *Chosen Approach*: Room Database for structured lists (`WatchlistEntity`, `ContinueWatchingEntity`, `PlaybackHistoryEntity`) and Jetpack DataStore for profile settings (`userName`, `playbackQuality`, `themePref`).
   - *Why*: Allows instant offline responsiveness, zero login friction, and durable user progress that survives app restarts.
   - *Alternatives Considered*: Firebase Auth/Firestore was evaluated, but local persistence avoids unwanted cloud auth gating and fits the on-device user profile requirement.

2. **Media3 ExoPlayer Integration**:
   - *Chosen Approach*: `androidx.media3.exoplayer` paired with Compose `AndroidView` holding a `PlayerView` and a custom Compose overlay UI.
   - *Why*: Provides hardware-accelerated H.264/HLS playback, custom UI overlays with Compose animations, and standard Android lifecycle management (pausing on background, releasing on dispose).

3. **Rich Sample Content Repository**:
   - *Chosen Approach*: A comprehensive, beautifully curated in-memory dataset of 30+ movies and series spanning various genres with real HD streaming URLs (Blender Studio open films: *Tears of Steel*, *Sintel*, *Cosmos Laundromat*, *Big Buck Bunny*, *Charge*), high-resolution poster art, cast lists, and episodic structures.
   - *Why*: Guarantees instant, reliable playback and discovery without requiring third-party API keys that could rate-limit or fail.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                              MainActivity                              │
│                                   │                                    │
│                     CineStreamApp (Scaffold & Nav)                     │
│               Floating Bottom Navigation (5 Tabs)                      │
└────────────────────────────────────────────────────────────────────────┘
          │                                                    │
┌─────────────────────────┐                        ┌─────────────────────┐
│      Screens Layer      │                        │  Player Experience  │
├─────────────────────────┤                        ├─────────────────────┤
│ 1. OnboardingScreen     │                        │ 7. VideoPlayerScreen│
│ 2. HomeScreen           │                        │    - Media3 Exo     │
│ 3. SearchScreen         │                        │    - Custom Controls│
│ 4. SeriesScreen         │                        │    - Timeline Scrub │
│ 5. MoviesScreen         │                        │    - Episode Jumper │
│ 6. DetailsScreen        │                        │    - Audio/Speed    │
│ 8. ProfileScreen        │                        └─────────────────────┘
└─────────────────────────┘                                    ▲
          │                                                    │
          ▼                                                    │
┌──────────────────────────────────────────────────────────────┴─────────┐
│                      ViewModels & State Layer                          │
│   • MainViewModel (Active Tab, User Profile, Current Route)            │
│   • ContentViewModel (Catalog, Filters, Search Query, Categories)      │
│   • WatchlistViewModel (Watchlist & Continue Watching State)           │
│   • PlayerViewModel (ExoPlayer State, Progress, Speed, Next Episode)   │
└────────────────────────────────────────────────────────────────────────┘
          │                                                    │
          ▼                                                    ▼
┌───────────────────────────────┐           ┌────────────────────────────┐
│      Data & Storage Layer     │           │     Mock Streaming API     │
│ • Room DB: Watchlist, History │           │ • Curated Movies & Series  │
│ • DataStore: User Preferences │           │ • Stream URLs (HLS / MP4)  │
└───────────────────────────────┘           └────────────────────────────┘
```

### Data Entities & Schema

1. **`MovieOrSeries`**:
   - `id: String`, `title: String`, `type: ContentType (MOVIE / SERIES)`
   - `overview: String`, `releaseYear: Int`, `rating: Double`, `ageRating: String`, `duration: String`
   - `genres: List<String>`, `posterUrl: String`, `backdropUrl: String`, `videoUrl: String`
   - `cast: List<CastMember>`, `director: String`, `seasons: List<Season>?`

2. **`Season` & `Episode`**:
   - `seasonNumber: Int`, `episodes: List<Episode>`
   - `Episode`: `id: String`, `episodeNumber: Int`, `title: String`, `duration: String`, `overview: String`, `thumbnailUrl: String`, `videoUrl: String`

3. **Room Database Entities**:
   - `WatchlistItem`: `id: String`, `title: String`, `posterUrl: String`, `rating: Double`, `type: String`, `addedAt: Long`
   - `ContinueWatchingItem`: `id: String`, `title: String`, `backdropUrl: String`, `positionMs: Long`, `durationMs: Long`, `episodeTitle: String?`, `lastWatchedAt: Long`
   - `SearchHistoryItem`: `id: Int`, `query: String`, `searchedAt: Long`

---

## 5. Step-by-Step Implementation Strategy

1. **Build Configuration & Dependencies**:
   - Add `androidx.media3:media3-exoplayer:1.5.1` and `androidx.media3:media3-ui:1.5.1` to `gradle/libs.versions.toml`.
   - Uncomment `coil-compose`, `androidx-navigation-compose`, `androidx-datastore-preferences` in `app/build.gradle.kts`.
   - Set app name "CineStream" in `metadata.json` and `res/values/strings.xml`.
2. **Design System & Theming**:
   - Define custom M3 color scheme in `Color.kt` and `Theme.kt` with `#080A10` deep black, `#11141D` card surface, and `#8B5CF6` electric purple accent.
   - Vector drawables & icons for video playback and streaming branding.
3. **Domain Models & Content Catalog**:
   - Create models for Content, Season, Episode, Cast, Genres.
   - Build extensive catalog with high-quality imagery and valid playable video streams.
4. **Data Persistence**:
   - Room Entities, DAOs, and `CineStreamDatabase` for Watchlist and Continue Watching.
   - Preference Repository for user name and settings.
5. **UI Screen Implementations**:
   - Screen 1: `OnboardingScreen` (name entry, animated backdrop).
   - Screen 2: `HomeScreen` (hero banner, greeting, 10 carousels).
   - Screen 3: `SearchScreen` (search input, suggestions, results grid, empty state).
   - Screen 4: `SeriesScreen` (series discovery, season count badges, filters).
   - Screen 5: `MoviesScreen` (movie discovery, 9 genre filters, sorting).
   - Screen 6: `DetailsScreen` (cinematic backdrop, metadata, cast, series episode list).
   - Screen 7: `VideoPlayerScreen` (full-screen Media3 ExoPlayer, custom overlay, skip, speed, auto-hide).
   - Screen 8: `ProfileScreen` (user avatar, watchlist, continue watching management, settings).
   - Bottom Navigation Bar with animated indicator pills and auto-hide during playback.
6. **Verification & Testing**:
   - Run `compile_applet` to ensure pristine compilation and error-free build.
