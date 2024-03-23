package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.NonlinearTest;

public class DasOptimizationTesting {

  /*
   * Sets up the graph objects needed to test the functions
   */
  @BeforeEach
  void setup() {

  }

  /*
   * Uses the DasOptimization package's built in test functions to see if exceptions are thrown or
   * convergence is not reached by the solver.
   */
  @Test
  void testSolver() {
    NonlinearTest.extendedRosenbrockFunction(8, 1, false);
    NonlinearTest.helicalValleyFunction(8, 1, false);
    NonlinearTest.powellSingularFunction(12, 1, false);
    NonlinearTest.trigonometricFunction(8, 1, false);
  }
}
