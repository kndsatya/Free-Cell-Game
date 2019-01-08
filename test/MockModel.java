import java.util.List;

import freecell.model.FreecellOperations;
import freecell.model.PileType;

/**
 * Represents a mock model that seems to mimic a Free Cell game
 * where it isn't.
 */
public class MockModel implements FreecellOperations {

  private StringBuilder log;
  private final StringBuilder uniqueString;

  /**
   * Constructs a mock model provided with log and uniqustring as parameters.
   * @param log Represents the input log that logs all the input provided to the model by the
   *            controller.
   * @param uniqueString represents a unique string that will be returned by a model to the
   *                    controller.
   */
  public MockModel(StringBuilder log, StringBuilder uniqueString) {
    this.log = log;
    this.uniqueString = uniqueString;
  }

  @Override
  public List getDeck() {
    return null;
  }

  @Override
  public void startGame(List deck, boolean shuffle) throws IllegalArgumentException {
    for (int i = 0; i < deck.size(); i++) {
      log.append(deck.get(i).toString()).append(" ");
    }
    log.append(shuffle);

  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    //Do Nothing

  }

  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public String getGameState() {
    return uniqueString.toString();
  }
}
