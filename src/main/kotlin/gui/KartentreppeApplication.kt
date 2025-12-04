package gui

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.components.gamecomponentviews.CardView
import service.RootService
import service.Refreshable
import gui.GameFinishedMenuScene
import entity.Player
import kotlin.system.exitProcess

/**
 * Represents the main application for the SoPra board game.
 * The application initializes the [RootService] and displays the scenes.
 */
class KartentreppeApplication : BoardGameApplication("Kartentreppe"), Refreshable {

  /**
   * The root service instance. This is used to call service methods and access the entity layer.
   */
    private val rootService: RootService = RootService()

    /**
     * The main game scene displayed in the application.
     */
  private val gameScene               = KartentreppeScene(rootService)
  private val waitMenuScene           = GameWaitModeMenuScene(rootService)
  private val newGameMenuScene        = NewGameMenuScene(rootService)
  .apply {
    quitButton.onMouseClicked = {
      exitProcess(0)
    }
  }
  private val endGameScene            = GameFinishedMenuScene(rootService)
  .apply {
    menuButton.onMouseClicked = {
      showMenuScene(newGameMenuScene)
    }
  }
    /**
     * Initializes the application by displaying the [newGameMenuScene].
     */

    init {
      rootService.addRefreshables(
        this,
        gameScene,
        newGameMenuScene,
        waitMenuScene,
        endGameScene,
      )
      this.showMenuScene(newGameMenuScene,0)
      this.showGameScene(gameScene)
    }

    /**
     * Hides the [GameWaitMenu] and shows the [gameScene]
     */ 
    override fun refreshAfterStartTurn() {
      gameScene.refreshAfterStartTurn()
      this.hideMenuScene(100)
      this.showGameScene(gameScene)
    }

    /**
     * Updates the log on the [waitMenuScene] and shows the [waitMenuScene]
     */ 
    override fun refreshAfterEndTurn() {
      val game = checkNotNull(rootService.currentGame)
      waitMenuScene.playerTurnLabel.text = "${game.players[game.currentPlayer % 2].name} is next"
      waitMenuScene.logPanel.text = game.log.joinToString("\n")
      gameScene.refreshAfterEndTurn()
      this.showMenuScene(waitMenuScene,0)
    }

    /**
     * Set up the [gameScene] in the background while sending the player to the [waitMenuScene]
     */ 
    override fun refreshAfterStartGame() {
      val game = checkNotNull(rootService.currentGame) { "No active game found!" }
      waitMenuScene.playerTurnLabel.text = "${game.players[game.currentPlayer % 2].name} is next"
      waitMenuScene.logPanel.text = game.log.joinToString("\n")
      gameScene.refreshAfterStartGame()
      this.hideMenuScene(100)
      this.showMenuScene(waitMenuScene, 200)
    }

    /**
     * sends the players to the [endGameScene]
     * @param winner is the object of the (possible) winner. 
     */ 
    override fun refreshAfterEndGame(winner : Player?) {
      endGameScene.refreshAfterEndGame(winner)
      gameScene.components.forEach {
        component -> gameScene.removeComponents(component)
      }
      this.showMenuScene(endGameScene, 200)
    }
  }
