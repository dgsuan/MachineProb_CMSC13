# SYSTEM ESCAPE

A small JavaFX skeleton for the CMSC 13: Survey of Programming Paradigms Machine Problem.

The current version implements one complete gameplay loop:

1. Open the main menu.
2. Select **PLAY**.
3. Move through a simple horizontal system map.
4. Approach the **TRIAL 1** gate and press `E`.
5. Answer five programming-paradigm questions using the mouse.
6. Earn at least 3 EXP to complete the trial, or retry from its checkpoint after failing.

## Requirements

- JDK 11 or newer (JDK 17 or 21 is recommended)
- Maven 3.8 or newer

JavaFX dependencies are configured in [pom.xml](pom.xml).

## Run the game

From the project folder, run:

```powershell
mvn clean javafx:run
```

To compile and run the automated checks without opening the game window:

```powershell
mvn clean test
```

## Controls

| Key | Action |
| --- | --- |
| `A` / Left Arrow | Move left |
| `D` / Right Arrow | Move right |
| `Space` | Jump |
| `E` | Enter Trial 1 when near the gate |
| `Enter` | Return to the map after a trial result |
| `Esc` | Return to the main menu from the world |

Quiz answers and menu options use clickable JavaFX buttons.

## Project structure

```text
src/main/java/cmsc13/game/
- Main.java                Maven/JavaFX entry point
- GameApplication.java     Menu, map, quiz overlay, and game flow
- GameState.java           Current screen/state enum
- Player.java              Movement, jump, gravity, and respawn
- Gate.java                Proximity-based Trial 1 gate
- Trial.java               Question progress and EXP evaluation
- Question.java            Reusable question data model
- QuestionManager.java     Five sample Topic 1 questions
- QuestionType.java        Future theory/programming classification
```

## Expanding the game later

To add Trial 2, add a second `Gate`, store its questions in `QuestionManager`, and create a `Trial` from that question list. Player movement, the world viewport, and the quiz UI can remain the same.

The player and gate currently use JavaFX shapes. Replace the visual nodes in `Player` and `Gate` with images or sprites later without changing the trial logic.
