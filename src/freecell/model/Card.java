package freecell.model;

/**
 * A package protected class that represents a card in a deck.
 * Each card has a face value,color and  suit associated with it and each card
 * has getter methods to retrieve it's color, suit and it's face value.
 */
public class Card {

  private final Suit suit;
  private final Face faceValue;

  /**
   * A constructor to construct a card when
   * given with a suit and a face value for the card.
   * @param suit represent the suit of a card
   * @param faceValue represents the face value of a card.
   */
  Card(Suit suit, Face faceValue) {

    this.suit = suit;
    this.faceValue = faceValue;
  }

  /**
   * Default constructor that assigns default suit as Hearts and default face value as 'A'.
   */
  Card() {
    this.suit = Suit.HEART;
    this.faceValue = Face.A;
  }

  /**
   * A method that can provide the suit that a particular card
   * belongs to.
   * @return the suit a card belongs to.
   */
  Suit getSuit() {
    return suit;
  }

  /**
   * A method that provides the color of a card.
   * @return the color of the card.
   */
  Color getColor() {

    if (suit == Suit.CLUB || suit == Suit.SPADE) {
      return Color.BLACK;
    }

    return Color.RED;
  }

  /**
   * A method that provides the face value of a card.
   * @return face value associated with a card.
   */
  Face getFaceValue() {
    return faceValue;
  }

  /**
   * provides string representation of each card.
   * Each card is represented in it's string form as
   * Face value followed by it's suit's symbol.
   * @return
   */
  public String toString() {
    return this.getFaceValue().associatedValue() + this.getSuit().getSuitSymbol();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!( o instanceof Card )) {
      return false;
    }

    Card otherCard = (Card) o;

    return ( this.getFaceValue() == otherCard.getFaceValue()
            && this.getSuit() == otherCard.getSuit() );
  }

  @Override
  public int hashCode() {

    return 32 * faceValue.ordinal() + 32 * suit.ordinal();
  }

}
