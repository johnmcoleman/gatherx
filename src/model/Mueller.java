package model;

import java.io.Serializable;

/**
 * The Mueller Class solves the Mueller equation for flow rate as well as contains resistance
 * network properties for individual pipe segments.
 * 
 * @author John Coleman
 */
public class Mueller implements PressureEquation, Serializable {
  private static final long serialVersionUID = 6960658019772311093L;

  /**
   * Solves the Mueller equation for flow rate for a selected pipe segment and its end nodes.
   * 
   * @param pipe the pipe segment for which the inlet pressure will be solved.
   * @return the calculated flow rate of the inlet node
   */
  public double getFlowRateMagnitude(Pipe pipe) throws ArithmeticException {
    double outletPressure = pipe.getOutPressure();
    double inletPressure = pipe.getInPressure();
    double diameterD = pipe.getPipeDiameter();
    double lengthL = pipe.getPipeLength() * 5280d; // convert from miles to feet
    double dgravityS = pipe.getGravity();
    double flowRate = (((2826.0 * Math.pow(diameterD, 2.725)) / (Math.pow(dgravityS, 0.425))) * Math
        .pow((Math.abs((Math.pow(inletPressure, 2d) - Math.pow(outletPressure, 2d))) / lengthL),
            0.575))
        * (24d);
    if (Double.isInfinite(flowRate) || Double.isNaN(flowRate)) {
      throw new ArithmeticException("Invalid parameters");
    }
    return flowRate;
  }

  @Override
  public double getFlowRateCoefficient(Pipe pipe) {
    double lengthL = pipe.getPipeLength() * 5280d; // convert from miles to feet
    double gravityS = pipe.getGravity();
    double diameterD = pipe.getPipeDiameter();
    double efficiencyE = pipe.getEfficiency();
    double coefficient =
        Math.pow((Math.pow(gravityS, 0.425)) / (2826 * efficiencyE * Math.pow(diameterD, 2.725)),
            1.7391304347826089) * lengthL * (1d / 24d);
    return coefficient;
  }

  @Override
  public double getFlowExponent() {
    return 1.7391304347826089;
  }
}
