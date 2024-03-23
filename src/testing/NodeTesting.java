package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Node;

public class NodeTesting {
  private static Node defaultNode;
  private static Node controlNode;
  private static Node inlet1;
  private static Node inlet1Copy;
  private static Node inlet1CloseCopy;
  private static Node inlet1DoubleOnlyCopy;
  private static Node inlet1StringsOnlyCopy;

  @BeforeEach
  void init() {
    defaultNode = new Node();
    controlNode = new Node("Node 1", "Control Valve", 1.893, 45, 889.6, 905.6, 1, 18, "neutral");
    inlet1 = new Node("Inlet 1", "Inlet", 1.893, 45, 889.6, 905.6, 1, 18, "success");
    inlet1Copy = new Node("Inlet 1", "Inlet", 1.893, 45, 889.6, 905.6, 1, 18, "success");
    inlet1DoubleOnlyCopy =
        new Node("Different", "Changed", 1.893, 45, 889.6, 905.6, 1, 18, "success");
    inlet1StringsOnlyCopy =
        new Node("Inlet 1", "Inlet", 1.89345, 45.00001, 889.6232, 9109109.6, 1, 18, "success");
    inlet1CloseCopy =
        new Node("Inlet 1", "Inlet", 1.8931, 45.0001, 889.6001, 905.6001, 1, 18, "success");
  }

  @Test
  void testConstantEqualsTrue() {
    assertEquals(true, inlet1.constantsEquals(inlet1Copy, 1e-06));
  }

  @Test
  void testConstantEqualsFalse() {
    assertEquals(false, inlet1.constantsEquals(controlNode, 1e-06));
    assertEquals(false, inlet1.constantsEquals(inlet1DoubleOnlyCopy, 1e-06));
    assertEquals(false, inlet1.constantsEquals(inlet1StringsOnlyCopy, 1e-06));
  }

  /*
   * Tests that the threshold properly impacts if double values of the constants in the node objects
   * match.
   */
  @Test
  void testConstantsEqualThreshold() {
    assertEquals(false, inlet1CloseCopy.constantsEquals(inlet1, 1e-06));
    assertEquals(true, inlet1CloseCopy.constantsEquals(inlet1, 1e-01));
  }

  /*
   * Tests that setting all the constants of a node to another node makes their constants
   * equivalent.
   */
  @Test
  void setAllConstants() {
    defaultNode.setAllConstants(inlet1);
    assertEquals(true, defaultNode.constantsEquals(inlet1, 1e-06));
  }
}
