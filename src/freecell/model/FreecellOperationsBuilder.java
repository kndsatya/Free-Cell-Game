package freecell.model;

/**
 * This is a builder interface that provides methods
 * such as cascades, opens and build which can be used to
 * build a free cell model with customized number of cascade piles
 * and custom number of open piles.
 */
public interface FreecellOperationsBuilder {
  /**
   * This method is used to configure the number of cascade piles
   * required for the game.
   * @param cascadePilesCount No.of cascade piles required for a new game.
   * @return FreecellOperationsBuilder object.
   */
  FreecellOperationsBuilder cascades(int cascadePilesCount);

  /**
   * This method is used to configure the number of open piles
   * required for the game.
   * @param openPilesCount No.of open piles required for a new game.
   * @return FreecellOperationsBuilder object.
   */
  FreecellOperationsBuilder opens(int openPilesCount);

  /**
   * This method builds the model for the free cell
   * and return it.
   * @param <K> K is the card type
   * @return FreecellOperations model.
   */
  <K> FreecellOperations<K> build();
}