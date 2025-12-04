package service
import entity.*



/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable(val rootService : RootService) : Refreshable {

  var refreshAfterStartGameCalled : Boolean = false
  private set

  var refreshAfterStartTurnCalled : Boolean = false
  private set

  var refreshAfterEndTurnCalled : Boolean = false
  private set

  var refreshAfterCombineCardsCalled : Boolean = false
  private set

  var refreshAfterDiscardCardCalled : Boolean = false
  private set

  var refreshAfterDestroyCardCalled : Boolean = false
  private set

  var refreshAfterEndGameCalled : Boolean = false
  private set

  var refreshAfterShuffleStackCalled : Boolean = false
  private set

  var winner : Player? = null

  /**
   * resets all called properties to false
  */ 
  fun reset() {
    refreshAfterStartGameCalled       = false
    refreshAfterStartTurnCalled       = false
    refreshAfterEndTurnCalled         = false
    refreshAfterCombineCardsCalled    = false
    refreshAfterDiscardCardCalled     = false
    refreshAfterDestroyCardCalled     = false
    refreshAfterEndGameCalled         = false
    refreshAfterShuffleStackCalled    = false
    winner                            = null
  }
  /**
   * just sets the refresh value to true for testing
  */ 
  override fun refreshAfterStartGame() {
    refreshAfterStartGameCalled = true
  }
  /**
   * just sets the refresh value to true for testing
  */ 
  override fun refreshAfterStartTurn() {
    refreshAfterStartTurnCalled = true
  }
  /**
   * just sets the refresh value to true for testing
  */ 
  override fun refreshAfterEndTurn() {
    refreshAfterEndTurnCalled = true
  }
  /**
   * just sets the refresh value to true for testing
   * @param handCard the [Card] to combine from hand
   * @param stairCard the [Card] to combine from stair
  */ 
  override fun refreshAfterCombineCards(handCard : Card, stairCard : Card) {
    refreshAfterCombineCardsCalled = true
  }
  /**
   * just sets the refresh value to true for testing
   * @param handCard the [Card] to discard
  */ 
  override fun refreshAfterDiscardCard(handCard : Card) {
    refreshAfterDiscardCardCalled = true
  }
  /**
   * just sets the refresh value to true for testing
   * @param stairCard the [Card] object to destroy 
   * from the stair
  */ 
  override fun refreshAfterDestroyCard(stairCard : Card) {
    refreshAfterDestroyCardCalled = true
  }
  /**
   * just sets the refresh value to true for testing
   * @param winner the [Player] object that represents the winner
  */ 
  override fun refreshAfterEndGame(winner : Player?) {
    this.winner = winner
    refreshAfterEndGameCalled = true
  }
  /**
   * just sets the refresh value to true for testing
  */ 
  override fun refreshAfterShuffleStack() {
    refreshAfterShuffleStackCalled = true
  }



}

