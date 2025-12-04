package entity

/** 
 * Player data class contains the information used in the game for players
 * @property name is the name of the player
 * @property score is the score of the player
 * @property hand is the list of the card(s) the player has at his/her hand
 * @property combinedCards is the list of cards which were combined during their turn
*/

class Player(
        val name: String,
        var score: Int = 0,
        val hand: MutableList<Card> = mutableListOf(),
        val combinedCards: MutableList<Card> = mutableListOf(),
        var hasCombinedCards: Boolean = false
)
