package gui

import entity.Player
import entity.Card
import service.RootService
import service.Refreshable
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual
import gui.KartentreppeApplication


/**
 * This class holds the UI elements that are shown at [endGameScene]
 */
class GameFinishedMenuScene(private val rootService : RootService) : MenuScene(1920,1080) , Refreshable {

  val cardImageLoader = CardImageLoader()

  val winnerDeclarationLabel = Label(
    posX          = 710,
    posY          = 50, 
    width         = 500,
    height        = 100,
    font          = Font(size=32, color=Color.WHITE, family="Monospace")
  )

  val scoreLabel = Label(
    posX          = 785,
    posY          = 150, 
    width         = 350,
    height        = 100,
    font          = Font(size=32, color=Color.WHITE, family="Monospace")
  )

  val menuButton  = Button(
    posX          = 860,
    posY          = 800,
    width         = 200,
    height        = 100,
    text          = "Go back to menu",
    visual        = ColorVisual.GREEN,
    font          = Font(size=22,color=Color.BLACK)
  ).apply {
    onMouseClicked = {
      this.opacity = 0.0
    }
  }


  init {
    background = ColorVisual(108, 168, 59)
    addComponents(
      winnerDeclarationLabel,
      scoreLabel,
      menuButton,
    )
  }

  /**
   * refreshes the [endGameScene] after endGame() function is called
   * @param winner is the player that won
   */
  override fun refreshAfterEndGame(winner : Player?) {
    val cardImageLoader = CardImageLoader()
    val game = checkNotNull(rootService.currentGame) { "Game is null" }
    when (winner) {
      game.players[0] -> winnerDeclarationLabel.text = "${game.players[0].name} has won the game!"
      game.players[1] -> winnerDeclarationLabel.text = "${game.players[1].name} has won the game!"
      null            -> winnerDeclarationLabel.text = "The game ended in a draw!"
    }
    var index = 0
    scoreLabel.text = "${game.players[0].score} - ${game.players[1].score}"
    for(card in game.players[0].combinedCards) {
      val cardView = Button(
        visual = cardImageLoader.frontImageFor(card.suit, card.value),
        height = 200,
        width  = 130,
      ).apply {
        isDisabled = false
        posX       = 50.0
        posY       = 100.0 + (index * 30)
      }
      this.addComponents(cardView)
      index++
    }
    index = 0
    for(card in game.players[1].combinedCards) {
      val cardView = Button(
        visual = cardImageLoader.frontImageFor(card.suit, card.value),
        height = 200,
        width  = 130,
      ).apply {
        isDisabled = false
        posX       = 1740.0
        posY       = 100.0 + (index * 30)
      }
      this.addComponents(cardView)
      index++
    }
  }
}
