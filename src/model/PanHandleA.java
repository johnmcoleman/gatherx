package model;

import java.io.Serializable;

/**
 * The PanHandleA Class solves the Panhandle A equation for flow rate as well as contains resistance
 * network properties for individual pipe segments.
 * 
 * @author John Coleman
 */
public class PanHandleA implements PressureEquation, Serializable {
  private static final long serialVersionUID = -252407043556148868L;

  /**
   * Solves the Panhandle A equation for flow rate for a selected pipe segment and its end nodes.
   * 
   * @param pipe the pipe segment for which the inlet pressure will be solved.
   * @param inlet the inlet node of the pipe segment.
   * @param outlet the outlet node of the pipe segment.
   * @return the calculated flow rate of the inlet node
   */
  public double getFlowRateMagnitude(Pipe pipe) throws ArithmeticException {
    double outletPressure = pipe.getOutPressure();
    double inletPressure = pipe.getInPressure();
    double diameterD = pipe.getPipeDiameter();
    double lengthL = pipe.getPipeLength();
    double dpressureBasePB = pipe.getBasePressure();
    double dgravityS = pipe.getGravity();
    double dtAvg = pipe.getAvgTemp();
    double dzAvg = pipe.getZ();
    double dtB = pipe.getBaseTemp();
    double flowRate = (435.87 * Math.pow((dtB / dpressureBasePB), 1.0788)
        * Math.pow(Math.abs((Math.pow(inletPressure, 2.0) - Math.pow(outletPressure, 2.0)))
            / (Math.pow(dgravityS, 0.853) * lengthL * dtAvg * dzAvg), 0.5392)
        * Math.pow(diameterD, 2.6182));

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
        .pow((Math.pow((basePressurePb / baseTemperatureTb), 1.0788)
            * (1d / (435.87 * efficiencyE * Math.pow(diameterD, 2.6182)))), 1.8545994065281899)
        * (Math.pow(gravityS, 0.853) * lengthL * averageTemperatureTavg * averageZFactorZavg);
    return coefficient;
  }

  @Override
  public double getFlowExponent() {
    return 1.8545994065281899;
  }
}
