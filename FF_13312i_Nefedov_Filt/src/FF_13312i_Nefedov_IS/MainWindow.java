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
     * Creates a main window and adds a view with a scroll pane to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(1024, 768, "Task IS - Valeriy P. Nefedov");

        controller = cntrl;

        try{
            createMenu();
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        JScrollPane scrollPane = new JScrollPane(controller.view);
        add(scrollPane);
    }

    /**
     * Show grid button's callback function
     */
    public void onGrid() {
        if (!controller.isShowGrid)
            controller.isShowGrid = true;
        else
            controller.isShowGrid = false;

        controller.view.repaint();
    }

    /**
     * Draw isolines button's callback function
     */
    public void onIsolines() {
        if (!controller.isDrawIsoline)
            controller.isDrawIsoline = true;
        else
            controller.isDrawIsoline = false;

        controller.view.repaint();
    }

    /**
     * Normal mode button's callback function
     */
    public void onNormal() { controller.mode = "norm"; controller.view.repaint(); }

    /**
     * Interpolar mode button's callback function
     */
    public void onInterpolar() { controller.mode = "interpol"; controller.view.repaint(); }

    /**
     * About button's callback function
     */
    public void onAbout() { /*controller.showAbout();*/ }

    /**
     * Exit button's callback function
     */
    public void onExit(){
        System.exit(0);
    }

    /**
     * Creates menu, toolbar, sets a hot keys and callbacks to it
     * @throws NoSuchMethodException - if the specified callback method doesn't exists
     */
    public void createMenu() throws NoSuchMethodException {
        try {
            addSubMenu("File", KeyEvent.VK_F);;
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("View", KeyEvent.VK_V);
            addMenuItem("View/Show Grid", "Show Grid", KeyEvent.VK_G, "8link.gif", "onGrid");
            addMenuItem("View/Show Isolines", "Show Isolines", KeyEvent.VK_I, "Circle.png", "onIsolines");

            addSubMenu("View/Mode", KeyEvent.VK_M);
            addMenuItem("View/Mode/Normal", "Normal mode", KeyEvent.VK_N, "Black.gif", "onNormal");
            addMenuItem("View/Mode/Interpolar", "Interpolar mode", KeyEvent.VK_I, "Black.gif", "onInterpolar");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About", "About", KeyEvent.VK_A, "About.gif", "onAbout");

            addToolBarButton("View/Show Grid");
            addToolBarButton("View/Show Isolines");
            addToolBarSeparator();
            addToolBarButton("View/Mode/Normal");
            addToolBarButton("View/Mode/Interpolar");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarButton("File/Exit");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
