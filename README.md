# Kartentreppe 
A two-player board game written in Kotlin using [BGW](https://github.com/tudo-aqua/bgw)-Framework.
![GameBoard](assets/Kartentreppe.png)

# Requirements
- Requires Java 11+.
# How to run
First, unpack the file from the releases and run the scripts in `bin` \
On Windows:
```bat
./bin/Kartentreppe.bat
```
On MacOS/Linux:
```bash
./bin/Kartentreppe
```
If you clone the repository, then: 
``` bash
./gradlew run
```
# Building from Source
clone the project or download the source code from the releases
``` bash
./gradlew build 
```
# Gameplay
![Gameplay](assets/gameplay.gif)
Players take turns playing a card from their hand and combining with a face-up card on the card stairscase
A card can be player if it either:
- has the same value as the target card, or
- has the same suit as the target card

If a face-up card has been successfully combined with a card from the hand, both cards are removed and the player receives points

If the current player doesn't want to combine cards or simply can't, they may alternatively discard a card from their hand. The card is then placed on the discard pile.

Each player may destroy one face-up card once per turn. The card is then placed on the discard pile and the face-down card underneath is revealed. The player continues their turn. This action costs the player 5 points.

## End Game
The goal of the game is to get the most points possible. The game ends:
- if there are no more cards left on the stairs, or 
- if the draw stack is empty and no cards were removed from the stairs since the last mix.





