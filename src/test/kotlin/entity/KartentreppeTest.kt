package entity

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.collections.mutableListOf


/**
 * This class includes the tests of functions or variables.
*/
class KartentreppeTest {
    private lateinit var players : MutableList<Player>
    private lateinit var players2 : MutableList<Player>
    private lateinit var stairs : List<MutableList<Card>>
    private lateinit var drawStack : MutableList<Card>
    private lateinit var hand : MutableList<Card>
    private lateinit var player1 : Player
    private lateinit var player2 : Player
    private lateinit var player3 : Player

    /**
     * KartentreppeInit() function creates [Card] and [Player] objects that were used in other tests.
     * this function runs before other tests, hence the @BeforeTest
    */
    @BeforeTest
    fun KartentreppeInit() {
        hand = mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.SPADES),
            Card(CardValue.SIX, CardSuit.CLUBS),
            Card(CardValue.SEVEN, CardSuit.DIAMONDS)
        )
        drawStack = MutableList(5){ Card(CardValue.TWO, CardSuit.DIAMONDS) }
        stairs = listOf(
            mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.SPADES),
            Card(CardValue.SIX, CardSuit.CLUBS),
            Card(CardValue.SEVEN, CardSuit.DIAMONDS),
            ),
            mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.SPADES),
            Card(CardValue.SIX, CardSuit.CLUBS),
            ),
            mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.SPADES),
            ),
            mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            ),
            mutableListOf(
            Card(CardValue.EIGHT,CardSuit.CLUBS)
            )
        )
        player1 = Player("Max", 0, hand.toMutableList(), mutableListOf())
        player2 = Player("Muster", 0, hand.toMutableList(), mutableListOf())
        player3 = Player("Mr. Singleplayer", 0, hand.toMutableList(), mutableListOf())

        players = mutableListOf()
        players2 = mutableListOf()

        players.add(player1)
        players.add(player2)

        players2.add(player3)

}
    /**
     * testStairs checks that in the [stairs] there are at most 15 cards
    */
    @Test
    fun testStairsSize() {
        // Test: There should be at most 15 cards in the center
        assertTrue(stairs.size <= 15, "Stair size is exceeded.")

    }
    /**
     * testPlayerListSize() checks that there are exactly 2 players in the players list
     */
    @Test
    fun testPlayersListSize() {
        assertTrue(players.size == 2, "There should be two players to play the game")
        assertFalse(players2.size == 2 ,"This list shouldn't have 2 players")
    }
}
