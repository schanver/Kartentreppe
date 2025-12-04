package entity

/**
 * The entity class that that stores the card stacks and some vital information for the game
 * @property currentPlayer determines the current turn of the player
 * @property hasDestroyed allows systems to know whether the player already destroyed a card this turn
 * @property log is the list of string we store our logs
 * @property drawStack is the list of cards we keep as our draw stack
 * @property discardStack is the list of cards we store our discarded cards
 * @property stairs is the list of cards we set to display at the center of the game board
*/
class Kartentreppe(
        var currentPlayer: Int = 0,
        var hasDestroyedCard: Boolean = false,
        val log: MutableList<String> = mutableListOf(),
        val players: List<Player> = listOf(),
        val drawStack: MutableList<Card> = mutableListOf(),
        val discardStack: MutableList<Card> = mutableListOf(),
        val stairs: MutableList<MutableList<Card>> = mutableListOf()
)
