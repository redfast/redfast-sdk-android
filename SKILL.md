---
name: engage
description: Guide for integrating the Recurly Engage Android SDK (V3). Use when the user asks about com.redfast packages, PromptManager, PromptOverlay, PromptInline, PromptOverlayTriggerType, PromptEvent, PromptResult, or any Recurly Engage Android SDK setup (Jitpack, flavors, FCM/ADM push, Google/Amazon IAP).
---

# Recurly Engage Android SDK v3 — Integration Skill

You are helping an Android developer integrate the Recurly Engage (Redfast) Android SDK v3 into their host app. The SDK source of truth is the `v3` branch of this repository — always read source files before giving advice, as API signatures may have changed.

Before starting, ask the developer:
1. What platform are you targeting? (Google Play, Amazon, or both)
2. Do you need push notifications?
3. Do you need in-app purchases?
4. Are you using Jetpack Compose?

Based on the answers, guide them through the relevant sections below. Skip sections that don't apply.

---

## SDK Architecture

The SDK has two modules consumed as a single artifact per flavor:

- **core** (`com.redfast.core`) — Networking, state, business logic. No Android UI.
- **ui** (`com.redfast.ui`) — Jetpack Compose components, push, IAP. Depends on core.

Available artifacts via Jitpack:
| Artifact | Store | Push | IAP |
|---|---|---|---|
| `redfast-sdk-google` | Google Play | FCM | Google Play Billing |
| `redfast-sdk-amazon` | Amazon | ADM (A3L) | Amazon IAP |
| `redfast-sdk-noiap` | Generic | FCM | None |
| `redfast-sdk-core` | Generic | None | None |

---

## 1. Gradle Setup

### Add Jitpack repository

In `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Add the SDK dependency

In the app `build.gradle.kts`, pick the artifact matching the target platform:

```kotlin
dependencies {
    // Google Play
    implementation("com.github.redfast.redfast-sdk-android-build:redfast-sdk-google:VERSION")

    // Amazon
    implementation("com.github.redfast.redfast-sdk-android-build:redfast-sdk-amazon:VERSION")

    // No IAP
    implementation("com.github.redfast.redfast-sdk-android-build:redfast-sdk-noiap:VERSION")
}
```

If the host app has product flavors for store variants, use flavor-specific configurations:

```kotlin
dependencies {
    "googleImplementation"("com.github.redfast.redfast-sdk-android-build:redfast-sdk-google:VERSION")
    "amazonImplementation"("com.github.redfast.redfast-sdk-android-build:redfast-sdk-amazon:VERSION")
}
```

### Requirements

- Min SDK: 24
- Compile/Target SDK: 36
- Kotlin: 2.0+
- Jetpack Compose (BOM 2024.x)

### Transitive dependencies the SDK adds

The SDK bundles (no need to add manually): Retrofit 3.0, OkHttp 4.12, Gson, Coil 2.6, Compose Material3, Compose Runtime Saveable.

### ProGuard / R8

If minification is enabled, add rules for the SDK's networking stack:

```proguard
# Retrofit
-keep class com.redfast.api.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Gson
-keep class com.redfast.domain.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
```

---

## 2. Platform Credentials

### Google Play (FCM)

Place `google-services.json` in the app module root. Add the Google Services plugin:

```kotlin
// project build.gradle.kts
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// app build.gradle.kts
plugins {
    id("com.google.gms.google-services")
}
```

The SDK bundles `PromotionCloudMessage` (a `FirebaseMessagingService`) with the correct manifest entries. No additional manifest registration needed.

### Amazon (ADM)

Place `api_key.txt` in `app/src/main/assets/`. Generate it from the Amazon Developer Console using the app's MD5 and SHA256 signing certificate fingerprints.

The Amazon flavor requires A3L Messaging. Add it as a dependency:

```kotlin
"amazonImplementation"(files("libs/A3LMessaging-1.1.0.aar"))
```

---

## 3. SDK Initialization

Initialize `PromptManager` once in `Application.onCreate()`. It is a singleton — subsequent calls are ignored.

```kotlin
import com.redfast.ui.PromptManager
import com.redfast.domain.PromptResultCode

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PromptManager.initialize(
            context = this,
            appId = "YOUR_APP_ID",       // UUID from Redfast dashboard
            userId = "CURRENT_USER_ID",  // your app's authenticated user
            onComplete = { result ->
                // Called on Main thread
                if (result.code == PromptResultCode.OK) {
                    // SDK ready — prompts can now be triggered
                }
            }
        )
    }
}
```

What happens on `initialize()`:
1. Builds `DeviceInfo` (manufacturer, model, phone vs TV)
2. Sets up IAP billing client (`IapManager.setupIap`)
3. Registers for push tokens (`PushManager.initialize`)
4. Starts a background ping loop on `Dispatchers.IO` that syncs prompt data from the server
5. Calls `onComplete` on Main thread once the first sync succeeds

**Changing user after login:**

```kotlin
PromptManager.get().setUserId(authenticatedUserId)
// Prompt list refreshes on the next ping cycle
```

---

## 4. Displaying Prompts

The SDK provides three Compose components for different use cases.

### PromptOverlay — Screen or button triggered modals

Auto-triggers modal, interstitial, or bottom banner prompts based on triggers configured in Pulse (Redfast dashboard). Renders nothing until a prompt is matched.

**Place it at the end of the Composable tree** so it overlays all other content:

```kotlin
import com.redfast.ui.PromptOverlay
import com.redfast.ui.PromptOverlayTriggerType
import com.redfast.domain.PromptEvent

@Composable
fun HomeScreen() {
    // Screen content
    Column(Modifier.fillMaxSize()) {
        // ...
    }

    // Overlay — must be LAST so it renders on top
    PromptOverlay(
        triggerType = PromptOverlayTriggerType.Screen(name = "home"),
        onEvent = { event: PromptEvent ->
            // Handle prompt events
        }
    )
}
```

The `name` string must match the trigger configured in Pulse. The SDK resolves the prompt, applies the configured delay, and shows it automatically.

For button-triggered prompts:

```kotlin
PromptOverlay(
    triggerType = PromptOverlayTriggerType.Button(clickId = "purchase"),
    onEvent = { event -> /* handle */ }
)
```

### PromptInline — Embedded banners

Renders an inline prompt within the layout. Scales to fit its container using the image's aspect ratio.

```kotlin
import com.redfast.ui.PromptInline
import com.redfast.domain.InlineCloseButtonStyle
import com.redfast.domain.InlineTimerStyle
import com.redfast.domain.InlineFocusStyle

// Inside a LazyColumn, Column, etc.
PromptInline(
    zoneId = "android-banner",
    closeButton = InlineCloseButtonStyle(
        color = "#000000",
        bgColor = "#FFFFFF",
        size = 20
    ),
    timer = InlineTimerStyle(
        fontSize = 14,
        fontColor = "#FFFFFF"
    ),
    focusStyle = InlineFocusStyle(       // for Android TV (optional)
        borderColor = "#ff4400",
        borderWidth = 1,
        borderRadius = 5
    ),
    modifier = Modifier.padding(horizontal = 8.dp),
    onEvent = { event: PromptEvent ->
        // Same event types as PromptOverlay
    }
)
```

All style parameters are optional. `zoneId` must match the Zone ID configured in Pulse.

### ShowPrompt — Manual rendering

Routes a `Prompt` object to the correct built-in UI component (popup, interstitial, or bottom banner):

```kotlin
import com.redfast.ui.ShowPrompt

ShowPrompt(
    prompt = prompt,
    onEvent = { event -> /* handle */ }
)
```

---

## 5. Custom Prompt Rendering

When the built-in UI doesn't fit the app's design, retrieve prompt data directly and render custom UI.

```kotlin
import com.redfast.api.dto.PathType
import com.redfast.ui.PromptManager

val pm = PromptManager.get()

// All prompts of a given type
val modals = pm.getPrompts(PathType.MODAL)

// Prompts matching specific triggers (checks suppression + holdout)
val prompts = pm.getTriggerablePrompts(
    screenName = "home",     // "*" matches any
    clickId = "*",           // "*" matches any
    type = PathType.MODAL
)
```

### Prompt metadata

```kotlin
val prompt = prompts.first()
prompt.button1?.label        // primary CTA text
prompt.button2?.label        // secondary CTA text
prompt.button3?.label        // decline button text
prompt.inAppSku              // IAP product SKU (if configured)
prompt.deeplink              // decoded key-value map
prompt.deviceMeta            // rf_metadata map
```

### Reporting interactions

Each method is a `suspend` function — call from a coroutine:

```kotlin
scope.launch {
    prompt.impression()  // MUST call when showing the prompt
    prompt.goal()        // primary CTA clicked
    prompt.goal2()       // secondary CTA clicked
    prompt.decline()     // decline button clicked
    prompt.dismiss()     // closed / swiped away
    prompt.timeout()     // countdown expired
    prompt.holdout()     // user in control group — don't show UI
}
```

**PathType values:**
| Name | Value |
|---|---|
| `ALL` | -1 |
| `INVISIBLE` | 1 |
| `MODAL` | 2 |
| `HORIZONTAL` | 5 |
| `VIDEO` | 6 |
| `TEXT` | 7 |
| `VERTICAL` | 8 |
| `TILE` | 9 |
| `INTERSTITIAL` | 10 |
| `BOTTOM_BANNER` | 13 |

---

## 6. Handling Events

`PromptEvent` is a sealed class emitted by all prompt components:

```kotlin
onEvent = { event: PromptEvent ->
    when (event) {
        is PromptEvent.Impression -> { /* prompt was shown */ }
        is PromptEvent.Clicked    -> { /* button 1 or 2 tapped */ }
        is PromptEvent.Decline    -> { /* button 3 tapped */ }
        is PromptEvent.Timeout    -> { /* countdown expired */ }
        is PromptEvent.Dismissed  -> { /* close / swipe */ }
    }
}
```

### PromptResult

Every event carries a `PromptResult`:

```kotlin
val result = event.result
result.code         // PromptResultCode enum
result.value        // Map — deeplink key-value pairs (on BUTTON1/BUTTON2)
result.meta         // Map — rf_metadata custom key-value pairs
result.promptMeta   // PromptMeta — prompt name, ID, experiment, variation, button label
```

### PromptResultCode

| Code | Value | Meaning |
|---|---|---|
| `OK` | 1 | Generic success |
| `ERROR` | -100 | Unexpected error |
| `NOT_APPLICABLE` | -101 | No matching prompt |
| `DISABLED` | -102 | Prompts disabled |
| `SUPPRESSED` | -103 | Suppressed by interval |
| `IMPRESSION` | 100 | Prompt shown |
| `BUTTON1` | 101 | Primary CTA |
| `BUTTON2` | 102 | Secondary CTA |
| `BUTTON3` | 103 | Decline |
| `DISMISS` | 110 | Closed |
| `TIMEOUT` | 111 | Countdown expired |
| `HOLDOUT` | 120 | Control group |

### PromptMeta

```kotlin
data class PromptMeta(
    val promptName: String?,
    val promptID: String?,
    val promptVariationName: String?,
    val promptVariationID: String?,
    val promptExperimentName: String?,
    val promptExperimentID: String?,
    val promptType: Int?,
    val buttonLabel: String?
)
```

### Analytics integration example

```kotlin
fun trackPromptEvent(event: PromptEvent) {
    val name = when (event) {
        is PromptEvent.Impression -> "Prompt Impression"
        is PromptEvent.Clicked    -> "Prompt Click"
        is PromptEvent.Decline    -> "Prompt Decline"
        is PromptEvent.Timeout    -> "Prompt Timeout"
        is PromptEvent.Dismissed  -> "Prompt Dismiss"
    }
    analytics.track(name, mapOf(
        "promptName" to event.result.promptMeta?.promptName,
        "promptID" to event.result.promptMeta?.promptID,
        "variation" to event.result.promptMeta?.promptVariationName,
        "experiment" to event.result.promptMeta?.promptExperimentName
    ))
}
```

---

## 7. Deeplinks & Custom Metadata

### Deeplinks

When a prompt has a deeplink configured, `result.value` contains a decoded map on `BUTTON1`/`BUTTON2` events:

```kotlin
if (event is PromptEvent.Clicked) {
    val url = event.result.value?.get("url") as? String
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(intent)
    }
}
```

### Custom metadata (rf_metadata)

Custom key-value pairs configured in Pulse appear in `result.meta`:

```kotlin
val plan = event.result.meta?.get("plan")    // "premium"
val offer = event.result.meta?.get("offer")  // "50off"
```

### Invisible prompt metadata

Retrieve merged `rf_metadata` from all invisible-type prompts (no UI rendered):

```kotlin
val meta: Map<String, Any> = PromptManager.get().getMeta()
```

---

## 8. Push Notifications

Push is initialized automatically when `PromptManager.initialize()` is called. The SDK handles:
- Token registration with the server
- Receiving and displaying notifications
- Tracking impression on receive and goal on tap

### What the developer must provide

**FCM (google/noiap flavors):**
- `google-services.json` in the app module
- Google Services Gradle plugin

**ADM (amazon flavor):**
- `api_key.txt` in `app/src/main/assets/`
- `A3LMessaging-1.1.0.aar` in the app's dependencies

### What the SDK handles automatically

- `PromotionCloudMessage` service (FCM or ADM) is declared in the SDK's manifest and merged automatically
- `NotificationOpenReceiver` handles tap-to-open tracking
- Notification channel creation on API 26+
- Image loading for rich notifications via Coil

No additional code needed in the host app for basic push support.

---

## 9. In-App Purchases

IAP billing client is initialized automatically. The developer interacts via `PromptManager`:

```kotlin
val pm = PromptManager.get()

// Query product details
pm.iapGetProductDetails("sku_premium", IapProductType.subscription) { products ->
    // products: List<IapProduct> with sku, title, description, price
}

// Launch purchase flow (requires Activity reference)
pm.iapPurchaseProducts(activity, productDetailsList, null) { purchases, error ->
    if (error == null) { /* success */ }
}

// Acknowledge / consume
pm.iapNotifyAppStore(purchase, IapProductType.subscription) { code, message ->
    // handle
}

// Query user's existing purchases
pm.iapGetPurchased { purchases, type -> }
```

**Call `iapOnActivityResumed()` in your Activity's `onResume`** to reconnect the billing client:

```kotlin
override fun onResume() {
    super.onResume()
    PromptManager.get().iapOnActivityResumed()
}
```

**IapProductType:**
| Type | Value |
|---|---|
| `consumable` | `"consumable"` |
| `nonConsumable` | `"android-nonConsumable"` |
| `subscription` | `"subscription"` |

---

## 10. Additional APIs

```kotlin
val pm = PromptManager.get()

// Custom tracking (must be configured in Pulse)
pm.customTrack("genres")

// Reset suppression state (debug/testing)
pm.resetGoal()

// Enable/disable prompts globally
pm.enablePrompt(false)
pm.enablePrompt(true)

// User management
pm.setUserId("new-user-id")
pm.getUserId()
```

---

## 11. Lifecycle & State Considerations

### Configuration changes (rotation)

The SDK uses `rememberSaveable` with a custom `Saver` internally. Prompt state (visibility, countdown start time) survives rotation without developer intervention.

### Process death

Local suppression state is persisted to disk. Prompt data (from the server) requires a re-sync — the ping loop handles this automatically when the app restarts.

### Threading guarantees

- `onComplete` and `onEvent` callbacks are delivered on Main
- Network calls and tracking run on `Dispatchers.IO`
- All Compose components run on Main
- Tracking calls (`impression`, `goal`, `dismiss`, etc.) are fire-and-forget on IO

---

## Common Mistakes

1. **Calling `PromptManager.get()` before `initialize()`** — throws `IllegalStateException`. Always initialize in `Application.onCreate()`.

2. **Placing `PromptOverlay` before screen content** — it must be last in the Composable tree to overlay everything.

3. **Not calling `iapOnActivityResumed()`** — the billing client may disconnect. Reconnect on `onResume`.

4. **Not calling `prompt.impression()` in custom rendering** — if using `getPrompts`/`getTriggerablePrompts` with custom UI, you must call `impression()` when shown and `holdout()` for control group users.

5. **Calling suspend prompt methods from Main without a scope** — `impression()`, `goal()`, etc. are suspend functions. Wrap in `lifecycleScope.launch { }` or a Compose coroutine scope.

6. **Missing ProGuard rules** — if minification is enabled, add rules for Retrofit, Gson, and the SDK domain classes.

7. **Duplicate `initialize()` calls** — subsequent calls are silently ignored. Use `setUserId()` to change the user.
