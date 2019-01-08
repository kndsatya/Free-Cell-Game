package freecell.model;




/**
 * This a model for the free cell game and implements all methods
 * of the interface FreecellOperations.  This class represents
 * a free cell game and contains a deck of cards, group of open
 * piles, cascade piles and foundation piles.
 *
 * <p>Freecell is a Solitaire-type game, which uses the regular deck of 52 suit-value cards.
 * There are four suits: clubs (♣), diamonds (♦), hearts (♥), and spades (♠). Hearts
 * and diamonds are colored red; clubs and spades are colored black.
 * There are thirteen values: ace (written A), two through ten (written 2 through 10),
 * jack (J), queen (Q) and king (K). In Freecell, aces are considered “low”, or less
 * than a two. (In other games, they’re considered “high”, or greater than a king.)</p>
 *
 * <p>The game play is divided among three types of card piles. First,
 * there are four foundation piles, one for each suit.
 * These four piles are initially empty, and the goal of Freecell
 * is to collect all cards of all suits in their respective foundation piles.
 * A card can be added to a foundation pile if and only if its suit
 * matches that of the pile, and its value is one more than that of
 * the card currently on top of the pile (i.e. the next card in foundation
 * pile 2 in the figure above can only be 3♣). If a foundation pile
 * is currently empty, any ace can be added to it: there is no required ordering
 * of suits in the foundation piles.</p>
 *
 * <p>The second type of pile is the cascade pile,
 * also indexed starting from 1. Usually a game of Freecell has eight cascade
 * piles, but our game will allow as few as four. Because the initial deal of the game
 * is shuffled , a cascade pile may initially contain cards in any order.
 * However, a card from some pile can be moved to the end of a cascade pile if and only
 * if its color is different from that of the currently last card, and its value is exactly
 * one less than that of the currently last card (e.g. in the figure above, the next card
 * in cascade pile 1 can be 4♦ or 4♥ while the next card in cascade pile 3 can be 10♠ or 10♣).
 * This sequence of decreasing-value, alternating-color cards is called a build.
 * (Different variants of Freecell, or other solitaire games,
 * differ primarily in what builds are permitted.)</p>
 *
 * <p>Finally, the third type of pile is the open pile
 * (Usually a game of Freecell has four open piles,
 * but our game will allow as few as one. An open pile may contain at most one card.
 * An open pile is usually used as a temporary buffer during the game to hold cards.</p>
 *
 * <p>In this implementation moving multiple cards from any pile is not a valid move and will throw
 * IllegalArgumentException. Always only one card can be moved and that is the top most card in
 * each pile.</p>
 *
 * <p>Also the minimum number of possible open piles is 1 and maximum is 4.
 * Similarly for cascade pile the minimum is 4 and maximum is 8.</p>
 */
public class FreecellModel extends FreeCellAbstractModel {



  /**
   * Constructs a free cell game when provided with the number of
   * open piles and cascade piles for that game.
   * @param openPilesCount No.of open piles required for a free cell game.
   * @param casCadePilesCount No.of cascade piles required for a free cell game.
   */
  private FreecellModel(int openPilesCount, int casCadePilesCount) {

    super(openPilesCount,casCadePilesCount);
  }



  /**
   * A static inner class that implements FreecellOperationsBuilder interface and helps
   * in creating the FreecellModel.
   */
  public static class FreeCellBuilder implements FreecellOperationsBuilder {

    private int cascadePilesCount;
    private int openPilesCount;

    /**
     * A default constructor that assigns the no.of
     * cascade piles and no. of open piles required for a freecell game
     * to 4 and 1 respectively.
     */
    private FreeCellBuilder() {
      cascadePilesCount = 4;
      openPilesCount = 1;
    }

    /**
     * This method is used to configure the number of cascade piles
     * required for the game.
     * @param cascadePilesCount No.of cascade piles required for a new game.
     * @return FreecellOperationsBuilder object.
     * @throws IllegalArgumentException is thrown when the cascaded piles count is
     *                                  less than the default value as assigned by the
     *                                  default constructor or greater than 8 as it's the
     *                                  number of cascade piles in a general free cell game.
     */
    public FreecellOperationsBuilder cascades(int cascadePilesCount)
            throws IllegalArgumentException {

      if (cascadePilesCount < this.cascadePilesCount) {
        throw new IllegalArgumentException("No. of cascade piles should not be less than 4");
      }
      this.cascadePilesCount = cascadePilesCount;
      return this;
    }

    /**
     * This method is used to configure the number of open piles
     * required for the game.
     * @param openPilesCount No.of open piles required for a new game.
     * @return FreecellOperationsBuilder object
     * @throws IllegalArgumentException is thrown when the openpilesCount is
     *                                  less than the default value 1 as set by
     *                                  the default constructor or greater than
     *                                  4 as the general game of free cell contains 4
     *                                  open piles only.
     */
    public FreecellOperationsBuilder opens(int openPilesCount) throws IllegalArgumentException {

      if (openPilesCount < this.openPilesCount) {
        throw new IllegalArgumentException("No.of open piles shouldn't be less than 1");
      }
      this.openPilesCount = openPilesCount;
      return this;
    }

    @Override
    public FreecellOperations<Card> build() {

      return new FreecellModel(openPilesCount, cascadePilesCount);

    }

  }

  /**
   * A static method that can be used by any class to create and use
   * an object of type FreecellOperationsBuilder in order to further create
   * a FreecellModel.
   * @return an object of type FreecellOperationsBuilder
   */
  public static FreecellOperationsBuilder getBuilder() {
    return new FreeCellBuilder();
  }


  /**
   * Move a card from the given source pile to the given destination pile, if
   * the move is valid. A move is valid if and only if it obeys the
   * card storage in each pile as per the description of the class "FreecellModel" .
   * Note: In this implementation of move method a move from foundation piles to any other piles
   * is allowed as long is it's a valid move as per the class' description.
   *
   * <p>In this implementation moving multiple cards from any pile is not a valid move and will
   * throw IllegalArgumentException. Always only one card can be moved and that is the top most
   * card in each pile.</p>
   *
   *
   * @param source         The type of the source pile see @link{PileType}
   * @param pileNumber     The pile number of the given type, starting at 0
   * @param cardIndex      The index of the card to be moved from the source
   *                       pile, starting at 0
   * @param destination    The type of the destination pile (see
   * @param destPileNumber The pile number of the given type, starting at 0
   * @throws IllegalArgumentException If the move is not possible {@link
   *                                  PileType})
   * @throws IllegalStateException    If a move is attempted before the game has
   *                                  started
   */
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {

    boolean isMoved;
    Card sourceCard;

    if (openPiles.size() == 0 && foundationPiles.size() == 0 && cascadePiles.size() == 0) {
      throw new IllegalStateException("Move can't be called before the game has started");
    }

    if (source == PileType.CASCADE) {
      validateSourcePileNumber(cascadePiles, pileNumber);
      validateSourcecard(cardIndex, cascadePiles.get(pileNumber).size() - 1);
      sourceCard = cascadePiles.get(pileNumber).get(cardIndex);
    } else if (source == PileType.FOUNDATION) {
      validateSourcePileNumber(foundationPiles, pileNumber);
      validateSourcecard(cardIndex, foundationPiles.get(pileNumber).size() - 1);
      sourceCard = foundationPiles.get(pileNumber).get(cardIndex);
    } else {
      validateSourcePileNumber(openPiles, pileNumber);
      validateSourcecard(cardIndex, 0);
      sourceCard = openPiles.get(pileNumber);
    }


    if (destination == PileType.CASCADE) {
      isMoved = moveToCascade(sourceCard, cascadePiles, destPileNumber);
    } else if (destination == PileType.FOUNDATION) {
      isMoved = moveToFoundation(sourceCard, foundationPiles, destPileNumber);
    } else {
      isMoved = moveToOpen(sourceCard, openPiles, destPileNumber);
    }


    if (isMoved) {

      if (source == PileType.CASCADE) {
        removeFromCascadeFoundation(cascadePiles, pileNumber, cardIndex);
      } else if (source == PileType.FOUNDATION) {
        removeFromCascadeFoundation(foundationPiles, pileNumber, cardIndex);
      } else {
        removeFromOpen(openPiles, pileNumber, cardIndex);
      }

    }


  }


}