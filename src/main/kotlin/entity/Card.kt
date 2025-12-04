package entity

/**
 * Data class for the type of cards we use in the game. 
 * It is identified by [CardValue] and [CardSuit]
 *
 * @property value is the numerical value of the card
 * @property suit is the suit of the card (clubs, spades,hearts,diamonds)
 */

data class Card(
  val value : CardValue,
  val suit : CardSuit
)

{
  /**
   * This function prints out the [value] and [suit] of the [Card] object
   */ 
  override fun toString() = "${value}${suit}"

  /**
   * returns the difference between two [value] of [Card] objects
   * @param other is the secondary card we compare with the first card
   * @return 0; if the cards have the same value 
   */ 
  fun compareValue(other : Card) : Int {
    return this.value.toInt() - other.value.toInt() 
  }

  /**
   * compares the suit of two cards 
   * @param other is the secondary card we compare with the first card
   * @return true if cards have the same suit, false if not
   */
  fun compareSuit(other : Card) : Boolean {
    return this.suit == other.suit
  }
}
