# YTAdSlayer
YTAdSlayer is an Android app (minimum SDK 33) that aims to monitor YouTube playback on your device and help "slay" in-stream ads by reacting when YouTube is playing a video.

The project is currently in an early experimental phase. The core idea is to use an accessibility-based background service to detect when the YouTube app is active and playing media, then expose callbacks to the app so you can build custom logic around ad detection and handling.

---

## Project Status

Current state:

- **UI**: Simple Jetpack Compose screen showing the app title and a message that the accessibility watcher is ready to configure.
- **Service skeleton**: An accessibility service `com.jfc.YouTubeWatcher` is declared in the manifest with configuration provided via `@xml/accessibility_service_config`.
- **Architecture**: High-level design work is in progress for how the background monitoring should be structured.

Planned work (from `Todo.md`):

- Design architecture for a background service to monitor foreground apps and detect YouTube playback.
- Decide between (or combine) AccessibilityService, UsageStatsManager, media session callbacks, and notification listener approaches.
- Create a robust Android service / accessibility or foreground-service skeleton within this app.
- Determine and document all required permissions and user settings (usage access, accessibility, notification access, etc.).
- Implement logic to identify when YouTube is playing a video and expose callbacks for ad-related handling.

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

The accessibility-based watcher and any additional monitoring approaches will likely require some or all of the following (still being designed):

- **Accessibility service**: Enabling the `YouTubeWatcher` accessibility service in system settings.
- **Usage access**: Allowing the app to see which app is in the foreground (if UsageStatsManager is used).
- **Notification listener**: Granting notification access (if notification-based detection is used).
- **Media session callbacks**: Integrations for observing media playback state.

Once the architecture is finalized and implemented, this section will be expanded with exact steps and screenshots for enabling the required permissions.

---

## Architecture Overview (Planned)

High-level design goals:

- **Background monitoring**: A long-lived service that can observe when YouTube is active and playing a video.
- **Detection strategies**: Evaluate accessibility events, usage stats, media sessions, and notifications to find a reliable combination for detecting playback and ads.
- **Callback surface**: Provide a clean interface inside the app for responding to events such as *YouTube started playback*, *ad likely detected*, *playback resumed*, etc.
- **Privacy-focused**: Only inspect the minimal information required to detect playback state and ads, avoiding unnecessary data collection.

As these components are implemented, the README will be updated with concrete class and package references.

---

## Roadmap

- **Short term**
  - Finalize the monitoring approach (AccessibilityService vs. UsageStatsManager vs. media session vs. notifications).
  - Flesh out the `YouTubeWatcher` service implementation.
  - Expose basic callbacks/events to the UI.

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