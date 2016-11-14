package FF_13312i_Nefedov_IS;

import FF_13312i_Nefedov_IS.InputFilters.DividerFilter;
import FF_13312i_Nefedov_IS.InputFilters.MatrixElementFilter;
import FF_13312i_Nefedov_IS.InputFilters.OffsetFilter;
import FF_13312i_Nefedov_IS.controller.CheckboxController;
import FF_13312i_Nefedov_IS.controller.ContourSliderController;
import FF_13312i_Nefedov_IS.controller.Controller;
import FF_13312i_Nefedov_IS.controller.GammaSliderController;
import ru.nsu.cg.MainFrame;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

/**
 * A main app's window
 * Extends MainFrame class
 */
public class MainWindow extends MainFrame {

    private Controller controller;
    private Object[] matrixOptionsMessage;
    private JDialog contourDialog;
    private JDialog gammaDialog;

    public List<JTextField> matrixElements;
    public JTextField matrixDivider;
    public JTextField matrixOffset;
    public JCheckBox isAutoDivider;

    /**
     * Class constructor where you can specify the main controller
     * Creates a main window and adds a originalView with a scroll pane to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(1024, 768, "Task Filt - Valeriy P. Nefedov");

        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel matrixPanel = new JPanel(new GridLayout(5, 5));
        JPanel optionsPanel = new JPanel(new GridLayout(3, 3));

        DividerFilter dividerFilter = new DividerFilter();
        OffsetFilter offsetFilter = new OffsetFilter();
        MatrixElementFilter matrixElementFilter = new MatrixElementFilter();

        controller = cntrl;
        matrixElements = new ArrayList<>();
        matrixDivider = new JTextField();
        matrixOffset = new JTextField();
        isAutoDivider = new JCheckBox("Auto divider");

        isAutoDivider.addItemListener((new CheckboxController(controller)).createCheckboxListener());

        ((PlainDocument) matrixDivider.getDocument()).setDocumentFilter(dividerFilter);
        ((PlainDocument) matrixOffset.getDocument()).setDocumentFilter(offsetFilter);

        addSliderDialogs();

        for (int i = 0; i < 25; i++) {
            JTextField element = new JTextField();

            ((PlainDocument) element.getDocument()).setDocumentFilter(matrixElementFilter);

            matrixElements.add(element);
            matrixPanel.add(matrixElements.get(matrixElements.size() - 1));
        }

        optionsPanel.add(new JLabel("Divider"));
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(new JLabel("Offset"));
        optionsPanel.add(matrixDivider);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(matrixOffset);
        optionsPanel.add(isAutoDivider);

        matrixOptionsMessage = new Object[3];
        matrixOptionsMessage[0] = "Enter a conversion matrix:";
        matrixOptionsMessage[1] = matrixPanel;
        matrixOptionsMessage[2] = optionsPanel;

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
     * Original mode button's callback function
     */
    public void onOriginal() {
        int answer = JOptionPane.showConfirmDialog(this, matrixOptionsMessage, "Matrix Conversion", JOptionPane.OK_CANCEL_OPTION);

        controller.doMatrixConversion(answer);
    }

    /**
     * Filtered mode button's callback function
     */
    public void onFiltered() { controller.doBlurConversion(); }

    /**
     * Splitted mode button's callback function
     */
    public void onSplitted() { controller.doSharpenConversion(); }

    /**
     * About button's callback function
     */
    public void onAbout() {
        controller.doGammaCorrection();

        this.gammaDialog.setVisible(true);
    }

    /**
     * Exit button's callback function
     */
    public void onExit(){ controller.doContourSelection(); this.contourDialog.setVisible(true); /*System.exit(0);*/ }

    /**
     * Creates menu, toolbar, sets a hot keys and callbacks to it
     * @throws NoSuchMethodException - if the specified callback method doesn't exists
     */
    public void createMenu() throws NoSuchMethodException {
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("View", KeyEvent.VK_V);

            addSubMenu("View/Mode", KeyEvent.VK_M);
            addMenuItem("View/Mode/Original", "Show original image", KeyEvent.VK_O, "Green.gif", "onOriginal");
            addMenuItem("View/Mode/Filtered", "Show filtered image", KeyEvent.VK_F, "Blue.gif", "onFiltered");
            addMenuItem("View/Mode/Splitted", "Show splitted image", KeyEvent.VK_F, "Red.gif", "onSplitted");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About", "About", KeyEvent.VK_A, "About.gif", "onAbout");

            addToolBarButton("View/Mode/Original");
            addToolBarButton("View/Mode/Filtered");
            addToolBarButton("View/Mode/Splitted");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarButton("File/Exit");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
