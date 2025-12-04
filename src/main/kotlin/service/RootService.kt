package service
import entity.*


/**
 * The root service class is responsible for managing services and the entity layer reference.
 * This class acts as a central hub for every other service within the application.
 *
 * @property gameService is the [GameService] instance with a reference to [RootService] for a two-way dependency
 * @property playerActionService is the [PlayerActionService] instance with a reference to [RootService] 
 * for a two-way dependency
 * @property currentGame is a [Kartentreppe] object that we use to initiate the entites for a game.
 */
class RootService {

  val gameService = GameService(this)
  val playerActionService = PlayerActionService(this)
  var currentGame : Kartentreppe? = null

  /**
   * Adds a new [Refreshable] to the list of refreshables.
   *
   * @param newRefreshable The [Refreshable] to be added
   */
    fun addRefreshable(newRefreshable: Refreshable) {
      gameService.addRefreshable(newRefreshable)
      playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to [rootService]
     *
     * @param newRefreshables The [Refreshable]s to be added
     */
      fun addRefreshables(vararg newRefreshables : Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
      }
    }
