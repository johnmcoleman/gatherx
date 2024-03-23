package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Mueller;
import model.Node;
import model.PanHandleA;
import model.PanHandleB;
import model.Pipe;
import model.Weymouth;

public class PressureEquationTesting {
  // Pipes to test TODO assign pipes end nodes to prevent test from failing
  private static Pipe defaultPipe;
  private static Pipe goodPipe;
  private static Pipe badPipe;

  // Pipe end nodes to test
  private static Node defaultInletNode;
  private static Node defaultOutletNode;
  private static Node goodInletNode;
  private static Node goodOutletNode;
  private static Node badInletNode;
  private static Node badOutletNode;
  private static Node goodPipeEndNode1;
  private static Node goodPipeEndNode2;


  // Pressure equations to test
  Weymouth weymouth;
  Mueller mueller;
  PanHandleA panHandleA;
  PanHandleB panHandleB;

  @BeforeEach
  void setup() {
    defaultPipe = new Pipe();
    goodPipe = new Pipe("Good Pipe", 5000000.0, 5.0, 6.0, 810, 800.0, 0.78, 14.73, 530, 520, 0.9,
        0.01, 1.0, 808.725751 - 800.0, "Neutral", 20);
    badPipe =
        new Pipe("Bad Pipe", 0, -2, 0.00000000001, Double.MAX_VALUE, -69, Double.POSITIVE_INFINITY,
            0, Double.NEGATIVE_INFINITY, 1, 420, 0.0000000001, 1.0, -28, "Neutral", 14);

    // Pipe end nodes to test
    defaultInletNode = new Node();
    defaultOutletNode = new Node();
    goodInletNode =
        new Node("Good Inlet Node", "Inlet", 5000000.0, 0, 808.725751, 808.725751, 1, 1, "Neutral");
    goodOutletNode =
        new Node("Good Outlet Node", "Outlet", 5000000.0, 0, 800.0, 800.0, 1, 1, "Neutral");
    badInletNode = new Node("Bad Inlet Node", "Inlet", -27.89, 0, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY, 1, 1, "Neutral");
    badOutletNode = new Node("Bad Outlet Node", "Inlet", -27.89, 0, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY, 1, 1, "Neutral");
    goodPipeEndNode1 =
        new Node("Good Node End 1", "Outlet", 5000000.0, 0, 800.0, 800.0, 1, 1, "neutral");
    goodPipeEndNode2 =
        new Node("Good Node End 1", "Outlet", 5000000.0, 0, 810.0, 810.0, 1, 1, "neutral");
    goodPipe.setEnd1(goodPipeEndNode1);
    goodPipe.setEnd2(goodPipeEndNode2);

    // Pressure equations to test
    weymouth = new Weymouth();
    mueller = new Mueller();
    panHandleA = new PanHandleA();
    panHandleB = new PanHandleB();
  }

  /**
   * Tests that getFlowRate yields the correct mathematical result for safe parameters.
   */
  @Test
  void testGetFlowRateSafeValues() {
    assertEquals(5354766.384, weymouth.getFlowRateMagnitude(goodPipe), 0.1);
    assertEquals(6970892.599, panHandleA.getFlowRateMagnitude(goodPipe), 0.1);
    assertEquals(7776682.989, panHandleB.getFlowRateMagnitude(goodPipe), 0.1);
    assertEquals(7485278.632, mueller.getFlowRateMagnitude(goodPipe), 0.1);
  }

  /**
   * Tests getPressureDifferentialCoefficient yields the correct mathematical result for safe
   * parameters.
   */
  @Test
  void testGetFlowRateCoefficient() {
    Pipe pipe1 = new Pipe("Pipe1", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    assertEquals(0.0000000009974018074, weymouth.getFlowRateCoefficient(pipe1), 1e-16);
    assertEquals(0.00000000477477963342344, panHandleA.getFlowRateCoefficient(pipe1), 1e-16);
    assertEquals(0.00000000075655552561659, panHandleB.getFlowRateCoefficient(pipe1), 1e-16);
    assertEquals(0.0000002603239812, mueller.getFlowRateCoefficient(pipe1), 1e-16);
  }

  /**
   * Tests getGetFlowExponent yields the correct mathematical result for safe parameters.
   */
  @Test
  void testGetFlowExponent() {
    assertEquals(2d, weymouth.getFlowExponent(), 1e-6);
    assertEquals(1.8545994065281899, panHandleA.getFlowExponent(), 1e-6);
    assertEquals(1.9607843137254901, panHandleB.getFlowExponent(), 1e-6);
    assertEquals(1.7391304347826089, mueller.getFlowExponent(), 1e-6);
  }
}
