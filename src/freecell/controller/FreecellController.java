package freecell.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import freecell.model.Card;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

/**
 * This class is the controller for the freecell game and implements the IFreecellController
 * interface. The class is used to perform then role of a controller i.e. get input from the view
 * and to pass it to the model. Also to get output from the model and pass it to the view. The class
 * uses Readable object for getting input and Appendable object for output.
 */
public class FreecellController implements IFreecellController<Card> {

  private final Readable in;
  private final Appendable out;

  /**
   * Constructs a FreecellController taking in Readable and Appendable objects as parameters for
   * input and output purposes respectively. Assigns the objects to its private Readable and
   * Appendable fields.
   *
   * @param rd Readable object for input
   * @param ap Appendable object for output
   * @throws IllegalArgumentException if any of them is null
   */
  public FreecellController(Readable rd, Appendable ap) throws IllegalArgumentException {

    if (checkForNull(rd)) {
      throw new IllegalArgumentException("Readable object can't be null");
    }

    if (checkForNull(ap)) {
      throw new IllegalArgumentException("Appendable object can't be null");
    }

    this.in = rd;
    this.out = ap;
  }

  /**
   * Start and play a new game of freecell with the provided deck. This deck should be used as-is.
   * This method returns only when the game is over (either by winning or by quitting)
   *
   * <p>User input for a move is a sequence of three inputs (separated by spaces or newlines).
   * a. The source pile (e.g., "C1", as a single word). The pile number begins at 1.</p>
   *
   * <p>b. The card index with the index beginning at 1.</p>
   *
   * <p>c. The destination pile (e.g., "F2", as a single word). The pile number is again
   * counted from 1</p>
   *
   * <p>If user enters an invalid card index, then enter the card index alone,
   * similarly for source and destination.</p>
   *
   * <p>Give 'q' or 'Q' to quit the game.</p>
   *
   * @param deck    the deck to be used to play this game
   * @param model   the model for the game
   * @param shuffle shuffle the deck if true, if false doesn't shuffle.
   * @throws IllegalArgumentException if the deck is null or invalid, or if the model is null
   * @throws IllegalStateException    if the controller is unable to read input or transmit output
   */
  public void playGame(List<Card> deck, FreecellOperations<Card> model,
                       boolean shuffle) throws IllegalArgumentException, IllegalStateException {

    String input = new String();
    PileType sourcePileType = PileType.CASCADE;
    PileType destinationPileType = PileType.CASCADE;
    int sourcePileNumber = 0;
    int sourceCardIndex = 0;
    int destinationPileNumber = 0;
    boolean isSourcePileRead = false;
    boolean isCardIndexRead = false;
    boolean isDestinationPileRead = false;

    if (checkForNull(model)) {
      throw new IllegalArgumentException("model can't be null");
    }

    model.startGame(deck, shuffle);

    writetoOut(model.getGameState());
    writetoOut("\n");

    Scanner scan = new Scanner(this.in).useDelimiter("\\s|\\n");

    while (true) {

      if (!scan.hasNext()) {
        continue;
      }

      input = scan.next();

      if (checkForNull(input)) {
        continue;
      }

      if (input.length() == 0) {
        continue;
      }

      if (input.equals("q") || input.equals("Q")) {

        writetoOut("Game quit prematurely.");
        return;

      }

      if (!isSourcePileRead) {
        if (!input.matches("^[OCF][0-9]+")) {
          continue;
        }
        sourcePileType = getPileType(input.charAt(0));
        sourcePileNumber = Integer.parseInt(input.substring(1));
        isSourcePileRead = true;
      }

      if (!isCardIndexRead) {

        if (!input.matches("[0-9]+")) {
          continue;
        }
        sourceCardIndex = Integer.parseInt(input);

        isCardIndexRead = true;

      }

      if (!isDestinationPileRead) {

        if (!input.matches("^[OCF][0-9]+")) {
          continue;
        }
        destinationPileType = getPileType(input.charAt(0));
        destinationPileNumber = Integer.parseInt(input.substring(1));
        isDestinationPileRead = true;
      }


      if (isSourcePileRead && isCardIndexRead && isDestinationPileRead) {

        isSourcePileRead = isCardIndexRead = isDestinationPileRead = false;
        callMove(sourcePileType, sourcePileNumber, sourceCardIndex, destinationPileType,
                destinationPileNumber, model);

      }

      if (checkForGameOver(model)) {
        return;
      }

    }

  }

  /**
   * A private helper method to return the pile type based on the argument representing those
   * piles.
   *
   * @param pileCharacter 'C' - cascade pile, 'O' - Open pile and 'F' - Foundation pile
   * @return the corresponding pile type
   */
  private PileType getPileType(char pileCharacter) {

    switch (pileCharacter) {
      case 'C':
        return PileType.CASCADE;
      case 'F':
        return PileType.FOUNDATION;
      default:
        return PileType.OPEN;
    }
  }

  /**
   * A private helper method to append the output to the Appendable object given the message as
   * argument.
   *
   * @param message string to be appended to the output.
   * @throws IllegalStateException if the controller cannot transmit the output properly
   */
  private void writetoOut(String message) throws IllegalStateException {

    try {
      this.out.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("controller is unable to transmit the output properly");
    }
  }

  /**
   * checks if an object is null.
   *
   * @param object any object in java.
   * @return true if the object is null else false.
   */
  private boolean checkForNull(Object object) {

    try {
      Objects.requireNonNull(object);
    } catch (NullPointerException e) {
      return true;
    }

    return false;

  }

  /**
   * Helper method to call move operation of model's move() method.
   *
   * @param sourcePileType        Type of the source pile.
   * @param sourcePileNumber      pile number of source card.
   * @param sourceCardIndex       index of the source card.
   * @param destinationPileType   type of the destination pile.
   * @param destinationPileNumber Pile number of the destination card.
   * @param model                 represents a Freecell game model
   */

  private void callMove(PileType sourcePileType, int sourcePileNumber, int sourceCardIndex,
                        PileType destinationPileType, int destinationPileNumber,
                        FreecellOperations model) {

    try {
      model.move(sourcePileType, sourcePileNumber - 1,
              sourceCardIndex - 1, destinationPileType, destinationPileNumber - 1);
      writetoOut(model.getGameState());
      writetoOut("\n");
    } catch (IllegalArgumentException e) {

      writetoOut("Invalid move. Try again." + " " + e.toString().substring(36) + "\n");
    }
  }

  /**
   * A helper method to check if the game is over. If game is over, it will transmit final game
   * state and Game Over string.
   *
   * @param model Represents a free cell model.
   * @return true if the game is over else false.
   */
  private boolean checkForGameOver(FreecellOperations model) {

    if (model.isGameOver()) {

      writetoOut(model.getGameState());
      writetoOut("\n");
      writetoOut("Game over.");
      return true;
    }

    return false;
  }

}
