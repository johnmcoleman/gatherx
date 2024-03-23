package model;

import java.io.Serializable;

/**
 * The Weymouth Class solves the Weymouth equation for flow rate as well as contains resistance
 * network properties for individual pipe segments.
 * 
 * @author John Coleman
 */
public class Weymouth implements PressureEquation, Serializable {
  private static final long serialVersionUID = -3515476544680392941L;

  /**
   * Solves the Weymouth equation for the magnitude of the flow rate for a selected pipe segment and
   * its end nodes.
   * 
   * @param pipe the pipe segment for which the inlet pressure will be solved.
   * @return the calculated flow rate of the inlet node
   */
  public double getFlowRateMagnitude(Pipe pipe) throws ArithmeticException {
    double outletPressure = pipe.getOutPressure();
    double inletPressure = pipe.getInPressure();
    double diameterD = pipe.getPipeDiameter();
    double lengthL = pipe.getPipeLength();
    double pressureBasePB = pipe.getBasePressure();
    double gravityS = pipe.getGravity();
    double tAvg = pipe.getAvgTemp();
    double zAvg = pipe.getZ();
    double tB = pipe.getBaseTemp();
    double flowRate = (433.5 * (tB / pressureBasePB)
        * Math.pow((Math.abs((Math.pow(inletPressure, 2.0) - Math.pow(outletPressure, 2.0)))
            / (gravityS * lengthL * tAvg * zAvg)), 0.5)
        * Math.pow(diameterD, 2.667));
    if (Double.isInfinite(flowRate) || Double.isNaN(flowRate)) {
      throw new ArithmeticException("Invalid parameters");
    }
    return flowRate;
  }

  @Override
  public double getFlowRateCoefficient(Pipe pipe) {
    double lengthL = pipe.getPipeLength();
    double basePressurePb = pipe.getBasePressure();
    double gravityS = pipe.getGravity();
    double averageTemperatureTavg = pipe.getAvgTemp();
    double averageZFactorZavg = pipe.getZ();
    double baseTemperatureTb = pipe.getBaseTemp();
    double efficiencyE = pipe.getEfficiency();
    double diameterD = pipe.getPipeDiameter();
    double coefficient = Math
        .pow(((basePressurePb / baseTemperatureTb)
            * (1d / (433.5 * efficiencyE * Math.pow(diameterD, 2.667)))), 2d)
        * (gravityS * lengthL * averageTemperatureTavg * averageZFactorZavg);
    return coefficient;
  }

  @Override
  public double getFlowExponent() {
    return 2d;
  }
}
