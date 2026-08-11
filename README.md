# Artificialss Showcase

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Platform: iOS](https://img.shields.io/badge/iOS-000000?logo=apple&logoColor=white)](https://developer.apple.com/ios/)

A **Compose Multiplatform** showcase application demonstrating Artificialss's development capabilities. Built with production-grade architecture and premium UI patterns to impress potential customers.

This is a public reference build: browse the code, clone it, and run it locally to see the patterns in action.

## Architecture

**Pattern:** MVP (Model-View-Presenter)

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│  Composable  │────>│   Presenter  │────>│  Repository  │
│   (View)     │<────│  (StateFlow) │<────│  (Interface) │
└─────────────┘     └──────────────┘     └──────────────┘
      UI only         Business logic       Data access
```

- **View**: Composables render state and forward events — zero logic
- **Presenter**: Extends `ViewModel`, exposes `StateFlow<UiState>`, handles all business logic
- **Repository**: Interface-first pattern; swap mock <-> remote via Koin

## Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Kotlin | 2.1.20 |
| UI | Compose Multiplatform | 1.8.0 |
| Local Storage | Room KMP | 2.7.1 |
| Charts | Custom Canvas (BarChart, LineChart, DonutChart) | - |
| Networking | Ktor Client (OkHttp engine Android, Darwin engine iOS) | 3.0.3 |
| Images | Coil 3 + Ktor | 3.1.0 |
| DI | Koin 4 (BOM) | 4.0.4 |
| GraphQL | Apollo Kotlin 4 | 4.0.1 |
| Maps (Android) | Google Maps Compose | 6.5.3 |
| Maps (iOS) | Custom Canvas interactive map | - |

## Screens

| Screen | Description |
|--------|-------------|
| **Splash** | Animated entry with fade transition, primary-colored edge-to-edge background, auto-navigates to Login |
| **Login** | Email/password with visibility toggle, "Remember me" checkbox, "Forgot password?" link, "Sign Up" prompt, loading spinner with text, session persistence (survives rotation), primary status bar |
| **Dashboard** | Unified home + analytics: balance card, quick actions, spending donut, recent transactions (preview + "See all" bottom sheet), and full analytics section with Bitcoin price chart (live API), bar/donut charts, and period selector |
| **Map** | Interactive map widget with shop markers (Google Maps on Android, Canvas on iOS), tap markers to view shop details in a bottom sheet |
| **Gallery** | Image grid with sort (A-Z, Z-A, by album) and layout toggle (2-col grid, 3-col grid, list). Clean card layout. Full-screen image viewer on tap. Placeholder/error states for failed image loads |
| **Profile** | User avatar (tap to change from gallery), editable name/title/email via dialog, stats row, activity feed, AI assistant chatbot with simulated responses |
| **Components** | Comprehensive UI component sampler — see [UI Kit](#ui-kit-components-screen) below |

## Features

### Welcome Dialog
After login, a near-fullscreen welcome dialog introduces the app, explains who Artificialss is, highlights key features with bullet points, and explains the live theme switching system.

### Custom Chart Library
Three Canvas-based chart components, fully cross-platform (no third-party chart library):
- **BarChart** — Animated rounded bars with per-entry optional color, tap-to-select with tooltip bubble
- **LineChart** — Cubic bezier curves with gradient fill, animated dots, tap-to-select with vertical indicator and tooltip bubble
- **DonutChart** — Arc segments with center label, legend with percentages, tap-to-select with highlight and dynamic center text

All charts use `Animatable` with `tween(FastOutSlowInEasing)` for smooth entry animations. Touch tooltips are speech-bubble shaped with triangular pointers, rendered via `TextMeasurer` + Canvas drawing. Tooltips auto-flip below the anchor if they would clip at the top.

### Platform Map (expect/actual)
- **Android**: Google Maps Compose with real markers and debounced camera tracking
- **iOS**: Canvas-based interactive map with drag-to-pan, tap-to-select markers, grid streets, and pin graphics
- Camera state (lat, lng, zoom) is preserved across rotation via `rememberSaveable`

### Gallery Controls
- **Sort**: Toggle between A-Z, Z-A, and by-album sorting (icon turns primary when active)
- **Layout**: Switch between 2-column grid, 3-column grid, and list view
- Grid cards show clean images without overlay text
- List view shows thumbnail + title per row
- Image loading: `SubcomposeAsyncImage` with loading placeholder (surfaceVariant + image icon) and error state (errorContainer + broken image icon)

### Profile Features
- **Edit Profile**: Dialog with name, title, and email fields — saves locally in-memory
- **Avatar Picker**: Tap avatar to choose from a grid of 9 sample portraits
- **AI Assistant**: "Ask AI" chip in the activity area opens a bottom sheet chatbot with 3 quick actions:
  - "What are my pending tasks?" — returns a bulleted task list
  - "Summarize my recent activity" — returns a productivity summary
  - "Suggest next steps for my project" — returns actionable suggestions
  - Simulates AI API call with 1.2s typing indicator before response
  - Chat bubbles with asymmetric rounded corners, auto-scroll, scrim overlay

### UI Kit (Components Screen)
A comprehensive interactive sampler showcasing Material 3 components:

| Category | Components |
|----------|-----------|
| **Buttons** | Primary, Outlined, Elevated, Tonal, Text, Disabled |
| **Icon Buttons** | Favorite, Notifications, Delete (error tint), Info (primary tint) |
| **Text Fields** | Full Name (required validation), Email (regex validation with error), Password (masked input with visibility toggle, min-length validation) |
| **Chips** | 7 filter chips with checkmark on selection (Kotlin, Compose, Multiplatform, etc.) |
| **Toggle Switches** | 3 labeled switches with independent state |
| **Checkboxes** | 3 labeled checkboxes with independent state |
| **Radio Buttons** | Subscription plan cards (Free / Pro / Enterprise) — icon, title, subtitle, border highlight, selected background, check mark |
| **Progress Indicators** | Circular indeterminate (3 sizes), circular determinate with animated percentage labels, linear indeterminate (rounded), linear determinate (3 animated bars), shimmer loading placeholder (avatar + title/description lines using Modifier.shimmerEffect()) |
| **Sliders** | Brightness (continuous, custom circle thumb), Temperature (stepped, tertiary color, circle thumb), Volume (error/red color, circle thumb), Price Range (RangeSlider with secondary color) |
| **Snackbar & Toast** | Snackbar, Snackbar with action + dismiss, custom Toast overlay with auto-dismiss |
| **Dialogs** | Simple (title + message + OK), Info (with icon), Confirmation (delete account, error-colored button, warning icon), Input (New Event form with 3 text fields) |

All sections are wrapped in `Card` containers with dividers. Uses `LazyColumn` for performance. Progress bars animate on first render via `animateFloatAsState`.

### Live Style Switching
Bottom bar with three toggle controls that change the app's appearance in real time:
- **Theme**: Light / Dark
- **Font**: Default / Serif / Monospace
- **Color**: Blue / Green / Purple

Current values shown in gray circular badges. Style state survives rotation.

### Edge-to-Edge
- Top navigation bar uses primary color with `onPrimary` content, respects status bar insets
- Splash and Login screens extend primary background behind the status bar
- Bottom style bar respects navigation bar insets
- Both bars never overlap system UI

### Rotation Resilience
All critical state survives configuration changes:
- Current route, theme, font, and color palette stored as `rememberSaveable` integers
- Map camera position preserved via primitive state types (`mutableDoubleStateOf`, `mutableFloatStateOf`)
- Login form fields, gallery controls, and bottom sheet state all survive rotation
- Login session state persists — logged-in users skip Splash/Login on rotation

## Setup

### Prerequisites

- Android Studio Hedgehog+ or IntelliJ IDEA
- JDK 11+
- Xcode 15+ (for iOS)

### Google Maps API Key

The Map screen's Android markers use Google Maps Compose, which requires an API key. Without one, the map renders as a blank/grey tile grid but the app still runs (shop details and the iOS Canvas map are unaffected).

1. Go to the [Google Cloud Console](https://console.cloud.google.com/) and create a project (or select an existing one).
2. Enable the **Maps SDK for Android** API for that project (APIs & Services → Library).
3. Go to **APIs & Services → Credentials → Create Credentials → API key**.
4. (Recommended) Restrict the key to **Android apps** and add this app's package name (`com.artificialss.showcase`) and debug signing certificate SHA-1, and restrict it to the Maps SDK for Android API.
5. Create `local.properties` at the project root if it doesn't exist (it's git-ignored, so it's never committed) and add your key:

```properties
sdk.dir=/path/to/your/Android/sdk
MAPS_API_KEY=your_api_key_here
```

6. Rebuild and run — the key is injected into `AndroidManifest.xml` via a Gradle manifest placeholder (`composeApp/build.gradle.kts`), so no code changes are needed.

### Run Android

```shell
./gradlew :composeApp:assembleDebug
```

Or use the run configuration in Android Studio.

### Run iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator.

### Run Tests

```shell
./gradlew :composeApp:allTests
```

## Project Structure

```
composeApp/src/
├── commonMain/kotlin/com/artificialss/showcase/
│   ├── data/
│   │   ├── local/          # Room entities, DAOs, AppDatabase, DatabaseSeeder
│   │   ├── remote/         # Apollo client provider
│   │   ├── repository/     # Interfaces + Mock/Remote implementations
│   │   ├── mapper/         # Entity <-> Domain mapping functions
│   │   └── mock/           # Deterministic mock data generators
│   ├── domain/model/       # Pure Kotlin domain models
│   ├── di/                 # Koin modules (one per feature)
│   └── ui/
│       ├── theme/          # Material3 theme, colors, typography, AppStyleState
│       ├── navigation/     # AppRoute sealed interface, MAIN_ROUTES
│       ├── components/
│       │   ├── charts/     # BarChart, LineChart, DonutChart (Canvas-based)
│       │   └── map/        # CameraState, MapMarker, PlatformMapView (expect)
│       └── feature/        # Screen packages (Screen + Presenter + UiState)
│           ├── dashboard/  # Unified home + analytics (DashboardScreen)
│           ├── map/        # Interactive map with shop markers
│           ├── gallery/    # Image grid with sort/layout controls
│           ├── profile/    # User profile with stats and activity
│           └── components/ # UI Kit sampler (ComponentsScreen)
├── androidMain/kotlin/     # Platform.android.kt, PlatformMapView (Google Maps)
└── iosMain/kotlin/         # Platform.ios.kt, PlatformMapView (Canvas)
```

## Navigation

State-based navigation using a `sealed interface AppRoute` with `when` dispatch. No navigation library — route index stored as `rememberSaveable` integer. Top bar uses `FlowRow` to wrap icons into multiple rows when needed.

**Routes:** Dashboard, Map, Gallery, Profile, Components

## Data Layer

### Database Model (Room KMP)

The app uses Room KMP with a single `AppDatabase` (v1, `showcase.db`) containing four tables:

| Table | Entity | Primary Key | Columns |
|-------|--------|-------------|---------|
| `transactions` | `TransactionEntity` | `id: String` | `amount: Double`, `merchant: String`, `category: String`, `date: String` |
| `chart_data` | `ChartDataEntity` | `id: String` | `label: String`, `value: Double`, `type: String` (LINE/BAR), `period: String` (WEEK/MONTH/QUARTER) |
| `shop_locations` | `ShopLocationEntity` | `id: String` | `name`, `category`, `latitude: Double`, `longitude: Double`, `rating: Float`, `address`, `hours` |
| `gallery_items` | `GalleryItemEntity` | `id: String` | `imageUrl: String`, `title: String` |

Each table has a DAO exposing `getAll(): Flow<List<Entity>>`, `count(): Int`, and `insertAll()`. Enums are stored as their `.name` string and mapped back via `entries.firstOrNull { it.name == stored }` in the mapper layer.

### Domain Models

Domain models are pure Kotlin data classes with no Room or framework dependencies:

- **`Transaction`** — `id`, `amount`, `merchant`, `category: TransactionCategory` (enum with 8 values: Food, Transport, Entertainment, Shopping, Bills, Health, Travel, Other), `date`
- **`ChartDataPoint`** — `id`, `label`, `value`, `type: ChartType` (LINE, BAR), `period`
- **`ChartPeriod`** — Enum: `WEEK("This Week")`, `MONTH("This Month")`, `QUARTER("This Quarter")`
- **`ShopLocation`** — `id`, `name`, `category`, `latitude`, `longitude`, `rating`, `address`, `hours`
- **`GalleryItem`** — `id`, `imageUrl`, `thumbnailUrl`, `title`, `albumTitle`
- **`UserProfile`** — `id`, `name`, `role`, `email`, `avatarUrl`, `projectCount`, `reviewCount`, `starCount`
- **`ActivityItem`** — `id`, `title`, `description`, `timestamp`

### Mapper Layer

Bidirectional extension functions (`Entity.toDomain()` / `DomainModel.toEntity()`) handle conversion between database entities and domain models. Enum fields are stored as strings in Room and parsed back safely with fallbacks (e.g., unknown category maps to `TransactionCategory.OTHER`).

### Mock Data Generation Algorithm

Mock data is generated deterministically using `kotlin.random.Random(seed)` with a fixed seed (`42L`), ensuring identical data across runs and platforms. The `DatabaseSeeder` runs on first launch and seeds each table only if it's empty (`count() > 0` guard).

#### TransactionMockGenerator
- Generates **20 transactions** from a pool of 15 real-world merchant names (Netflix, Spotify, Amazon, Starbucks, etc.)
- Each merchant is pre-mapped to a `TransactionCategory` (e.g., Netflix -> Entertainment, Uber -> Transport)
- Amounts are random doubles in `[2.0, 500.0]`, rounded to 2 decimal places via `(raw * 100).roundToInt() / 100.0`
- Dates are spread across a month: `2025-03-{(index % 28 + 1)}`
- IDs follow the pattern `txn_0`, `txn_1`, etc.

#### ChartMockGenerator
- Generates data points for **every combination** of `ChartPeriod` x `ChartType` x labels
- Labels per period: Week = `[Mon..Sun]` (7), Month = `[Week 1..Week 4]` (4), Quarter = `[Jan, Feb, Mar]` (3)
- Value ranges depend on chart type: LINE = `[1000, 15000]`, BAR = `[20, 70]`
- Values rounded to 2 decimal places
- Produces `(7+4+3) x 2 = 28` data points total
- IDs: `chart_0`, `chart_1`, etc.

#### ShopLocationMockGenerator
- Generates **10 shops** with real Madrid store names (Casa del Libro, El Corte Ingles, Zara Home, etc.)
- Each shop has a pre-assigned category and address
- Coordinates are scattered around Madrid center (`40.4168, -3.7038`) with random offsets in `[-0.015, +0.015]` range
- Ratings are random in `[3.0, 5.0]`, rounded to 1 decimal place
- All shops share the same hours (`10:00 - 21:00`)

#### GalleryMockGenerator
- Uses a **static list** of 12 curated Unsplash image URLs (no randomness)
- Each item has both a full-size (`?w=600`) and thumbnail (`?w=200`) URL
- Items are organized into 4 albums: Nature (6), Travel (3), Landscape (2), Night (1)
- The `seed` parameter is accepted but unused (for API consistency)

#### UserMockGenerator
- Returns a **single hardcoded profile** (Elena Rodriguez, Senior Product Designer)
- Generates a **static list** of 6 activity items (pushed, code review, created issue, released, updated docs, sprint planning)
- No randomness — fully deterministic without a seed

### Repository Pattern

Each feature has a `Repository` interface and a `MockRepository` implementation:

```
Repository (interface)          MockRepository (implementation)
─────────────────────          ──────────────────────────────
TransactionRepository    <──   MockTransactionRepository(TransactionDao)
ChartRepository          <──   MockChartRepository(ChartDataDao)
ShopLocationRepository   <──   MockShopLocationRepository(ShopLocationDao)
GalleryRepository        <──   MockGalleryRepository(GalleryItemDao)
BitcoinRepository        <──   RemoteBitcoinRepository(HttpClient)
UserRepository           <──   MockUserRepository()
```

Mock repositories read from Room via DAOs and apply `Entity.toDomain()` mappers. Data flows as reactive `Flow<List<T>>` streams from Room through the repository to the Presenter.

### GraphQL API (Apollo Kotlin 4)

The Gallery feature supports a live data source via [GraphQLZero](https://graphqlzero.almansi.me/), a free fake GraphQL API built on JSONPlaceholder data. This demonstrates how the app can seamlessly swap between local mock data and a real network API.

#### Apollo Setup

- **Client**: `ApolloClientProvider` creates a singleton `ApolloClient` pointing at `https://graphqlzero.almansi.me/api`
- **Cache**: In-memory normalized cache (`MemoryCacheFactory`, 10 MB) avoids redundant network requests
- **DI**: Registered in `networkModule` as `single<ApolloClient> { ApolloClientProvider.create() }`
- **Code Generation**: Apollo Kotlin generates type-safe Kotlin classes from `.graphql` operation files. Generated code lives in `composeApp/src/commonMain/kotlin/com/artificialss/showcase/graphql/` (gitignored)

#### GraphQL Operations

Two queries are defined in `composeApp/src/commonMain/graphql/operations/`:

**`GetGalleryPhotos.graphql`** — Paginated photo list with album info:
```graphql
query GetGalleryPhotos($page: Int, $limit: Int) {
  photos(options: { paginate: { page: $page, limit: $limit } }) {
    data {
      id
      title
      url
      thumbnailUrl
      album {
        title
      }
    }
  }
}
```

**`GetPhotosByAlbum.graphql`** — All photos in a specific album:
```graphql
query GetPhotosByAlbum($albumId: ID!) {
  album(id: $albumId) {
    id
    title
    photos {
      data { id, title, url, thumbnailUrl }
    }
  }
}
```

#### RemoteGalleryRepository

`RemoteGalleryRepository` implements the same `GalleryRepository` interface as `MockGalleryRepository`. It:

1. Executes `GetGalleryPhotosQuery(page=1, limit=20)` via Apollo
2. Maps each GraphQL response photo to a `GalleryItem` domain model using a private `toDomain()` extension:
   - `id` → `photo.id`
   - `imageUrl` → `photo.url`
   - `thumbnailUrl` → `photo.thumbnailUrl`
   - `title` → `photo.title`
   - `albumTitle` → `photo.album.title`
3. Wraps the call in `runCatching` — network errors silently return an empty list
4. Emits the result as a `Flow<List<GalleryItem>>`

#### How It Connects to Sort and Layout

The Gallery screen's sort/layout controls are **entirely client-side** and operate on the domain model list regardless of data source:

```
Data Source (Mock or Remote)
    │
    ▼
GalleryRepository.getGalleryItems(): Flow<List<GalleryItem>>
    │
    ▼
GalleryPresenter → StateFlow<GalleryUiState.Success(items)>
    │
    ▼
GalleryScreen (Composable)
    │
    ├── items received as List<GalleryItem>
    │
    ├── Sort (client-side):
    │   ├── GallerySortOrder enum: TITLE_ASC, TITLE_DESC, ALBUM
    │   ├── Cycles on tap: A-Z → Z-A → By Album → A-Z
    │   └── Applies: sortedBy { it.title } / sortedByDescending / sortedBy { it.albumTitle }
    │
    ├── Layout (client-side):
    │   ├── GalleryLayout enum: GRID_2, GRID_3, LIST
    │   ├── Cycles on tap: 2-col → 3-col → List → 2-col
    │   └── Switches between LazyVerticalGrid(2), LazyVerticalGrid(3), LazyColumn
    │
    └── Combined: remember(items, sortOrder) { sorted list }
```

The sorted list is memoized with `remember(items, sortOrder)` so it only recomputes when inputs change. Because sorting operates on the `GalleryItem` domain model (not on entities or GraphQL types), it works identically whether data comes from Room or Apollo.

#### Switching to Live API

Change one Koin binding in `galleryModule`:

```kotlin
// Mock (default):
single<GalleryRepository> { MockGalleryRepository(get()) }

// Live (GraphQLZero):
single<GalleryRepository> { RemoteGalleryRepository(get()) }
```

No changes needed in the Presenter, Screen, or filter/sort logic — the repository interface contract is identical.

### Bitcoin Price API (Ktor Client)

The Analytics/Dashboard "Bitcoin Price" line chart fetches live data from the [CoinGecko API](https://www.coingecko.com/en/api), a free public cryptocurrency API (no auth required). This demonstrates cross-platform HTTP networking with Ktor Client in KMP.

#### Ktor Setup

- **Common**: `ktor-client-core` in `commonMain` — platform-agnostic HTTP client API
- **Android**: `ktor-client-okhttp` engine — uses OkHttp under the hood
- **iOS**: `ktor-client-darwin` engine — uses URLSession under the hood
- **DI**: `HttpClient()` registered as singleton in Koin (auto-detects platform engine)

#### API Endpoint

```
GET https://api.coingecko.com/api/v3/coins/bitcoin/market_chart
    ?vs_currency=usd
    &days={7|30|90}
```

Returns `{"prices": [[timestamp, price], ...]}` — array of `[Unix ms, USD price]` pairs.

#### Data Flow

```
CoinGecko API (HTTPS)
    │
    ▼
Ktor HttpClient.get() → bodyAsText()
    │
    ▼
kotlinx.serialization.json manual parsing
    │
    ▼
RemoteBitcoinRepository.parsePrices()
    ├── Samples evenly to match period label count
    ├── WEEK: 7 points (Mon–Sun)
    ├── MONTH: 4 points (Week 1–4)
    └── QUARTER: 3 points (Jan–Mar)
    │
    ▼
List<ChartDataPoint> → AnalyticsPresenter
    │
    ▼
LineChart composable (cubic bezier, gradient fill, tap tooltip)
```

#### Period Mapping

| ChartPeriod | API `days` | Sampled Points | Labels |
|-------------|-----------|---------------|--------|
| WEEK | 7 | 7 | Mon, Tue, Wed, Thu, Fri, Sat, Sun |
| MONTH | 30 | 4 | Week 1, Week 2, Week 3, Week 4 |
| QUARTER | 90 | 3 | Jan, Feb, Mar |

#### Error Handling

If the API call fails (no network, rate limit, etc.), the Bitcoin chart gracefully shows an empty state while the bar and donut charts (backed by Room) continue working normally.

### Data Flow

```
App Launch
    │
    ▼
DatabaseSeeder.seedIfEmpty()
    │
    ├── count() > 0? → skip
    │
    └── count() == 0?
            │
            ▼
        MockGenerator.generate(seed=42)
            │
            ▼
        DomainModel.toEntity()
            │
            ▼
        dao.insertAll(entities)

Runtime Read
    │
    ▼
Presenter → Repository.getX()
    │
    ▼
DAO.getAll(): Flow<List<Entity>>
    │
    ▼
.map { entities -> entities.map { it.toDomain() } }
    │
    ▼
StateFlow<UiState> → Composable
```

## License

MIT — see [LICENSE](LICENSE). Free to use, modify, and distribute.

---

Built with Compose Multiplatform by **Artificialss**
