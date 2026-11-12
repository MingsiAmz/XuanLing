# XuanLing Cultivation Mod

> An immersive cultivation mod developed for Minecraft Forge 1.21.1. It aims to introduce a complete cultivation system into the game, including realm breakthroughs, Qi/Mana systems, a visual custom skill/spell builder, and a highly stylized cultivation UI experience.

## ✨ Core Features

*   **Complete Cultivation System**: Includes realms such as Mortal, Qi Condensation, Foundation Establishment, Core Formation, and Nascent Soul. Each realm provides different attribute bonuses (Spiritual Sense, Comprehension, Charisma, Movement Speed, etc.) and breakthrough thresholds.
*   **Visual Spell Editor**: Say goodbye to hardcoded skills! The mod features a node-based canvas system where players can drag and connect nodes to freely combine basic types (Projectile, AOE, Self) and actions to customize their own spell logic.
*   **Custom Immersive UI**: A cultivation-style interface featuring cyan Qi and golden borders. Includes typewriter effect text, smoothly interpolated Qi/Mana HUDs, and detailed character status panels.
*   **Meditation System**: Combines keybindings, effects, animations, and network synchronization to implement a full meditation gameplay loop for restoring Qi.
*   **Secure Data Persistence**: Uses Gson to serialize player cultivation data into JSON files, stored in the `config/xuanlingmod/` directory, supporting categorized management for skills, spells, realms, and energy.

## 🛠️ Tech Stack & Environment

*   **MC Version**: 1.21.1
*   **Mod Loader**: Forge
*   **Language**: Java 21
*   **Build Tool**: Gradle

## 📦 Installation Guide

### For Developers
1.  Clone this repository:
    ```bash
    git clone <your-repository-url>
    ```
2.  Navigate to the project directory and build:
    ```bash
    cd xuanlingmod
    ./gradlew build
    ```
3.  The built artifact will be located in the `build/libs/` directory.

## 🏗️ Architecture Overview

The project adopts a standard Client/Server separated architecture:

*   **Data & Persistence**: `DataManager.java` handles JSON I/O; `BasicData.java` defines base attributes; `BasicRealm.java` hardcodes the realm system.
*   **Network Sync**: The `network/` package defines `CustomPackageImp` and various Payloads to ensure real-time two-way sync between server data and client UI.
*   **Component System**: `api/IComponent.java` defines standard behaviors; `data/component/SpellComponent.java` handles spell logic composition.
*   **GUI & Rendering**: `util/gui/` contains the custom UI library (layouts, labels, buttons); `gui/component/` provides the visual node editor; `TopLeftQiHud.java` handles top-left HUD rendering.
*   **Events & Interaction**: `event/NormalPlayerEvent.java` handles player login, death, interactions, and meditation logic.

## 📜 License

This project is licensed under the [Apache License](LICENSE).
