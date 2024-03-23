package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import controller.Action;
import controller.GatherX;
import controller.Message;
import controller.MessageDispatch;

/**
 * Detects actions on the file tab and allows the user to perform the listed action when clicking a
 * button.
 * 
 * @author John Coleman
 *
 */
public class FileTabListener implements ActionListener {
  private static ArrayList<JButton> fileTabBtns = new ArrayList<JButton>();
  private String btnText = "";

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    fileTabBtns = GatherX.getUiRegulator().getGraphicInterface().getFileBtns();
    MessageDispatch dispatcher = GatherX.getDispatcher();
    // sets which one of the view options is selected
    for (JButton button : fileTabBtns) {
      if (e.getSource().equals(button)) {
        btnText = button.getText();
      }
    }
    if (!btnText.contentEquals("")) {
      switch (btnText) {
        case "New":
          Message newMessage = new Message(Action.NEW_FILE, "");
          dispatcher.dispatch(newMessage);
          break;
        case "Open":
          String filePath = MainView.createFileOpenerWindow();
          Message loadMessage = new Message(Action.OPEN_FILE, filePath);
          dispatcher.dispatch(loadMessage);
          break;
        case "Import CSV":
          // load a CSV
          String csvPath = MainView.createCSVOpenerWindow();
          if (csvPath.split("\\.")[csvPath.split("\\.").length - 1].toLowerCase().equals("csv")) {
            Message importMessage = new Message(Action.IMPORT_CSV, csvPath);
            dispatcher.dispatch(importMessage);
          } else {
            JOptionPane.showMessageDialog(GatherX.getUiRegulator().getGraphicInterface(),
                "Not a valid CSV file. Please select a file with extension .csv.");
          }
          break;
        case "Export CSV":
          // export a CSV
          csvPath = MainView.createCSVExportWindow();
          if (csvPath.split("\\.")[csvPath.split("\\.").length - 1].toLowerCase().equals("csv")) {
            Message importMessage = new Message(Action.EXPORT_CSV, csvPath);
            dispatcher.dispatch(importMessage);
          } else {
            JOptionPane.showMessageDialog(GatherX.getUiRegulator().getGraphicInterface(),
                "Not a valid CSV file. Please select a file with extension .csv.");
          }
          break;
        case "<html><center>Next<br />Time Frame</center></html>":
          Message graphMessage = new Message(Action.ADD_GRAPH, "");
          graphMessage = new Message(Action.NEXT_GRAPH, "");
          dispatcher.dispatch(graphMessage);
          break;
        case "<html><center>Previous<br />Time Frame</center></html>":
          graphMessage = new Message(Action.PREV_GRAPH, "");
          dispatcher.dispatch(graphMessage);
          break;
        case "<html><center>Move<br />Time Frame</center></html>":
          graphMessage = new Message(Action.MOVE_GRAPH,
              GatherX.getUiRegulator().getGraphicInterface().getSpnValue());
          dispatcher.dispatch(graphMessage);
          break;
        case "Close":
          loadMessage = new Message(Action.CLOSE_FILE, "");
          dispatcher.dispatch(loadMessage);
          break;
        case "Save":
          switch (command) {
            case "Save ":
              Message saveMessage = new Message(Action.SAVE_FILE, "");
              dispatcher.dispatch(saveMessage);
              break;
            case "Save As":
              filePath = MainView.createFileSaverWindow();
              saveMessage = new Message(Action.SAVE_FILE_AS, filePath);
              dispatcher.dispatch(saveMessage);
              break;
            case "Save All":
              filePath = MainView.createFileSaverWindow(true);
              saveMessage = new Message(Action.SAVE_ALL_FILES, filePath);
              dispatcher.dispatch(saveMessage);
              break;
            default:
          }
          break;
        case "Exit":
          int confirm = JOptionPane.showOptionDialog(GatherX.getUiRegulator().getGraphicInterface(),
              "Are you sure you want to close GatherX? Unsaved work will be lost.",
              "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
              null, null);
          if (confirm == JOptionPane.YES_OPTION) {
            // GatherX.verifier.setDuration(true);
            System.exit(0);
          }
          break;
        default:
      }
    }
  }
}


