import org.junit.Test;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import freecell.controller.FreecellController;
import freecell.controller.IFreecellController;
import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FreecellControllerTest {


  @Test
  public void checkForControllerObjectCreation() {

    try {
      IFreecellController controller = new FreecellController(null, new StringBuffer("abc\n"));
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing.
    }


    try {
      IFreecellController controller = new FreecellController(new StringReader("abc\n"),
              null);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing.
    }


    try {
      IFreecellController controller = new FreecellController(null, null);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing.
    }

    IFreecellController controller = new FreecellController(new StringReader("abc\n"),
            new StringBuffer("abc\n"));


  }


  @Test
  public void testForemptyReadable() {
    try {
      IFreecellController controller = new FreecellController(new StringReader(""),
              new StringBuffer("abc\n"));
    } catch (Exception e) {
      fail("Exception isn't expected");
    }
  }


  @Test
  public void testForemptyAppendable() {
    try {
      IFreecellController controller = new FreecellController(new StringReader("123\n"),
              new StringBuffer(""));
    } catch (Exception e) {
      fail("Exception isn't expected here");
    }
  }

  @Test
  public void testForInvalidDeck() {

    IFreecellController controller = new FreecellController(new StringReader(""),
            new StringBuffer(""));
    FreecellOperations model = FreecellModel.getBuilder().build();
    List<Card> deck = new ArrayList();

    try {
      controller.playGame(deck, model, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }


    try {
      controller.playGame(null, model, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

    try {
      controller.playGame(model.getDeck(), null, false);
      fail("Above line should have thrown exception");
    } catch (IllegalArgumentException e) {
      //Do Nothing
    }

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
  public void testStartGamewithNoShuffle() {

    StringBuffer out = new StringBuffer();

    IFreecellController controller = new FreecellController(new StringReader("q"),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nGame quit prematurely.", out.toString());
  }

  @Test
  public void testStartGamewithShuffle() {

    StringBuffer out = new StringBuffer();

    IFreecellController controller = new FreecellController(new StringReader("Q"),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, true);
    assertNotEquals(getExpectedDeal(model.getDeck(), 4, 1)
                    + "\nGame quit prematurely.",
            out.toString());
    String[] outlines = out.toString().split("\\n");
    assertEquals("Game quit prematurely.", outlines[9]);
  }


  @Test
  public void testForInputContainsNullString() {

    StringBuffer out = new StringBuffer();
    String input = "test " + null + " void" + " Q";

    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nGame quit prematurely.", out.toString());

  }

  @Test
  public void testForQuits() {
    StringBuffer out = new StringBuffer();
    String input = "C3 q 13 F2";

    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nGame quit prematurely.", out.toString());
    input = "C3 13 q F2 q";
    out = new StringBuffer();
    controller = new FreecellController(new StringReader(input),
            out);

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nGame quit prematurely.", out.toString());

    input = "C3 13 F2 Q";
    out = new StringBuffer();
    controller = new FreecellController(new StringReader(input),
            out);

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nF1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIgnoreInvalidCardIndex() {

    String input = "C3 hello 13 F2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nF1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testForIgnoreInvalidDestinationPile() {


    String input = "C3\n13 w W xty 23 F2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nF1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testForIgnoreInvalidCardIndexAndDestPile() {


    String input = "C3\n see 13\n w\n" + "" + " W xty 23 F2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nF1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIgnoreAllInvaalidInputs() {

    String input = "3C\nc3\nC3w\nC3q\nC3 see 13\n w\n" + "" + " W xty 23  2F\nf2\nF2w\nF2q\nF2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nF1:\n"
            + "F2: A♦\n"
            + "F3:\n"
            + "F4:\n"
            + "O1:\n"
            + "C1: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "C2: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n"
            + "C4: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testInputPattern() {

    String input = "2C 13 F2 C2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nGame quit prematurely.", out.toString());
  }

  @Test
  public void testForInvalidMoveSourcePileOutOfRange() {
    String input = "C5 13 F2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
            + "\nInvalid move. Try again. provided source pile number doesn't exist\n"
            + "Game quit prematurely.", out.toString());
  }


  @Test
  public void testForInvalidMoveCardIndexOutOfRange() {
    String input = "C4 18 F2 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
                    + "\nInvalid move. Try again. provided source card isn't the last"
                    + " card in the source"
                    + " pile\nGame quit prematurely.",
            out.toString());
  }

  @Test
  public void testForInvalidMoveDestinationPileOutofRange() {
    String input = "C4 13 F5 q";
    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
                    + "\nInvalid move. Try again. Destination Pile Number is invalid"
                    + "\nGame quit prematurely.",
            out.toString());

  }

  @Test
  public void testInvalidDestinationOpenPile() {
    StringBuilder out = new StringBuilder();
    String input = "C4 13 O6 q";
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);

    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
                    + "\nInvalid move. Try again. Destination pile number isn't valid"
                    + "\nGame quit prematurely.",
            out.toString());
  }

  @Test
  public void testInvalidDestinationCascadePile() {
    StringBuilder out = new StringBuilder();
    String input = "C4 13 C7 q";
    IFreecellController controller = new FreecellController(new StringReader(input),
            out);

    FreecellOperations model = FreecellModel.getBuilder().build();
    controller.playGame(model.getDeck(), model, false);
    assertEquals(getExpectedDeal(model.getDeck(), 4, 1)
                    + "\nInvalid move. Try again. Destination Pile Number is invalid"
                    + "\nGame quit prematurely.",
            out.toString());
  }

  @Test
  public void testForGameOver() {

    StringBuilder input = new StringBuilder(" ");
    for (int i = 1; i <= 4; i++) {
      for (int j = 13; j >= 1; j--) {
        input.append("C").append(i).append(" ").append(j).append(" ").append("F").append(i)
                .append(" ");
      }
    }

    StringBuffer out = new StringBuffer();
    IFreecellController controller = new FreecellController(new StringReader(input.toString()),
            out);
    FreecellOperations model = FreecellModel.getBuilder().build();

    controller.playGame(model.getDeck(), model, false);

    String[] output = out.toString().split("\\n");
    String actualOutput = new String();
    for (int i = 10; i >= 1; i--) {
      actualOutput += output[output.length - i] + "\n";
    }

    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
                    + "F2: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
                    + "F3: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
                    + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
                    + "O1:\n"
                    + "C1:\n"
                    + "C2:\n"
                    + "C3:\n"
                    + "C4:\nGame over.",
            actualOutput.substring(0, actualOutput.length() - 1));
  }


}