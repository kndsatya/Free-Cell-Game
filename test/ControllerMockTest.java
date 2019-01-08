import org.junit.Test;


import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;

import freecell.controller.FreecellController;
import freecell.controller.IFreecellController;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;

/**
 * Controller test using a Mock model.
 */
public class ControllerMockTest {

  @Test
  public void mockModeltest() {
    StringBuffer out = new StringBuffer();
    Readable in = new StringReader("q");
    IFreecellController controller = new FreecellController(in, out);
    StringBuilder log = new StringBuilder(); //log for mock model
    StringBuilder uniqueCode = new StringBuilder("uniqueString");
    FreecellOperations model = FreecellModel.getBuilder().build();
    FreecellOperations mockModel = new MockModel(log, uniqueCode);
    controller.playGame(model.getDeck(), mockModel, false);
    StringBuilder expectedLogString = new StringBuilder();
    List deck = model.getDeck();
    for (int i = 0; i < deck.size(); i++) {
      expectedLogString.append(deck.get(i).toString()).append(" ");
    }
    expectedLogString.append(false);

    assertEquals(expectedLogString.toString(), log.toString());//inputs reached the model correctly.
    assertEquals(uniqueCode.toString() + "\nGame quit prematurely.",
            out.toString()); //output of model transmitted correctly
  }
}
