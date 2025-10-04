#  Memory-Based Card Matching Game (JavaFX)

A **tile-matching memory game** built with **JavaFX**, following the **Model-View-Controller (MVC)** architecture.  

The user flips over cards to find matching pairs before the timer runs out.  
This project showcases object-oriented programming principles.

---

## Team Members
**Elvin Nguyen, Caden Maki**

---

##  How to Run

### 1) Clone or download
```git clone [https://github.com/elvinnguyen/CardMatchingGame.git]
```

### 2) This project was ran on IntelliJ
1. Launch **IntelliJ IDEA**.  
2. Open the project folder.
3. Make sure your JavaFX SDK is installed.

### 3) Configure JavaFX VM options
Go to **Run -> Edit Configurations -> VM Options** and add:
```
--module-path [path_to_javafx_lib] --add-modules javafx.controls,javafx.fxml
```
### 4) Run the game
-This program also has a Swing version implemented; be sure to use the JavaFX version instead.
- Run the class `jfx.FXMemoryGame`.
- The game window will appear with a 4×4 grid of cards.

---

## Gameplay Overview

- The game starts with 16 face-down cards (8 pairs).
- Click on two cards to flip them:
- If they match, they stay face-up.
- If they don’t match, they flip back after a short delay.
- Each match increases your score.
- You have a time limit to find all pairs.
- When the timer hits 0, and you haven't found all pairs the game ends, and you lose.

---

##  Features Implemented

| Feature | Description |
|----------|--------------|
| **4×4 Grid Layout** | A 16-card grid created in JavaFX |
| **Card Flipping** | flipping logic with match detection |
| **Scoring System** | Increments score for each matched pair |
| **Countdown Timer** | A timer that counts down while you're playing |
| **MVC Architecture** | Separation between model, controller, and view |
| **Game Win/Lose Conditions** | Detects when all pairs are matched or time runs out |

---

## Controls

| Action | Description |
|--------|--------------|
| Left click mouse| Flip a card |
| Automatic | Timer starts at game launch |

---

## Known Issues
- There is no way to pause the game, we will introduce a system that will allow the player to pause the game  
- The user cannot adjust his timer, right now the only way to adjust the times is through changing the code in FXMemoryGame.java
- There are no sound effects for getting a matching pair or wrong pair.  

---

## External Libraries
**JavaFX SDK 25** — used for GUI rendering, animations, and user interaction.