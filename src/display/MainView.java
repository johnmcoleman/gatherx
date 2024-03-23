package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import controller.GatherX;
import controller.Runner;
import model.FlowCalc;
import model.Graph;

/**
 * Creates the frame upon which all the panels are placed and determines where they are placed and
 * how they are reset when the user interface is refreshed.
 * 
 * @author John Coleman
 *
 */
public class MainView extends JFrame {

  public JPanel contentPane;

  // lists of categorized buttons
  private static ArrayList<JButton> insertBtns = new ArrayList<JButton>();
  private static ArrayList<JButton> unitsBtns = new ArrayList<JButton>();
  private static ArrayList<JButton> homeBtns = new ArrayList<JButton>();
  private static ArrayList<JButton> fileBtns = new ArrayList<JButton>();
  private static ArrayList<JButton> viewBtns = new ArrayList<JButton>();
  private static ArrayList<JMenuItem> fileMenuItems = new ArrayList<JMenuItem>();
  private static ArrayList<JMenuItem> editMenuItems = new ArrayList<JMenuItem>();
  private static ArrayList<JMenuItem> textSizeMenuItems = new ArrayList<JMenuItem>();

  private ArrayList<JSpinner> spinnerView = new ArrayList<JSpinner>();

  private JButton btnUndo;
  private JButton btnRedo;
  private DataPanel dataPanel;
  private IdPanel idPanel;
  public DrawingPanel graphPanel;
  public DrawingBackgroundPanel graphBackGroundPanel;
  private SystemPanel systemPanel;
  private CalculationPanel calculationPanel;
  private ConsolePanel consolePanel;
  private JPanel bottomPanel;
  private static JFileChooser fileChooser;
  private JMenuBar menuBar;
  private JTabbedPane modeSelectionPane;
  private JPanel filePanel;
  private JPanel homePanel;
  private JPanel insertPanel;
  private JPanel viewPanel;
  private JPanel westPanel;
  private JScrollPane idScrollPane;
  private JScrollPane systemScrollPane;
  private JScrollPane dataScrollPane;
  private JScrollPane consoleScrollPane;
  private GridBagConstraints calculationPanelConstraints;
  private GridBagConstraints idPanelConstraints;
  private Dimension dataPanelDimensions = new Dimension(350, 800);

  /**
   * Create the frame.
   */
  public MainView() {
    initGUI();
  }

  /**
   * Initializes the graphic user interface's primary frame with blank panels which are based on
   * default settings from the model package.
   */
  private void initGUI() {
    // set default parameters
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setResizable(true);
    this.setVisible(true);
    this.setTitle("GatherX");
    this.setLayout(new BorderLayout());

    // creates actionlisteners
    ActionListener buttonListen = new ButtonModeListener();
    ActionListener viewListen = new ViewTabListener();
    ActionListener fileListen = new FileTabListener();
    ActionListener homeListen = new HomeTabListener();

    // loads images
    this.setIconImage(
        new ImageIcon(Runner.class.getResource("/images/applicationLogo.png")).getImage());
    Icon displayNodePropertiesIcon =
        new ImageIcon(Runner.class.getResource("/images/displayNodeProperties.png"));
    Icon displayPipePropertiesIcon = new ImageIcon(Runner.class.getResource("/images/load.png"));
    Icon listNodePropertiesIcon =
        new ImageIcon(Runner.class.getResource("/images/listNodeProperties.png"));
    Icon listPipePropertiesIcon =
        new ImageIcon(Runner.class.getResource("/images/listPipeProperties.png"));
    Icon exitIcon = new ImageIcon(Runner.class.getResource("/images/exit.png"));
    Icon openIcon = new ImageIcon(Runner.class.getResource("/images/open.png"));
    Icon importCSVIcon = new ImageIcon(Runner.class.getResource("/images/importCSV.png"));
    Icon exportCSVIcon = new ImageIcon(Runner.class.getResource("/images/exportCSV.png"));
    Icon nextGraphIcon = new ImageIcon(Runner.class.getResource("/images/nextGraph.png"));
    Icon previousGraphIcon = new ImageIcon(Runner.class.getResource("/images/previousGraph.png"));
    Icon moveGraphIcon = new ImageIcon(Runner.class.getResource("/images/moveGraph.png"));
    Icon closeIcon = new ImageIcon(Runner.class.getResource("/images/close.png"));
    Icon deleteIcon = new ImageIcon(Runner.class.getResource("/images/delete.png"));
    Icon newIcon = new ImageIcon(Runner.class.getResource("/images/new.png"));
    Icon newCalculationIcon = new ImageIcon(Runner.class.getResource("/images/newCalculation.png"));
    Icon redoIcon = new ImageIcon(Runner.class.getResource("/images/redo.png"));
    Icon saveIcon = new ImageIcon(Runner.class.getResource("/images/save.png"));
    Icon selectIcon = new ImageIcon(Runner.class.getResource("/images/select.png"));
    Icon textSizeIcon = new ImageIcon(Runner.class.getResource("/images/textSize.png"));
    Icon undoIcon = new ImageIcon(Runner.class.getResource("/images/undo.png"));
    Icon inletIcon = new ImageIcon(Runner.class.getResource("/images/inletIcon.png"));
    Icon pipeIcon = new ImageIcon(Runner.class.getResource("/images/pipeIcon.png"));
    Icon outletIcon = new ImageIcon(Runner.class.getResource("/images/outletIcon.png"));
    Icon teeIcon = new ImageIcon(Runner.class.getResource("/images/teeIcon.png"));

    // creates container panels
    contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    setContentPane(contentPane);

    westPanel = new JPanel();
    westPanel.setLayout(new GridBagLayout());
    contentPane.add(westPanel, BorderLayout.WEST);

    bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());
    contentPane.add(bottomPanel, BorderLayout.SOUTH);

    // creates node and pipe display panel
    idPanel = new IdPanel();
    idScrollPane = new JScrollPane(idPanel);
    idScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    idScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    idPanelConstraints = new GridBagConstraints();
    idPanelConstraints.gridx = 0;
    idPanelConstraints.gridy = 0;
    idPanelConstraints.weighty = 1;
    idPanelConstraints.weightx = 1;
    idPanelConstraints.fill = GridBagConstraints.BOTH;
    westPanel.add(idScrollPane, idPanelConstraints);

    // creates top tabbed pane
    modeSelectionPane = new JTabbedPane(JTabbedPane.TOP);
    contentPane.add(modeSelectionPane, BorderLayout.NORTH);

    // creates file panel and options
    filePanel = new JPanel();
    filePanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    GridBagConstraints filePanelConstraint = new GridBagConstraints();
    modeSelectionPane.addTab("File", null, filePanel, null);
    filePanel.setLayout(new GridBagLayout());
    filePanelConstraint.fill = GridBagConstraints.NONE;
    filePanelConstraint.anchor = GridBagConstraints.WEST;
    filePanelConstraint.weightx = 0.0;
    filePanelConstraint.weighty = 0.5;
    filePanelConstraint.insets = new Insets(5, 5, 5, 5);

    filePanelConstraint.gridx = 0;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnNew = new JButton("New", newIcon);
    btnNew.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnNew.setHorizontalTextPosition(SwingConstants.CENTER);

    filePanel.add(btnNew, filePanelConstraint);
    btnNew.addActionListener(fileListen);
    fileBtns.add(btnNew);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnOpen = new JButton("Open", openIcon);
    btnOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnOpen.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnOpen, filePanelConstraint);
    btnOpen.addActionListener(fileListen);
    fileBtns.add(btnOpen);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnClose = new JButton("Close", closeIcon);
    btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnClose.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnClose, filePanelConstraint);
    btnClose.addActionListener(fileListen);
    fileBtns.add(btnClose);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnSave = new JButton("Save", saveIcon);
    btnSave.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnSave.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnSave, filePanelConstraint);
    btnSave.addActionListener(fileListen);
    fileBtns.add(btnSave);

    JPopupMenu saveBtnPopup = new JPopupMenu();
    JMenuItem saveMenuItem = new JMenuItem("Save ");
    JMenuItem saveAsMenuItem = new JMenuItem("Save As");
    JMenuItem saveAllMenuItem = new JMenuItem("Save All");
    saveMenuItem.addActionListener(fileListen);
    saveAsMenuItem.addActionListener(fileListen);
    saveAllMenuItem.addActionListener(fileListen);
    saveBtnPopup.add(saveMenuItem);
    saveBtnPopup.add(saveAsMenuItem);
    saveBtnPopup.add(saveAllMenuItem);
    btnSave.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        saveBtnPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnICSV = new JButton("Import CSV", importCSVIcon);
    btnICSV.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnICSV.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnICSV, filePanelConstraint);
    btnICSV.addActionListener(fileListen);
    fileBtns.add(btnICSV);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnECSV = new JButton("Export CSV", exportCSVIcon);
    btnECSV.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnECSV.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnECSV, filePanelConstraint);
    btnECSV.addActionListener(fileListen);
    fileBtns.add(btnECSV);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnPrevG =
        new JButton("<html><center>Previous<br />Time Frame</center></html>", previousGraphIcon);
    btnPrevG.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnPrevG.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnPrevG, filePanelConstraint);
    btnPrevG.addActionListener(fileListen);
    fileBtns.add(btnPrevG);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnNextG =
        new JButton("<html><center>Next<br />Time Frame</center></html>", nextGraphIcon);
    btnNextG.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnNextG.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnNextG, filePanelConstraint);
    btnNextG.addActionListener(fileListen);
    fileBtns.add(btnNextG);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JButton btnAddG =
        new JButton("<html><center>Move<br />Time Frame</center></html>", moveGraphIcon);
    btnAddG.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnAddG.setHorizontalTextPosition(SwingConstants.CENTER);
    filePanel.add(btnAddG, filePanelConstraint);
    btnAddG.addActionListener(fileListen);
    fileBtns.add(btnAddG);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.gridheight = 1;
    JSpinner spnMoveG = new JSpinner();
    JComponent editor = spnMoveG.getEditor();
    JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
    tf.setColumns(4);
    filePanel.add(spnMoveG, filePanelConstraint);
    spnMoveG.setBounds(0, 0, 2147483647, 0);
    spinnerView.add(spnMoveG);

    filePanelConstraint.gridx += 1;
    filePanelConstraint.gridy = 0;
    filePanelConstraint.weightx = 1.0;
    filePanelConstraint.gridheight = 1;
    JButton btnExit = new JButton("Exit", exitIcon);
    btnExit.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnExit.setHorizontalTextPosition(SwingConstants.CENTER);

    filePanel.add(btnExit, filePanelConstraint);
    btnExit.addActionListener(fileListen);
    fileBtns.add(btnExit);

    // creates home panel and options
    homePanel = new JPanel();
    homePanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    GridBagConstraints homePanelConstraint = new GridBagConstraints();
    modeSelectionPane.addTab("Home", null, homePanel, null);
    homePanel.setLayout(new GridBagLayout());
    homePanelConstraint.fill = GridBagConstraints.NONE;
    homePanelConstraint.anchor = GridBagConstraints.WEST;
    homePanelConstraint.weightx = 0.0;
    homePanelConstraint.weighty = 0.5;
    homePanelConstraint.insets = new Insets(5, 5, 5, 5);

    homePanelConstraint.gridx = 0;
    homePanelConstraint.gridy = 0;
    homePanelConstraint.gridheight = 1;
    JButton btnNewCalculation = new JButton("New Calculation", newCalculationIcon);
    btnNewCalculation.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnNewCalculation.setHorizontalTextPosition(SwingConstants.CENTER);
    homePanel.add(btnNewCalculation, homePanelConstraint);
    btnNewCalculation.addActionListener(homeListen);
    homeBtns.add(btnNewCalculation);

    JPopupMenu newCalculationBtnPopup = new JPopupMenu();
    JMenuItem pressureMenuItem = new JMenuItem("Pressure");
    JMenuItem efficiencyMenuItem = new JMenuItem("Efficiency");
    pressureMenuItem.addActionListener(homeListen);
    efficiencyMenuItem.addActionListener(homeListen);
    newCalculationBtnPopup.add(pressureMenuItem);
    newCalculationBtnPopup.add(efficiencyMenuItem);
    btnNewCalculation.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        newCalculationBtnPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });

    homePanelConstraint.gridx += 1;
    btnUndo = new JButton("Undo", undoIcon);
    btnUndo.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnUndo.setHorizontalTextPosition(SwingConstants.CENTER);
    homePanel.add(btnUndo, homePanelConstraint);
    btnUndo.addActionListener(homeListen);
    homeBtns.add(btnUndo);

    homePanelConstraint.gridx += 1;
    btnRedo = new JButton("Redo", redoIcon);
    btnRedo.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnRedo.setHorizontalTextPosition(SwingConstants.CENTER);
    homePanel.add(btnRedo, homePanelConstraint);
    btnRedo.addActionListener(homeListen);
    homeBtns.add(btnRedo);

    homePanelConstraint.gridx += 1;
    homePanelConstraint.weightx = 1.0;
    homePanelConstraint.gridheight = 1;
    JButton btnTextSize = new JButton("Text Size", textSizeIcon);
    btnTextSize.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnTextSize.setHorizontalTextPosition(SwingConstants.CENTER);
    homePanel.add(btnTextSize, homePanelConstraint);
    btnTextSize.addActionListener(homeListen);
    homeBtns.add(btnTextSize);

    JPopupMenu textSizeBtnPopup = new JPopupMenu();
    JMenuItem tweleveMenuItem = new JMenuItem("12");
    JMenuItem fourteenMenuItem = new JMenuItem("14");
    JMenuItem sixteenMenuItem = new JMenuItem("16");
    JMenuItem twentyMenuItem = new JMenuItem("20");
    JMenuItem twentyFourMenuItem = new JMenuItem("24");
    tweleveMenuItem.addActionListener(homeListen);
    fourteenMenuItem.addActionListener(homeListen);
    sixteenMenuItem.addActionListener(homeListen);
    twentyMenuItem.addActionListener(homeListen);
    twentyFourMenuItem.addActionListener(homeListen);
    textSizeBtnPopup.add(tweleveMenuItem);
    textSizeBtnPopup.add(fourteenMenuItem);
    textSizeBtnPopup.add(sixteenMenuItem);
    textSizeBtnPopup.add(twentyMenuItem);
    textSizeBtnPopup.add(twentyFourMenuItem);
    btnTextSize.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        textSizeBtnPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });


    // creates insert panel and options
    insertPanel = new JPanel();
    insertPanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    modeSelectionPane.addTab("Insert", null, insertPanel, null);
    insertPanel.setLayout(new GridBagLayout());
    GridBagConstraints insertPanelContraints = new GridBagConstraints();
    insertPanelContraints.weightx = 0;
    insertPanelContraints.weighty = 1.0;
    insertPanelContraints.insets = new Insets(5, 5, 5, 5);
    insertPanelContraints.fill = GridBagConstraints.NONE;
    insertPanelContraints.anchor = GridBagConstraints.WEST;

    insertPanelContraints.gridx = 0;
    insertPanelContraints.gridy = 0;
    JButton btnSelect = new JButton("Select", selectIcon);
    btnSelect.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnSelect.setHorizontalTextPosition(SwingConstants.CENTER);

    btnSelect.addActionListener(buttonListen);
    insertPanel.add(btnSelect, insertPanelContraints);
    insertBtns.add(btnSelect);

    insertPanelContraints.gridx += 1;
    JButton btnDelete = new JButton("Delete", deleteIcon);
    btnDelete.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnDelete.setHorizontalTextPosition(SwingConstants.CENTER);

    insertPanel.add(btnDelete, insertPanelContraints);
    btnDelete.addActionListener(buttonListen);
    insertBtns.add(btnDelete);

    insertPanelContraints.gridx += 1;
    JButton btnPipe = new JButton("Pipe", pipeIcon);
    btnPipe.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnPipe.setHorizontalTextPosition(SwingConstants.CENTER);
    insertPanel.add(btnPipe, insertPanelContraints);
    btnPipe.addActionListener(buttonListen);
    insertBtns.add(btnPipe);

    insertPanelContraints.gridx += 1;
    JButton btnInlet = new JButton("Inlet", inletIcon);
    btnInlet.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnInlet.setHorizontalTextPosition(SwingConstants.CENTER);
    insertPanel.add(btnInlet, insertPanelContraints);
    btnInlet.addActionListener(buttonListen);
    insertBtns.add(btnInlet);

    insertPanelContraints.gridx += 1;
    JButton btnOutlet = new JButton("Outlet", outletIcon);
    btnOutlet.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnOutlet.setHorizontalTextPosition(SwingConstants.CENTER);
    insertPanel.add(btnOutlet, insertPanelContraints);
    btnOutlet.addActionListener(buttonListen);
    insertBtns.add(btnOutlet);

    insertPanelContraints.gridx += 1;
    insertPanelContraints.weightx = 1;
    JButton btnTee = new JButton("Tee", teeIcon);
    btnTee.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnTee.setHorizontalTextPosition(SwingConstants.CENTER);
    insertPanel.add(btnTee, insertPanelContraints);
    btnTee.addActionListener(buttonListen);
    insertBtns.add(btnTee);

    // creates view panel and options
    viewPanel = new JPanel();
    viewPanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    modeSelectionPane.addTab("View", null, viewPanel, null);
    viewPanel.setLayout(new GridBagLayout());
    GridBagConstraints viewPanelContraints = new GridBagConstraints();
    viewPanelContraints.weightx = 0.0;
    viewPanelContraints.weighty = 1.0;
    viewPanelContraints.fill = GridBagConstraints.NONE;
    viewPanelContraints.anchor = GridBagConstraints.WEST;

    JMenuItem noneView = new JMenuItem("None");
    JMenuItem inletPressureView = new JMenuItem("Inlet Pressure");
    JMenuItem volumesView = new JMenuItem("Inlet Volumes");
    noneView.addActionListener(viewListen);
    inletPressureView.addActionListener(viewListen);
    volumesView.addActionListener(viewListen);

    JPopupMenu listNodePropertiesPopup = new JPopupMenu();
    listNodePropertiesPopup.add(noneView);
    listNodePropertiesPopup.add(inletPressureView);
    listNodePropertiesPopup.add(volumesView);

    JButton listNodePropertiesBtn = new JButton(
        "<html><center>List Node<br />Properties</center></html>", listNodePropertiesIcon);
    listNodePropertiesBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    listNodePropertiesBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    listNodePropertiesBtn.addActionListener(viewListen);
    viewBtns.add(listNodePropertiesBtn);
    listNodePropertiesBtn.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        listNodePropertiesPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });
    viewPanelContraints.gridx = 0;
    viewPanelContraints.gridy = 0;
    viewPanel.add(listNodePropertiesBtn, viewPanelContraints);

    JMenuItem noneViewDisp = new JMenuItem("None");
    JMenuItem inletPressureViewDisp = new JMenuItem("Inlet Pressure");
    JMenuItem inletVolumesViewDisp = new JMenuItem("Inlet Volumes");
    inletPressureViewDisp.addActionListener(viewListen);
    noneViewDisp.addActionListener(viewListen);
    inletVolumesViewDisp.addActionListener(viewListen);

    JPopupMenu displayNodePropertiesPopup = new JPopupMenu();
    displayNodePropertiesPopup.add(noneViewDisp);
    displayNodePropertiesPopup.add(inletPressureViewDisp);
    displayNodePropertiesPopup.add(inletVolumesViewDisp);

    JButton displayNodePropertiesBtn = new JButton(
        "<html><center>Display Node<br />Properties</center></html>", displayNodePropertiesIcon);
    displayNodePropertiesBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    displayNodePropertiesBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    displayNodePropertiesBtn.addActionListener(viewListen);
    viewBtns.add(displayNodePropertiesBtn);
    displayNodePropertiesBtn.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        displayNodePropertiesPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });
    viewPanelContraints.gridx = 1;
    viewPanelContraints.gridy = 0;
    viewPanel.add(displayNodePropertiesBtn, viewPanelContraints);

    JMenuItem noneViewPipe = new JMenuItem("None");
    JMenuItem volumesViewPipe = new JMenuItem("Volumes");
    JMenuItem efficiencyViewPipe = new JMenuItem("Efficiency");
    JMenuItem pressureGradViewPipe = new JMenuItem("Pressure Gradient");
    JMenuItem velocityViewPipe = new JMenuItem("Velocity");
    volumesViewPipe.addActionListener(viewListen);
    efficiencyViewPipe.addActionListener(viewListen);
    pressureGradViewPipe.addActionListener(viewListen);
    velocityViewPipe.addActionListener(viewListen);
    noneViewPipe.addActionListener(viewListen);

    JPopupMenu listPipePropertiesPopup = new JPopupMenu();
    listPipePropertiesPopup.add(noneViewPipe);
    listPipePropertiesPopup.add(volumesViewPipe);
    listPipePropertiesPopup.add(efficiencyViewPipe);
    listPipePropertiesPopup.add(pressureGradViewPipe);
    listPipePropertiesPopup.add(velocityViewPipe);

    JButton listPipePropertiesBtn = new JButton(
        "<html><center>List Pipe<br />Properties</center></html>", listPipePropertiesIcon);
    listPipePropertiesBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    listPipePropertiesBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    listPipePropertiesBtn.addActionListener(viewListen);
    viewBtns.add(listPipePropertiesBtn);
    listPipePropertiesBtn.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        listPipePropertiesPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });
    viewPanelContraints.weightx = 0.0;
    viewPanelContraints.gridx = 2;
    viewPanelContraints.gridy = 0;
    viewPanel.add(listPipePropertiesBtn, viewPanelContraints);

    JMenuItem noneViewPipeDisp = new JMenuItem("None");
    JMenuItem efficiencyPipeDisp = new JMenuItem("Efficiency");
    JMenuItem velocityViewPipeDisp = new JMenuItem("Velocity");
    JMenuItem pressureDifferentialViewPipe = new JMenuItem("Pressure Gradient");
    noneViewPipeDisp.addActionListener(viewListen);
    efficiencyPipeDisp.addActionListener(viewListen);
    velocityViewPipeDisp.addActionListener(viewListen);
    pressureDifferentialViewPipe.addActionListener(viewListen);

    JPopupMenu dispPipePropertiesPopup = new JPopupMenu();
    dispPipePropertiesPopup.add(noneViewPipeDisp);
    dispPipePropertiesPopup.add(efficiencyPipeDisp);
    dispPipePropertiesPopup.add(velocityViewPipeDisp);
    dispPipePropertiesPopup.add(pressureDifferentialViewPipe);

    JButton dispPipePropertiesBtn = new JButton(
        "<html><center>Display Pipe<br />Properties</center></html>", displayPipePropertiesIcon);
    dispPipePropertiesBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    dispPipePropertiesBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    dispPipePropertiesBtn.addActionListener(viewListen);
    viewBtns.add(dispPipePropertiesBtn);
    dispPipePropertiesBtn.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        dispPipePropertiesPopup.show(e.getComponent(), 0, e.getComponent().getHeight());
      }
    });
    viewPanelContraints.weightx = 1.0;
    viewPanelContraints.gridx = 3;
    viewPanelContraints.gridy = 0;
    viewPanel.add(dispPipePropertiesBtn, viewPanelContraints);

    // creates the console text output panel
    consolePanel = new ConsolePanel();
    consoleScrollPane = new JScrollPane(consolePanel);
    consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    consoleScrollPane.setPreferredSize(new Dimension(100, 150));
    consoleScrollPane.getVerticalScrollBar().setUnitIncrement(20);
    bottomPanel.add(consoleScrollPane, BorderLayout.NORTH);

    // creates default data panel
    dataPanel = new DataPanel();
    dataScrollPane = new JScrollPane(dataPanel);
    dataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    dataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    contentPane.add(dataScrollPane, BorderLayout.EAST);
    contentPane.remove(dataScrollPane);

    // creates bottom panel which allows for switching between graphs
    systemPanel = new SystemPanel();
    systemScrollPane = new JScrollPane(systemPanel);
    systemScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    systemScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    bottomPanel.add(systemScrollPane, BorderLayout.SOUTH);

    // creates bottom panel which allows for switching between graphs
    calculationPanel = new CalculationPanel();
    calculationPanelConstraints = new GridBagConstraints();
    calculationPanelConstraints.gridx = 1;
    calculationPanelConstraints.gridy = 0;
    calculationPanelConstraints.fill = GridBagConstraints.VERTICAL;
    westPanel.add(calculationPanel, calculationPanelConstraints);

    // creates file filter
    fileChooser = new JFileChooser();

    // monitors for changes between the options selction tabbed panes
    MainTabListener tabListen = new MainTabListener();
    modeSelectionPane.addChangeListener(tabListen);

    // sets button colors
    for (JButton btn : fileBtns) {
      btn.setOpaque(true);
      btn.setBorderPainted(false);
      btn.setForeground(UserInterfaceRegulator.WHITE1);
      btn.setBackground(UserInterfaceRegulator.DARKGREY1);
      btn.setContentAreaFilled(false);
    }
    for (JButton btn : homeBtns) {
      btn.setOpaque(true);
      btn.setBorderPainted(false);
      btn.setForeground(UserInterfaceRegulator.WHITE1);
      btn.setBackground(UserInterfaceRegulator.DARKGREY1);
      btn.setContentAreaFilled(false);
    }
    for (JButton btn : insertBtns) {
      btn.setOpaque(true);
      btn.setBorderPainted(false);
      btn.setForeground(UserInterfaceRegulator.WHITE1);
      btn.setBackground(UserInterfaceRegulator.DARKGREY1);
      btn.setContentAreaFilled(false);
    }
    for (JButton btn : viewBtns) {
      btn.setOpaque(true);
      btn.setBorderPainted(false);
      btn.setForeground(UserInterfaceRegulator.WHITE1);
      btn.setBackground(UserInterfaceRegulator.DARKGREY1);
      btn.setContentAreaFilled(false);
    }

    // monitors for changes in frame state and refreshes the interface accordingly
    this.addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent arg0) {
        GatherX.refreshInterface();
      }
    });

    // warn user before exiting on close
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent we) {
        int confirm = JOptionPane.showOptionDialog(GatherX.getUiRegulator().getGraphicInterface(),
            "Are you sure you want to close GatherX? Unsaved work will be lost.",
            "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
            null, null);
        if (confirm == JOptionPane.YES_OPTION) {
          // GatherX.verifier.setDuration(true);
          System.exit(0);
        }
      }
    });

    // creates panel that visually displays a graph
    graphBackGroundPanel = new DrawingBackgroundPanel();
    graphBackGroundPanel.setLayout(new GridBagLayout());
    contentPane.add(graphBackGroundPanel, BorderLayout.CENTER);
    GridBagConstraints stackConstraint = new GridBagConstraints();
    Insets insets = new Insets(5, 5, 5, 5);
    stackConstraint.insets = insets;
    stackConstraint.fill = GridBagConstraints.BOTH;
    stackConstraint.weightx = 1;
    stackConstraint.weighty = 1;
    stackConstraint.gridx = 0;
    stackConstraint.gridy = 0;

    graphPanel = new DrawingPanel();
    graphPanel.setBackground(UserInterfaceRegulator.DARKGREY2);
    graphBackGroundPanel.add(graphPanel, stackConstraint);
  }

  /**
   * Creates save as file selection window and returns the file path.
   * 
   * @return the file path selected by the user.
   */
  public static String createFileSaverWindow() {
    String filePath = "";
    MainView.fileChooser = new JFileChooser();
    fileChooser.showSaveDialog(null);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fileChooser.getSelectedFile() != null) {
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
      if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".gtrx")) {
        filePath = filePath + ".gtrx";
      }
    }
    return filePath;
  }

  /**
   * Creates save all file selection window and returns the file path.
   * 
   * @param bool an extra parameter used to distinguish between the save all selection window and
   *        the save as selection window.
   * @return the file path selected by the user.
   */
  public static String createFileSaverWindow(boolean bool) {
    String filePath = "";
    MainView.fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.showSaveDialog(null);
    filePath = fileChooser.getSelectedFile().getAbsolutePath();
    return filePath;
  }

  /**
   * Creates a file open window filtered to only gather x project files.
   * 
   * @return the file path selected by the user to open.
   */
  public static String createFileOpenerWindow() {
    String filePath = "";
    MainView.fileChooser = new JFileChooser();
    FileTypeFilter docFilter = new FileTypeFilter(".gtrx", "GatherX Project File");
    fileChooser.addChoosableFileFilter(docFilter);
    fileChooser.showOpenDialog(null);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fileChooser.getSelectedFile() != null) {
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
    }
    return filePath;
  }

  /**
   * Does the same as above but for CSVs
   * 
   * @return the file path selected by the user to open.
   * @author Nick
   */
  public static String createCSVOpenerWindow() {
    String filePath = "";
    MainView.fileChooser = new JFileChooser();
    FileTypeFilter docFilter = new FileTypeFilter(".csv", "CSV Files");
    fileChooser.addChoosableFileFilter(docFilter);
    fileChooser.showOpenDialog(null);
    // fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fileChooser.getSelectedFile() != null) {
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
    }
    return filePath;
  }

  /**
   * Creates export as CSV selection window and returns the file path.
   *
   * @return the file path selected by the user.
   */
  public static String createCSVExportWindow() {
    String filePath = "";
    MainView.fileChooser = new JFileChooser();
    fileChooser.showSaveDialog(null);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (fileChooser.getSelectedFile() != null) {
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
      if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
        filePath = filePath + ".csv";
      }
    }
    return filePath;
  }

  /**
   * Sets the right hand side individual node and pipe data panel type according to pipe/node and
   * node type as well as node/pipe id.
   */
  public void setDataPanel() {
    contentPane.remove(dataPanel);
    dataPanel.initDataPanel();
    contentPane.add(dataPanel, BorderLayout.EAST);
  }

  public void removeDataPanel() {
    contentPane.remove(dataPanel);
  }

  /**
   * Updates the right hand side individual node and pipe data panel based on information from the
   * controller.
   * 
   * @param panelInfo comma separated information about the selected node or pipe.
   */
  public void setDataPanel(String panelInfo) {
    contentPane.remove(dataScrollPane);
    String[] panelInfoSplit = panelInfo.split(",");
    if (panelInfoSplit.length == 2) {
      String objectType = panelInfoSplit[0];
      String nodeType = panelInfoSplit[1];
      if (objectType.equals("Node")) {
        dataPanel.setDataPanel(nodeType);
      } else if (objectType.equals("Pipe")) {
        dataPanel.setDataPanel();
      } else {
        // TODO exception handling
      }
    } else {
      // TODO exception handling
    }
    dataScrollPane = new JScrollPane(dataPanel);
    dataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    dataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    dataScrollPane.setPreferredSize(dataPanelDimensions);
    dataScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    contentPane.add(dataScrollPane, BorderLayout.EAST);
  }

  /**
   * Updates the farthest left hand side panel which displays all node and pipe names and their
   * data.
   * 
   * @param graph the current graph in the gathering system
   */
  public void setIdPanel(Graph graph) {
    // remove old idPanel
    westPanel.remove(idScrollPane);
    // create new idPanel
    idPanel = new IdPanel(graph);
    idPanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    idScrollPane = new JScrollPane(idPanel);
    idScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    idScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    idScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    westPanel.add(idScrollPane, idPanelConstraints);
    idPanel.revalidate();
    idPanel.repaint();
  }

  public static ArrayList<JButton> getGraphingButtons() {
    return insertBtns;
  }

  public static ArrayList<JButton> getAnalysisButtons() {
    return unitsBtns;
  }

  public static ArrayList<JButton> getCalculationButtons() {
    return MainView.homeBtns;
  }

  public static ArrayList<JMenuItem> getTextSizeMenuItems() {
    return textSizeMenuItems;
  }

  public static void setTextSizeMenuItems(ArrayList<JMenuItem> textSizeMenuItems) {
    MainView.textSizeMenuItems = textSizeMenuItems;
  }

  public DataPanel getDataPanel() {
    return dataPanel;
  }

  public DrawingPanel getGraphPnl() {
    return graphPanel;
  }

  public void setGraphPnl(DrawingPanel graphPnl) {
    this.graphPanel = graphPnl;
    graphPanel.revalidate();
    graphPanel.repaint();
  }

  public IdPanel getIdPanel() {
    return idPanel;
  }

  public void setIdPanel(IdPanel idPanel) {
    this.idPanel = idPanel;
  }

  public static ArrayList<JMenuItem> getFileMenuItems() {
    return fileMenuItems;
  }

  public static void setFileMenuItems(ArrayList<JMenuItem> fileMenuItems) {
    MainView.fileMenuItems = fileMenuItems;
  }

  public static ArrayList<JMenuItem> getEditMenuItems() {
    return editMenuItems;
  }

  public static void setEditMenuItems(ArrayList<JMenuItem> editMenuItems) {
    MainView.editMenuItems = editMenuItems;
  }

  public static JFileChooser getFileChooser() {
    return fileChooser;
  }

  public JTabbedPane getModeSelectionPane() {
    return modeSelectionPane;
  }

  public void setModeSelectionPane(JTabbedPane modeSelectionPane) {
    this.modeSelectionPane = modeSelectionPane;
  }

  public JMenuBar getTheMenuBar() {
    return menuBar;
  }

  public void setTheMenuBar(JMenuBar menuBar) {
    this.menuBar = menuBar;
  }

  public JPanel getHomePanel() {
    return homePanel;
  }

  public void setHomePanel(JPanel homePanel) {
    this.homePanel = homePanel;
  }

  public JPanel getInsertPanel() {
    return insertPanel;
  }

  public void setInsertPanel(JPanel insertPanel) {
    this.insertPanel = insertPanel;
  }

  public JPanel getViewPanel() {
    return viewPanel;
  }

  public void setViewPanel(JPanel viewPanel) {
    this.viewPanel = viewPanel;
  }

  public SystemPanel getSystemPanel() {
    return systemPanel;
  }

  public void setSystemPanel(SystemPanel systemPanel) {
    this.systemPanel = systemPanel;
  }

  public ArrayList<JButton> getViewBtns() {
    return viewBtns;
  }

  public void setViewBtns(ArrayList<JButton> viewBtns) {
    this.viewBtns = viewBtns;
  }

  public static ArrayList<JButton> getFileBtns() {
    return fileBtns;
  }

  public static void setFileBtns(ArrayList<JButton> fileBtns) {
    MainView.fileBtns = fileBtns;
  }

  public JPanel getFilePanel() {
    return filePanel;
  }

  public void setFilePanel(JPanel filePanel) {
    this.filePanel = filePanel;
  }

  public ArrayList<JButton> getHomeBtns() {
    return this.homeBtns;
  }

  public void setHomeBtns(ArrayList<JButton> homeBtns) {
    this.homeBtns = homeBtns;
  }

  public JButton getBtnUndo() {
    return btnUndo;
  }

  public void setBtnUndo(JButton btnUndo) {
    this.btnUndo = btnUndo;
  }

  public JButton getBtnRedo() {
    return btnRedo;
  }

  public void setBtnRedo(JButton btnRedo) {
    this.btnRedo = btnRedo;
  }

  public DrawingBackgroundPanel getGraphBackGroundPanel() {
    return graphBackGroundPanel;
  }

  public void setGraphBackGroundPanel(DrawingBackgroundPanel graphBackGroundPanel) {
    this.graphBackGroundPanel = graphBackGroundPanel;
  }

  public String getSpnValue() {
    JSpinner newSpinner = this.spinnerView.get(0);
    String Spinvalue = newSpinner.getValue() + "";
    return (Spinvalue);
  }

  /**
   * Changes the color of the undo and redo buttons on the home panel based on if they are active or
   * not.
   * 
   * @param redosLeft
   * @param undosLeft
   */
  public void setChangeButtons(boolean redosLeft, boolean undosLeft) {
    if (redosLeft) {
      btnRedo.setForeground(UserInterfaceRegulator.LIGHTGREY1);
    } else {
      btnRedo.setForeground(UserInterfaceRegulator.WHITE1);
    }
    if (undosLeft) {
      btnUndo.setForeground(UserInterfaceRegulator.LIGHTGREY1);
    } else {
      btnUndo.setForeground(UserInterfaceRegulator.WHITE1);
    }
  }

  /**
   * Clears and then builds and adds the bottom panel which displays the names of all the open
   * project files.
   * 
   * @param systemNames
   */
  public void setSystemPanel(ArrayList<String> systemNames) {
    bottomPanel.remove(systemScrollPane);
    // create new system panel
    systemPanel = new SystemPanel(systemNames);
    systemPanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    systemScrollPane = new JScrollPane(systemPanel);
    systemScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    systemScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    bottomPanel.add(systemScrollPane, BorderLayout.SOUTH);
    systemScrollPane.revalidate();
    systemScrollPane.repaint();
  }

  public CalculationPanel getCalculationPanel() {
    return calculationPanel;
  }

  public void setCalculationPanel(FlowCalc newCalculation) {
    westPanel.remove(calculationPanel);
    calculationPanel = new CalculationPanel(newCalculation);
    calculationPanel.setBackground(UserInterfaceRegulator.DARKGREY1);
    westPanel.add(calculationPanel, calculationPanelConstraints);
    calculationPanel.revalidate();
    calculationPanel.repaint();
  }

  public ConsolePanel getConsolePanel() {
    return consolePanel;
  }

  public void setConsolePanel(ConsolePanel consolePanel) {
    this.consolePanel = consolePanel;
  }

}
