import org.junit.Test;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class FreecellModelTest {

  private final int mincascadePilesCount = 4;
  private final int maxcascadePilesCount = 8;
  private final int minOpenPilesCount = 1;
  private final int maxOpenPilesCount = 4;
  private final Random r = new Random();


  @Test
  public void checkForObjectCorrectness() {

    FreecellOperations gameObject;
    gameObject = FreecellModel.getBuilder().build();
    List deck = gameObject.getDeck();
    gameObject.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 4, 1), gameObject.getGameState());
    gameObject = FreecellModel.getBuilder().cascades(5).build();
    gameObject.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 5, 1), gameObject.getGameState());
    gameObject = FreecellModel.getBuilder().opens(3).build();
    gameObject.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 4, 3), gameObject.getGameState());
    gameObject = FreecellModel.getBuilder().opens(4).cascades(8).build();
    gameObject.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 8, 4), gameObject.getGameState());

    try {
      gameObject = FreecellModel.getBuilder().cascades(-1).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      gameObject = FreecellModel.getBuilder().cascades(0).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      gameObject = FreecellModel.getBuilder().cascades(3).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    try {
      gameObject = FreecellModel.getBuilder().cascades(9).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      gameObject = FreecellModel.getBuilder().opens(-1).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      gameObject = FreecellModel.getBuilder().opens(0).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      gameObject = FreecellModel.getBuilder().opens(5).build();
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }
  }

  @Test
  public void testgetDeck() {

    FreecellOperations gameObject;
    gameObject = FreecellModel.getBuilder().build();
    List deck = gameObject.getDeck();
    try {
      deckValidate(deck);
    } catch (IllegalStateException e) {
      fail("Above line shouldn't have thrown exception");
    }

    deck.add(deck.get(deck.size() - 1));

    try {
      deckValidate(deck);
      fail("Above line should have thrown exception");
    } catch (IllegalStateException e) {
      //Do nothing;
    }

    deck.remove(deck.size() - 1);
    deck.remove(deck.size() - 1);
    deck.add(deck.get(deck.size() - 1));
    try {
      deckValidate(deck);
      fail("Above line should have thrown exception");
    } catch (IllegalStateException e) {
      //
    }
    try {
      deck = gameObject.getDeck();
    } catch (IllegalArgumentException e) {
      fail("Above line shouldn't have thrown exception");
    }

  }

  private void deckValidate(List deck) throws IllegalStateException {

    if (deck.size() != 52) {
      throw new IllegalStateException("Deck contains a card with invalid suit");
    }
    StringBuilder deckString = new StringBuilder();
    for (int i = 0; i < deck.size(); i++) {
      deckString.append(deck.get(i).toString()).append(',');
    }
    deckString.deleteCharAt(deckString.length() - 1);
    String[] cardsGroup = deckString.toString().split(",");
    Set<String> spadesSet = new HashSet<>();
    Set<String> clubsSet = new HashSet<>();
    Set<String> diamondsSet = new HashSet<>();
    Set<String> heartsSet = new HashSet<>();
    for (int i = 0; i < cardsGroup.length; i++) {
      if (cardsGroup[i].matches("^[1][0]['♠']")
              || cardsGroup[i].matches("[A2-9JQK]['♠']")) {
        spadesSet.add(cardsGroup[i]);
      } else if (cardsGroup[i].matches("^[1][0]['♣']")
              || cardsGroup[i].matches("[A2-9JQK]['♣']")) {
        clubsSet.add(cardsGroup[i]);
      } else if (cardsGroup[i].matches("^[1][0]['♦']")
              || cardsGroup[i].matches("[A2-9JQK]['♦']")) {
        diamondsSet.add(cardsGroup[i]);
      } else if (cardsGroup[i].matches("^[1][0]['♥']")
              || cardsGroup[i].matches("[A2-9JQK]['♥']")) {
        heartsSet.add(cardsGroup[i]);
      } else {
        throw new IllegalStateException("Deck contains a card with invalid suit");
      }
    }

    if (spadesSet.size() != 13 || clubsSet.size() != 13
            || diamondsSet.size() != 13 || heartsSet.size() != 13) {
      throw new IllegalStateException("Deck contains a card with invalid suit");
    }
  }


  @Test
  public void testStartGame() {
    FreecellOperations game = FreecellModel.getBuilder().build();
    List deck = new ArrayList();
    try {
      game.startGame(null, true);
      fail("Above line should have thrown have exception");
    } catch (IllegalArgumentException e) {
       //Do nothing
    }
    try {
      game.startGame(deck, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      assertEquals("", game.getGameState());
    }
    deck = game.getDeck();
    for (int i = 0; i <= 10; i++) {
      deck.remove(deck.size() - 1);
    }
    //Exception for card numbers not equal to 52.
    try {
      game.startGame(deck, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      // Do nothing
    }
    /*check if deck has duplicate cards, but all the cards
    are having unique reference and deck having size of 52*/
    deck = game.getDeck();
    List anotherDeck = game.getDeck();
    List resultDeck = new ArrayList();
    for (int i = 0; i < 26; i++) {
      resultDeck.add(deck.add(i));
    }
    for (int i = 0; i < 26; i++) {
      resultDeck.add(anotherDeck.add(i));
    }

    try {
      game.startGame(resultDeck, false);
      fail("Illegal Argument exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing;
    }

    //check for deck having sie greater than 52;
    deck = game.getDeck();
    for (int i = 0; i <= 10; i++) {
      deck.add(deck.size() - 1);
    }
    try {
      game.startGame(deck, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do nothing
    }


    int randomCascadePiles = mincascadePilesCount + r.nextInt(maxcascadePilesCount);
    int randomOpenPiles = minOpenPilesCount + r.nextInt(maxOpenPilesCount);
    game = FreecellModel.getBuilder().cascades(randomCascadePiles).opens(randomOpenPiles).build();

    deck = game.getDeck();
    game.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, randomCascadePiles,
            randomOpenPiles), game.getGameState());

    List deckToShuffle = new ArrayList();
    deckToShuffle.addAll(deck);
    game.startGame(deckToShuffle, true);
    assertNotEquals(getExpectedDeal(deck, randomCascadePiles, randomOpenPiles),
            game.getGameState());


  }

  private String getExpectedDeal(List deck, int cascadePilesCount, int openPilesCount) {

    StringBuilder expectedDeal = new StringBuilder();
    expectedDeal.append("F1:\nF2:\nF3:\nF4:\n");
    for (int i = 1; i <= openPilesCount; i++) {
      expectedDeal.append("O").append(i).append(":\n");
    }
    StringBuilder[] cascadeCards = new StringBuilder[cascadePilesCount];

    for (int i = 0; i < cascadePilesCount; i++) {
      cascadeCards[i] = new StringBuilder();
    }

    for (int i = 0; i < deck.size(); i++) {
      cascadeCards[i % cascadePilesCount].append(deck.get(i).toString()).append(", ");
    }


    for (int i = 0; i < cascadeCards.length; i++) {
      cascadeCards[i].deleteCharAt(cascadeCards[i].length() - 1)
              .deleteCharAt(cascadeCards[i].length() - 1);
      expectedDeal.append("C").append(i + 1).append(": ").append(cascadeCards[i]).append("\n");
    }

    expectedDeal.deleteCharAt(expectedDeal.length() - 1);
    return expectedDeal.toString();
  }


  @Test
  public void testGetGameState() {

    int randomCascadePiles = mincascadePilesCount + r.nextInt(maxcascadePilesCount);
    int randomOpenPiles = minOpenPilesCount + r.nextInt(maxOpenPilesCount);

    FreecellOperations game = FreecellModel.getBuilder().cascades(randomCascadePiles)
            .opens(randomOpenPiles).build();

    assertEquals("", game.getGameState());
    List deck = game.getDeck();
    assertEquals(false, game.isGameOver());
    game.startGame(deck, false);
    assertEquals(false, game.isGameOver());

  }

  @Test
  public void testMove() {
    FreecellOperations game = FreecellModel.getBuilder().opens(4).build();
    List deck = game.getDeck();
    rearrangeDeck(deck);
    assertEquals(false, game.isGameOver());
    try {
      game.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalStateException e) {
      //DO Nothing
    }
    game.startGame(deck, false);
    assertEquals(false, game.isGameOver());
    try {
      game.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.OPEN, 0, 0, PileType.CASCADE, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.CASCADE, -1, 12, PileType.FOUNDATION,
              3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }


    try {
      game.move(PileType.CASCADE, 5, 12, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }


    try {
      game.move(PileType.CASCADE, 4, 12, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.CASCADE, 0, 13, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.CASCADE, 0, -1, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }


    try {
      game.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.CASCADE, 0, 12, PileType.OPEN, 5);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    try {
      game.move(PileType.CASCADE, 0, 12, PileType.OPEN, 4);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO nothing
    }

    assertEquals(getExpectedDeal(deck, 4, 4), game.getGameState());
    game.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    //test if open pile accepts more than 1 card
    try {
      game.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    //test if open pile accepts more than 1 card
    try {
      game.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    //test if open pile accepts more than 1 card
    try {
      game.move(PileType.CASCADE, 2, 12, PileType.OPEN, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    //test if open pile accepts more than 1 card
    try {
      game.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    game.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:A♠\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 1, 11, PileType.OPEN, 2);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:A♠\n"
            + "O3:2♠\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 0, 0, PileType.OPEN, 3);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:A♠\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    //check if empty foundation pile accepts anything ohter than 'ACE"
    try {
      game.move(PileType.OPEN, 2, 0, PileType.FOUNDATION, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }
    game.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
    assertEquals("F1: A♠\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 2, 11, PileType.OPEN, 1);
    assertEquals("F1: A♠\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:2♦\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 1, 0, PileType.CASCADE, 1);
    assertEquals("F1: A♠\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♦\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    /**check if foundation pile accepts a card with face value greater than one over the
     face value of it's last card, but belongs to different color*/
    try {
      game.move(PileType.CASCADE, 1, 11, PileType.FOUNDATION, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }
    game.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 1);
    assertEquals("F1: A♠\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♦\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    /*check if foundation pile accepts a card with face value greater than one over the
    face value of it's last card, but belongs to same color and different suit*/
    try {
      game.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }
    game.move(PileType.CASCADE, 1, 11, PileType.FOUNDATION, 1);
    assertEquals("F1: A♠\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    /**
     * check if a foundation pile accepts a card that belongs to the same suit as that of it's last
     * card but it's greater by more than 1.
     */
    try {
      game.move(PileType.CASCADE, 1, 10, PileType.FOUNDATION, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }

    /* check if the cascade pile accepts a card if the card is
    having a different color compared to it's last card in the pile  but is
    less than a value greater than 1.*/
    try {
      game.move(PileType.CASCADE, 3, 12, PileType.CASCADE, 1);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }

    /**check if cascade pile accepts a card of same color, but of
     * different suit and the face value is less than one by the face value of
     * last card in the pile.
     */
    try {
      game.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 1);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }

    /**
     * check if cascade pile accepts a card of same suit and whose face value is
     * one less than that of the face value of the last card from the pile.
     */
    try {
      game.move(PileType.OPEN, 0, 3, PileType.CASCADE, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }

    /** check if cascade pile accepts a card of different color and whose
     * face value is greater than that of the face value of it's last card.
     */
    try {
      game.move(PileType.CASCADE, 2, 10, PileType.CASCADE, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }


    /** check if cascade pile accepts a card of different color and whose
     * face value is equal to that of the face value of it's last card.
     */
    try {
      game.move(PileType.FOUNDATION, 1, 1, PileType.CASCADE, 0);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //DO Nothing
    }

    game.move(PileType.FOUNDATION, 0, 0, PileType.FOUNDATION, 3);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4: A♠\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    game.move(PileType.FOUNDATION, 3, 0, PileType.OPEN, 0);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    game.move(PileType.FOUNDATION, 1, 1, PileType.CASCADE, 1);
    assertEquals("F1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♦\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 1, 11, PileType.FOUNDATION, 1);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());

    /**
     * Trying to move a card from foundation pile that's
     * not the last card in the list.
     */
    try {
      game.move(PileType.FOUNDATION, 1, 0, PileType.FOUNDATION,
              2);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    /**
     * Remove a card from source pile that is empty.
     */
    try {
      game.move(PileType.FOUNDATION, 2, 0, PileType.FOUNDATION,
              3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    /**
     * Trying to remove a card from cascade pile that is not the last card.
     */
    try {
      game.move(PileType.CASCADE, 2, 3, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    /**
     * Trying to remove a card from open pile that is not the last card.
     */
    try {
      game.move(PileType.OPEN, 0, 1, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    /**
     * Trying to remove a card from an empty open pile.
     */
    try {
      game.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 3);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    /**Trying to move last card of a cascade pile to it's own place.
     */
    game.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 0);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    /**Trying to move last card of a foundation pile to it's own place.
     */
    game.move(PileType.FOUNDATION, 1, 1, PileType.FOUNDATION, 1);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    /**Trying to move card of a open pile to it's own place.
     */
    game.move(PileType.OPEN, 3, 0, PileType.OPEN, 3);
    assertEquals("F1:\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♠\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
    assertEquals("F1: A♠\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 2, 0, PileType.FOUNDATION, 0);
    assertEquals("F1: A♠, 2♠\n"
            + "F2: A♦, 2♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 3, 0, PileType.FOUNDATION, 2);
    assertEquals("F1: A♠, 2♠\n"
            + "F2: A♦, 2♦\n"
            + "F3: A♣\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    for (int i = 11; i >= 0; i--) {
      game.move(PileType.CASCADE, 0, i, PileType.FOUNDATION, 2);
    }
    assertEquals("F1: A♠, 2♠\n"
            + "F2: A♦, 2♦\n"
            + "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1:\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    /**Attempt to remove a card from an empty cascade pile**/
    try {
      game.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 2);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do nothing
    }

    for (int i = 10; i >= 0; i--) {
      game.move(PileType.CASCADE, 1, i, PileType.FOUNDATION, 0);
    }

    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F2: A♦, 2♦\n" +
            "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1:\n"
            + "C2:\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());

    for (int i = 10; i >= 0; i--) {
      game.move(PileType.CASCADE, 2, i, PileType.FOUNDATION, 1);
    }

    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1:\n"
            + "C2:\n"
            + "C3:\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());

    assertEquals(false, game.isGameOver());
    for (int i = 12; i >= 0; i--) {
      game.move(PileType.CASCADE, 3, i, PileType.FOUNDATION, 3);
    }

    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1:\n"
            + "C2:\n"
            + "C3:\n"
            + "C4:", game.getGameState());

    assertEquals(true, game.isGameOver());


    game.move(PileType.FOUNDATION, 3, 12, PileType.CASCADE, 0);
    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♥\n"
            + "C2:\n"
            + "C3:\n"
            + "C4:", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.FOUNDATION, 3, 11, PileType.CASCADE, 1);
    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "F3: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥\n"
            + "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♥\n"
            + "C2: Q♥\n"
            + "C3:\n"
            + "C4:", game.getGameState());
    assertEquals(false, game.isGameOver());

    game.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 4, 4), game.getGameState());
    assertEquals(false, game.isGameOver());
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
    for (int i = 0; i < cardsList.size(); i++) {

      if (i == 3) {
        index = 2;
      } else {
        index = 1;
      }

      List clublist = new ArrayList();
      List spadelist = new ArrayList();
      List diamondlist = new ArrayList();
      List heartList = new ArrayList();

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

      clublist.addAll(spadelist);
      clublist.addAll(diamondlist);
      clublist.addAll(heartList);
      deck.addAll(clublist);

    }

  }

  @Test
  public void testGameStartAfterFewMoves() {

    FreecellOperations game = FreecellModel.getBuilder().opens(4).build();
    List deck = game.getDeck();
    rearrangeDeck(deck);
    assertEquals(false, game.isGameOver());
    game.startGame(deck, false);
    assertEquals(false, game.isGameOver());
    assertEquals(getExpectedDeal(deck, 4, 4), game.getGameState());
    game.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    game.move(PileType.CASCADE, 2, 12, PileType.CASCADE, 0);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:A♠\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.CASCADE, 1, 11, PileType.OPEN, 2);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:A♣\n"
            + "O2:A♠\n"
            + "O3:2♠\n"
            + "O4:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.move(PileType.OPEN, 0, 0, PileType.OPEN, 3);
    assertEquals("F1:\n"
            + "F2:\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "O2:A♠\n"
            + "O3:2♠\n"
            + "O4:A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♦\n"
            + "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥", game.getGameState());
    assertEquals(false, game.isGameOver());
    game.startGame(deck, false);
    assertEquals(getExpectedDeal(deck, 4, 4), game.getGameState());

  }


}