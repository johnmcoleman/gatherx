package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Node;
import model.Pipe;

public class PipeTesting {
  private static Pipe defaultPipe;
  private static Pipe pipe1;
  private static Pipe pipe2;
  private static Pipe pipe3;
  private static Pipe pipe1Copy;
  private static Pipe pipe1CloseCopy;
  private static Pipe pipe1CopyStringsOnly;
  private static Pipe pipe1CopyDoublesOnly;
  private static Node endNodeDefaultA;
  private static Node endNodeDefaultB;
  private static Node endNode1A;
  private static Node endNode1B;
  private static Node endNode2A;
  private static Node endNode2B;
  private static Node endNode3A;
  private static Node endNode3B;

  @BeforeEach
  void init() {
    defaultPipe = new Pipe();
    pipe1 = new Pipe("Pipe 1", 0, 10.34, Pipe.pipeSizes[4], 345.4, 453.98022, 0.2, 12, 345, 332,
        0.9, 0.37, 0.23, 108, "neutral", 23.4);
    pipe2 = new Pipe("Pipe 2", 14, 114.34, Pipe.pipeSizes[9], 555.4, 353.98022, 0.7, 12, 102, 11,
        0.3, 0.37, 0.7, 201.52088, "neutral", 10.2);
    pipe3 = new Pipe();
    pipe3.setFlowRate(1e6);
    pipe1CopyStringsOnly = new Pipe("Pipe 1", 0, 10.341, Pipe.pipeSizes[4], 345.4009, 453.92022,
        3.2, 2.12, 9000045, 1.1332, 0.91, 0.371, 1.23, 10811, "neutral", 234.00009);
    pipe1CopyDoublesOnly = new Pipe("Changed String", 0, 10.34, Pipe.pipeSizes[4], 345.4, 453.98022,
        0.2, 12, 345, 332, 0.9, 0.37, 0.23, 108, "Different String", 23.4);
    pipe1Copy = new Pipe("Pipe 1", 0, 10.34, Pipe.pipeSizes[4], 345.4, 453.98022, 0.2, 12, 345, 332,
        0.9, 0.37, 0.23, 108, "neutral", 23.4);
    pipe1CloseCopy = new Pipe("Pipe 1", 0.01, 10.35, Pipe.pipeSizes[4], 345.41, 453.99022, 0.2,
        12.01, 345.01, 332.01, 0.91, 0.38, 0.24, 108.01, "neutral", 23.41);
    endNodeDefaultA = new Node();
    endNodeDefaultA.setInletPressure(0);
    endNodeDefaultA.setOutletPressure(0);
    endNodeDefaultB = new Node();
    endNodeDefaultB.setInletPressure(0);
    endNodeDefaultB.setOutletPressure(0);
    endNode1A = new Node("End Node 1A", "Inlet", 100, 0, 345.4, 345.4, 0, 0, "neutral");
    endNode1B = new Node("End Node 1B", "Inlet", 100, 0, 453.98022, 453.98022, 0, 0, "neutral");
    endNode2A = new Node("End Node 2A", "Inlet", 100, 0, 353.98022, 353.98022, 0, 0, "neutral");
    endNode2B = new Node("End Node 2B", "Inlet", 100, 0, 555.4, 555.4, 0, 0, "neutral");
    endNode3A = new Node("End Node 2A", "Inlet", 100, 0, 300, 300, 0, 0, "neutral");
    endNode3B = new Node("End Node 2B", "Inlet", 100, 0, 300, 300, 0, 0, "neutral");
    pipe1.setEnd1(endNode1A);
    pipe1.setEnd2(endNode1B);
    pipe2.setEnd1(endNode2A);
    pipe2.setEnd2(endNode2B);
    pipe3.setEnd1(endNode3A);
    pipe3.setEnd2(endNode3B);
    defaultPipe.setEnd1(endNodeDefaultA);
    defaultPipe.setEnd2(endNodeDefaultB);
  }

  /**
   * Tests that calcVelocity throws an Exception for the default values TODO End nodes needed
   */
  @Test
  void testCalcVelocityDefaultException() {
    assertThrows(ArithmeticException.class, () -> defaultPipe.calcVelocity());
  }

  /**
   * Tests that calcVelocity calculates properly for pipe1
   */
  @Test
  void testCalcVelocityPipe1() {
    pipe1.calcVelocity();
    assertEquals(0, pipe1.getVelocity(), 1e-06);
  }

  /**
   * Tests that calcVelocity calculates properly for pipe3
   */
  @Test
  void testCalcVelocityPipe3() {
    pipe3.calcVelocity();
    assertEquals(6.538324100196521, pipe3.getVelocity(), 1e-06);
  }

  @Test
  void testConstantsEqualTrue() {
    assertEquals(true, pipe1Copy.constantsEquals(pipe1, 1e-06));
  }

  @Test
  void testConstantsEqualFalse() {
    assertEquals(false, pipe2.constantsEquals(pipe1, 1e-06));
    assertEquals(false, pipe1CopyDoublesOnly.constantsEquals(pipe1, 1e-06));
    assertEquals(false, pipe1CopyStringsOnly.constantsEquals(pipe1, 1e-06));
  }

  /*
   * Tests that the threshold properly impacts if double values of the constants in the pipe objects
   * match.
   */
  @Test
  void testConstantsEqualThreshold() {
    assertEquals(false, pipe1CloseCopy.constantsEquals(pipe1, 1e-06));
    assertEquals(true, pipe1CloseCopy.constantsEquals(pipe1, 1e-01));
  }

  /*
   * Tests that setting all the constants of a pipe to another pipe makes their constants
   * equivalent.
   */
  @Test
  void setAllConstants() {
    defaultPipe.setAllConstants(pipe1);
    assertEquals(true, defaultPipe.constantsEquals(pipe1, 1e-06));
  }
}
