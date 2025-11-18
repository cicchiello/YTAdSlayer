# YTAdSlayer
YTAdSlayer is an Android app (minimum SDK 33) that aims to monitor YouTube playback on your device and help "slay" in-stream ads by reacting when YouTube is playing a video.

The project is currently in an **early experimental phase**. The core idea is to use an accessibility-based background service to detect when the YouTube app is active and playing media, then expose callbacks to the app so you can build custom logic around ad detection and handling.

The first concrete implementation of this idea is an accessibility service that:

- Watches for when the foreground app is **YouTube** (`com.google.android.youtube`).
- Recursively inspects the current window hierarchy while YouTube is active.
- Looks for an **enabled** UI element whose text contains `skip` (e.g., "Skip ads").
- Logs when such a potential *Skip* button is found, and includes experimental logic for trying to click it.

---

## Project Status

Current state:

- **UI**: Simple Jetpack Compose screen showing the app title and a message that the accessibility watcher is ready to configure.
- **Accessibility service**: `com.jfc.YouTubeWatcher` is declared in the manifest with configuration provided via `@xml/accessibility_service_config` and is wired up as an `AccessibilityService` implementation.
- **Current behavior**:
  - Detects when the active package is **YouTube** and toggles an internal `in-YouTube` flag.
  - While YouTube is active, periodically scans the active window for an **enabled** view whose text contains `skip`.
  - Logs when such a view is found and, in experimental code paths, attempts to perform a click action on the node hierarchy.
- **Architecture**: A first-pass accessibility-based watcher is implemented; broader monitoring architecture and additional detection strategies are still in progress.


---

## Requirements

- **Android Studio** (Giraffe+ / latest stable recommended)
- **Android SDK**: compileSdk 35, targetSdk 35
- **Min SDK**: 33
- **Kotlin** + **Jetpack Compose** tooling enabled

All Gradle configuration is defined in `build.gradle.kts` files and uses the Android Gradle Plugin with the Compose BOM.

---

## Building and Running

1. **Clone the project**

   ```bash
   git clone <your-fork-or-origin-url>
   cd YTAdSlayer
   ```

2. **Open in Android Studio**

   - Choose *Open an existing project* and select the `YTAdSlayer` directory.
   - Let Gradle sync complete.

3. **Select a device**

   - Use a physical device or emulator running **Android 13 (API 33)** or higher.

4. **Run the app**

   - Use the **Run** action in Android Studio targeting the `app` module.

You should see a simple screen titled **"YT Ad Slayer"** with a message that the accessibility watcher is ready to configure.

---

## Permissions and User Setup (Planned)

The accessibility-based watcher and any additional monitoring approaches will require some or all of the following. The **accessibility service** is already in use; the others are still being evaluated.

- **Accessibility service** (current): Enabling the `YouTubeWatcher` accessibility service in system settings so it can observe YouTube UI and look for potential Skip buttons.
- **Usage access**: Allowing the app to see which app is in the foreground (if UsageStatsManager is used).
- **Notification listener**: Granting notification access (if notification-based detection is used).
- **Media session callbacks**: Integrations for observing media playback state.

Once the architecture is more complete, this section will be expanded with exact steps and screenshots for enabling the required permissions and verifying that the watcher is active.

---

## Architecture Overview (Planned)

High-level design goals:

- **Background monitoring**: A long-lived service that can observe when YouTube is active and playing a video.
- **Detection strategies**: Evaluate accessibility events, usage stats, media sessions, and notifications to find a reliable combination for detecting playback and ads.
- **Callback surface**: Provide a clean interface inside the app for responding to events such as *YouTube started playback*, *ad likely detected*, *playback resumed*, etc.
- **Privacy-focused**: Only inspect the minimal information required to detect playback state and ads, avoiding unnecessary data collection.

Current implementation notes:

- The `YouTubeWatcher` service listens to accessibility events and checks the **package name** to detect when YouTube becomes active or inactive.
- A simple handler periodically re-scans the active window while YouTube is active to look for an enabled "Skip"-like button.
- Detection is currently **heuristic and experimental**, primarily intended for local experimentation and logging.

As additional components are implemented (e.g., richer heuristics, configuration UI, diagnostics), the README will be updated with more concrete class and package references.

---

## Roadmap

- **Short term**
  - Iterate on the accessibility-based monitoring approach (tuning event handling and skip-button detection).
  - Decide if/when to introduce complementary mechanisms (UsageStatsManager, media session, notifications).
  - Flesh out the `YouTubeWatcher` service implementation and behavior around skip detection.
  - Expose basic callbacks/events and state to the UI (e.g., is watcher enabled, last detection events).

- **Medium term**
  - Improve configuration UI so users can enable/disable detection features and see status.
  - Add diagnostics/logging screens to help debug detection accuracy.

- **Long term**
  - Experiment with more advanced ad detection heuristics.
  - Harden the service for stability and battery impact.

---

## Contributing / Working on the Project

This project is currently experimental and evolving. If you are iterating locally:

- Use `Todo.md` to track design decisions and next steps.
- Keep accessibility and privacy implications in mind when adding new detection logic.