package entity

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 *  This class test the functions and properties regarding the [Player] class
 */
class PlayerTest {

  private lateinit var testPlayer: Player
  private lateinit var testPlayer2: Player
  private lateinit var hand: MutableList<Card>

  /**
   * setup() function creates Card and Player objects that were used in other tests.
   * this function runs before other tests, hence the @BeforeTest
   */
    @BeforeTest
    fun setup() {
      hand =
      mutableListOf(
        Card(CardValue.TWO, CardSuit.HEARTS),
        Card(CardValue.THREE, CardSuit.SPADES),
        Card(CardValue.FIVE, CardSuit.DIAMONDS),
        Card(CardValue.JACK, CardSuit.CLUBS),
        Card(CardValue.KING, CardSuit.HEARTS)
      )
      testPlayer = Player("Bob", 0, hand.toMutableList(), mutableListOf())
      testPlayer2 = Player("Alfred", -6, hand.toMutableList(), mutableListOf())
    }

    /**
     * testPlayerInitialization() function tests if the objects created in setup() function
     * are initialized correctly
     *  It also tests whether the size of our handCards change, after we remove a card from our hand
     */
    @Test
    fun testPlayerInitialization() {
      assertEquals("Bob", testPlayer.name)
      assertEquals(0, testPlayer.score)
      assertEquals(5, testPlayer.hand.size)

      testPlayer2.hand.remove(Card(CardValue.FIVE, CardSuit.DIAMONDS))
      assertEquals("Alfred", testPlayer2.name)
      assertEquals(4, testPlayer2.hand.size)
    }

    /**
     * Tests whether the playerScore is negative and
     *  gives an error message if somehow the player gets a negative score
     */
    @Test
    fun testNegativeScoreNotAllowed() {
      assertFalse(testPlayer2.score >= 0, "This score should've been negative")
    }

  }
