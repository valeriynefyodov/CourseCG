package FF_13312i_Nefedov_IS;

import FF_13312i_Nefedov_IS.controller.Controller;
import ru.nsu.cg.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * A main app's window
 * Extends MainFrame class
 */
public class MainWindow extends MainFrame {

    private Controller controller;

    /**
     * Class constructor where you can specify the main controller
     * Creates a main window and adds a originalView with a scroll pane to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(1024, 768, "Task Filt - Valeriy P. Nefedov");

        JPanel mainPanel = new JPanel(new GridLayout(1,2));

        controller = cntrl;

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

    /**
     * Original mode button's callback function
     */
    public void onOriginal() { controller.copySRCToDST(); }

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
    public void onAbout() { controller.doStampingConversion(); }

    /**
     * Exit button's callback function
     */
    public void onExit(){ controller.doContourSelection(); /*System.exit(0);*/ }

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
