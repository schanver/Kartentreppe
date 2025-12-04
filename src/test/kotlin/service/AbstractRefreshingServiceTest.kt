package service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * This class tests the functionality of the [AbstractRefreshingService] class.
 */
class AbstractRefreshingServiceTest {

    /**
     * This refreshable is initialized in the [setUp] function hence it is a late-initialized property.
     */
    private lateinit var testRefreshable: Refreshable
    private lateinit var rootService : RootService
    /**
     * This abstractRefreshingService is initialized in the [setUp] function hence it is a late-initialized property.
     */
    private lateinit var abstractRefreshingService: AbstractRefreshingService

    /**
     * Initialize service to set up the test environment. This function is executed before every test.
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        testRefreshable = TestRefreshable(rootService)
        abstractRefreshingService = object : AbstractRefreshingService() {}
        abstractRefreshingService.addRefreshable(testRefreshable)
    }

    /**
     * Tests if the refreshable is notified and is the correct one.
     */
    @Test
    fun testIfRefreshableIsNotified() {
        var refreshWasCalled = false
        var isTestRefreshable = false

        /**
         * This function is used to test if the refresh method was called on the test refreshable.
         */
        fun Refreshable.refreshForTesting() {
            refreshWasCalled = true
            isTestRefreshable = this === testRefreshable
        }

        abstractRefreshingService.onAllRefreshables { this.refreshForTesting() }

        assertTrue(refreshWasCalled, "The refresh method should have been called.")
        assertTrue(isTestRefreshable, "The refresh method should have been called on the test refreshable.")
    }

    /**
     * Tests if [RootService.addRefreshable] properly forwards the added [Refreshable] to
     * its service classes.
     */
    @Test
    fun testRootServiceMultiRefreshable() {
      val testRefreshable1 = TestRefreshable(rootService)
      val testRefreshable2 = TestRefreshable(rootService)
      rootService.addRefreshables(testRefreshable1, testRefreshable2)

      // Test if testRefreshables were successfully added to GameService
      assertFalse(testRefreshable1.refreshAfterStartGameCalled)
      assertFalse(testRefreshable2.refreshAfterStartGameCalled)
      rootService.gameService.onAllRefreshables { refreshAfterStartGame() }
      assertTrue(testRefreshable1.refreshAfterStartGameCalled)
      assertTrue(testRefreshable2.refreshAfterStartGameCalled)
      testRefreshable1.reset()
      testRefreshable2.reset()

      // Test if testRefreshable was successfully added to PlayerActionService
      assertFalse(testRefreshable1.refreshAfterStartGameCalled)
      assertFalse(testRefreshable2.refreshAfterStartGameCalled)
      rootService.playerActionService.onAllRefreshables { refreshAfterStartGame() }
      assertTrue(testRefreshable1.refreshAfterStartGameCalled)
      assertTrue(testRefreshable2.refreshAfterStartGameCalled)
      testRefreshable1.reset()
      testRefreshable2.reset()

    }
  }
