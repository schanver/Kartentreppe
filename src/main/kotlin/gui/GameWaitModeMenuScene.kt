package gui

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Color
import service.RootService
import service.Refreshable
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.event.KeyCode
import gui.KartentreppeApplication

/**
 * This class hold the UI elements we use during the WaitModeMenu such as logs of
 * the actions of the previous player etc.
 */
class GameWaitModeMenuScene(private val rootService: RootService) : MenuScene(800,800), Refreshable {



  internal val playerTurnLabel = Label(
    width                   = 300,
    height                  = 50,
    posX                    = 250,
    posY                    = 200,
    isWrapText              = true,
    text                    = "",
    font                    = Font(
      size=32,
      color=Color.BLACK,
      fontWeight=Font.FontWeight.BOLD,
      family="Monospace"
    )
  )
  val logPanel = Label(
    width       = 500,
    height      = 400,
    posX        = 150,
    posY        = 250,
    isWrapText  = true,
    text        = "",
    font        = Font( 
      size=20,
      color=Color.BLACK,
      family="Monospace"
    )
  )
  val startTurnButton = Button(
    width   = 200,
    height  = 50,
    posX    = 300,
    posY    = 700,
    font    = Font(
      size=28,
      color=Color.BLACK,

    ),
    text    = "Start Turn",
    visual  = ColorVisual.YELLOW
  ).apply {
    onMouseClicked = {
      rootService.gameService.startTurn()
    }
    onKeyReleased = { keyEvent ->
      if (keyEvent.keyCode == KeyCode.ENTER) {
        rootService.gameService.startTurn()
      }
    }
  }

  init {
    opacity = 1.0
    rootService.addRefreshable(this)
    addComponents(
      startTurnButton,
      playerTurnLabel,
      logPanel,
    )
  }

}
