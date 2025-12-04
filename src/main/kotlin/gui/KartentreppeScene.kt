package gui

import entity.*
import service.Refreshable
import service.RootService
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.event.AnimationFinishedEvent
import kotlin.checkNotNull


/**
 * This class holds the ui elements of our game scene
 * @property rootService lets us link it with the service layer through the [RootService]
 *
 */ 
class KartentreppeScene(private val rootService: RootService) : BoardGameScene(1920,1200), Refreshable {

  var selectedCard : CardView? = null

  private val playerNameLabel = Label(
    posX = 1500,
    posY = 150,
    width = 250,
    height = 100,
    font   = Font(
      size=30,
      family="Monospace",
      color=Color.WHITE,
    )
  )
  private val secondPlayerNameLabel = Label(
    posX = 1500,
    posY = 250,
    width = 250,
    height = 100,
    font   = Font(
      size=30,
      family="Monospace",
      color=Color.WHITE,
    )
  )

  private val discardStack : CardStack<CardView> = CardStack<CardView>(
    posX              = 170,
    posY              = 650,
    visual            = ColorVisual(255,255,255,50)
  )

  private val drawStack : CardStack<CardView> = CardStack(
    posX              = 350,
    posY              = 650,
    visual            = ColorVisual(255, 255, 255, 50)
  )

  private val destroyButton   = Button(
    posX              = 170,
    posY              = 550,
    width             = 100,
    height            = 35,
    text              = "DESTROY",
    font              = Font(size=18, color=Color.WHITE),
    visual            = ColorVisual.DARK_GRAY
  )
  .apply { 
    isDisabled = true
    isVisible  = false
    onMouseClicked = {
      if(selectedCard != null) {
        try {
          rootService.playerActionService.destroyFromStairs(cardMap.backward(checkNotNull(selectedCard)))
        } catch(e: IllegalStateException) {
          this.isVisible = false
          selectedCard?.opacity = 1.0
          selectedCard = null
          errorLabel.text = "You already destroyed a card this turn!"
          playAnimation(DelayAnimation(2000).apply {
            onFinished = { _ : AnimationFinishedEvent ->
              errorLabel.text = ""
            }
          })
        } catch(e: IllegalArgumentException) {
          this.isVisible = false
          selectedCard?.opacity = 1.0
          selectedCard = null
          errorLabel.text = "You don't have enough points!"
          playAnimation(DelayAnimation(2000).apply {
            onFinished = { _ : AnimationFinishedEvent ->
              errorLabel.text = ""
            }
          })
        }
      }
    }
  }
  private val discardButton   = Button(
    posX              = 170,
    posY              = 600,
    width             = 100,
    height            = 35,
    text              = "DISCARD",
    font              = Font(size=18,color=Color.WHITE),
    visual            = ColorVisual.DARK_GRAY
  )
  .apply { 
    isDisabled = true
    isVisible  = false
    onMouseClicked = {
      if(selectedCard != null) {
        rootService.playerActionService.discardCard(cardMap.backward(checkNotNull(selectedCard)))
      }
    }
  }

  private val handLayoutPlayer1 : LinearLayout<CardView> = LinearLayout<CardView>(
    posX              = 635,
    posY              = 1000,
    width             = 650,
    height            = 200,
    alignment         = Alignment.CENTER,
    visual            = ColorVisual(255,255,255,80)
  )
  private val handLayoutPlayer2 : LinearLayout<CardView> = LinearLayout<CardView>(
    posX              = 710,
    posY              = 0,
    height            = 150,
    width             =  500,
    alignment         = Alignment.CENTER,
    visual            = ColorVisual(255,255,255,50)
  ).apply {
    opacity = 0.0
    repeat(5) {
      val cardView = CardView(
        front = CardImageLoader().backImage,
        back = CardImageLoader().backImage,
        height = 150,
        width = 100,
      )
      cardView.isDisabled = true
      this.add(cardView)
    }
  } 

  private val stairColumn1 : LinearLayout<CardView> = LinearLayout<CardView> (
    posX                        = 716.25,
    posY                        = 180,
    width                       = 97.5,
    height                      = 750,
    spacing                     = 0.0,
    orientation                 = Orientation.VERTICAL,
    alignment                   = Alignment.BOTTOM_CENTER,
  )
  private val stairColumn2 : LinearLayout<CardView> = LinearLayout<CardView> (
    posX                        = 813.75,
    posY                        = 330,
    width                       = 97.5,
    height                      = 600,
    alignment                   = Alignment.BOTTOM_CENTER,
    orientation                 = Orientation.VERTICAL,
  )
  private val stairColumn3 : LinearLayout<CardView> = LinearLayout<CardView> (
    posX                        = 911.25,
    posY                        = 480,
    width                       = 97.5,
    height                      = 450,
    alignment                   = Alignment.BOTTOM_CENTER,
    orientation                 = Orientation.VERTICAL,
  )
  private val stairColumn4 : LinearLayout<CardView> = LinearLayout<CardView> (
    posX                        = 1008.75,
    posY                        = 630,
    width                       = 97.5,
    height                      = 300,
    alignment                   = Alignment.BOTTOM_CENTER,
    orientation                 = Orientation.VERTICAL,
  )
  private val stairColumn5 : LinearLayout<CardView> = LinearLayout<CardView> (
    posX                        = 1106.25,
    posY                        = 780,
    width                       = 97.5,
    height                      = 150,
    orientation                 = Orientation.VERTICAL,
    alignment                   = Alignment.BOTTOM_CENTER,
  )
  private val errorLabel = Label(
    posX                        = 50,
    posY                        = 50,
    width                       = 300,
    isWrapText                  = true,
    height                      = 100,
    font                        = Font(size=23,family="Monospace",color=Color.RED),
    visual                      = ColorVisual(255,255,255,0) 
  )

  private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

  init {
    background = ColorVisual(108, 168, 59)
    addComponents(
      playerNameLabel,
      secondPlayerNameLabel,
      discardStack,
      discardButton,
      drawStack,
      destroyButton,
      handLayoutPlayer1,
      handLayoutPlayer2,
      stairColumn1,
      stairColumn2,
      stairColumn3,
      stairColumn4,
      stairColumn5,
      errorLabel,
    )
  }

  /**
   * Checks if the selected card is in any of the [LinearLayout]s
   * @param card is a nullable [CardView] object that represents the selected card
   * @throws IllegalStateException if the [card] is null
   */
  private fun isInStairs(card : CardView?) : Boolean {
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }
    checkNotNull(card) { println("Selected card is null") }
    return game.stairs.any { stair -> 
      stair.isNotEmpty() && stair.first() == cardMap.backward(card)
    }
  }

  /**
   * Sets up the GUI before starting the game
   */
  override fun refreshAfterStartGame() {
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }
    val cardImageLoader = CardImageLoader()

    cardMap.clear()

    val allCards = game.drawStack + game.discardStack +
    game.players.flatMap { it.hand } +
    game.stairs.flatten()

    for(card in allCards) {
      val cardView = CardView(
        front = cardImageLoader.frontImageFor(card.suit, card.value),
        back  = cardImageLoader.backImage,
        width = 130,
        height = 200
      )
      cardMap.add(card to cardView)
    }

    handLayoutPlayer2.opacity = 1.0

    discardStack.clear()
    drawStack.clear()
    handLayoutPlayer1.clear()
    stairColumn1.clear()
    stairColumn2.clear()
    stairColumn3.clear()
    stairColumn4.clear()
    stairColumn5.clear()
    selectedCard = null

    addCardViewToStairs()
    addCardViewToStacks()
  }


  /**
   * A helper function to add [CardView] to their respective positions in [LinearLayout]
   */ 
  private fun addCardViewToStairs() {
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }
    val listOfStairs = listOf(stairColumn1, stairColumn2, stairColumn3, stairColumn4, stairColumn5)

    stairColumn1.clear()
    stairColumn2.clear()
    stairColumn3.clear()
    stairColumn4.clear()
    stairColumn5.clear()

    for(i in game.stairs.indices) {
      for(stairCard in game.stairs[i]){
        val cardView = cardMap.forward(stairCard)
        if(stairCard == game.stairs[i].first()){
          cardView.showFront()
          cardView.isDisabled = false
        } else {
          cardView.showBack()
          cardView.isDisabled = true
        }
        listOfStairs[i].add(cardView)
        attachStairHandlers(cardView)
      }
    }
  }

  /**
   * A helper function that modifies the selected hand card of the player
   * e.g. upscaling when hovered or reducing opacity when clicked on it
   * @param cardView is the selected [CardView]
   */
  private fun attachHandHandlers(cardView: CardView) {
    cardView.apply {
      onMouseEntered = { this.scale(1.25) }
      onMouseExited  = { this.scale(1.0) }
      onMouseClicked = {
        if(selectedCard == null) {
          selectedCard = this
          this.opacity = 0.3
          discardButton.isDisabled = false
          discardButton.isVisible  = true
        }
        else if(selectedCard == this) {
          this.opacity = 1.0
          selectedCard = null
          discardButton.isDisabled = true
          discardButton.isVisible  = false
        }
        else if(isInStairs(selectedCard)){
          this.opacity = 1.0
          selectedCard?.opacity = 1.0
          val stairCard = cardMap.backward(checkNotNull(selectedCard))
          val handCard  = cardMap.backward(this)
          try {
            rootService.playerActionService.combineCards(handCard, stairCard)
          } catch(e: IllegalStateException) {
            this.opacity = 1.0
            selectedCard?.opacity = 1.0
            selectedCard = null
            errorLabel.text = "This is an invalid combination!"
            playAnimation(DelayAnimation(3000).apply { 
              onFinished = { _ : AnimationFinishedEvent ->
                errorLabel.text = "" }
            })
          }
        }
        else {
          selectedCard?.opacity = 1.0
          selectedCard = this
          this.opacity = 0.3
          discardButton.isDisabled = false
          discardButton.isVisible  = true
        }
      }
    }
  }

  /**
   * A helper function that modifies the selected stair card
   * e.g. upscaling when hovered or reducing opacity when clicked on it
   * @param cardView is the selected [CardView]
   */
  private fun attachStairHandlers(cardView: CardView) {
    cardView.apply {
      height = 150.0
      width  = 97.5
      onMouseEntered = { this.scale(1.25) }
      onMouseExited  = { this.scale(1.0) }
      onMouseClicked = {
        if(selectedCard == null) {
          selectedCard = this
          this.opacity = 0.3
          destroyButton.isDisabled = false
          destroyButton.isVisible  = true
        } 
        else if(selectedCard == this) {
          selectedCard = null
          this.opacity = 1.0
          destroyButton.isDisabled = true
          destroyButton.isVisible  = false
        }
        else if(selectedCard?.parent == handLayoutPlayer1) {
          val cardOnStair = cardMap.backward(this)
          val handCard  = cardMap.backward(checkNotNull(selectedCard))
          rootService.playerActionService.combineCards(handCard, cardOnStair)
          selectedCard = null
        } else {
          selectedCard?.opacity = 1.0
          selectedCard = this
          this.opacity = 0.3
          destroyButton.isDisabled = false
          destroyButton.isVisible  = true
        }
      }
    }
  }

  /**
   * this helper function adds the [CardView]s to [discardStack] and [drawStack] respectively
   */
  private fun addCardViewToStacks() {
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }

    for(card in game.drawStack) {
      val cardView = cardMap.forward(card)
      cardView.apply {
        height  = 200.0
        width   = 130.0
      }
      cardView.showBack()
      drawStack.add(cardView)
    }

    for(card in game.discardStack) {
      val cardView = cardMap.forward(card)
      cardView.apply { 
        isDisabled = true
        height  = 200.0
        width   = 130.0 
        opacity = 1.0
        onMouseEntered = null
        onMouseExited  = null
      }
      cardView.showBack()
      discardStack.add(cardView)
    }
  }

  /**
   * Refreshes the game screen after the turn ends
   */
  override fun refreshAfterEndTurn() {
    handLayoutPlayer1.clear()
    stairColumn1.clear()
    stairColumn2.clear()
    stairColumn3.clear()
    stairColumn4.clear()
    stairColumn5.clear()
    discardStack.clear()
    drawStack.clear()

    addCardViewToStacks()
    addCardViewToStairs()
  }

  /**
   * Refreshes the screen after the turn start
   */
  override fun refreshAfterStartTurn() {
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }
    val playerToMove = game.players[game.currentPlayer % 2]
    val nextPlayer   = game.players[(game.currentPlayer + 1) % 2]
    playerNameLabel.text = "${playerToMove.name} : ${playerToMove.score}"
    secondPlayerNameLabel.text = "${nextPlayer.name} : ${nextPlayer.score}"
    handLayoutPlayer1.clear()
    selectedCard = null
    for (handCard in playerToMove.hand) {
      val cardView = cardMap.forward(handCard)
      cardView.showFront()
      handLayoutPlayer1.add(cardView)
      attachHandHandlers(cardView)
    }

    discardButton.isDisabled = true
    discardButton.isVisible = false
    destroyButton.isDisabled = true
    destroyButton.isVisible = false
  }

  override fun refreshAfterDiscardCard(handCard : Card) {
    handLayoutPlayer1.remove(cardMap.forward(handCard))
    selectedCard = null
    discardButton.isDisabled = true
    discardButton.isVisible = false
    destroyButton.isDisabled = true
    destroyButton.isVisible = false
  }

  /**
   * Refreshes the GUI after [combineCards] function is called
   * @param handCard the selected card in hand
   * @param stairCard the selected card in stairs
   */
  override fun refreshAfterCombineCards(handCard : Card, stairCard : Card) {
    val game = checkNotNull(rootService.currentGame)
    handLayoutPlayer1.clear()

    selectedCard = null

    val player = game.players[game.currentPlayer % 2]
    for(card in player.hand) {
      val cardView = cardMap.forward(card)
      cardView.showFront()
      handLayoutPlayer1.add(cardView)
    }
    addCardViewToStairs()
  }

  /*
   * refreshes the GUI after a card is destroyed from the stairs
   * @param stairCard the destroyed card in stairs
   */
  override fun refreshAfterDestroyCard(stairCard : Card) {
    val game = checkNotNull(rootService.currentGame) { "No active game is found" }
    stairColumn1.clear()
    stairColumn2.clear()
    stairColumn3.clear()
    stairColumn4.clear()
    stairColumn5.clear()

    discardButton.isDisabled = true
    discardButton.isVisible = false
    destroyButton.isDisabled = true
    destroyButton.isVisible = false
    selectedCard = null

    val playerToMove = game.players[game.currentPlayer % 2]
    val nextPlayer   = game.players[(game.currentPlayer + 1) % 2]
    playerNameLabel.text = "${playerToMove.name} : ${playerToMove.score}"
    secondPlayerNameLabel.text = "${nextPlayer.name} : ${nextPlayer.score}"
    addCardViewToStairs()
  }

  /**
   * Refreshes the [StackView]s after the [drawCard] is reshuffled 
   */
  override fun refreshAfterShuffleStack() { 
    val game = checkNotNull(rootService.currentGame) { "No active game found!" }

    drawStack.clear()
    for(card in game.drawStack) {
      val cardView = cardMap.forward(card)
      cardView.showBack()
      drawStack.add(cardView)
    }
  }


}
