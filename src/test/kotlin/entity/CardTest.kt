package entity

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

  /**
   * This class contains the functions that tests the properties and methods of the data class [Card]
   *
   */ 

class CardTest {

    private lateinit var card1: Card
    private lateinit var card2: Card
    private lateinit var card3: Card

    /**
     * setup() function creates Card objects that were used in other tests.
     * this function runs before other tests, hence the @BeforeTest
    */

    @BeforeTest
    fun setup() {
        card1 = Card(CardValue.TWO, CardSuit.HEARTS)
        card2 = Card(CardValue.THREE, CardSuit.SPADES)
        card3 = Card(CardValue.TWO, CardSuit.HEARTS)
    }

    /**
     * Tests that a [Card] object is initialized with the correct value and suit.
     *
     * Ensures the properties `value` and `suit` of the card match
     * the one provided during construction
    */
    @Test
    fun testCardInitialization() {
        assertEquals(CardValue.TWO, card1.value)
        assertEquals(CardSuit.HEARTS, card1.suit)
    }

    /**
     * testToString() function should return the value and suit of the card formatted like the following:
     *  [Card.value][Card.suit]
    */
    @Test
    fun testToString() {
        val result = card1.toString()
        assertTrue(result.contains("2"), "toString should include card value")
        assertTrue(result.contains("♥"), "toString should include card suit")
        assertTrue(result.equals("2♥"), "toString should format it correctly!")
    }

    /**
     * Tests whether two [Card] objects have the same [value]
     * It should return 0 when two cards have the same [value] and the difference of their values
     * when they have different values
     */
    @Test
    fun testCompareValue() {

        // TWO vs THREE → should be negative
        assertTrue(card1.compareValue(card2) < 0, "TWO should be less than THREE")

        // TWO vs TWO → should be equal
        assertEquals(0, card1.compareValue(card3))
    }

    /**
     * Tests two different [Card] objects with distinct values or suit
     * are not considered equal.
     *
     * This ensures that the `equals` method correctly distinguishes between cards that are not identical.
     */
    @Test
    fun testEqualityReference() {
        assertFalse(card1.equals(card2), "These two cards shouldn't be equals")
    }

    /**
     * Test if two [Card] objects have the same suits
    */
    @Test
    fun testCompareSuit() {
      assertFalse(card1.compareSuit(card2), "These two cards should have different suits!")
      assertTrue(card1.compareSuit(card3), "These two card should've the same suits!")
    }
}
