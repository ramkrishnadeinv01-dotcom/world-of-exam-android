# World of Exam Android wrapper (production URL configured)

This Capacitor wrapper is configured to open the existing production portal:

`https://ramkrishnadeinv01-dotcom.github.io/live-quiz-competition/`

It uses the existing hosted website and Firebase backend; it does not create a second database or copy participant data. Internet access is required for login, examinations, uploads, and results.

## Build an APK with GitHub Actions

1. Create a new GitHub repository for this Android wrapper (or place the contents of this `android-wrapper` folder at the root of an existing app-source repository).
2. Upload all files and folders from this `android-wrapper` folder, including `.github/workflows/build-android-apk.yml`.
3. In that GitHub repository, open **Actions**.
4. Select **Build World of Exam Android APK** and click **Run workflow**.
5. Wait for the workflow to finish successfully. Open the completed run and download the `WorldOfExam-debug-apk` artifact. It contains `app-debug.apk`.
6. Transfer the APK to the Android phone and install it. Android may ask permission to install apps from that browser/file manager.

The workflow builds a **debug APK for testing/sideloading**. It is not a Play Store release APK and is not signed with a private production key. For Play Store publication, create and securely retain a release keystore and generate a signed release build.

## Build locally (optional)

Requirements: Node.js 22+, Android Studio, Android SDK, and Java 21. From this folder:

```bash
npm install
mkdir -p www
npx cap add android
npx cap sync android
cd android
./gradlew assembleDebug
```

The APK is generated at `android/app/build/outputs/apk/debug/app-debug.apk`.

## Important behaviour

- The app loads the production GitHub Pages website in its Android WebView.
- Portal changes published to GitHub Pages appear in the app without rebuilding the APK.
- The wrapper is online-first. It does not make Firebase-backed exams or results available offline.
- The Capacitor `server.url` is configured to the supplied production URL. Treat this as a hosted-web wrapper; test authentication, file uploads/downloads, back navigation, and external links on the target Android phones before wider distribution.

## Android PDF download handling

This repository includes a custom Capacitor `MainActivity` that connects normal HTTP/HTTPS WebView download requests to Android's Download Manager. Files should appear in the phone's **Downloads** folder. The GitHub Actions workflow copies this activity into the generated Capacitor Android project before building.

After uploading this updated repository to GitHub, run **Actions → Build World of Exam Android APK → Run workflow**. Install the newly generated debug APK and test the same result PDF again.

Note: browser-generated `blob:` downloads are not handled by this first pass. If a result PDF still does not download, report what happens; blob-based generation may need an additional native bridge. This build remains a debug/test APK, not a signed public release APK.
