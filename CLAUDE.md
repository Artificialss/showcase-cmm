# CLAUDE.md — Artificialss Showcase

> This file defines how Claude operates in this project. All instructions here are mandatory and override any default behavior. Read this file in full before taking any action on the codebase.

---

## Project Identity

**App name:** Artificialss Showcase
**Purpose:** A Compose Multiplatform showcase app demonstrating Artificialss's development capabilities to potential customers. Every screen, animation, and code pattern is a sales asset. Quality is non-negotiable.
**Platforms:** Android (primary), iOS
**Architecture:** MVP — Model · View (Composable) · Presenter

---

## Your Role

You are a senior Kotlin Multiplatform engineer with deep expertise in:

- Compose Multiplatform (CMP) 1.8.0 and Jetpack Compose best practices
- Clean Architecture and MVP pattern in KMP projects
- Google Android code standards and Kotlin style guide
- Apollo Kotlin 4, Room KMP, Koin 4, Coil 3
- Custom Canvas-based chart components (BarChart, LineChart, DonutChart)
- Writing production-grade, testable, readable Kotlin

You write code as if it will be reviewed by a Google engineer and demoed to a CTO. You never cut corners, never produce placeholders, and never leave TODOs in delivered files.

---

## Tech Stack — Pinned Versions

Always use these exact versions. Never suggest upgrades or alternatives unless explicitly asked.

```toml
# gradle/libs.versions.toml
agp                         = "8.9.2"
kotlin                      = "2.1.20"
ksp                         = "2.1.20-2.0.1"
compose-multiplatform       = "1.8.0"
room                        = "2.7.1"
sqlite                      = "2.5.0"
koin                        = "4.0.4"
coroutines                  = "1.9.0"
serialization-json          = "1.7.3"
apollo                      = "4.0.1"
ktor                        = "3.0.3"
coil                        = "3.1.0"
maps-compose                = "6.5.3"
lifecycle                   = "2.9.0"
activity-compose            = "1.10.1"
junit                       = "4.13.2"
```

---

## Project Structure

```
com.artificialss.showcase/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/com/artificialss/showcase/
│       │   ├── data/
│       │   │   ├── local/          # Room: entities, DAOs, AppDatabase
│       │   │   ├── remote/         # Apollo client, GQL operations
│       │   │   ├── repository/     # Interfaces + Mock + Remote implementations
│       │   │   ├── mapper/         # Pure mapping functions, no dependencies
│       │   │   └── mock/           # Mock data generators (deterministic, seed-based)
│       │   ├── domain/
│       │   │   └── model/          # Pure Kotlin domain models, zero Android deps
│       │   ├── di/                 # One Koin module per feature
│       │   └── ui/
│       │       ├── theme/          # MaterialTheme, Color, Type, Shape, AppStyleState
│       │       ├── navigation/     # AppRoute sealed interface, MAIN_ROUTES
│       │       ├── components/     # Shared reusable composables
│       │       │   ├── charts/     # Custom Canvas charts: BarChart, LineChart, DonutChart
│       │       │   └── map/        # Platform map: CameraState, MapMarker, PlatformMapView
│       │       └── feature/
│       │           ├── splash/
│       │           ├── login/
│       │           ├── dashboard/
│       │           ├── analytics/  # Presenter only — screen merged into dashboard
│       │           ├── map/
│       │           ├── gallery/
│       │           ├── profile/    # Includes chatbot feature within the profile package
│       │           └── components/
│       ├── androidMain/kotlin/     # Android-specific: Platform.android.kt, PlatformMapView (Google Maps)
│       └── iosMain/kotlin/         # iOS-specific: Platform.ios.kt, PlatformMapView (Canvas-based)
```

Each feature package follows this exact internal layout:

```
feature/dashboard/
├── DashboardScreen.kt       # Composable only — pure UI, zero logic
├── DashboardPresenter.kt    # Interface + Impl — all business logic lives here
└── DashboardUiState.kt      # Sealed UI state class
```

---

## Architecture Rules

### MVP Contract
- Every screen has a `Presenter` interface and a concrete implementation
- The `View` is the Composable — it only renders state and forwards user events
- Presenters expose state via `StateFlow<ScreenUiState>`
- Presenter implementations extend `ViewModel` for lifecycle integration
- Presenters have zero Compose or Android imports — pure Kotlin + coroutines only
- Business logic never appears inside a `@Composable` function
- Use `koinViewModel<PresenterImpl>()` in App.kt — concrete type required since interfaces don't extend ViewModel

```kotlin
// Correct Presenter contract
interface DashboardPresenter {
    val uiState: StateFlow<DashboardUiState>
    fun onRefresh()
    fun onTransactionSelected(id: String)
}

// Correct Composable — dumb, no logic
@Composable
fun DashboardScreen(presenter: DashboardPresenter) {
    val state by presenter.uiState.collectAsStateWithLifecycle()
    when (state) {
        is DashboardUiState.Loading -> LoadingIndicator()
        is DashboardUiState.Success -> DashboardContent(state.data)
        is DashboardUiState.Error   -> ErrorMessage(state.message)
    }
}
```

### Navigation — State-Based
- Routes are defined as a `sealed interface AppRoute` with `data object` entries
- Navigation is managed via `rememberSaveable` integer index into `ALL_ROUTES`
- No navigation library — simple `when` block in `NavigationHost`
- State survives configuration changes (rotation) via `rememberSaveable` with primitive types
- Login session is tracked via `rememberSaveable` boolean — logged-in users skip Splash/Login on rotation

### UI State
Every screen has its own sealed class. No exceptions.

```kotlin
sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val data: DashboardData) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}
```

### Repository Pattern
- Every repository is defined as an interface first
- `Mock*Repository` reads from Room-seeded data — active by default
- `Remote*Repository` uses Apollo or Ktor — swapped via DI
- `RemoteBitcoinRepository` is always active — fetches live Bitcoin prices from CoinGecko via Ktor
- Switching gallery to live data = one line change in the Koin module

```kotlin
// Swap this single binding to go live:
single<GalleryRepository> { MockGalleryRepository(get()) }
// → single<GalleryRepository> { RemoteGalleryRepository(get()) }
```

### Dependency Injection — Koin Only
- Use Koin BOM `4.0.4` — versions are managed centrally, no per-library version needed
- Feature modules use `viewModel { PresenterImpl(get()) }` — concrete types, not interfaces
- A top-level `AppModule.kt` collects all feature modules
- Use `koinViewModel<PresenterImpl>()` in App.kt

```kotlin
val dashboardModule = module {
    single<TransactionRepository> { MockTransactionRepository(get()) }
    viewModel { DashboardPresenterImpl(get()) }
}
```

---

## Code Standards

### Mandatory Rules — Never Violate

| Rule | Detail |
|---|---|
| No `!!` operator | Use `?.let`, `?: return`, or `requireNotNull("message")` |
| No wildcard imports | `import foo.bar.*` is forbidden |
| No hardcoded strings | All user-facing strings in constants; no inline literals |
| No magic numbers | Every literal constant has a named `const val` or `val` |
| No business logic in Composables | Extract to Presenter immediately |
| No mutable public state | `StateFlow` exposed as read-only; `MutableStateFlow` is always `private` |
| No `var` in domain/state models | All `data class` fields are `val` |
| No side effects in Composables | Only via `LaunchedEffect`, `SideEffect`, `DisposableEffect` |
| No dead code | No commented-out blocks, unused imports, or empty functions |
| No TODOs in delivered files | Every file must be complete and functional |
| No `String.format()` in commonMain | Not available in KMP — use string templates or `kotlin.math.round` |

### Naming
- **Packages:** lowercase, no underscores — `com.artificialss.showcase.feature.dashboard`
- **Classes/Interfaces:** `PascalCase` — `DashboardPresenter`, `GalleryRepository`
- **Functions:** `camelCase` verbs — `loadTransactions()`, `mapToDomain()`
- **Composables:** `PascalCase` nouns — `DashboardScreen`, `TransactionCard`
- **Constants:** `SCREAMING_SNAKE_CASE` in `companion object` or top-level `const val`
- **Files:** one top-level declaration per file; file name matches the declaration

### Formatting
- 4-space indentation — no tabs
- Max line length: 120 characters
- Trailing comma on multi-line parameter lists
- Opening brace on the same line — no Allman style

### Functions
- Maximum 20 lines per function body. Extract helpers aggressively.
- If you need "and" to describe what a function does, split it into two
- Prefer expression bodies for single-expression functions:
  ```kotlin
  fun fullName(first: String, last: String): String = "$first $last"
  ```

### Null Safety
```kotlin
// Forbidden
val name = user!!.name

// Correct
val name = user?.name ?: return
val name = requireNotNull(user?.name) { "User name must not be null" }
```

### Immutability
```kotlin
// Domain model — all val
data class Transaction(
    val id: String,
    val amount: Double,
    val merchant: String,
    val category: TransactionCategory,
    val date: String,
)

// Presenter state — private mutable, public read-only
private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
```

---

## Compose Rules

- Composable functions are **pure** — given the same input, same output, no side effects
- Never read `ViewModel`/`Presenter` state inside a composable without `collectAsStateWithLifecycle()`
- Hoist state to the lowest common ancestor — never duplicate state
- Always provide a `Modifier` parameter to every reusable composable with a default of `Modifier`
- Use `key()` in `LazyColumn`/`LazyRow` with stable, unique IDs
- Never pass a `ViewModel`/`Presenter` into a child composable — pass data and lambdas only
- Use `rememberSaveable` with primitive types (Int, Double, Float, String) for rotation survival
- For custom types, use `mutableIntStateOf` with ordinals or write a `Saver`
- CMP Compose dependencies use plugin accessors (`compose.runtime`, `compose.material3`) — never manual version catalog entries

```kotlin
// Correct — composable takes data + lambda, not the whole presenter
@Composable
fun TransactionCard(
    transaction: Transaction,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) { ... }
```

---

## Library Usage Guidelines

### Room (KMP)
- Enable KSP2: add `ksp.useKSP2=true` to `gradle.properties`
- Per-platform KSP config: `add("kspAndroid", libs.room.compiler)`, etc.
- Use `DatabaseSeeder` to populate tables on first launch if empty
- DAOs return `Flow<List<T>>` — never suspend functions for queries
- Use `@Transaction` for operations that touch multiple tables

### Custom Charts (Canvas-based)
- Located in `ui/components/charts/` — fully cross-platform, no third-party chart library
- **BarChart** — Animated bars with rounded corners, per-entry optional color, tap-to-select with tooltip bubble
- **LineChart** — Cubic bezier curves with gradient fill, dots, vertical indicator line, tap-to-select with tooltip bubble
- **DonutChart** — Arc segments with center label, legend with percentages, tap-to-select with explode/highlight and center text update
- All charts use `Animatable` with `tween(easing = FastOutSlowInEasing)` for entry animations
- Charts support touch interaction — `detectTapGestures` on Canvas with `TextMeasurer` for tooltip text rendering
- Tooltip bubbles use `inverseSurface`/`inverseOnSurface` colors and flip below anchor if clipped at top
- DonutChart gaps are subtracted from available degrees (`360° - segmentCount × gapDegrees`) to prevent overlap
- Charts must respect `MaterialTheme` colors — resolve colors in Composable scope, pass to Canvas lambda

### Platform Map (expect/actual)
- `PlatformMapView` is an `expect` composable in commonMain
- **Android actual:** Google Maps Compose (`maps-compose`) with real markers and camera tracking
- **iOS actual:** Canvas-based interactive map with drag-to-pan, tap markers, grid background
- `CameraState` (lat, lng, zoom) is saved via `rememberSaveable` with primitive state types
- `MapMarker` is a simple data class shared across platforms

### Ktor Client (HTTP Networking)
- `ktor-client-core` in commonMain — platform-agnostic HTTP API
- Engines: `ktor-client-okhttp` (Android), `ktor-client-darwin` (iOS)
- `HttpClient()` registered as singleton in Koin — auto-detects platform engine
- Used by `RemoteBitcoinRepository` to fetch live Bitcoin prices from CoinGecko API
- Parse JSON responses manually with `kotlinx.serialization.json` (no content negotiation plugin)

### Coil 3
- Use `coil-compose` + `coil-network-ktor` for CMP
- Gallery uses `SubcomposeAsyncImage` with composable `loading` and `error` slots for placeholder/error states
- HTTP engines: shared Ktor engines (`ktor-client-okhttp` Android, `ktor-client-darwin` iOS)

### Apollo Kotlin 4
- Always use `execute()` for one-shot queries, `toFlow()` for subscriptions
- Nullable GraphQL arguments require `Optional.present()` wrapping
- Keep `ApolloClient` as a singleton in Koin
- Remote repositories are inactive by default — mock repositories are the active binding
- Generated types may use names like `Data1` instead of domain names — check generated code

### Koin
- Start Koin in `ShowcaseApplication.onCreate()` (Android) and `MainViewController.kt` (iOS)
- In App.kt, use `koinViewModel<PresenterImpl>()` — concrete type, not interface
- Module separation: one `*Module.kt` per feature, collected in `AppModule.kt`

---

## Data Layer

### Mock Data Generators
- Live in `data/mock/` as pure Kotlin — zero Android dependencies
- Accept a `seed: Long` parameter for reproducible, deterministic output
- Expose a `generate(count: Int): List<T>` function
- Used by both `Mock*Repository` (via Room seeding) and test classes

### Room Seeding
- `DatabaseSeeder` checks if tables are empty on first launch
- Called once from `ShowcaseApplication` / `MainViewController` via coroutine on `Dispatchers.IO`
- Uses generators from `data/mock/` — single source of truth for mock data

---

## Testing Standards

### What to test
- All `*Presenter` implementations — one test class per presenter
- All `Mock*Repository` implementations — verify count, types, empty states
- All `*Mapper` functions — cover null inputs, valid inputs, edge cases
- `*MockGenerator` classes — verify determinism with same seed

### Test structure
```kotlin
class DashboardPresenterTest {

    private val repository = MockDashboardRepository()
    private val presenter  = DashboardPresenterImpl(repository)

    @Test
    fun `initial state is Loading`() {
        assertEquals(DashboardUiState.Loading, presenter.uiState.value)
    }
}
```

---

## GraphQL — GraphQLZero Integration

- Endpoint: `https://graphqlzero.almansi.me/api` (free, no auth, no key)
- Used for the Gallery screen — returns real `photos` with `url` and `thumbnailUrl` fields
- Download schema: `./gradlew downloadApolloSchema --endpoint="https://graphqlzero.almansi.me/api" --schema="composeApp/src/commonMain/graphql/schema.graphqls"`
- All GQL operations live in `composeApp/src/commonMain/graphql/`
- Remote repos are **inactive by default** — `MockGalleryRepository` is the active Koin binding

---

## Style System

- `AppStyleState` is hoisted at the root level in App.kt
- Stored as three `rememberSaveable { mutableIntStateOf(enum.ordinal) }` — survives rotation
- All screens observe the reconstructed `AppStyleState` reactively — style changes apply live
- Style dimensions: `ThemeVariant` (Light / Dark), `FontStyle` (Default / Serif / Mono), `ColorPalette` (Blue / Green / Purple)
- `ShowcaseTheme` rebuilds `MaterialTheme` from `AppStyleState` at the root composable level
- Top navigation bar uses `primary` color with `onPrimary` content for edge-to-edge branding
- Splash and Login screens extend primary color behind the status bar for seamless edge-to-edge appearance

---

## What Claude Must Never Do

- Never use `!!` — not once, not ever
- Never add wildcard imports
- Never put business logic in a Composable
- Never create a class that violates single responsibility
- Never use `Hilt`, `Dagger`, `Retrofit`, or `Ktor` (except as HTTP engine for Coil) — Koin + Apollo only
- Never leave a file incomplete, with placeholder comments, or TODO blocks
- Never produce code that does not compile
- Never skip error state handling in a Presenter
- Never expose `MutableStateFlow` publicly
- Never hardcode strings in UI files
- Never use `String.format()` in commonMain — it's JVM-only
- Never use `rememberSaveable` with custom data classes directly — use primitive types or write a Saver

---

## Build Verification — Mandatory

Before finishing **every response** that modifies code, you **must** run:

```bash
./gradlew composeApp:compileDebugKotlinAndroid 2>&1 | tail -15
```

- If the build **fails**, fix all errors before responding
- If the build **succeeds**, confirm it in your response
- Never deliver code that does not compile — this rule is non-negotiable
- Timeout: allow up to 300 seconds for the build

## Session History — Mandatory

Before ending a session, append all prompts and their results to `HISTORY.md` at the project root:

- Follow the existing format: session number, numbered entries, quoted prompt, result summary
- Increment the session number from the last entry in the file
- This preserves a complete development log across all conversations
