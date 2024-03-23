package model;

public interface PressureEquation {
  /**
   * Returns the magnitude of the expected flow rate due to the pressure differential across a pipe.
   * 
   * @param pipe
   * @return
   */
  double getFlowRateMagnitude(Pipe pipe);

  /**
   * Returns the resistance coefficient of a single pipe multiplied by the flow in a resistance
   * network model.
   * 
   * @param pipe
   * @return
   */
  double getFlowRateCoefficient(Pipe pipe);

  /**
   * Returns the exponent to which the flow is raised in a resistance network model.
   * 
   * @return
   */
  double getFlowExponent();
}
