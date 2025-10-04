# Project Report

## Design Decisions

### Architecture
The project follows the MVC structure:
- Model: Contains the main game logic (`Card`, `Board`, `GameModel`). 
- View: Implemented in `FXMemoryGame`, responsible for creating the grid, updating the visuals, and showing the timer and messages.
- Controller: `GameController` manages the user input, coordinates between model and view, checks for matching cards, and controls the timer.
We chose **JavaFX** over Swing for its more modern design options and easier grid layouts. With that being said there is a Swing implementation in the project code. Be sure to use the JavaFX version.

### Data Structures
- The game board uses a 2D array (`Card[][]`) to form the grid.
- ArrayList is used to both randomize and assign pairs.
- Integers and booleans are used to track card IDs, states, and matches.
These structures provide quick access and are easy to manage for small grids.

### Algorithms
- Shuffle: Uses `Collections.shuffle()` for random card id distribution.
- Match Checking: Constant-time O(1) comparison between two flipped cards.
- Game Completion: Linear O(n) check to iterate through all the cards and see if they are matched

## Challenges Faced
1. Synchronizing UI with Logic and getting JavaFX up and running
   - **Solution:** We added JavaFX to our project structure and the VM option, then we added an `inputLocked` flag to prevent user clicks during animations.
   
2. **Making a timer and dealing with conflicts**
   - **Solution:** Controlled the JavaFX `Timeline` through the controller to make sure of consistent countdown behavior. 

## What We Learned
- We Strengthened OOP principles and MVC separation.
- We learned how to use JavaFX event handle timelines.
- We gained a better understanding of how to connect UI and model logic.

## If We Had More Time
- We would add a system to pause the game
- A difficulty slider where you could adjust how much time you have and how long the cards stay flipped before turning over.
- Sound effects for matched cards and unmatched cards.
-Some form of leaderboard or a personal ranking system