package freecell.model;

/**
 * An enumerated type that represent the type of SUIT.
 * There are four types of suits. SPADE, CLUB, DIAMOND, HEART
 */

enum Suit {

  SPADE('\u2660'), CLUB('\u2663'), DIAMOND('\u2666'), HEART('\u2665');

  private final char suitSymbol;

  /**
   * constructor to associate a symbol with a suit
   * during it's construction.
   * @param suitSymbol represents a symbol corresponding to a particular suit.
   */
  Suit(char suitSymbol) {
    this.suitSymbol = suitSymbol;
  }

  /**
   * A method that return the suit symbol associated with a suit.
   * @return suit symbol associated with a suit.
   */
  char getSuitSymbol() {
    return suitSymbol;
  }
}



