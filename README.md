# assignment-jaykybert
assignment-jaykybert created by GitHub Classroom

Dots and Boxes Extensions Implemented

Part 1 - Logic
  - Support for any grid dimensions: This is capped to 15x15 on the UI.
  - Support for 5 players and 5 bots simultaneously: Capped at 10 due to the need for lots of paint objects causing clutter.
  - Improved AI with 3 levels of difficulty: Making random moves, avoiding 2-line boxes, and completing 3-line boxes.

Part 2 - User Interface
  - Main Menu where the user can change players, bots, difficulty, columns, rows, and whether to shuffle the player list.
  - UI elements (top banner, text, dots, and lines) scale to the game grid and the phone size.
  - Boxes will change to their owner's colour when completed.
  - Text indicating current player turn and their score, displayed in the player's colour.
  - End of game text displaying the winner (accounts for ties).
  - Toast messages that notify the user if a line is already drawn, out-of-bounds, or if they haven't picked any players in the menu.
  - Toast message at the end of the game with all player names and their respective scores.
