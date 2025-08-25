## Overview

Fish Game is a side-scrolling game where you play as a fish trying to survive in a pond full of other fish. The goal is to eat smaller fish to gain experience and level up, while avoiding being eaten by larger fish. The game ends when your fish reaches the maximum level or is eaten by an enemy.

Hook Game is a sequel game where you play as a worm on the hook sinking deeper and deeper, popping bubbles and dodging fish for as long as you can to get the highest score. The game ends when your worm is eaten by an enemy.

## Key Concepts and Structure

- **Predicate and Comparator Interfaces:**  
  The game defines interfaces for predicates and comparators, allowing flexible logic for filtering and comparing fish, such as determining if a fish is offscreen or if one fish can eat another.

- **List Abstractions:**  
  Custom `IList<T>` and its implementations (`MtList`, `ConsList`) provide map, filter, fold, and other common list operations for handling fish and enemies in the game.

- **Fish Representation:**  
  Each fish has coordinates, velocity, level, experience, score, facing direction, and color. Fish can move, collide, eat, and be drawn on the screen.

- **Game World (`FishGame`):**  
  The main game logic is encapsulated in `FishGame`, which extends a World class and manages the player, enemies, random events, and world updates.  
  - The game spawns enemies at random intervals and positions.
  - The player can move using keyboard arrows.
  - Collision handling allows for eating (leveling up) or being eaten (game over).
  - The game world ends if the player reaches level 6 or is eaten.

- **Functional and Object-Oriented Patterns:**  
  The game leverages function objects and method abstractions for flexible event handling and list processing.

## Main Classes and Their Responsibilities

- **Fish:** Handles movement, drawing, collision, eating, leveling, and screen boundary logic.
- **FishGame:** Manages the world state, player and enemy spawning, ticking (game updates), key events, and end conditions.
- **APosn:** Position abstraction for movement and placement.
- **IList, MtList, ConsList:** Custom list implementation for functional manipulation of fish collections.
- **Utils:** Helper methods for list construction and generation.
- **Function and Predicate Interfaces:** Abstract behavior for flexible filtering, mapping, and comparison.
