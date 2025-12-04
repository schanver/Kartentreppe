package gui

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.KeyCode
import service.Refreshable
import service.RootService
import kotlin.collections.listOf
import kotlin.system.exitProcess

/**
 * This class hold the UI elements we use at the start menu such as 
 * text fields for players to type their name in, buttons to 
 * start or quit the game etc.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(1920,1200), Refreshable {


  private val headlineLabel = Label(
    width = 500, height = 150,
    posX = 710, posY = 250,
    text = "Kartentreppe",
    font = Font(
      size=44,
      color=Color.BLACK,
      family="Comic Sans",
      fontWeight=Font.FontWeight.BOLD,
      fontStyle=Font.FontStyle.ITALIC,
    )
  )
  private val player1Label = Label(
    width = 250, height = 35,
    posX = 660, posY= 450,
    text = "Player 1:",
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  )
  private val player1Input : TextField = TextField(
    width = 250, height = 50,
    posX = 660, posY = 500,
    text = "",
    visual = ColorVisual.WHITE,
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  ).apply { 
    onKeyPressed = {
      startButton.isDisabled = this.text.isBlank() || player2Input.text.isBlank()
    }
    onKeyReleased = {
    keyEvent -> 
      if(keyEvent.keyCode == KeyCode.ENTER) {
        rootService.gameService.startGame(
          this.text.trim(),
          player2Input.text.trim()
      )
      }
  }
  }
  private val player2Label = Label(
    width = 250, height = 50,
    posX = 1070, posY = 450,
    text = "Player 2:",
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  )

  private val player2Input: TextField = TextField(
    width = 250, height = 50,
    posX = 1070, posY = 500,
    text = "",
    visual = ColorVisual.WHITE,
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  ).apply {
    onKeyPressed = {
      startButton.isDisabled = player1Input.text.isBlank() || this.text.isBlank()
    }
    onKeyReleased = {
      keyEvent -> 
        if(keyEvent.keyCode == KeyCode.ENTER) {
          rootService.gameService.startGame(
            player1Input.text.trim(),
            this.text.trim()
          )
        }
      }
  }

  val quitButton = Button(
    width = 100, height = 50,
    posX = 910, posY = 800,
    text = "Quit",
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  ).apply {
    isFocusable = true
    visual = ColorVisual.RED
    onKeyReleased = {
      keyEvent -> 
        if(keyEvent.keyCode == KeyCode.ENTER) {
          exitProcess(0)
        }
      }
    }

  private val startButton = Button(
    width = 100, height = 50,
    posX = 910, posY = 700,
    text = "Start",
    font = Font(
      size=24,
      color=Color.BLACK,
      family="Monospace",
      fontWeight=Font.FontWeight.BOLD
    )
  ).apply {
    isDisabled = true
    isFocusable = true
    visual = ColorVisual(136, 221, 136)
    onMouseClicked = {
      rootService.gameService.startGame(
        player1Input.text.trim(),
        player2Input.text.trim()
      )
    }
  onKeyReleased = {
    keyEvent -> 
      if(keyEvent.keyCode == KeyCode.ENTER) {
        rootService.gameService.startGame(
          player1Input.text.trim(),
          player2Input.text.trim()
      )
      }
  }
  }



  init {
    opacity = 1.0
    background = ColorVisual(255,255,255,90)
    addComponents(
      headlineLabel,
      player1Label, player2Label,
      player1Input,player2Input,
      startButton, quitButton
    )
  }
}
