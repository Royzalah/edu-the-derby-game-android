
# 🏀 The Derby Game

![Language](https://img.shields.io/badge/Language-Kotlin-8A2BE2?style=for-the-badge&logo=kotlin)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVC-blue?style=for-the-badge)
![Style](https://img.shields.io/badge/Style-Pixar_Inspired-orange?style=for-the-badge)

> **A high-octane Android arcade game.** Dodge defenders, grab power-ups, and fight for your spot in the location-based Hall of Fame.

---

## 📱 Gameplay Demo

See the game in action!

<div align="center">
  <video src="https://github.com/user-attachments/assets/4e834202-1765-4345-a27c-e653dfe3761f" width="400" />
</div>

---

## 📖 About The Project

**The Derby Game** is a native Android application developed as a final project for the "UI Development" course.
Unlike standard infinite runners, this game combines **physics-based sensor controls**, a **dynamic matrix engine**, and a vibrant **Pixar-like aesthetic**.

The project focuses on Clean Code principles, utilizing a strict separation of concerns (MVC) and advanced Android components like Fragments, Sensors, and Google Maps integration.

---

## ✨ Key Features

### 🕹️ Dual-Core Navigation
* **Tilt Mode:** Utilizes the device's **Accelerometer** via a custom `TiltDetector` with noise cancellation (debounce) for immersive, physics-based movement.
* **Button Mode:** Classic tactile UI arrows for precision maneuvering.

### 🌍 Location-Based Hall of Fame
* **Top 10 Leaderboard:** High scores are persisted locally using `SharedPreferences` & `Gson`.
* **Map Integration:** Seamless integration with **Google Maps SDK**. Clicking a score in the list zooms directly to the location where the record was achieved.

### 🔊 Smart Audio Engine
* **Audio Ducking:** Implemented a thread-safe Singleton `SignalManager`. Background music automatically lowers its volume when sound effects (SFX) like collisions or power-ups are triggered.

### ⚡ Dynamic Mechanics
* **Power-Ups:**
    * 🛡️ **Shield:** Grants 4 seconds of immunity.
    * 🚀 **Speed Boost:** Increases game tempo dynamically.
    * ❤️ **Health Pack:** Restores lost lives.
* **Defenders:** Randomly generated enemies with unique skins and patterns.

---

## 📂 Under The Hood

The project follows a modular package structure to ensure scalability and maintainability, as shown below:

```text
com.example.myproject
├── 📂 adapter          # ScoreAdapter (RecyclerView)
├── 📂 enum             # DefenderType, FoulType, TrophyType
├── 📂 fragment         # HighScoreFragment, MapFragment
├── 📂 interface        # CallbackHighScore
├── 📂 logic            # Business Logic (GameManager, GameGridManager, TiltDetector,GameTicker)
├── 📂 model            # Data Entities (AttackPlayer, BaseObject, Defender, Trophy)
├── 📂 utilities        # Constants, Score, SharedPreferencesManager, SignalManager ,VibrationManager , App
├── 📝 GameActivity.kt       # Main Game Loop & Sensors
├── 📝 HighScoreActivity.kt  # Leaderboard Container
├── 📝 HomeActivity.kt       # Main Menu & Settings
└── 📝 ResultActivity.kt     # End Game Logic

```

---

## 🚀 Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

* **Android Studio:** Koala/Ladybug or newer.
* **Min SDK:** API 26 (Android 8.0).
* **Device:** Physical Android device recommended (for accurate Sensor testing).

### 📥 Installation

1. **Clone the repository**
Open your terminal and run:
```bash
git clone [https://github.com/Royzalah/edu-the-derby-game-android.git]

```


2. **Open in Android Studio**
* Select `File > Open` and navigate to the cloned directory.
* Let Gradle sync the project.


3. **🔑 Map Configuration (Crucial Step)**
To enable the Google Maps feature, you need an API Key.
* **Option A (Secure - Recommended):** Add to `local.properties`:
```properties
MAPS_API_KEY=your_actual_api_key_here

```


* **Option B (Quick):** Add directly to `AndroidManifest.xml`:
```xml
<meta-data 
    android:name="com.google.android.geo.API_KEY" 
    android:value="YOUR_KEY_HERE" />

```




4. **Run the App**
Connect your device via USB or start an emulator, then press `Shift + F10`.

---

## 🎮 How To Play

### The Mission

Survive on the court as long as possible! Dodge defenders, collect trophies, and keep your health up.

### 🕹️ Controls

| Action | Sensor Mode (Tilt) | Button Mode (Arrows) |
| --- | --- | --- |
| **Move Left/Right** | Tilt device Left/Right | Tap Left/Right Arrows |
| **Speed Up** ⚡ | Tilt Forward | *(Auto)* |
| **Slow Down** 🐢 | Tilt Backward | *(Auto)* |

### 🎁 Collectibles & Hazards

* 🏆 **Bonus Trophy:** Collect for **+50 Points**.
* 🛡️ **Shield:** Grants immunity for **4 seconds**.
* ❤️ **Heart:** Restores **1 Life**.
* ⚡ **Speed Boost:** Hyper-speed mode!
* 👮 **Defenders:** Avoid at all costs! **3 Fouls = Game Over**.

---

## 🛣️ Roadmap

Here's our plan for future updates:

* [x] **Core Engine:** Custom `Handler` loop for smooth 60fps rendering.
* [x] **Sensor Fusion:** Smart `TiltDetector` with noise cancellation.
* [x] **Leaderboard:** Local Top 10 system with GPS location data.
* [ ] **Multiplayer:** 1v1 PvP mode via Firebase Realtime Database.
* [ ] **Skin Shop:** Unlockable characters (Pixar-style animations).
* [ ] **Cloud Sync:** Cross-device progress synchronization.

---

## 👨‍💻 Developed By

<div align="center">

**Roei zalah**

🚀 *Android Developer & CS Student*





