import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;


import static org.junit.Assert.assertEquals;

/**
 * Tests MultiMove Model for the multiple cards movement.
 */

public class MultiMoveTest {

  FreecellOperations freecellModel;

  /**
   * Checking if multiple cards can be moved with empty open piles and no empty cascade piles.
   */
  @Test
  public void testMultiMove() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);
    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 1);

    String expectedState = "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n" + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣, 6♥, 5♠, 4♥, "
            + "3♠, 2♥, A♠\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";

    assertEquals(expectedState, freecellModel.getGameState());



  }

  /**
   * Multi move is not possible because of insufficient intermediate empty piles.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalid() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(4).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);
    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 1);

  }

  /**
   * Multi move is not possible because of insufficient intermediate empty piles.
   * Testing if the game state remains same after invalid move.
   */
  @Test
  public void testMultiMoveInvalidAssert() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(4).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);
    try {
      freecellModel.move(PileType.CASCADE, 0, 7,
              PileType.CASCADE, 1);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedString = "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";

    assertEquals(expectedString, freecellModel.getGameState());

    //System.out.println(freecellModel.getGameState());

  }

  /**
   * Checking if multiple cards can be moved with empty open piles and empty cascade piles.
   */
  @Test
  public void testMultiMoveWithEmptyCascadePile() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(2).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 0; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 1);

    String expectedState = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣, 6♥, 5♠, "
            + "4♥, 3♠, 2♥, A♠\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4:";

    assertEquals(expectedState, freecellModel.getGameState());
  }

  /**
   * Multiple cards cannot be moved to empty cascade pile as there are not enough intermediate
   * slots.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToEmptyCascadeInvalid() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(2).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 0; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 3);
  }

  /**
   * Multiple cards cannot be moved to empty cascade pile as there are not enough intermediate
   * slots. Testing if the game state remains same after invalid move.
   */
  @Test
  public void testMultiMoveToEmptyCascadeInvalidAssert() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(2).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 0; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    try {
      freecellModel.move(PileType.CASCADE, 0, 7,
              PileType.CASCADE, 3);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedString = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4:";
    assertEquals(expectedString, freecellModel.getGameState());

  }



  /**
   * Multiple cards moved to an empty cascade pile.
   */
  @Test
  public void testMultiMoveToEmptyCascadeValid() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 0; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }


    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 3);

    String expectedState = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: 6♥, 5♠, 4♥, 3♠, 2♥, A♠";

    assertEquals(expectedState, freecellModel.getGameState());
  }

  /**
   * Throws IllegalArgumentException because the cards are of same colour in the build.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSourceBuildInvalidDueToColour() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    freecellModel.move(PileType.CASCADE, 3, 7, PileType.CASCADE, 1);

  }

  /**
   * Throws IllegalArgumentException because the cards are of same colour in the build.
   * Testing if the game state remains same after invalid move.
   */
  @Test
  public void testSourceBuildInvalidDueToColourAssert() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    try {
      freecellModel.move(PileType.CASCADE, 3, 7,
              PileType.CASCADE, 1);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedState = "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";

    assertEquals(expectedState, freecellModel.getGameState());

  }

  /**
   * Throws IllegalArgumentException because the source card is not one less than the
   * destination card.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildInvalidDueToDestinationNumber() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 0, 8,
            PileType.CASCADE, 3);

  }

  /**
   * Throws IllegalArgumentException because the source card is not one less than the
   * destination card. Testing if the game state remains same after invalid move.
   */
  @Test
  public void testBuildInvalidDueToDestinationNumberAssert() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    try {
      freecellModel.move(PileType.CASCADE, 0, 8,
              PileType.CASCADE, 3);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedString = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦";


    assertEquals(expectedString, freecellModel.getGameState());
  }

  /**
   * Throws IllegalArgumentException.class because the source card is of same colour as
   * destination.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildInvalidDueToDestinationColour() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 0, 7,
            PileType.CASCADE, 3);

  }

  /**
   * Throws IllegalArgumentException because the source card is of same colour as
   * destination. Testing if the game state remains same after invalid move.
   */
  @Test
  public void testBuildInvalidDueToDestinationColourAssert() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    try {
      freecellModel.move(PileType.CASCADE, 0, 7,
              PileType.CASCADE, 3);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedString = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦";

    assertEquals(expectedString, freecellModel.getGameState());


  }

  /**
   * Throws IllegalArgumentException.class because the source card is not one less than the next
   * build card.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildInvalidDueToSourceNumber() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 2, 10,
            PileType.CASCADE, 3);


  }

  /**
   * Throws IllegalArgumentException because the source card is not one less than the next
   * build card. Testing if the game state remains same after invalid move.
   */
  @Test
  public void testBuildInvalidDueToSourceNumberAssert() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 7; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    try {
      freecellModel.move(PileType.CASCADE, 2, 10,
              PileType.CASCADE, 3);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedString = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦";

    assertEquals(expectedString, freecellModel.getGameState());
  }

  /**
   * Moving multiple cards with occupied open pile.
   */
  @Test
  public void testMoveMultipleCardsWithOccupiedOpenPile() {

    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    for (int i = 12; i >= 8; i--) {
      freecellModel.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 1);
    }

    freecellModel.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);
    freecellModel.move(PileType.CASCADE, 0, 8,
            PileType.CASCADE, 3);

    String expectedState = "F1:\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:7♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♠, 4♥, 3♠, 2♥, A♠";

    assertEquals(expectedState, freecellModel.getGameState());
  }

  /**
   * Throws IllegalArgumentException.class because multiple cards cannot be moved to open pile.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoveMultipleCardsToOpen() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    freecellModel.move(PileType.CASCADE, 0, 8,
            PileType.OPEN, 0);

  }

  /**
   * Throws IllegalArgumentException.class because multiple cards cannot be moved to open pile.
   * Testing if the game state remains same after invalid move.
   */
  @Test
  public void testMoveMultipleCardsToOpenAssert() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    try {
      freecellModel.move(PileType.CASCADE, 0, 8,
              PileType.OPEN, 0);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedState = "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣, 4♣, 3♣, 2♣, A♣, 7♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";

    assertEquals(expectedState, freecellModel.getGameState());

  }

  /**
   * Throws IllegalArgumentException.class because multiple cards cannot be moved to foundation
   * pile.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoveMultipleCardsToFoundation() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    freecellModel.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);
    for (int i = 11; i > 7; i--) {
      freecellModel.move(PileType.CASCADE, 1, i,
              PileType.FOUNDATION, 3);
    }

    freecellModel.move(PileType.CASCADE, 1, 7,
            PileType.FOUNDATION, 3);

  }

  /**
   * Throws IllegalArgumentException.class because multiple cards cannot be moved to foundation
   * pile. Testing if the game state remains same after invalid move.
   */
  @Test
  public void testMoveMultipleCardsToFoundationAssert() {
    freecellModel = FreecellMultiMoveModel.getBuilder().opens(5).build();
    List deck = freecellModel.getDeck();

    rearrangeDeck(deck);
    freecellModel.startGame(deck, false);

    freecellModel.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);
    for (int i = 11; i > 7; i--) {
      freecellModel.move(PileType.CASCADE, 1, i,
              PileType.FOUNDATION, 3);
    }

    try {
      freecellModel.move(PileType.CASCADE, 1, 7,
              PileType.FOUNDATION, 3);
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    String expectedState = "F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4: A♣, 2♣, 3♣, 4♣\n"
            + "O1:7♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "O5:\n"
            + "C1: K♠, Q♥, J♠, 10♥, 9♠, 8♥, 7♠, 6♥, 5♠, 4♥, 3♠, 2♥, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 5♣, 6♣\n"
            + "C3: Q♠, 10♠, 8♠, 5♥, 4♠, 2♠, K♥, J♥, 9♥, 7♥, 6♠, 3♥, A♥\n"
            + "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦";

    assertEquals(expectedState, freecellModel.getGameState());

  }


  private void rearrangeDeck(List deck) {

    List aces = (List) deck.stream().filter(x -> x.toString().contains("A"))
            .collect(Collectors.toList());
    List twos = (List) deck.stream().filter(x -> x.toString().contains("2"))
            .collect(Collectors.toList());
    List threes = (List) deck.stream().filter(x -> x.toString().contains("3"))
            .collect(Collectors.toList());
    List fours = (List) deck.stream().filter(x -> x.toString().contains("4"))
            .collect(Collectors.toList());
    List fives = (List) deck.stream().filter(x -> x.toString().contains("5"))
            .collect(Collectors.toList());
    List sixes = (List) deck.stream().filter(x -> x.toString().contains("6"))
            .collect(Collectors.toList());
    List sevens = (List) deck.stream().filter(x -> x.toString().contains("7"))
            .collect(Collectors.toList());
    List eights = (List) deck.stream().filter(x -> x.toString().contains("8"))
            .collect(Collectors.toList());
    List nines = (List) deck.stream().filter(x -> x.toString().contains("9"))
            .collect(Collectors.toList());
    List tens = (List) deck.stream().filter(x -> x.toString().contains("10"))
            .collect(Collectors.toList());
    List jokers = (List) deck.stream().filter(x -> x.toString().contains("J"))
            .collect(Collectors.toList());
    List queens = (List) deck.stream().filter(x -> x.toString().contains("Q"))
            .collect(Collectors.toList());
    List kings = (List) deck.stream().filter(x -> x.toString().contains("K"))
            .collect(Collectors.toList());
    deck.removeAll(deck);
    List<List> cardsList = new ArrayList();
    cardsList.add(kings);
    cardsList.add(queens);
    cardsList.add(jokers);
    cardsList.add(tens);
    cardsList.add(nines);
    cardsList.add(eights);
    cardsList.add(sevens);
    cardsList.add(sixes);
    cardsList.add(fives);
    cardsList.add(fours);
    cardsList.add(threes);
    cardsList.add(twos);
    cardsList.add(aces);

    int index;
    List clublist = new ArrayList();
    List spadelist = new ArrayList();
    List diamondlist = new ArrayList();
    List heartList = new ArrayList();
    for (int i = 0; i < cardsList.size(); i++) {

      if (i == 3) {
        index = 2;
      } else {
        index = 1;
      }


      for (int j = 0; j < cardsList.get(i).size(); j++) {

        switch (cardsList.get(i).get(j).toString().charAt(index)) {

          case '♣':
            clublist.add(cardsList.get(i).get(j));
            break;

          case '♠':
            spadelist.add(cardsList.get(i).get(j));
            break;

          case '♦':
            diamondlist.add(cardsList.get(i).get(j));
            break;

          case '♥':
            heartList.add(cardsList.get(i).get(j));
            break;
          default: //Do Nothing
        }
      }

    }

    Collections.reverse(spadelist);
    Collections.reverse(clublist);
    Collections.reverse(heartList);
    Collections.reverse(diamondlist);

    deck.add(spadelist.get(12));
    deck.add(clublist.get(12));
    deck.add(spadelist.get(11));
    deck.add(diamondlist.get(12));

    deck.add(heartList.get(11));
    deck.add(clublist.get(11));
    deck.add(spadelist.get(9));
    deck.add(diamondlist.get(11));

    deck.add(spadelist.get(10));
    deck.add(clublist.get(10));
    deck.add(spadelist.get(7));
    deck.add(diamondlist.get(10));

    deck.add(heartList.get(9));
    deck.add(clublist.get(9));
    //deck.add(spadelist.get(5));
    deck.add(heartList.get(4));

    deck.add(diamondlist.get(9));

    deck.add(spadelist.get(8));
    deck.add(clublist.get(8));
    deck.add(spadelist.get(3));
    deck.add(diamondlist.get(8));

    deck.add(heartList.get(7));
    deck.add(clublist.get(7));
    deck.add(spadelist.get(1));
    deck.add(diamondlist.get(7));

    deck.add(spadelist.get(6));
    deck.add(clublist.get(4));
    deck.add(heartList.get(12));
    deck.add(diamondlist.get(6));

    deck.add(heartList.get(5));
    deck.add(clublist.get(5));
    deck.add(heartList.get(10));
    deck.add(diamondlist.get(5));

    deck.add(spadelist.get(4));
    deck.add(clublist.get(3));
    deck.add(heartList.get(8));
    deck.add(diamondlist.get(4));

    deck.add(heartList.get(3));
    deck.add(clublist.get(2));
    deck.add(heartList.get(6));
    deck.add(diamondlist.get(3));

    deck.add(spadelist.get(2));
    deck.add(clublist.get(1));
    //deck.add(heartList.get(4));
    deck.add(spadelist.get(5));
    deck.add(diamondlist.get(2));

    deck.add(heartList.get(1));
    deck.add(clublist.get(0));
    deck.add(heartList.get(2));
    deck.add(diamondlist.get(1));

    deck.add(spadelist.get(0));
    deck.add(clublist.get(6));
    deck.add(heartList.get(0));
    deck.add(diamondlist.get(0));


  }


}