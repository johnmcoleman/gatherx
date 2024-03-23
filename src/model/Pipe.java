package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Pipe class represents a physical pipeline connecting equipment as an edge connecting nodes
 * within a gas gathering system.
 * 
 * @author John Coleman
 */
public class Pipe implements Serializable {
  private static final long serialVersionUID = 7050702094438552063L;

  // Standard pipe sizes
  final static double twoInchPipe = 2.375 - (2 * 0.154);
  final static double threeInchPipe = 3.500 - (2 * 0.216);
  final static double fourInchPipe = 4.500 - (2 * 0.237);
  final static double sixInchPipe = 6.625 - (2 * 0.280);
  final static double eightInchPipe = 8.625 - (2 * 0.322);
  final static double tenInchPipe = 10.750 - (2 * 0.365);
  final static double twelveInchPipe = 12.750 - (2 * 0.375);
  final static double sixteenInchPipe = 16.000 - (2 * 0.375);
  final static double twentyInchPipe = 20.000 - (2 * 0.375);
  final static double twentyFourInchPipe = 24.000 - (2 * 0.375);
  final static double twentySixInchPipe = 26.000 - (2 * 0.375);
  final static double thirtyInchPipe = 30.000 - (2 * 0.375);
  public final static double[] pipeSizes = {twoInchPipe, threeInchPipe, fourInchPipe, sixInchPipe,
      eightInchPipe, tenInchPipe, twelveInchPipe, sixteenInchPipe, twentyInchPipe,
      twentyFourInchPipe, twentySixInchPipe, thirtyInchPipe};

  // pipe properties
  private String pipeName;
  private Node end1;
  private Node end2;
  private double length; // length of the pipe (mi)
  private double diameter; // diameter of the pipe (in)
  private double flowRate; // flow rate through the pipe (scfd)
  private double maxPressureGradient;

  // local ambient properties of the pipeline and gas inside it
  private double gravityS; // specific gravity of gas (relative to air)
  private double pressureBasePB; // base or atmospheric pressure (psia)
  private double tempAvg; // average temperature of pipe and gas (R)
  private double tempBase; // base temperature of pipe and gas (R)
  private double zAvg; // average compressibility factor of gas in the pipe (N/A)
  private double velocity; // calculated average velocity
  private double efficiency; // calculated empirical/theoretical

  private List<Double> numericalProperties; // for making shallow copies
  private List<String> stringProperties;

  // visual pipe properties for the user interface
  private Point location;
  private String state;

  /**
   * Constructs a default pipe object.
   */
  public Pipe() {
    pipeName = "default";
    flowRate = 0.0;
    length = 1.0;
    diameter = pipeSizes[2];
    gravityS = 0.7;
    pressureBasePB = 14.7;
    tempAvg = 530;
    tempBase = 520;
    zAvg = 1.0;
    location = new Point(0, 0);
    efficiency = 1.0;
    maxPressureGradient = 0;
    velocity = 1;
    state = "neutral";
    setArray();
  }

  /**
   * Constructor for opening saved pipeline data.
   * 
   * @param name pipe name.
   * @param flow pipe flow rate (scfd).
   * @param len pipe length (mi).
   * @param dia pipe diameter (in).
   * @param pInlet inlet pressure (psia).
   * @param pOutlet outlet pressure (psia).
   * @param gravity specific gravity (air).
   * @param pressureBase atmospheric pressure (psi).
   * @param averageTemp gas and pipe temperature (R).
   * @param baseTemp atmospheric temperature (R).
   * @param zValue compressibility factor (dimensionless).
   * @param friction pipe material roughness factor (dimensionless).
   * @param eff theoretical/actual flow rate (dimensionless).
   * @param maxGrad maximum allowable pressure gradient (psi/mi).
   * @param status notes if the pipe has had data changed from the default or has bad values.
   * @param vel the velocity of the gas in the pipe (ft/s).
   */
  public Pipe(String name, double flow, double len, double dia, double pInlet, double pOutlet,
      double gravity, double pressureBase, double averageTemp, double baseTemp, double zValue,
      double friction, double eff, double maxGrad, String status, double vel) {
    // pressure1 = pInlet;
    // pressure2 = pOutlet;
    diameter = dia;
    gravityS = gravity;
    length = len;
    zAvg = zValue;
    tempAvg = averageTemp;
    tempBase = baseTemp;
    pipeName = name;
    flowRate = flow;
    pressureBasePB = pressureBase;
    efficiency = eff;
    maxPressureGradient = maxGrad;
    velocity = vel;
    state = status;
    setArray();
  }

  public void setArray() {
    numericalProperties = new ArrayList<Double>();
    numericalProperties.add(diameter);
    numericalProperties.add(gravityS);
    numericalProperties.add(length);
    numericalProperties.add(zAvg);
    numericalProperties.add(tempAvg);
    numericalProperties.add(tempBase);
    numericalProperties.add(flowRate);
    numericalProperties.add(pressureBasePB);
    numericalProperties.add(efficiency);
    stringProperties = new ArrayList<String>();
    stringProperties.add(pipeName);
    stringProperties.add(state);
  }

  /**
   * Make a deep copy of another pipe WITHOUT its end nodes.
   * 
   * @param oldPipe the pipe which this pipe is taking the values of
   */
  public void setAllConstants(Pipe oldPipe) {
    this.setDiameter(oldPipe.getPipeDiameter());
    this.setGravity(oldPipe.getGravity());
    this.setPipeLength(oldPipe.getPipeLength());
    this.setZ(oldPipe.getZ());
    this.setAvgTemp(oldPipe.getAvgTemp());
    this.setBaseTemp(oldPipe.getBaseTemp());
    this.setFlowRate(oldPipe.getFlowRate());
    this.setBasePressure(oldPipe.getBasePressure());
    this.setPipeEff(oldPipe.getEfficiency());
    this.setPipeName(oldPipe.getPipeName());
    this.setState(oldPipe.getState());
    setArray();
  }

  /**
   * Checks if the string and double fields of the pipe object are equivalent within a specified
   * threshold of another pipe object
   * 
   * @param oldPipe the pipe which this pipe is being compared to
   * @param threshold the threshold within which double values are considered equivalent
   * @return wether or not the constants of the two pipes are equivalent
   */
  public boolean constantsEquals(Pipe oldPipe, double threshold) {
    boolean equal = true;
    oldPipe.setArray();
    this.setArray();
    List<Double> oldPipeNumProperties = oldPipe.getNumericalProperties();
    List<Double> pipeNumProperties = this.getNumericalProperties();
    for (int index = 0; index < oldPipeNumProperties.size(); index++) {
      double oldConstant = oldPipeNumProperties.get(index);
      double constant = pipeNumProperties.get(index);
      if (!(Math.abs(oldConstant - constant) < threshold)) {
        equal = false;
      }
    }
    List<String> oldPipeStrProperties = oldPipe.getStringProperties();
    List<String> pipeStrProperties = this.getStringProperties();
    for (int index = 0; index < oldPipeStrProperties.size(); index++) {
      String oldString = oldPipeStrProperties.get(index);
      String string = pipeStrProperties.get(index);
      if (!(string.contentEquals(oldString))) {
        equal = false;
      }
    }
    return equal;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getPipeName());
    sb.append(",");
    sb.append(this.getEnd1().getNodeName());
    sb.append(",");
    sb.append(this.getEnd2().getNodeName());
    sb.append(",");
    // Adds Pipe Values
    sb.append(this.getPipeLength());
    sb.append(",");
    sb.append(this.getPipeDiameter());
    sb.append(",");
    sb.append(this.getFlowRate());
    sb.append(",");
    sb.append(this.getInPressure());
    sb.append(",");
    sb.append(this.getOutPressure());
    sb.append(",");
    sb.append(this.getMaxPressureGradient());
    sb.append(",");
    sb.append(this.getGravity());
    sb.append(",");
    sb.append(this.getBasePressure());
    sb.append(",");
    sb.append(this.getAvgTemp());
    sb.append(",");
    sb.append(this.getBaseTemp());
    sb.append(",");
    sb.append(this.getZ());
    sb.append(",");
    sb.append(this.getEfficiency());
    sb.append(",");
    sb.append(this.getState());

    return sb.toString();
  }


  /**
   * Calculates the expected velocity of the pipe based on its parameters
   * 
   * @throws ArithmeticException if zero parameter values cause a divide by zero exception
   */
  public void calcVelocity() throws ArithmeticException {
    double pressureAvg = (getInPressure() + getOutPressure()) / 2;
    if (pressureAvg == 0 || tempBase == 0 || diameter == 0) {
      throw new ArithmeticException("Cannot divide by zero");
    }
    this.velocity = 0.002122 * (pressureBasePB / tempBase) * (zAvg * tempAvg / pressureAvg)
        * ((flowRate * efficiency) / (diameter * diameter));
  }

  public double getVelocity() throws ArithmeticException {
    this.calcVelocity();
    return velocity;
  }

  public double getLiquidDropout() {
    double k = 1.0; // Factor of how correlated efficiency and dropout are
    double liquidVolume =
        k * (1 - efficiency) * (Math.PI * Math.pow(diameter / 24, 2) * (5280 * length)); // ft^3
    // If efficiency is greater than 1 then there is no liquid present
    if (liquidVolume < 0) {
      liquidVolume = 0;
    }
    return liquidVolume;
    // Units: ft^3
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPipeName() {
    return pipeName;
  }

  public Node getEnd1() {
    return end1;
  }

  public Node getEnd2() {
    return end2;
  }

  public void setEnd1(Node end) {
    this.end1 = end;
  }

  public void setEnd2(Node end) {
    this.end2 = end;
  }

  public double getPipeFlowRate() {
    return flowRate;
  }

  public double getPipeLength() {
    return length;
  }

  public double getPipeDiameter() {
    return diameter;
  }

  public double getInPressure() {
    return end1.getInletPressure();
  }

  public double getOutPressure() {
    return end2.getInletPressure();
  }

  public double getEfficiency() {
    return efficiency;
  }

  public double getGravity() {
    return gravityS;
  }

  public double getAvgTemp() {
    return tempAvg;
  }

  public double getBaseTemp() {
    return tempBase;
  }

  public double getBasePressure() {
    return pressureBasePB;
  }

  public double getZ() {
    return zAvg;
  }

  public void addFlowRate(double volume) {
    this.flowRate += volume;
  }

  public void setPipeName(String name) {
    this.pipeName = name;
  }

  // this value is read only for the user
  public void setInPressure(double pressure) {
    // end1.setInletPressure(pressure);
  }

  // this value is read only for the user
  public void setOutPressure(double pressure) {
    // end2.setInletPressure(pressure);
  }

  public void setDiameter(double dia) {
    this.diameter = dia;
  }

  public void setPipeLength(double len) {
    this.length = len;
  }

  public void setGravity(double gravity) {
    this.gravityS = gravity;
  }

  public void setAvgTemp(double temp) {
    this.tempAvg = temp;
  }

  public void setBaseTemp(double temp) {
    this.tempBase = temp;
  }

  // not negative but can be zero
  public void setBasePressure(double pressure) {
    this.pressureBasePB = pressure;
  }

  public void setZ(double zAvg) {
    this.zAvg = zAvg;
  }

  public void setPipeEff(double eff) {
    this.efficiency = eff;
  }

  public void setFlowRate(double volumes) {
    this.flowRate = volumes;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public double getPressureDifferential() {
    return Math.abs(getInPressure() - getOutPressure());
  }

  public double getPressureGradient() {
    return this.getPressureDifferential() / length;
  }

  public void setMaxPressureGradient(double pressureGradient) {
    this.maxPressureGradient = pressureGradient;
  }

  public double getMaxPressureGradient() {
    return maxPressureGradient;
  }

  public void setEfficiency(double efficiency) {
    this.efficiency = efficiency;
  }

  public double getFlowRate() {
    return flowRate;
  }

  public List<Double> getNumericalProperties() {
    return numericalProperties;
  }

  public void setNumericalProperties(List<Double> numericalProperties) {
    this.numericalProperties = numericalProperties;
  }

  public List<String> getStringProperties() {
    return stringProperties;
  }

  public void setStringProperties(List<String> stringProperties) {
    this.stringProperties = stringProperties;
  }


}
