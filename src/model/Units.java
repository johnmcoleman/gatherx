package model;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public class Units implements Serializable, Cloneable {
  // length conversions english units
  final private static double MI_TO_FT = 5280.0;
  final private static double FT_TO_IN = 12.0;
  // length conversions metric units
  final private static double M_TO_FT = 3.28084;
  final private static double M_TO_MI = 0.000621371;
  // volume conversions english units
  final private static double MSCFD_TO_SCFD = 1000.0;
  final private static double MMSCFD_TO_SCFD = 1000000.0;
  final private static double MMSCFD_TO_MSCFD = 1000.0;
  // time conversions
  final private static double HR_TO_MIN = 60.0;
  final private static double DAY_TO_HR = 24.0;
  final private static double YR_TO_DAY = 365.0;

  // current units in usage
  // node
  private String nodeVolumeUnit;
  private String nodePressureUnit;
  // pipe
  private String lengthUnit;
  private String inletPressureUnit;
  private String outletPressureUnit;
  private String pressureDifferentialUnit;
  private String diameterUnit;
  private String averageTemperatureUnit;
  private String baseTemperatureUnit;
  private String basePressureUnit;


  @Override
  public Object clone() {
    try {
      return (Units) super.clone();
    } catch (CloneNotSupportedException e) {
      return new Units();
    }
  }

  /**
   * Converts display coordinates to backend model coordinates.
   * 
   * @param xPos the x coordinate position of the object on the user interface.
   * @param yPos the y coordinate position of the object on the user interface.
   * @param zoomRatio the zoom ratio currently in use on the user interface.
   * @param visualCenter the panning offset on the user interface.
   * @param panelDim the dimensions of the user interface.
   * @return a coordinate point for the backend model
   */
  public static Point toBackEndCoords(int xPos, int yPos, double zoomRatio, Point visualCenter,
      Dimension panelDim) {
    Point newCoords = new Point(0, 0);
    newCoords.x = (int) (xPos + (1.0 / (2.0 * zoomRatio)) * panelDim.width + visualCenter.x);
    newCoords.y = (int) (yPos + (1.0 / (2.0 * zoomRatio)) * panelDim.height + visualCenter.y);
    return newCoords;
  }

  /**
   * Converts backend model coordinates to display coordinates.
   * 
   * @param xPos the x coordinate position of the object in the backend model.
   * @param yPos the y coordinate position of the object in the backend model.
   * @param zoomRatio the zoom ratio currently in use on the user interface.
   * @param visualCenter the panning offset on the user interface.
   * @param panelDim the dimensions of the user interface.
   * @return a coordinate point for the user interface.
   */
  public static Point toFrontEndCoords(int xPos, int yPos, double zoomRatio, Point visualCenter,
      Dimension panelDim) {
    Point newCoords = new Point(0, 0);
    newCoords.x = (int) (xPos - (1.0 / (2.0 * zoomRatio)) * panelDim.width - visualCenter.x);
    newCoords.y = (int) (yPos - (1.0 / (2.0 * zoomRatio)) * panelDim.height - visualCenter.y);
    return newCoords;
  }

  /**
   * Default constructor.
   */
  public Units() {
    // node
    nodeVolumeUnit = "scfd";
    nodePressureUnit = "psia";
    // pipe
    lengthUnit = "mi";
    inletPressureUnit = "psia";
    outletPressureUnit = "psia";
    pressureDifferentialUnit = "psi";
    diameterUnit = "in";
    averageTemperatureUnit = "R";
    baseTemperatureUnit = "R";
    basePressureUnit = "psia";
  }

  /**
   * Converts a physical quantity from one unit to another.
   * 
   * @param quantity the numerical quantity to be converted.
   * @param unitIn the unit of the quantity.
   * @param unitOut the unit the quantity will be converted to.
   * @param unitType the physical parameter the unit measures.
   * @return the quantity in the out unit.
   */
  public static double convert(double quantity, String unitIn, String unitOut, String unitType) {
    double convertedValue = quantity;
    // hierarchy checks unit type (vol, temp, pres) and then the in and out units to make the
    // conversion
    if (unitIn.equals(unitOut)) {
      return convertedValue;
    }
    if (unitType.equals("volume")) {
      // MSCF_TO_SCF
      if (unitIn.equals("scfd") && unitOut.equals("mscfd")) {
        convertedValue = quantity / MSCFD_TO_SCFD;
      }
      if (unitIn.equals("mscfd") && unitOut.equals("scfd")) {
        convertedValue = quantity * MSCFD_TO_SCFD;
      }
      // MMSCF_TO_SCF
      if (unitIn.equals("scfd") && unitOut.equals("mmscfd")) {
        convertedValue = quantity / MMSCFD_TO_SCFD;
      }
      if (unitIn.equals("mmscfd") && unitOut.equals("scfd")) {
        convertedValue = quantity * MMSCFD_TO_SCFD;
      }
      // MMSCF_TO_MSCF
      if (unitIn.equals("mscfd") && unitOut.equals("mmscfd")) {
        convertedValue = quantity / MMSCFD_TO_MSCFD;
      }
      if (unitIn.equals("mmscfd") && unitOut.equals("mscfd")) {
        convertedValue = quantity * MMSCFD_TO_MSCFD;
      }
    } else if (unitType.equals("gravity")) {

    } else if (unitType.equals("pressure")) {

    } else if (unitType.equals("length")) {
      // MI_TO_FT
      if (unitIn.equals("ft") && unitOut.equals("mi")) {
        convertedValue = quantity / MI_TO_FT;
      }
      if (unitIn.equals("mi") && unitOut.equals("ft")) {
        convertedValue = quantity * MI_TO_FT;
      }
      // FT_TO_IN
      if (unitIn.equals("in") && unitOut.equals("ft")) {
        convertedValue = quantity / FT_TO_IN;
      }
      if (unitIn.equals("ft") && unitOut.equals("in")) {
        convertedValue = quantity * FT_TO_IN;
      }
      // M_TO_FT
      if (unitIn.equals("ft") && unitOut.equals("m")) {
        convertedValue = quantity / M_TO_FT;
      }
      if (unitIn.equals("m") && unitOut.equals("ft")) {
        convertedValue = quantity * M_TO_FT;
      }
      // M_TO_MI
      if (unitIn.equals("mi") && unitOut.equals("m")) {
        convertedValue = quantity / M_TO_MI;
      }
      if (unitIn.equals("m") && unitOut.equals("mi")) {
        convertedValue = quantity * M_TO_MI;
      }
    } else if (unitType.equals("temperature")) {
      // F_TO_R
      if (unitIn.equals("F") && unitOut.equals("R")) {
        convertedValue = quantity + 459.67;
      }
      if (unitIn.equals("R") && unitOut.equals("F")) {
        convertedValue = quantity - 459.67;
      }
      // C_TO_R
      if (unitIn.equals("C") && unitOut.equals("R")) {
        convertedValue = quantity * (9.0 / 5.0) + 491.67;
      }
      if (unitIn.equals("R") && unitOut.equals("C")) {
        convertedValue = (quantity - 491.67) * (5.0 / 9.0);
      }
      // K_TO_R
      if (unitIn.equals("K") && unitOut.equals("R")) {
        convertedValue = quantity * (9.0 / 5.0);
      }
      if (unitIn.equals("R") && unitOut.equals("K")) {
        convertedValue = quantity * (5.0 / 9.0);
      }
    } else if (unitType.equals("time")) {
      // HR_TO_MIN
      if (unitIn.equals("min") && unitOut.equals("hr")) {
        convertedValue = quantity / HR_TO_MIN;
      }
      if (unitIn.equals("hr") && unitOut.equals("min")) {
        convertedValue = quantity * HR_TO_MIN;
      }
      // DAY_TO_HR
      if (unitIn.equals("hr") && unitOut.equals("day")) {
        convertedValue = quantity / DAY_TO_HR;
      }
      if (unitIn.equals("day") && unitOut.equals("hr")) {
        convertedValue = quantity * DAY_TO_HR;
      }
      // YR_TO_DAY
      if (unitIn.equals("day") && unitOut.equals("yr")) {
        convertedValue = quantity / YR_TO_DAY;
      }
      if (unitIn.equals("yr") && unitOut.equals("day")) {
        convertedValue = quantity * YR_TO_DAY;
      }
    } else {
      // TODO exception handling
    }
    return convertedValue;
  }

  public String getNodeVolumeUnit() {
    return nodeVolumeUnit;
  }

  public void setNodeVolumeUnit(String inletVolumeUnit) {
    this.nodeVolumeUnit = inletVolumeUnit;
  }

  public String getNodePressureUnit() {
    return nodePressureUnit;
  }

  public void setNodePressureUnit(String nodePressureUnit) {
    this.nodePressureUnit = nodePressureUnit;
  }

  public String getLengthUnit() {
    return lengthUnit;
  }

  public void setLengthUnit(String lengthUnit) {
    this.lengthUnit = lengthUnit;
  }

  public String getInletPressureUnit() {
    return inletPressureUnit;
  }

  public void setInletPressureUnit(String inletPressureUnit) {
    this.inletPressureUnit = inletPressureUnit;
  }

  public String getOutletPressureUnit() {
    return outletPressureUnit;
  }

  public void setOutletPressureUnit(String outletPressureUnit) {
    this.outletPressureUnit = outletPressureUnit;
  }

  public String getDiameterUnit() {
    return diameterUnit;
  }

  public void setDiameterUnit(String diameterUnit) {
    this.diameterUnit = diameterUnit;
  }

  public String getAverageTemperatureUnit() {
    return averageTemperatureUnit;
  }

  public void setAverageTemperatureUnit(String averageTemperatureUnit) {
    this.averageTemperatureUnit = averageTemperatureUnit;
  }

  public String getBaseTemperatureUnit() {
    return baseTemperatureUnit;
  }

  public void setBaseTemperatureUnit(String baseTemperatureUnit) {
    this.baseTemperatureUnit = baseTemperatureUnit;
  }

  public String getBasePressureUnit() {
    return basePressureUnit;
  }

  public void setBasePressureUnit(String basePressureUnit) {
    this.basePressureUnit = basePressureUnit;
  }

  public static double getMiToFt() {
    return MI_TO_FT;
  }

  public static double getFtToIn() {
    return FT_TO_IN;
  }

  public static double getmToFt() {
    return M_TO_FT;
  }

  public static double getmToMi() {
    return M_TO_MI;
  }

  public static double getMscfdToScfd() {
    return MSCFD_TO_SCFD;
  }

  public static double getMmscfdToScfd() {
    return MMSCFD_TO_SCFD;
  }

  public static double getMmscfdToMscfd() {
    return MMSCFD_TO_MSCFD;
  }

  public static double getHrToMin() {
    return HR_TO_MIN;
  }

  public static double getDayToHr() {
    return DAY_TO_HR;
  }

  public static double getYrToDay() {
    return YR_TO_DAY;
  }
}
