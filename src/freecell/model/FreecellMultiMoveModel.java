package freecell.model;

import java.util.ArrayList;

import java.util.List;

import java.util.SortedMap;


/**
 * Represents a multi move model which is nothing but a  Free cell model
 * that allows movement of multiple cards at a time from one cascade pile
 * to another cascade pile. (while it is also possible to move several cards from
 * a cascade pile to a foundation pile, we will ignore this feature in this variation).
 *
 * <p>Moving multiple cards must obey two conditions. The first condition is that they should
 * form a valid build, i.e. they should be arranged in alternating colors and consecutive,
 * descending values in the cascade pile that they are moving from. The second condition is
 * the same for any move to a cascade pile: these cards should form a build with the last card
 * in the destination cascade pile.</p>
 *
 * <p>It may be noted that the ability of moving cards (besides the two conditions above)
 * is not a special feature, but a convenience to the player. A multi-card move is
 * basically several single-card moves, using free open piles and empty cascade piles
 * as “intermediate slots”. Thus a multi-card move may not be feasible even though it
 * obeys the above two conditions, if there aren’t enough of these intermediate slots.
 * More specifically, it can be proved that the maximum number of cards that can be moved
 * when there are 𝑁 free open piles and 𝐾 empty cascade piles is (𝑁+1)∗2𝐾. Your implementation
 * of this variation should work within all these three conditions.</p>
 *
 * <p>In order to use an empty cascade pile as an intermediary for multi-card moves,
 * we will allow any card to move to an empty cascade pile (not just a king,
 * as some Freecell versions mandate).</p>
 *
 */

public class FreecellMultiMoveModel extends FreeCellAbstractModel {

  /**
   * Constructs a  multi move model when provided with the no of open piles and cascade piles
   * required for that game.
   *
   * @param openPilesCount    No.of open piles required for a multi move free cell game.
   * @param casCadePilesCount No.of cascade piles required for multi move free cell game.
   */
  private FreecellMultiMoveModel(int openPilesCount, int casCadePilesCount) {

    super(openPilesCount, casCadePilesCount);
  }


  /**
   * A static inner class that implements FreecellOperationsBuilder interface and helps in creating
   * the FreeCellMultiMoveModel.
   */
  public static class FreeCellBuilder implements FreecellOperationsBuilder {

    private int cascadePilesCount;
    private int openPilesCount;

    /**
     * A default constructor that assigns the no.of cascade piles and no. of open piles required
     * for a multi move free cell game to 4 and 1 respectively.
     */
    private FreeCellBuilder() {
      cascadePilesCount = 4;
      openPilesCount = 1;
    }

    /**
     * This method is used to configure the number of cascade piles required for the game.
     *
     * @param cascadePilesCount No.of cascade piles required for a new game.
     * @return FreecellOperationsBuilder object.
     * @throws IllegalArgumentException is thrown when the cascaded piles count is less than the
     *                                  default value as assigned by the default constructor.
     */
    public FreecellOperationsBuilder cascades(int cascadePilesCount)
            throws IllegalArgumentException {

      if (cascadePilesCount < this.cascadePilesCount) {
        throw new IllegalArgumentException("No. of cascade piles shouldn't be less than 4");
      }
      this.cascadePilesCount = cascadePilesCount;
      return this;
    }

    /**
     * This method is used to configure the number of open piles required for the game.
     *
     * @param openPilesCount No.of open piles required for a new game.
     * @return FreecellOperationsBuilder object
     * @throws IllegalArgumentException is thrown when the openpilesCount is less than the default
     *                                  value 1 as set by the default constructor.
     */
    public FreecellOperationsBuilder opens(int openPilesCount) throws IllegalArgumentException {

      if (openPilesCount < this.openPilesCount) {
        throw new IllegalArgumentException("No.of open piles should be less than 1");
      }
      this.openPilesCount = openPilesCount;
      return this;
    }

    @Override
    public FreecellOperations<Card> build() {

      return new FreecellMultiMoveModel(openPilesCount, cascadePilesCount);

    }

  }

  /**
   * A static method that can be used by any class to create and use an object of type
   * FreecellOperationsBuilder in order to further create a FreecellMultiMoveModel.
   *
   * @return an object of type FreecellOperationsBuilder
   */
  public static FreecellOperationsBuilder getBuilder() {
    return new FreecellMultiMoveModel.FreeCellBuilder();
  }


  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid. A
   * move is valid if and only if it obeys the card storage in each pile as per the description of
   * the class "FreecellMultiMoveModel" . Note: In this implementation of move method a move from
   * foundation piles to any other piles is allowed as long is it's a valid move as per the class'
   * description.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if the move is not possible {@link PileType})
   * @throws IllegalStateException    if a move is attempted before the game has starts
   */
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {

    boolean isMoved;
    Card sourceCard = new Card();
    List<Card> sourceCards = new ArrayList<>();

    if (openPiles.size() == 0 && foundationPiles.size() == 0 && cascadePiles.size() == 0) {
      throw new IllegalStateException("Move can't be called before the game has started");
    }

    if (source == PileType.CASCADE) {
      validateSourcePileNumber(cascadePiles, pileNumber);
      validateSourceCascadecards(cardIndex, pileNumber, destination);
      sourceCards = cascadePiles.get(pileNumber)
              .subList(cardIndex, cascadePiles.get(pileNumber).size());
      sourceCard = cascadePiles.get(pileNumber).get(cardIndex);
    } else if (source == PileType.FOUNDATION) {
      validateSourcePileNumber(foundationPiles, pileNumber);
      validateSourcecard(cardIndex, foundationPiles.get(pileNumber)
              .size() - 1);
      sourceCard = foundationPiles.get(pileNumber).get(cardIndex);
      sourceCards.add(sourceCard);
    } else {
      validateSourcePileNumber(openPiles, pileNumber);
      validateSourcecard(cardIndex, 0);
      sourceCard = openPiles.get(pileNumber);
      sourceCards.add(sourceCard);
    }


    if (destination == PileType.CASCADE) {
      isMoved = moveToCascade(sourceCards, cascadePiles, destPileNumber);
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


  /**
   * A helper method that is called when a card needs to be moved to one of the cascade piles.
   *
   * @param sourceCards    represents the list of cards that needs to be moved.
   * @param cascadePiles   represent the group of cascade piles.
   * @param destPileNumber represents the destination pile number
   * @return true if the source card is successfully moved and false if the source card after move
   *         operation remains in the same position as before.
   * @throws IllegalArgumentException an exception is thrown if the move to the cascade pile is
   *                                  invalid.
   */
  private boolean moveToCascade(List<Card> sourceCards, SortedMap<Integer, List<Card>> cascadePiles,
                                int destPileNumber)
          throws IllegalArgumentException {

    int openPilesNumber = openPilesCount - openPiles.size();
    int cascadePilesNumber = cascadePilesCount - cascadePiles.size();

    if (cascadePiles.containsKey(destPileNumber)) {

      int cardsListSizeInPile = cascadePiles.get(destPileNumber).size();
      Card destinationPilesLastCard = cascadePiles.get(destPileNumber).get(cardsListSizeInPile - 1);

      if (sourceCards.get(0) == destinationPilesLastCard) {
        return false;
      }

      validateBuild(destinationPilesLastCard, sourceCards.get(0));

      validateIntermediateSlotsAvailability(openPilesNumber, cascadePilesNumber,
              sourceCards.size());
      cascadePiles.get(destPileNumber).addAll(sourceCards);
      return true;
    } else {

      validateIntermediateSlotsAvailability(openPilesNumber,
              cascadePilesNumber - 1, sourceCards.size());
      cascadePiles.put(destPileNumber, new ArrayList<>());
      cascadePiles.get(destPileNumber).addAll(sourceCards);

      return true;
    }

  }


  /**
   * A private helper method that validates the source cascade cards if the card index and the
   * multiple cards form a valid build.
   *
   * @param cardIndex       the card/bottom card of a build to be moved
   * @param pileNumber      the source cascade pile number from which the card has to be moved
   * @param destinationPile the destination pile to which the card has to be moved
   * @throws IllegalArgumentException if the card index is not valid or the in the multi card move
   *                                  the build is not valid.
   */
  private void validateSourceCascadecards(int cardIndex, int pileNumber, PileType destinationPile)
          throws IllegalArgumentException {

    if (destinationPile != PileType.CASCADE && cardIndex
            != cascadePiles.get(pileNumber).size() - 1) {

      throw new IllegalArgumentException("Invalid card index.");
    }

    if (cardIndex < 0 || cardIndex > cascadePiles.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("Invalid card index.");
    }

    List<Card> sourceCards = cascadePiles.get(pileNumber);

    for (int i = cardIndex; i < sourceCards.size() - 1; i++) {
      try {
        validateBuild(sourceCards.get(i), sourceCards.get(i + 1));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Source cards doesn't form a valid build");
      }
    }

  }


  /**
   * A private helper method to verify if the cascade source cards form a valid build ie each card
   * should be one less than the bottom card and of different colour.
   *
   * @param bottomCard the bottom card in the case of a multi card move or the last card in case of
   *                   a single card move to be compared with top card
   * @param topCard    the card which is placed over bottom card
   * @throws IllegalArgumentException if the top card is not exactly one less than the bottom card
   *                                  or they are of the same colour.
   */
  private void validateBuild(Card bottomCard, Card topCard) throws IllegalArgumentException {

    if (topCard.getFaceValue().ordinal()
            != bottomCard.getFaceValue().ordinal() - 1) {

      throw new IllegalArgumentException("Face value  of the card"
              + " you wanted to add should be one less than that of existing last card from the"
              + " pile");
    }

    if (topCard.getColor() == bottomCard.getColor()) {

      throw new IllegalArgumentException("The card you wanted to add should have different color"
              + " to the existing card");
    }

  }

  /**
   * A private helper method that checks for the number of intermediate pile slots available
   * inorder for a multi card move to be possible.
   *
   * @param emptyOpenPilesNumber    Number of empty open piles available
   * @param emptyCascadePilesNumber Number of empty cascade piles available
   * @param numberOfSourceCards     Number of cards to be moved
   * @throws IllegalArgumentException If the number of cards to be moved is greater than maximum
   *                                  number of cards that can be moved at any time as described in
   *                                  this class's description.
   */
  private void validateIntermediateSlotsAvailability(int emptyOpenPilesNumber,
                                                     int emptyCascadePilesNumber,
                                                     int numberOfSourceCards)
          throws IllegalArgumentException {

    if (numberOfSourceCards > (emptyOpenPilesNumber + 1) * Math.pow(2, emptyCascadePilesNumber)) {
      throw new IllegalArgumentException("No.of card you wanted to move are greater than the"
              + "available intermediate slots");
    }

  }


}
