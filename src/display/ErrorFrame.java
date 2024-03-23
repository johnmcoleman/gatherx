package display;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Displays uncaught exceptions to the user.
 * 
 * @author John Coleman.
 *
 */
public class ErrorFrame extends JFrame {

  private JPanel contentPane;

  public ErrorFrame(String message) {
    // set default parameters
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(400, 400);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    this.setContentPane(contentPane);
    JLabel crashApology = new JLabel(
        "The program crashed while trying to execute your last command. Please email info@gatherxanalytics.com for assistence and we'll get back to you within 24 hours.Thank you for your patience.");
    crashApology.setForeground(UserInterfaceRegulator.DARKGREY2);
    JLabel label = new JLabel(message);
    label.setForeground(UserInterfaceRegulator.DARKGREY2);
    contentPane.add(label);
  }
}
