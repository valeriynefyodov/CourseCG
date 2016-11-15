package FF_13312i_Nefedov_Filt;

import FF_13312i_Nefedov_Filt.InputFilters.DividerFilter;
import FF_13312i_Nefedov_Filt.InputFilters.MatrixElementFilter;
import FF_13312i_Nefedov_Filt.InputFilters.OffsetFilter;
import FF_13312i_Nefedov_Filt.controller.*;
import ru.nsu.cg.MainFrame;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

/**
 * A main app's window
 * Extends MainFrame class
 */
public class MainWindow extends MainFrame {

    private Controller controller;

    private JPanel mainPanel;
    private Object[] matrixOptionsMessage;
    private JDialog contourDialog;
    private JDialog gammaDialog;

    public List<JTextField> matrixElements;
    public JTextField matrixDivider;
    public JTextField matrixOffset;
    public JCheckBox isAutoDivider;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Class constructor where you can specify the main controller
     * Creates a main window and adds a originalView with a scroll pane to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(1024, 768, "Task Filt - Valeriy P. Nefedov");

        controller = cntrl;

        mainPanel = new JPanel(new GridLayout(1,2));
        JPanel matrixPanel = new JPanel(new GridLayout(5, 5));
        JPanel optionsPanel = new JPanel(new GridLayout(3, 3));

        DividerFilter dividerFilter = new DividerFilter();
        OffsetFilter offsetFilter = new OffsetFilter();
        MatrixElementFilter matrixElementFilter = new MatrixElementFilter();

        matrixElements = new ArrayList<>();
        matrixDivider = new JTextField();
        matrixOffset = new JTextField();
        isAutoDivider = new JCheckBox("Auto divider");

        if (matrixOffset.getText().length() == 0) {
            matrixOffset.setText("0");
            matrixOffset.setForeground(new Color(150, 150, 150));
        }

        if (matrixDivider.getText().length() == 0) {
            matrixDivider.setText("1");
            matrixDivider.setForeground(new Color(150, 150, 150));
        }

        ((PlainDocument) matrixDivider.getDocument()).setDocumentFilter(dividerFilter);
        ((PlainDocument) matrixOffset.getDocument()).setDocumentFilter(offsetFilter);

        matrixOffset.addFocusListener((new PlaceholderController()).createPlaceholderListener(matrixOffset, "0"));
        matrixDivider.addFocusListener((new PlaceholderController()).createPlaceholderListener(matrixDivider, "1"));
        isAutoDivider.addItemListener((new CheckboxController(controller)).createCheckboxListener());

        addSliderDialogs();

        for (int i = 0; i < 25; i++) {
            JTextField element = new JTextField();

            ((PlainDocument) element.getDocument()).setDocumentFilter(matrixElementFilter);
            element.setHorizontalAlignment(JTextField.CENTER);
            element.addFocusListener((new PlaceholderController()).createPlaceholderListener(element, "0"));

            if (element.getText().length() == 0) {
                element.setText("0");
                element.setForeground(new Color(150, 150, 150));
            }

            matrixElements.add(element);
            matrixPanel.add(matrixElements.get(matrixElements.size() - 1));
        }

        optionsPanel.add(new JLabel("Divider:"));
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(new JLabel("Offset:"));
        optionsPanel.add(matrixDivider);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(matrixOffset);
        optionsPanel.add(isAutoDivider);

        matrixOptionsMessage = new Object[5];
        matrixOptionsMessage[0] = "Enter a conversion matrix:";
        matrixOptionsMessage[1] = matrixPanel;
        matrixOptionsMessage[2] = optionsPanel;
        matrixOptionsMessage[3] = "Divider: 1 to 9999. 1 by default";
        matrixOptionsMessage[4] = "Offset: -255 to 255. 0 by default";

        try{
            createMenu();
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        mainPanel.add(controller.originalView);
        mainPanel.add(controller.filteredView);

        add(mainPanel);
    }

    private void addSliderDialogs() {
        JOptionPane contourOptionPane = new JOptionPane();
        JOptionPane gammaOptionPane = new JOptionPane();

        JSlider contourSlider = createContourLevelSlider(contourOptionPane);
        JSlider gammaSlider = createGammaLevelSlider(gammaOptionPane);

        contourOptionPane.setMessage(new Object[] {"Select a level: ", contourSlider, "Current level: " + controller.getContourLevel()});
        contourOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        contourOptionPane.setOptionType(JOptionPane.DEFAULT_OPTION);

        gammaOptionPane.setMessage(new Object[] {"Select a gamma: ", gammaSlider, "Current gamma: " + controller.getGamma()});
        gammaOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        gammaOptionPane.setOptionType(JOptionPane.DEFAULT_OPTION);

        JDialog contourDialog = contourOptionPane.createDialog(this, "Contour selection");
        JDialog gammaDialog = gammaOptionPane.createDialog(this, "Gamma correction");

        contourDialog.setSize(500, 200);
        contourDialog.setVisible(false);

        gammaDialog.setSize(700, 200);
        gammaDialog.setVisible(false);

        this.contourDialog = contourDialog;
        this.gammaDialog = gammaDialog;
    }

    private JSlider createContourLevelSlider(JOptionPane optionPane) {
        JSlider slider = new JSlider(0, 255, 0);

        slider.setMajorTickSpacing(51);
        slider.setMinorTickSpacing(17);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener((new ContourSliderController(controller)).createChangeListener(optionPane, slider));
        return slider;
    }

    private JSlider createGammaLevelSlider(JOptionPane optionPane) {
        JSlider slider = new JSlider(0, 100, 10);

        slider.setLabelTable(createGammaSliderLabels());

        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);

        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener((new GammaSliderController(controller)).createChangeListener(optionPane, slider));
        return slider;
    }

    private Hashtable createGammaSliderLabels() {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        labelTable.put(1, new JLabel("0.1"));
        labelTable.put(5, new JLabel("0.5"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        labelTable.put(30, new JLabel("3"));
        labelTable.put(40, new JLabel("4"));
        labelTable.put(50, new JLabel("5"));
        labelTable.put(60, new JLabel("6"));
        labelTable.put(70, new JLabel("7"));
        labelTable.put(80, new JLabel("8"));
        labelTable.put(90, new JLabel("9"));
        labelTable.put(100, new JLabel("10"));

        return labelTable;
    }

    /**
     * Save button's callback function
     */
    public void onSave() { controller.saveImage(); }

    /**
     * New Document button's callback function
     */
    public void onNewDocument() { controller.openImage(); }

    /**
     * SRC -> DST button's callback function
     */
    public void onSRCToDST() { controller.copySRCToDST(); }

    /**
     * DST -> SRC button's callback function
     */
    public void onDSTtoSRC() { controller.copyDSTToSRC(); }

    /**
     * Identical transformation button's callback function
     */
    public void onIdentical() {
        controller.doIdenticalConversion(controller.originalView.canvas, controller.filteredView.canvas);
        controller.filteredView.repaint();
    }

    /**
     * Black & White filter button's callback function
     */
    public void onBW() { controller.doBWConversion(); }

    /**
     * Negative filter button's callback function
     */
    public void onNegative() { controller.doNegativeConversion(); }

    /**
     * Stamping filter button's callback function
     */
    public void onStamping() { controller.doStampingConversion(); }

    /**
     * Blur filter button's callback function
     */
    public void onBlur() { controller.doBlurConversion(); }

    /**
     * Sharpen filter button's callback function
     */
    public void onSharpen() { controller.doSharpenConversion(); }

    /**
     * Aquarelization filter button's callback function
     */
    public void onAqua() { controller.doAquarelizationConversion(); }

    /**
     * Contour selection button's callback function
     */
    public void onContour() {
        controller.doContourSelection();
        this.contourDialog.setVisible(true);
    }

    /**
     * Gamma correction button's callback function
     */
    public void onGamma() {
        controller.doGammaCorrection();
        this.gammaDialog.setVisible(true);
    }

    /**
     * Custom Matrix filter button's callback function
     */
    public void onCustomMatrix() {
        int answer = JOptionPane.showConfirmDialog(this, matrixOptionsMessage, "Matrix Conversion", JOptionPane.OK_CANCEL_OPTION);
        controller.doMatrixConversion(answer);
    }

    /**
     * About button's callback function
     */
    public void onAbout(){  }

    /**
     * Exit button's callback function
     */
    public void onExit(){ System.exit(0); }

    /**
     * Creates menu, toolbar, sets a hot keys and callbacks to it
     * @throws NoSuchMethodException - if the specified callback method doesn't exists
     */
    private void createMenu() throws NoSuchMethodException {
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Open", "Open", KeyEvent.VK_O, "New_document.gif", "onNewDocument");
            addMenuItem("File/Save As", "Save", KeyEvent.VK_S, "Save.gif", "onSave");
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("Edit", KeyEvent.VK_E);
            addMenuItem("Edit/Copy SRC -> DST", "SRC -> DST", KeyEvent.VK_S + KeyEvent.VK_D, "SRC-DST.png", "onSRCToDST");
            addMenuItem("Edit/Copy DST -> SRC", "DST -> SRC", KeyEvent.VK_D + KeyEvent.VK_S, "DST-SRC.png", "onDSTtoSRC");

            addSubMenu("Tools", KeyEvent.VK_T);
            addMenuItem("Tools/Identical Transformation", "Identical Transformation", KeyEvent.VK_I, "Identical.png", "onIdentical");

            addSubMenu("Tools/Filters", KeyEvent.VK_F);
            addMenuItem("Tools/Filters/Black & White", "Black & White filter", KeyEvent.VK_B, "BW.png", "onBW");
            addMenuItem("Tools/Filters/Negative", "Negative filter", KeyEvent.VK_N, "Negative.png", "onNegative");
            addMenuItem("Tools/Filters/Stamping", "Stamping filter", KeyEvent.VK_S, "Stamp.png", "onStamping");
            addMenuItem("Tools/Filters/Blur", "Blur filter", KeyEvent.VK_R, "Blur.png", "onBlur");
            addMenuItem("Tools/Filters/Sharpen", "Sharpen filter", KeyEvent.VK_H, "Sharpen.png", "onSharpen");
            addMenuItem("Tools/Filters/Aquarelization", "Aquarelization filter", KeyEvent.VK_A, "Aqua.png", "onAqua");
            addMenuItem("Tools/Filters/Contour selection", "Contour Selection", KeyEvent.VK_C, "Contour.png", "onContour");
            addMenuItem("Tools/Filters/Gamma correction", "Gamma Correction", KeyEvent.VK_G, "Gamma.png", "onGamma");
            addMenuItem("Tools/Filters/Custom Matrix filter", "Custom Matrix filter", KeyEvent.VK_M, "Matrix.png", "onCustomMatrix");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About", "About", KeyEvent.VK_A, "About.gif", "onAbout");

            addToolBarButton("File/Open");
            addToolBarButton("File/Save As");

            addToolBarSeparator();

            addToolBarButton("Edit/Copy SRC -> DST");
            addToolBarButton("Edit/Copy DST -> SRC");

            addToolBarSeparator();

            addToolBarButton("Tools/Filters/Black & White");
            addToolBarButton("Tools/Filters/Negative");
            addToolBarButton("Tools/Filters/Stamping");
            addToolBarButton("Tools/Filters/Blur");
            addToolBarButton("Tools/Filters/Sharpen");
            addToolBarButton("Tools/Filters/Aquarelization");
            addToolBarButton("Tools/Filters/Contour selection");
            addToolBarButton("Tools/Filters/Gamma correction");
            addToolBarButton("Tools/Filters/Custom Matrix filter");

            addToolBarSeparator();

            addToolBarButton("Help/About");
            addToolBarButton("File/Exit");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Shows an error dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public static void showErrorDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(null, message, dialogName, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public static void showMessageDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(null, message, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a scrollable message dialog
     * @param message - a list of strings containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public static void showScrollableMessageDialog (DefaultListModel message, String dialogName) {
        JList textArea = new JList(message);
        JScrollPane scrollPane = new JScrollPane(textArea);

        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scrollPane, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }
}
