# SystemBound:The Paradigm Trials

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
- QuestionBank.java        Loads and validates questions from JSON
- QuestionType.java        Future theory/programming classification
```

## Expanding the game later

Edit question content in `src/resources/questions.json`. The existing `QuestionBank` selects questions by trial ID for the game and review screens.

The player and gate currently use JavaFX shapes. Replace the visual nodes in `Player` and `Gate` with images or sprites later without changing the trial logic.

## Editing the question bank

Question content lives in `src/resources/questions.json`. Java loads this file
once when the game creates its `QuestionBank`; the trial and review screens use
the resulting `Question` objects.

Each JSON object contains `id`, `type`, `topic`, `questionText`, `choices`,
`correctAnswerIndex`, `explanation`, `difficulty`, `trialId`, `progType`, and
`progLang`. Match those names exactly.

- Use unique positive IDs and types `THEORY` or `PROGRAMMING`.
- Supply four non-empty choices. Answer indexes start at zero: A = 0, B = 1,
  C = 2, D = 3. If you reorder choices, update the answer index.
- Keep difficulty between 1 and 5 and trial IDs between 1 and 8.
- Programming questions need `progType` (for example, `Type A`) and
  `progLang` (for example, `Java`). Theory questions use `null` for these.
- In strings, write line breaks as `\n` and embedded double quotes as `\"`.
  JSON does not allow comments or trailing commas.
- The existing course checks require 145 questions: 70 theory and 75
  programming, including 10 theory questions in each of Trials 1 through 7.
  Topic names must match the course topics listed in `QuestionBank`.

Loading checks for missing fields, invalid answers, duplicate IDs, and invalid
question data before the game uses the bank. Run `mvn test` after editing.

The file is bundled into the game by Maven. Restart after editing it during
development; rebuild the packaged game to distribute updated questions.
No Java question arrays need to be edited.
