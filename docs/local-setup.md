# Running locally

## Android (emulator)

You need: Java 17+, Maven, Android Studio (SDK + an emulator), Node.js, Appium.

1. **Environment variables** (add to `~/.zshrc`, then open a new terminal):
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator
   ```
2. **Appium driver** (once):
   ```bash
   appium driver install uiautomator2
   appium driver doctor uiautomator2     # checks ANDROID_HOME, Java, adb
   ```
3. **App:** download the latest `.apk` from https://github.com/saucelabs/my-demo-app-rn/releases and save it as `apps/MyDemoAppRN.apk`.
4. **Start an emulator** from Android Studio's Device Manager, and check it's listed:
   ```bash
   adb devices
   ```
5. **Start Appium** in its own terminal and leave it running:
   ```bash
   appium
   ```
6. **Run:**
   ```bash
   mvn test                   # full regression
   mvn test -Dsuite=smoke     # smoke only
   mvn test -Dtest=PurchaseFlowTest
   ```

Report: `target/extent-report/index.html`.

## iOS (simulator)

You need everything above plus Xcode (from the App Store) with an iPhone simulator runtime.

1. ```bash
   xcode-select --install          # command line tools, if not already present
   appium driver install xcuitest
   appium driver doctor xcuitest
   ```
2. Download the `iOS-Simulator-...zip` from the same releases page, unzip it, and save the result as `apps/MyDemoAppRN.app`.
3. Check which simulators you have:
   ```bash
   xcrun simctl list devices available
   ```
4. Run, using a device name from that list:
   ```bash
   mvn test -Dplatform=ios -Dios.device.name="iPhone 15"
   ```
   The first iOS run takes a few minutes while Appium builds WebDriverAgent.

## Troubleshooting

| Symptom | Fix |
| --- | --- |
| `App build not found at ...` | The app file isn't in `apps/`; see step 3 |
| `Could not start a new session ... ECONNREFUSED` | Appium isn't running; start it with `appium` |
| `Could not find a connected Android device` | Start the emulator; check `adb devices` |
| Several devices connected | Pick one with `-Dandroid.udid=emulator-5554` |
| `/wd/hub` 404 errors | Appium 2+ has no `/wd/hub`; keep `appium.url=http://127.0.0.1:4723` |
