package freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An abstract class that implements the FreecellOperations interface
 * and represents a basic free cell model where at a time only one card can
 * be moved. It implements the methods getGameState, getDeck, startGame, isGameover.
 * Move method is kept as an abstract method.
 */
public abstract class FreeCellAbstractModel implements FreecellOperations<Card> {

  protected final List<Card> deck;
  protected final int openPilesCount;
  protected final int cascadePilesCount;
  protected final SortedMap<Integer, List<Card>> foundationPiles;
  protected final SortedMap<Integer, List<Card>> cascadePiles;
  protected final SortedMap<Integer, Card> openPiles;

  /**
   * Package protected constructor that is called as part of building the corresponding concrete
   * objects and takes in the number of
   * open and cascade piles with which the game is to be played.
   *
   * @param openPilesCount    the number of open piles
   * @param casCadePilesCount the number of cascade piles
   */
  protected FreeCellAbstractModel(int openPilesCount,int casCadePilesCount) {
    deck = constructDeck();
    this.openPilesCount = openPilesCount;
    this.cascadePilesCount = casCadePilesCount;
    foundationPiles = new TreeMap<>();
    cascadePiles = new TreeMap<>();
    openPiles = new TreeMap<>();
  }

  /**
   * A private helper method that helps to create deck of
   * cards when an instance of FreeCellModel is created.
   * @return List of cards i.e. deck
   */
  private List<Card> constructDeck() {

    List<Card> deck = new ArrayList<>();

    for (int i = 12; i >= 0; i--) {
      deck.add(new Card(Suit.SPADE, Face.values()[i]));
      deck.add(new Card(Suit.CLUB, Face.values()[i]));
      deck.add(new Card(Suit.DIAMOND, Face.values()[i]));
      deck.add(new Card(Suit.HEART, Face.values()[i]));
    }
    return deck;
  }


  @Override
  public List<Card> getDeck() {

    List<Card> deck = new ArrayList<>();
    deck.addAll(this.deck);
    return deck;
  }


  /**
   * Deal a new game of freecell with the given deck, with or without shuffling
   * it first. This method first verifies that the deck is valid. It deals the
   * deck among the cascade piles in round robin fashion. Thus if there are 4
   * cascade piles, the 1st pile will get cards 0, 4, 8, ..., the 2nd pile will
   * get cards 1, 5, 9, ..., the 3rd pile will get cards 2, 6, 10, ... and the
   * 4th pile will get cards 3, 7, 11, .... Depending on the number of cascade
   * piles, they may have a different number of cards. This implementation also
   * resets the entire game at any state of game when this method is called once
   * the game has already started.
   *
   * @param deck    the deck to be dealt
   * @param shuffle if true, shuffle the deck else deal the deck as-is
   * @throws IllegalArgumentException if the deck is invalid
   */
  public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {

    if (deck == null) {
      throw new IllegalArgumentException("Provided deck of cards is invalid");
    }

    if (!ifDeckIsValid(deck)) {

      throw new IllegalArgumentException("Provided deck of cards isn't valid");

    }

    cascadePiles.clear();
    foundationPiles.clear();
    openPiles.clear();

    //If shuffle is true, mixing the order of cards in the list using Collections.shuffle()
    if (shuffle) {
      Collections.shuffle(deck);
    }

    for (int cascadePileNumber = 0; cascadePileNumber < cascadePilesCount; cascadePileNumber++) {

      cascadePiles.put(cascadePileNumber, new ArrayList<>());
    }
    //Distributing cards in round robin fashion
    for (int deckCount = 0; deckCount < deck.size(); deckCount++) {

      int cascadePileNumber = deckCount % cascadePiles.size();
      cascadePiles.get(cascadePileNumber).add(deck.get(deckCount));

    }
  }

  @Override
  public abstract void move(PileType source,
                            int pileNumber,
                            int cardIndex,
                            PileType destination,
                            int destPileNumber) throws IllegalArgumentException,
          IllegalStateException;

  /**
   * Signal if the game is over or not. If this method
   * is called before a game has started it will return only false.
   *
   * @return true if game is over, false otherwise
   */
  public boolean isGameOver() {

    if (foundationPiles.size() != 4) {
      return false;
    }
    for (int pileNumber = 0; pileNumber < foundationPiles.size(); pileNumber++) {

      if (foundationPiles.get(pileNumber).size() != 13) {
        return false;
      }

    }
    return true;
  }

  /**
   * Return the present state of the game as a string. The string is formatted
   * as follows:
   * <pre>
   * F1:[b]f11,[b]f12,[b],...,[b]f1n1[n] (Cards in foundation pile 1 in order)
   * F2:[b]f21,[b]f22,[b],...,[b]f2n2[n] (Cards in foundation pile 2 in order)
   * ...
   * Fm:[b]fm1,[b]fm2,[b],...,[b]fmnm[n] (Cards in foundation pile m in
   * order)
   * O1:[b]o11[n] (Cards in open pile 1)
   * O2:[b]o21[n] (Cards in open pile 2)
   * ...
   * Ok:[b]ok1[n] (Cards in open pile k)
   * C1:[b]c11,[b]c12,[b]...,[b]c1p1[n] (Cards in cascade pile 1 in order)
   * C2:[b]c21,[b]c22,[b]...,[b]c2p2[n] (Cards in cascade pile 2 in order)
   * ...
   * Cs:[b]cs1,[b]cs2,[b]...,[b]csps (Cards in cascade pile s in order)
   *
   * where [b] is a single blank space, [n] is newline. Note that there is no
   * newline on the last line
   * </pre>
   * An empty string will be returned if this method is called before the start of the game.
   *
   * @return the formatted string as above
   */
  public String getGameState() {

    StringBuilder gameState = new StringBuilder();

    if (cascadePiles.size() == 0 && foundationPiles.size() == 0 && openPiles.size() == 0) {
      return gameState.toString();
    }

    this.formGameStateForFoundationCascadePiles(gameState,
            'F', foundationPiles, 4);
    this.formGameStateForOpenPile(gameState);
    this.formGameStateForFoundationCascadePiles(gameState,
            'C', cascadePiles, cascadePilesCount);
    gameState.deleteCharAt(gameState.length() - 1);
    return gameState.toString();
  }

  /**
   * Helper method to check if the deck is valid. Adds each of the card object
   * from the deck to a new set and check after all cards from deck are added to the set, the
   * set contains exactly 52 cards.
   * @return boolean The deck is valid or not.
   */
  private boolean ifDeckIsValid(List<Card> deck) {

    Set<Card> cardSet = new HashSet<>(deck);

    return cardSet.size() == 52;
  }

  /**
   * A helper method that builds game state for foundation and cascade piles.
   * @param gameState game state string that will be returned by the getGameState method
   * @param pileCharacter pileCharacter is 'C' for cascade and 'F' for foundation pile.
   * @param pile helps to determine the type of pile. whether it's PileType.CASCADE or
   *             PileType.FOUNDATION
   * @param numberOfPiles represents the number of cascade or foundation piles present in
   *                        a particular free cell game.
   */
  private void formGameStateForFoundationCascadePiles(StringBuilder gameState, char pileCharacter,
                                                      Map<Integer, List<Card>> pile,
                                                      int numberOfPiles) {

    for (int pileNumber = 0; pileNumber < numberOfPiles; pileNumber++) {
      gameState.append(pileCharacter).append(pileNumber + 1).append(':');

      if (pile.containsKey(pileNumber)) {

        gameState.append(" ");
        List<Card> cardsInPile = pile.get(pileNumber);

        for (int cardCount = 0; cardCount < cardsInPile.size() - 1; cardCount++) {
          gameState.append(cardsInPile.get(cardCount).toString()).append(", ");

        }

        gameState.append(cardsInPile.get(cardsInPile.size() - 1).toString());
      }
      gameState.append('\n');
    }
  }

  /**
   * Helper method to build a game state for Open Piles.
   * @param gameState game state string that will be returned by the getGameState method.
   */
  private void formGameStateForOpenPile(StringBuilder gameState) {

    for (int openPileNumber = 0; openPileNumber < openPilesCount; openPileNumber++) {
      gameState.append('O').append(openPileNumber + 1).append(':');
      if (openPiles.containsKey(openPileNumber)) {
        gameState.append(openPiles.get(openPileNumber).toString());
      }
      gameState.append("\n");
    }
  }


  /**
   *A helper method to validate  the source pile number
   * provided as an argument to the move method.
   * @param pile represents one of the 3 piles in the game.
   * @param pileNumber represents number of the pile from which a card
   *                   has to be moved.
   * @throws IllegalArgumentException An exception is thrown if the pileNumber is
   *                                  not in between 0 and No.of
   *                                  open piles-1 in this game if the Pile is of
   *                                  type open.An exception is thrown if the
   *                                  pileNumber is not in between 0 and No.of
   *                                  cascade piles-1 in this game if the Pile is
   *                                  of type CASCADE. An exception is thrown
   *                                  if the PileNumber is not in between 0 and 3
   *                                  if the Pile is of type FOUNDATION.
   *
   */
  protected void validateSourcePileNumber(SortedMap<?, ?> pile, int pileNumber)
          throws IllegalArgumentException {

    if (!pile.containsKey(pileNumber)) {
      throw new IllegalArgumentException("provided source pile number doesn't exist");
    }

  }


  /**
   * A helper method used to validate if the card to be moved
   * is the top most card in the pile or not.
   * @param cardIndex represents the index of the card to be moved.
   * @param sourcePilesLastCardIndex represents the index of the top most card
   *                                 in a source pile.
   * @throws IllegalArgumentException An exception is thrown
   *                                  if {@code cardIndex!=sourcePilesLastCardIndex}
   */
  protected void validateSourcecard(int cardIndex, int sourcePilesLastCardIndex)
          throws IllegalArgumentException {

    if (cardIndex != sourcePilesLastCardIndex) {
      throw new IllegalArgumentException("provided source card isn't the last card in the "
              + "source pile");
    }

  }

  /**
   * A helper method that is called when a card needs to be moved to one of
   * the cascade piles.
   * @param sourceCard represents the card that needs to be moved.
   * @param cascadePiles represent the group of cascade piles.
   * @param destPileNumber represents the destination pile number
   * @return true if the source card is successfully moved and false if the
   *              source card after move operation remains in the same position as before.
   * @throws IllegalArgumentException an exception is thrown if the move to the cascade pile
   *                                  is invalid.
   */
  protected boolean moveToCascade(Card sourceCard, SortedMap<Integer, List<Card>> cascadePiles,
                                  int destPileNumber)
          throws IllegalArgumentException {


    if (cascadePiles.containsKey(destPileNumber)) {

      int cardsListSizeInPile = cascadePiles.get(destPileNumber).size();
      Card destinationPilesLastCard = cascadePiles.get(destPileNumber).get(cardsListSizeInPile - 1);

      if (sourceCard == destinationPilesLastCard) {
        return false;
      }

      if (sourceCard.getFaceValue().ordinal()
              != destinationPilesLastCard.getFaceValue().ordinal() - 1) {

        throw new IllegalArgumentException("Face value  of the card"
                + " you wanted to add should be one less than that of existing last card from the"
                + " pile");
      }

      if (sourceCard.getColor() == destinationPilesLastCard.getColor()) {

        throw new IllegalArgumentException("The card you wanted to add should have different color"
                + " to the existing card");
      }

      cascadePiles.get(destPileNumber).add(sourceCard);
      return true;
    } else {
      validateDestinationPileNumber(PileType.CASCADE,destPileNumber);
      cascadePiles.put(destPileNumber, new ArrayList<>());
      cascadePiles.get(destPileNumber).add(sourceCard);

      return true;
    }

  }

  /**
   * A helper method that remove the source card from it's source
   * position, once it's successfully added to the destination pile.
   * This method is called if the source card belongs to either cascade
   * or Foundation pile.
   * @param piles represent the group of piles.
   * @param pileNumber represent the source card's pile number.
   * @param cardIndex represent the position of source card in it's pile.
   */
  protected void removeFromCascadeFoundation(SortedMap<Integer, List<Card>> piles, int pileNumber,
                                             int cardIndex) {

    while (cardIndex <= piles.get(pileNumber).size() - 1) {
      piles.get(pileNumber).remove(piles.get(pileNumber).size() - 1);
    }
    if (piles.get(pileNumber).size() == 0) {
      piles.remove(pileNumber);
    }

  }


  /**
   * A helper method that is called when a move
   * operation with Foundation Pile as a destination is made.
   * @param sourceCard represents the card that needs to be moved.
   * @param foundationPiles represent the group of foundation piles.
   * @param destPileNumber represents the destination pile number.
   * @return true if the source card is successfully moved and false if the
   *         source card after move operation remains in the same position as before.
   * @throws IllegalArgumentException an exception is thrown if the move to the foundation
   *                                  pile is invalid.
   */
  protected boolean moveToFoundation(Card sourceCard,
                                     SortedMap<Integer, List<Card>> foundationPiles,
                                     int destPileNumber)
          throws IllegalArgumentException {


    if (foundationPiles.containsKey(destPileNumber)) {

      int cardsListSizeInPile = foundationPiles.get(destPileNumber).size();
      Card destinationPilesLastCard = foundationPiles.get(destPileNumber)
              .get(cardsListSizeInPile - 1);

      if (sourceCard == destinationPilesLastCard) {
        return false;
      }

      if (sourceCard.getFaceValue().ordinal()
              != destinationPilesLastCard.getFaceValue().ordinal() + 1) {

        throw new IllegalArgumentException("Face value  of the card"
                + " you wanted to add should be one less than that of existing last card"
                + " from the pile");
      }

      if (sourceCard.getSuit() != destinationPilesLastCard.getSuit()) {

        throw new IllegalArgumentException("The card you wanted to add should have different color"
                + " to the existing card");
      }

      foundationPiles.get(destPileNumber).add(sourceCard);
      return true;
    } else {

      validateDestinationPileNumber(PileType.FOUNDATION,destPileNumber);
      if (sourceCard.getFaceValue() != Face.A) {
        throw new IllegalArgumentException("The first card in a foundation pile should be an ace.");
      }

      foundationPiles.put(destPileNumber, new ArrayList<>());
      foundationPiles.get(destPileNumber).add(sourceCard);

      return true;
    }

  }

  /**A helper method that is called
   * when a card needs to be moved to one of the
   * open piles.
   * @param sourceCard represents the card to be moved.
   * @param openPiles represent the group of open piles in the game.
   * @param destPileNumber represents the destination pile number for card.
   * @return true if the source card is successfully moved and false if the
   *         source card after move operation remains in the same position as before.
   * @throws IllegalArgumentException when an attempt to move a card to a pile that is already
   *                                  containing a card.
   */
  protected boolean moveToOpen(Card sourceCard, SortedMap<Integer, Card> openPiles,
                               int destPileNumber)
          throws IllegalArgumentException {


    if (destPileNumber < 0 || destPileNumber >= openPilesCount) {
      throw new IllegalArgumentException("Destination pile number isn't valid");
    }


    if (openPiles.containsKey(destPileNumber)) {

      Card destinationPilesLastCard = openPiles.get(destPileNumber);

      if (sourceCard == destinationPilesLastCard) {
        return false;
      }

      throw new IllegalArgumentException("Destination Open Pile already holds a card");
    } else {
      validateDestinationPileNumber(PileType.OPEN,destPileNumber);
      openPiles.put(destPileNumber, sourceCard);

      return true;
    }
  }

  /**
   * A helper method that removes source card
   * from it's position if it's successfully added to it's destination pile.
   * This method is called if the source card is from Open Pile.
   * @param openPiles represents a group of open piles.
   * @param pileNumber represents the source card's pile number
   * @param cardIndex represents the source card index in it's pile.
   */
  protected void removeFromOpen(SortedMap<Integer, Card> openPiles, int pileNumber, int cardIndex) {
    openPiles.remove(pileNumber);
  }


  /**
   * A helper method to check if the destination pile number is valid.
   * @param pileType The type of pile i.e. CASCADE or FOUNDATION or OPEN
   * @param destPileNumber pileNumber of the destination pile.
   */
  protected void validateDestinationPileNumber(PileType pileType, int destPileNumber) {
    switch (pileType) {
      case CASCADE:
        if (destPileNumber < 0 || destPileNumber > this.cascadePilesCount - 1) {
          throw new IllegalArgumentException("Destination Pile Number is invalid");
        }
        break;
      case OPEN:
        if (destPileNumber < 0 || destPileNumber > this.openPilesCount - 1) {
          throw new IllegalArgumentException("Destination Pile Number is invalid");
        }
        break;

      case FOUNDATION:
        if (destPileNumber < 0 || destPileNumber > 3) {
          throw new IllegalArgumentException("Destination Pile Number is invalid");
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid PileType");
    }
  }
}
