package FF_13312i_Nefedov_WF;
import FF_13312i_Nefedov_WF.controller.*;
import ru.nsu.cg.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A main app's window
 * Extends MainFrame class
 */
public class MainWindow extends MainFrame {

    private Controller controller;
    public List<JTextField> params;
    JPanel dialogPanel;

    /**
     * Class constructor where you can specify the main controller
     * Creates a main window and adds a originalView with a scroll pane to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(1280, 720, "Task WF - Valeriy P. Nefedov");
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        dialogPanel = new JPanel(new GridLayout(7, 2));
        controller = cntrl;

        this.params = new ArrayList<>();

        this.params.add(new JTextField(Integer.toString(controller.n)));
        this.params.add(new JTextField(Integer.toString(controller.m)));
        this.params.add(new JTextField(Integer.toString(controller.k)));
        this.params.add(new JTextField(Double.toString(controller.zn)));
        this.params.add(new JTextField(Double.toString(controller.zf)));
        this.params.add(new JTextField(Double.toString(controller.sw)));
        this.params.add(new JTextField(Double.toString(controller.sh)));

        dialogPanel.add(new JLabel("n:"));
        dialogPanel.add(this.params.get(0));
        dialogPanel.add(new JLabel("m:"));
        dialogPanel.add(this.params.get(1));
        dialogPanel.add(new JLabel("k:"));
        dialogPanel.add(this.params.get(2));
        dialogPanel.add(new JLabel("zn:"));
        dialogPanel.add(this.params.get(3));
        dialogPanel.add(new JLabel("zf:"));
        dialogPanel.add(this.params.get(4));
        dialogPanel.add(new JLabel("sw:"));
        dialogPanel.add(this.params.get(5));
        dialogPanel.add(new JLabel("sh:"));
        dialogPanel.add(this.params.get(6));

        try{
            createMenu();
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        mainPanel.add(controller.splineView);
        mainPanel.add(controller.wfView);
        add(mainPanel);
    }

    /**
     * Save button's callback function
     */
    public void onSave() { }

    /**
     * New Document button's callback function
     */
    public void onNewDocument() { controller.openFile(getOpenFileName(new String[] {"wf"}, "WF Files")); }

    /**
     * SRC -> DST button's callback function
     */
    public void onSRCToDST() { controller.isNewSpline = true; }

    /**
     * DST -> SRC button's callback function
     */
    public void onDSTtoSRC() { controller.deleteSpline(); }

    /**
     * Identical transformation button's callback function
     */
    public void onIdentical() { controller.deletePoint = true; }

    /**
     * Black & White filter button's callback function
     */
    public void onBW() {
        int answer = JOptionPane.showConfirmDialog(this, dialogPanel, "Parameters", JOptionPane.OK_CANCEL_OPTION);
        controller.setParams(answer);
    }

    /**
     * Negative filter button's callback function
     */
    public void onNegative() { }

    /**
     * Stamping filter button's callback function
     */
    public void onStamping() { }

    /**
     * Blur filter button's callback function
     */
    public void onBlur() { }

    /**
     * Sharpen filter button's callback function
     */
    public void onSharpen() { }

    /**
     * Aquarelization filter button's callback function
     */
    public void onAqua() { }

    /**
     * Contour selection button's callback function
     */
    public void onContour() { }

    /**
     * Gamma correction button's callback function
     */
    public void onGamma() { }

    /**
     * Custom Matrix filter button's callback function
     */
    public void onCustomMatrix() { }

    /**
     * About button's callback function
     */
    public void onAbout(){ /*controller.showAbout();*/ }

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
            addMenuItem("Tools/Filters/Watercolor", "Watercolor filter", KeyEvent.VK_A, "Aqua.png", "onAqua");
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
            addToolBarButton("Tools/Filters/Watercolor");
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
