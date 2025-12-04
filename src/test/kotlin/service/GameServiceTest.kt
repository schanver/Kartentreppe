package service
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.checkNotNull

import entity.*

/**
 * This class tests the functions implemented in [GameService] class.
*/
class GameServiceTest {

  private lateinit var rootService : RootService
  private lateinit var rootService2 : RootService
  private lateinit var testRefreshable : TestRefreshable

  /**
   * initialises the [rootSrvice] object before every test, hence the @BeforeTest tag
  */
  @BeforeTest
  fun setup() {
      rootService = RootService()
      rootService2 = RootService()
      testRefreshable = TestRefreshable(rootService)
      rootService.addRefreshable(testRefreshable)
  }


  /**
   * If there is no game set to currentGame
   * this should check if it is null
  */ 
  @Test
  fun errorWhenGameIsNullTest() {
      val game = rootService2.currentGame
      assertNull(game, "Game should've been null")
  }

  /**
   * tests the startGame() function
  */
    @Test
    fun startGameTest() {
        assertFalse(testRefreshable.refreshAfterStartGameCalled)
        rootService.gameService.startGame("Max","Marie")
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        val player1 = game.players[0]
        val player2 = game.players[1]
        assertEquals(5,player1.hand.size, "Player 1 should have 5 cards at his hand")
        assertEquals(5,player2.hand.size, "Player 2 should have 5 cards at his hand")
        assertEquals(27,game.drawStack.size, "Draw stack should've had 27 cards")
        assertEquals(15,game.stairs.sumOf { it.size }, "The stairs should've had 15 cards")
        assertTrue(testRefreshable.refreshAfterStartGameCalled)
        testRefreshable.reset()
    }

    /**
     * tests the startTurn() function
    */
    @Test
    fun startTurnTest() {
        assertFalse(testRefreshable.refreshAfterStartGameCalled)
        rootService.gameService.startGame("Max","Marie")
        assertTrue(testRefreshable.refreshAfterStartGameCalled)
        testRefreshable.reset()
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        assertFalse(testRefreshable.refreshAfterStartTurnCalled)
        rootService.gameService.startTurn()
        val player1 = game.players[0]
        val player2 = game.players[1]
        assertTrue(game.players[game.currentPlayer % 2] == player1, "Player 1 should've started the turn")
        assertFalse(game.players[game.currentPlayer % 2] == player2, "Player 2 shouldn't have started the turn")
        assertTrue(testRefreshable.refreshAfterStartTurnCalled)
        testRefreshable.reset()
    }
    /**
    * tests the createDrawStack function
    */
    @Test
    fun createDrawStackTest() {
        rootService.gameService.startGame("Max","Marie")
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        assertTrue(game.drawStack.size == 27, "The draw stack should've gotten 27 cards")
    }
    /**
     * tests the createStairs() function
    */
    @Test
    fun createStairsTest() {
        rootService.gameService.startGame("Max","Marie")
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        val stairSize = game.stairs.sumOf { it.size }
        assertTrue(stairSize == 15, "The stairs should've contained 15 cards")
    }

    /**
     * tests the endGame() function
    */
    @Test
    fun endGameTest() {
      rootService.gameService.startGame("Max","Marie")
      val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
      assertFalse(testRefreshable.refreshAfterEndGameCalled)
      game.players[0].score = 10
      game.players[1].score = 20
      rootService.gameService.endGame()
      assertTrue(testRefreshable.refreshAfterEndGameCalled)
      assertTrue(testRefreshable.winner == game.players[1]) 
      testRefreshable.reset()

      rootService.gameService.startGame("Leon", "Chris")
      val game2 = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
      game2.players[0].score = 20
      game2.players[1].score = 10
      rootService.gameService.endGame()
      assertTrue(testRefreshable.refreshAfterEndGameCalled)
      assertTrue(testRefreshable.winner == game2.players[0]) 
      testRefreshable.reset()

      rootService.gameService.startGame("Leon", "Chris")
      val game3 = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
      game3.players[0].score = 20
      game3.players[1].score = 20
      rootService.gameService.endGame()
      assertTrue(testRefreshable.refreshAfterEndGameCalled)
      assertNull(testRefreshable.winner)
      testRefreshable.reset()

    }

    /**
     * tests the endTurn() function
    */
    @Test
    fun endTurn() {
        rootService.gameService.startGame("Max","Marie")
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        assertFalse(testRefreshable.refreshAfterEndTurnCalled)
        rootService.gameService.endTurn()
        val player2 = game.players[1]
        assertFalse(player2.hasCombinedCards)
        assertFalse(game.hasDestroyedCard)
        assertTrue(player2 == game.players[game.currentPlayer % 2], "Player 1's turn should've ended")
        assertTrue(testRefreshable.refreshAfterEndTurnCalled)
        testRefreshable.reset()
      }

    /**
     * test the shuffleDrawStack() function
    */
    @Test
    fun shuffleDrawStackTest() {
        rootService.gameService.startGame("Max","Marie")
        val game = checkNotNull(rootService.currentGame) { "Game shouldn't be null" }
        val originalDrawStack = game.drawStack.toMutableList()
        assertFalse(testRefreshable.refreshAfterShuffleStackCalled)
        rootService.gameService.shuffleDrawStack()
        assertFalse(originalDrawStack == game.drawStack,
        "The draw stack should've been shuffled but it stayed the same (1 in 10888869450418352160768000000 chance")
        assertTrue(testRefreshable.refreshAfterShuffleStackCalled)
        testRefreshable.reset()

      }


}
