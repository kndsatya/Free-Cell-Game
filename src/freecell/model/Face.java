package freecell.model;


/**
 * An enum that contains representations for all the allowed face values
 * for cards in a suit.
 */
enum Face {

  A("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
  TEN("10"), JOKER("J"), QUEEN("Q"), KING("K");

  private final String associatedValue;

  /**
   * A method that helps to assign a string of enum's numeric
   * representation to it.
   *
   * @param associatedValue numeric representation of an enum as a string.
   */
  Face(String associatedValue) {
    this.associatedValue = associatedValue;
  }

  /**
   * A method returns the associated value of an enum as a string.
   *
   * @return The associated value of an enum as a string.
   */
  String associatedValue() {
    return associatedValue;
  }

}
