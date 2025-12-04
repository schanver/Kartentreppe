package service
import entity.*
import kotlin.collections.indexOf
import kotlin.collections.removeFirst
import kotlin.IllegalStateException
/**
 * this class handles the action a player can make during the game 
 * such as drawing, discarding, combining or destroying a card
 * @property rootService allows us to link the [RootService] with [PlayerActionService]
 * and [GameService]
 */ 
class PlayerActionService(private val rootService : RootService) : AbstractRefreshingService() {

  /**
   * this is a helper function 
   * @param playerWithTurn the player
   */ 
  internal fun prepareDrawStack(playerWithTurn: Player) {
    val game = checkNotNull(rootService.currentGame) { println("The game is null")
    "The game is null" }
    val needsRefill : Boolean = game.hasDestroyedCard || playerWithTurn.hasCombinedCards
    if(game.drawStack.isNotEmpty()) {
      return
    }
    else if(needsRefill) {
      game.drawStack.addAll(game.discardStack)
      game.discardStack.clear()
      rootService.gameService.shuffleDrawStack()
    }

  }
  /**
   * drawCard() function draws a [Card] from [drawStack] and adds it to player's [hand]
   * It first checks if there's any cards left in drawStack, if not it checks if 
   * @throws IllegalArgumentException if the game is not started or running
   * since we shuffled our [drawStack] at the start of the game we select the first element of the list.
   *
   */ 
  internal fun drawCard() : Unit {
    val game = checkNotNull(rootService.currentGame) { println("Game is null") }
    val playerWithTurn = game.players[game.currentPlayer % 2]

    prepareDrawStack(playerWithTurn)

    if(game.drawStack.isEmpty()) {
      rootService.gameService.endGame()
      return
    }

    val drawnCard : Card = game.drawStack.removeAt(0)
    playerWithTurn.hand.add(drawnCard)

  }
  /**
   * the method makes it possible for the user to combine one of 
   * his/hers card on hand with a revealed card on the stairs,
   * if the cards have the same value or same value and 
   * it adds the addition of the values of the cards to player's score
   * @param handCard is a [Card] object we select from our hand
   * @param stairCard is a [Card] object on the stair
   * Prerequisites:
   * - Game should be running 
   * - The handCard should be in player's hand
   * - The selected card on the stairs should've been already revealed
   * Postcondition(s);
   * - both cards are removed from the respective lists
   * - combined points are added to the player score
   * - both cards are sent to the [combined] stack of player
   * - Player's turn ends
   */ 
  fun combineCards(handCard : Card, stairCard : Card) : Unit {
    try {
      val game = checkNotNull(rootService.currentGame) { println("Game is null") }
      val playerWithTurn = game.players[game.currentPlayer % 2]
      check(handCard.compareValue(stairCard) == 0 ||
      handCard.compareSuit(stairCard)) { "This is an invalid combination" }
      val combinedScore : Int = stairCard.value.toInt() + handCard.value.toInt()
      playerWithTurn.score += combinedScore 
      playerWithTurn.hand.remove(handCard)
      playerWithTurn.hasCombinedCards = true
      playerWithTurn.combinedCards.add(handCard)

      for((i,step) in game.stairs.withIndex()) {
        val j = step.indexOf(stairCard) 
        if(j != -1) {
          step.removeAt(j)
          playerWithTurn.combinedCards.add(stairCard)
          game.log.add(
            "${playerWithTurn.name} combined ${handCard} with ${stairCard} ($i,$j) and got ${combinedScore} points. ")
            // Check if there are any cards left on the stairs, if not;then run the endGame() function
            if(game.stairs.all { it.isEmpty() }) {
              rootService.gameService.endGame()
              return
            }
            break
          }
        }
        onAllRefreshables { refreshAfterCombineCards(handCard,stairCard) }
        rootService.gameService.endTurn() 
      } catch(e: IllegalStateException) {
        println(e.message)
        throw e
      }
    }


    /**
     * this function sends the selected card from the hand to 
     * discardStack and draws a new card for the player 
     * before ending the turn
     * @param handCard is the card we want to discard
     * @throws IllegalStateException if the current game is null
     * @throws IllegalArgumentException if the [handCard] is not in player's [hand]
     */ 
  fun discardCard(handCard : Card) : Unit {
    try {
    val game = checkNotNull(rootService.currentGame) { println("Game is not started or not running right now") }
    val playerWithTurn = game.players[game.currentPlayer % 2]
    require(handCard in playerWithTurn.hand)
    playerWithTurn.hand.remove(handCard)
    game.discardStack.add(handCard)

    // Log it implicitely so that the other player doesn't know which cards the player discarded 
    game.log.add("${playerWithTurn.name} discarded a card. ")
    onAllRefreshables { refreshAfterDiscardCard(handCard) }
    rootService.gameService.endTurn()
  }
  catch(e: IllegalArgumentException) {
    println(e.message)
  } catch(e : IllegalStateException) {
    println(e.message)
  }
}

  /**
   * Allow the player to send a revealed card from the stairs to discard stack
   * in exchange for 5 points. 
   * Prerequisites:
   * - game should be running
   * - playerScore should be >= 5
   * Postcondition(s):
   * - selected card is sent to the discard stack
   * - player loses 5 points
   * - It is still the player's turn.
   *
   * @param stairCard is the selected card we want to destroy from the stairs
   * @throws IllegalStateException when the player has less than 5 points
   * @throws IllegalArgumentException when the game is not started/running
   */
  fun destroyFromStairs(stairCard : Card) : Unit {
    try {
      val game = checkNotNull(rootService.currentGame) { println("Game is null") }
      check(!game.hasDestroyedCard) { println("The player has already destroyed a card this turn!") }
      val playerWithTurn = game.players[game.currentPlayer % 2]
      require(playerWithTurn.score >= 5) { println("The player doesn't have enough points!") }
      for((i,step) in game.stairs.withIndex()) {
        val j = step.indexOf(stairCard) 
        if(j != -1) {
          step.removeAt(j)
          game.hasDestroyedCard = true
          game.discardStack.add(stairCard)
          playerWithTurn.score -= 5
          // Check if there are any cards left on the stairs, if not; then run the endGame() function
          if(game.stairs.all{ it.isEmpty() }) {
            rootService.gameService.endGame()
          }
          game.log.add("${playerWithTurn.name} destroyed the card ${stairCard.toString()} at position ($i, $j). ")
          break
        }
      }
      onAllRefreshables { refreshAfterDestroyCard(stairCard) }
    } catch(e: IllegalStateException) {
      println(e.message)
      throw e
    } catch(e: IllegalArgumentException) {
      println(e.message)
      throw e
    }
  }
}
