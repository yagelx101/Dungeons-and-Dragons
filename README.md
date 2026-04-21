Dungeons and Dragons CLI Game 🐉

Project Overview 🏰

A single-player, multi-level terminal-based board game inspired by Dungeons and Dragons.

The player navigates through dungeon levels, battling monsters and avoiding traps to reach the end.

Features a turn-based combat system, experience progression, and multiple unique playable characters.

System Architecture & Design 🏗️

Language: Java

Design Patterns:

Visitor Pattern: Used to elegantly handle double-dispatch interactions between different game tiles (Players, Enemies, Walls, Empty space) without relying on type casting or instanceof.

Observer / Callbacks Pattern: Decouples the game's business logic from the Command Line Interface (CLI), ensuring a clean architecture.

OOP Principles: Extensive use of Inheritance and Polymorphism to manage varied unit behaviors, combat mechanics, and special abilities.

Playable Classes 🛡️

Warrior: Uses Health as a resource. Features high defense and the Avenger's Shield ability.

Mage: Uses Mana. Excels at range with the Blizzard ability.

Rogue: Uses Energy. A fast-paced combatant with the Fan of Knives area-of-effect ability.

Hunter (Bonus): Uses Arrows. Employs precise ranged attacks with the Shoot ability.

Enemies & Obstacles 🧟

Monsters: Actively roam the map and chase the player if within their vision range.

Traps: Stationary hazards that alternate between visible and invisible states.

Bosses (Bonus): Powerful elite enemies that combine monster movement with unique special abilities (e.g., Shoebodybop).

Game Controls 🎮

w: Move up

s: Move down

a: Move left

d: Move right

e: Cast special ability

q: Do nothing (skip turn)

Building and Running the Game 🖥️

The game receives a directory path containing the level files (level1.txt, level2.txt, etc.) as a command-line argument.

Run the Game: java -jar hw3.jar <path_to_levels_directory>
