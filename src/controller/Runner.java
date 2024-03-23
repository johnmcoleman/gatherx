package controller;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import display.ErrorFrame;

/**
 * Runs the application and sets the UI manger settings for an esthetically pleasing dark mode.
 * 
 * @author John Coleman
 *
 */
public class Runner {
  private static final ColorUIResource LIGHTGREY1 = new ColorUIResource(179, 179, 179);
  private static final ColorUIResource LIGHTGREY2 = new ColorUIResource(204, 204, 204);
  private static final ColorUIResource DARKGREY1 = new ColorUIResource(64, 64, 64);
  private static final ColorUIResource BLACK1 = new ColorUIResource(48, 48, 48);
  private static final ColorUIResource DARKGREY2 = new ColorUIResource(92, 92, 92);
  private static final ColorUIResource WHITE1 = new ColorUIResource(243, 243, 243);
  private static final ColorUIResource BLUE1 = new ColorUIResource(127, 167, 204);
  private static final ColorUIResource LIGHTPURPLE1 = new ColorUIResource(176, 178, 231);

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          // sets gui defaults
          UIManager.put("ComboBox.foreground", BLACK1);
          UIManager.put("ComboBox.background", LIGHTGREY2);
          UIManager.put("ComboBox.buttonBackground", BLACK1);
          UIManager.put("ComboBox.disabledBackground", LIGHTGREY2);
          UIManager.put("ComboBox.disabledForeground", DARKGREY2);
          UIManager.put("ComboBox.selectionBackground", BLACK1);
          UIManager.put("ComboBox.selectionForeground", LIGHTGREY2);
          UIManager.put("ComboBox.listRenderer.background", BLACK1);
          UIManager.put("ComboBox.border", false);

          UIManager.put("TextField.background", BLACK1);
          UIManager.put("TextField.foreground", LIGHTGREY2);
          UIManager.put("TextField.inactiveBackground", DARKGREY2);
          UIManager.put("TextField.inactiveForeground", WHITE1);
          UIManager.put("TextField.caretForeground", WHITE1);

          UIManager.put("TextArea.foreground", WHITE1);

          UIManager.put("PopupMenu.background", DARKGREY1);
          UIManager.put("PopupMenu.foreground", LIGHTGREY2);
          UIManager.put("PopupMenu.border", LIGHTGREY1);
          UIManager.put("PopupMenu.selectionBackground", DARKGREY1);
          UIManager.put("PopupMenu.selectionForeground", LIGHTGREY2);
          UIManager.put("Menu.foreground", WHITE1);

          UIManager.put("Button.background", DARKGREY1);
          UIManager.put("Button.foreground", LIGHTGREY2);

          UIManager.put("List.background", BLACK1);
          UIManager.put("Label.foreground", WHITE1);

          UIManager.put("Panel.background", DARKGREY1);
          UIManager.put("Panel.foreground", WHITE1);
          UIManager.put("OptionPane.foreground", WHITE1);
          UIManager.put("OptionPane.messageForeground", WHITE1);
          UIManager.put("TextArea.background", DARKGREY1);
          UIManager.put("TextPane.background", DARKGREY1);
          UIManager.put("ToolBar.background", DARKGREY1);
          // starts program
          GatherX game = new GatherX();

        } catch (Exception e) {
          e.printStackTrace();
          ErrorFrame errorDisplay = new ErrorFrame(e.getMessage());
        }
      }
    });
  }
}
