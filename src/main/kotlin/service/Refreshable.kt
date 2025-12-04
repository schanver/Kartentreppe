package service

import entity.*
/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the GUI classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * GUI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 */

interface Refreshable {

  /**
   * this refreshes the GUI after the startGame() function is called
   */ 
    fun refreshAfterStartGame() : Unit {}

  /**
   * this refreshes the GUI after the endTurn() function is called
   */ 
    fun refreshAfterEndTurn() : Unit {}

  /**
   * this refreshes the GUI after the destroyFromStairs() function is called
   * æparam stairCard is the [Card] object players destroys on the stairs
   */ 
    fun refreshAfterDestroyCard(stairCard : Card) : Unit {}
  /**
   * this refreshes the GUI after the endGame() function is called
   * @param winner is the [Player] object we pass to the GUI, null represents the draw
   */
    fun refreshAfterEndGame(winner : Player?) : Unit {}
/**
   * this refreshes the GUI after the combineCards() function is called
   * @param handCard is the [Card] object the player has on the hand
   * @param stairCard is the [Card] object the player selects on the stair
   */
    fun refreshAfterCombineCards(handCard : Card, stairCard : Card) : Unit {}
/**
   * this refreshes the GUI after the discardCard() function is called
   * @param handCard is the [Card] object the player has on the hand
   */
    fun refreshAfterDiscardCard(handCard : Card) : Unit {}
  /**
   * this refreshes the GUI after the shuffleDrawStack() function is called
   */
    fun refreshAfterShuffleStack() : Unit {}
  /**
   * this refreshes the GUI after the startTurn() function is called
   */
    fun refreshAfterStartTurn() : Unit {}

}
