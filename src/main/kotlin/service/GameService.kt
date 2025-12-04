package service
import entity.*
import gui.KartentreppeScene
import gui.CardImageLoader
import tools.aqua.bgw.components.gamecomponentviews.CardView
import kotlin.collections.shuffle

/**
 *  This class contains the functions that the system calls during/before/after the game.
 */ 
class GameService(private val rootService: RootService) : AbstractRefreshingService() {


  /**
   * starts the game by initialising the values and stuff
   * @param playerName1 is the name of the first player
   * @param playerName2 is the name of the second player
   * @throws IllegalStateException if the [game] is null
  */ 

  fun startGame(playerName1 : String, playerName2 : String) : Unit {
    try {
    val player1 = Player(playerName1)
    val player2 = Player(playerName2)
    val players = listOf(player1,player2)
    rootService.currentGame = Kartentreppe(players=players)
    val game = checkNotNull(rootService.currentGame) { "Game is null" }

    val shuffledStack = CardValue.values().flatMap { value ->
      CardSuit.values().map { suit ->
        Card(value, suit)
      }
    }
    .shuffled().toMutableList()

    val handOfPlayer1 = shuffledStack.take(5)
    game.players[0].hand.addAll(handOfPlayer1) 
    shuffledStack.removeAll(handOfPlayer1)

    val handOfPlayer2 = shuffledStack.take(5)
    game.players[1].hand.addAll(handOfPlayer2) 
    shuffledStack.removeAll(handOfPlayer2)

    require(player1.hand.size == 5) {"${player1.name} should have 5 cards"}
    require(player2.hand.size == 5) {"${player2.name} should have 5 cards"}

    createDrawStack(shuffledStack) 
    createStairs(shuffledStack)

    onAllRefreshables { refreshAfterStartGame() }
  }
  catch(e: IllegalStateException) {
    println(e.message)
  } catch(e: IllegalArgumentException) {
    println(e.message)
  }
}

  /**
   * this function starts the turn of the player
   * @throws IllegalArgumentException if the player doesn't have 5 cards at hand at the start of the turn
  */ 
  fun startTurn() : Unit {
    try {
      val game = checkNotNull(rootService.currentGame) { "Game is not null" }
      val playerWithTurn = game.players[game.currentPlayer % 2]

      playerWithTurn.hasCombinedCards = false
      game.hasDestroyedCard = false

      game.log.clear()
      onAllRefreshables { refreshAfterStartTurn() }
    } catch(e: IllegalStateException) {
      println(e.message)
    } catch(e: IllegalArgumentException) {
      println(e.message)
    }
  }


  /**
   * this function creates a drawStack
   */ 
  private fun createDrawStack(shuffledStack : MutableList<Card>) : Unit {
    try {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    val takenCards = shuffledStack.take(27)
    game.drawStack.addAll(takenCards)
    shuffledStack.removeAll(takenCards)
    require(game.drawStack.size == 27) { "Not enough cards have been put into the drawStack!" }
  }
  catch(e: IllegalStateException) {
    println(e.message)
  } catch(e: IllegalArgumentException) {
    println(e.message)
  }
}

  /**
   * this function creates a list of lists that represent the stairs in the game at the start
   * @throws IllegalStateException if the game is null
  */

  private fun createStairs(shuffledStack : MutableList<Card>) : Unit {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    game.stairs.clear()
    for(i in 5 downTo 1) {
      val takenCards = shuffledStack.take(i).toMutableList() 
      game.stairs.add(takenCards)
      shuffledStack.removeAll(takenCards)
    }

  }

  /**
   * endGame function ends the game showing the winner (if there is one)
   * @throws IllegalStateException if the [game] is null
  */ 
  internal fun endGame() : Unit {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    var winner : Player? = null
    val scoreDifference = game.players[0].score - game.players[1].score
    if(scoreDifference > 0) {
    winner = game.players[0]
    game.log.add("${game.players[0]} has won!")
    } else if(scoreDifference < 0) {
      winner = game.players[1]
      game.log.add("${game.players[1]} has won!")
    } else {
      game.log.add("Game has ended in a draw")
    }
    onAllRefreshables { refreshAfterEndGame(winner) }
    rootService.currentGame = null
  }

  /**
   * ends the turn by incrementing [currentPlayer] and calling [refreshAfterEndTurn]
   * it also resets [hasCombinedCards] and [hasDestroyedCard] so that it doesn't end the game if the draw stack is empty
   */ 
  internal fun endTurn() : Unit {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    rootService.playerActionService.drawCard()
    if(rootService.currentGame != null) {
    game.currentPlayer = (game.currentPlayer + 1) % game.players.size
    onAllRefreshables { refreshAfterEndTurn() }
  }
  }

  /**
   * shuffles the cards in the [discardStack]
   * @throws IllegalStateException if the [game] is null
   *
  */ 
  internal fun shuffleDrawStack() : Unit {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    game.drawStack.shuffle()

    onAllRefreshables { refreshAfterShuffleStack() }
  }

}
