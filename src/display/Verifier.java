package display;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Verifier is used to verify and enforce the trial licenses, and may be used in the future for the
 * purpose of verifying paid licenses in general. It works by calculating the app time run after
 * each time the app is closed. When the app is started up, Verifier reads the cumulative millis
 * 
 * @param duration how long the current app has been running (in millis)
 * @param max the maximum amount that the app can run, cumulatively (in millis)
 * 
 * @author: Nick Chen
 */
public class Verifier extends javax.swing.JFrame {

  private long duration;
  private long max;
  private long current; // how long the app has run so far - logged in and read from an encoded file
  private long startTime;

  // directory variables
  private final String directory = System.getProperty("user.home");
  private final String fileName = "key.gtrd";
  private final String absolutePath = directory + File.separator + fileName;

  public Verifier(long max) {
    this.max = max;
    this.current = retrieveCurrent();
    this.duration = 0L;
    this.startTime = System.currentTimeMillis();
    // 30 days: 2.592*Math.pow(10,9)
  }

  /**
   * Determines whether or not the program is allowed to run.
   * 
   * @return boolean whether or not license/key is valid.
   */
  public boolean verify() {
    if (current + duration <= max) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the current time from a file if it exists. If doesn't exist, then exit the program.
   * 
   * Called when the program/class initializes.
   * 
   * @return long value of current time
   */
  public long retrieveCurrent() {
    // read current data from file:
    String startDate = "";
    File tempFile = new File(absolutePath);
    if (tempFile.exists()) {
      try (FileReader fileReader = new FileReader(absolutePath)) {
        int ch = fileReader.read();
        while (ch != -1) {
          startDate += (char) ch;
          ch = fileReader.read();
        }
        fileReader.close();
        return (long) (Long.valueOf(startDate));
      } catch (FileNotFoundException e) {
        // Exception handling
        // is not supposed to reach this lol
        return 0;
      } catch (IOException e) {
        // Exception handling
        return 0;
      }
    } else {
      return 0;
    }
  }


  /**
   * This can only be called after the duration is set - prevents coder error :)
   * 
   * If there is a current file, overwrites it, else creates a new file and logs the total time,
   * adds a shifted value (to throw em off)
   * 
   * 
   **/
  private void writeTime() {
    try (FileWriter fileWriter = new FileWriter(absolutePath, false)) {
      fileWriter.write(Long.toString(duration + current));
      fileWriter.close();
    } catch (FileNotFoundException e) {
      // Exception handling
    } catch (IOException e) {
      // Exception handling
    }
  }


  /**
   * Called when the program exits, calculates how long the program has been running, writes it to a
   * file.
   */
  public void setDuration(boolean end) {
    this.duration = System.currentTimeMillis() - this.startTime;
    if (end) {
      writeTime();
    }
  }


}
