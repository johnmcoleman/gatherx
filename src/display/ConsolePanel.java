package display;

import java.awt.ComponentOrientation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static ByteArrayOutputStream consoleText;
  private static JTextArea consoleTextArea;

  public ConsolePanel() {
    consoleText = new ByteArrayOutputStream();
    PrintStream console = new PrintStream(consoleText);
    System.setOut(console);
    System.setErr(console);

    consoleTextArea = new JTextArea(8, 300);
    consoleTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    consoleTextArea.setLineWrap(true);
    this.add(consoleTextArea);
  }

  public static void setConsoleText() {
    consoleTextArea.setText(consoleText.toString());
  }
}
