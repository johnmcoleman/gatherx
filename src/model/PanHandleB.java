package model;

import java.io.Serializable;


/**
 * The PanhandleB Class solves the Panhandle B equation for flow rate as well as contains resistance
 * network properties for individual pipe segments.
 * 
 * @author John Coleman
 */
public class PanHandleB implements PressureEquation, Serializable {
  private static final long serialVersionUID = 2568528383288113671L;

  /**
   * Solves the Panhandle B equation for flow rate for a selected pipe segment and its end nodes.
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
    double flowRate = (737.0 * Math.pow((dtB / dpressureBasePB), 1.02)
        * Math.pow(Math.abs((Math.pow(inletPressure, 2.0) - Math.pow(outletPressure, 2.0)))
            / (Math.pow(dgravityS, 0.961) * lengthL * dtAvg * dzAvg), 0.51)
        * Math.pow(diameterD, 2.53));

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
        .pow((Math.pow((basePressurePb / baseTemperatureTb), 1.02)
            * (1d / (737d * efficiencyE * Math.pow(diameterD, 2.53)))), 1.9607843137254901)
        * (Math.pow(gravityS, 0.961) * lengthL * averageTemperatureTavg * averageZFactorZavg);
    return coefficient;
  }

  @Override
  public double getFlowExponent() {
    return 1.9607843137254901;
  }
}
