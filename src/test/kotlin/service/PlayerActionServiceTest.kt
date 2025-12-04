package service

import entity.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

/**
 * This class contains the functions that tests the functionality of [PlayerActionService]
 */ 
class PlayerActionServiceTest {

  private lateinit var rootService : RootService
  private lateinit var player1 : Player
  private lateinit var player2 : Player
  private lateinit var playersList : List<Player>
  private lateinit var game : Kartentreppe
  private lateinit var testCard : Card
  private lateinit var testRefreshable : TestRefreshable
  
  /**
   * This sets up the varibles that are used in every test. It runs before the tests, hence the @BeforeTest tag
  */ 
  @BeforeTest
  fun setup() {
    
    testCard = Card(CardValue.FOUR, CardSuit.HEARTS)
    player1 = Player("Max",score=6)
    player2 = Player("Marie")
    playersList = listOf(player1,player2)
    game = Kartentreppe(players = playersList)
    game.currentPlayer = 0
    rootService = RootService()
    testRefreshable = TestRefreshable(rootService)
    rootService.currentGame = game
    rootService.addRefreshable(testRefreshable)

    game.stairs.add(
      mutableListOf(
        Card(CardValue.TWO, CardSuit.HEARTS),
      )
    )
    game.stairs.add(
      MutableList(1) {
        Card(CardValue.JACK, CardSuit.SPADES) 
      }
    )
    game.discardStack.add(Card(CardValue.ACE, CardSuit.HEARTS))
    game.discardStack.add(Card(CardValue.SIX, CardSuit.CLUBS))
    game.discardStack.add(Card(CardValue.SEVEN, CardSuit.SPADES))

    repeat(5) { 
      game.players[0].hand.add(testCard)
      game.players[1].hand.add(testCard)
    }

    game.drawStack.add(Card(CardValue.QUEEN,CardSuit.SPADES))
    game.drawStack.add(Card(CardValue.QUEEN,CardSuit.SPADES))
  }

  /**
   * tests the functionality of the function destroyFromStairs()
   */ 
  @Test
  fun destroyFromStairsTest() {

    assertFalse(testRefreshable.refreshAfterDestroyCardCalled)
    val cardToDestroy = game.stairs[1][0]
    rootService.playerActionService.destroyFromStairs(cardToDestroy)
    assertTrue(testRefreshable.refreshAfterDestroyCardCalled, "refreshAfterDestroyCard(card) should've been called")

    assertTrue(game.hasDestroyedCard, "The card should've been destroyed")
    assertTrue(game.discardStack.size == 4, "The discard stack should've gotten 4 cards in it")
    assertEquals(cardToDestroy,game.discardStack.last(),
    "The test card should be the same card that is added last in the discard stack")
    assertEquals(1,player1.score, "Max' score should've been reduced to 1")
    player2.score = 10
    player2.hand.removeAt(player2.hand.size - 1)
    rootService.gameService.endTurn()
    assertTrue(testRefreshable.refreshAfterEndTurnCalled)
    rootService.gameService.startTurn()

    assertTrue(testRefreshable.refreshAfterStartTurnCalled)
    testRefreshable.reset()
    assertFalse(testRefreshable.refreshAfterEndGameCalled)
    rootService.playerActionService.destroyFromStairs(game.stairs[0][0])
    assertTrue(testRefreshable.refreshAfterEndGameCalled, "refreshAfterEndGame() should've been called")

    assertTrue(game.hasDestroyedCard, "The card should've been destroyed")
    assertEquals(game.stairs.sumOf { it.size },0, "The stairs should've been empty!")
    assertEquals(5,player2.score, "Marie's score should have been 5")

    testRefreshable.reset()
  }
  
  /**
   * tests the funtionality of discardCard(handCard : Card) function
   */ 
  @Test
  fun discardCardTest() {

    assertFalse(testRefreshable.refreshAfterDiscardCardCalled)
    rootService.playerActionService.discardCard(testCard)
    assertTrue(testRefreshable.refreshAfterDiscardCardCalled, "Expected refreshAfterDiscardCard(...) to be called")

    assertTrue(game.players[0].hand.size == 5, "Player should've only had five cards!")
    assertEquals(Card(CardValue.QUEEN,CardSuit.SPADES), 
                game.players[0].hand.last(), 
                "The drawn card should've been ${Card(CardValue.QUEEN, CardSuit.DIAMONDS).toString()}") 
    assertTrue(testCard in game.discardStack, "The test card should've been in discardStack")
    assertEquals(4, game.discardStack.size, "Discard stack should have the size of 4!")
    assertTrue(testRefreshable.refreshAfterEndTurnCalled)
    rootService.gameService.startTurn()

    testRefreshable.reset()
    assertFalse(testRefreshable.refreshAfterDiscardCardCalled)
    assertFalse(testRefreshable.refreshAfterShuffleStackCalled)

    game.drawStack.clear()
    val cardToDiscard = game.players[1].hand.first()
    game.hasDestroyedCard = true
    rootService.playerActionService.discardCard(cardToDiscard)
    assertTrue(testRefreshable.refreshAfterDiscardCardCalled)
    assertTrue(testRefreshable.refreshAfterShuffleStackCalled)
    rootService.gameService.startTurn()  
    assertTrue(testRefreshable.refreshAfterStartTurnCalled)
    testRefreshable.reset()


    assertFalse(testRefreshable.refreshAfterDiscardCardCalled)
    assertFalse(testRefreshable.refreshAfterEndGameCalled)
    game.discardStack.addAll(game.drawStack)
    game.drawStack.clear()
    game.hasDestroyedCard = false
    game.players[0].hasCombinedCards = false

    rootService.playerActionService.discardCard(game.players[0].hand.first())

    assertTrue(testRefreshable.refreshAfterDiscardCardCalled)
    assertTrue(testRefreshable.refreshAfterEndGameCalled)
  }


  /**
   * tests the functionality of combineCards(handCard : Card, stairCard : Card) method
  */
  @Test
  fun combineCardsTest() {
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    val chosenCard = game.players[game.currentPlayer % 2].hand[0]
    val player1 = game.players[game.currentPlayer % 2]
    val pointDifference = player1.score

    assertFalse(testRefreshable.refreshAfterCombineCardsCalled)
    assertFalse(testRefreshable.refreshAfterEndTurnCalled)

    rootService.playerActionService.combineCards(chosenCard, game.stairs[0][0])

    assertTrue(testRefreshable.refreshAfterCombineCardsCalled)
    assertTrue(testRefreshable.refreshAfterEndTurnCalled)

    assertTrue(player1.combinedCards.size == 2, "Player's combined cards should be 2!")
    assertEquals(6, player1.score - pointDifference, "The player should've gotten 6 points")
    assertNotEquals(player1.name, game.players[game.currentPlayer % 2].name)

    // Set player2's hand so the stairs will be emptied
    val player2 = game.players[game.currentPlayer % 2]
    player2.hand.clear()
    repeat(5) { player2.hand.add(Card(CardValue.JACK, CardSuit.SPADES)) }
    val cardToChoose = player2.hand[0]

    assertFalse(testRefreshable.refreshAfterEndGameCalled)

    rootService.playerActionService.combineCards(cardToChoose,game.stairs[1][0])

    assertTrue(testRefreshable.refreshAfterCombineCardsCalled)
    assertTrue(testRefreshable.refreshAfterEndGameCalled)
  }


}
