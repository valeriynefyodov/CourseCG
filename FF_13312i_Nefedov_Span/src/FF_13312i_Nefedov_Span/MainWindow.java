package FF_13312i_Nefedov_Span;

import FF_13312i_Nefedov_Span.controller.Controller;
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
        super(1024, 768, "Polygons and ovals filler");

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
     * New document button's callback function
     */
    public void onNewFile(){
        controller.newFile();
    }

    /**
     * Save file button's callback function
     */
    public void onSave() { controller.saveFile(getSaveFileName("spn", "SPN Files")); }

    /**
     * Open file button's callback function
     */
    public void onOpen(){ controller.openFile(getOpenFileName("spn", "SPN Files")); }

    /**
     * Delete line button's callback function
     */
    public void onDelete(){
        controller.deleteShape();
    }

    /**
     * Polygon tool button's callback function
     */
    public void onPolygon() { controller.mode = "polygon"; }

    /**
     * Oval tool button's callback function
     */
    public void onOval() { controller.mode = "oval"; }

    /**
     * Fill tool button's callback function
     */
    public void onFill() { controller.mode = "fill"; }

    /**
     * 1px line width button's callback function
     */
    public void onOnePx() { controller.lineWidth = 1.0f; }

    /**
     * 2px line width button's callback function
     */
    public void onTwoPx() { controller.lineWidth = 2.0f; }

    /**
     * 3px line width button's callback function
     */
    public void onThreePx() { controller.lineWidth = 3.0f; }

    /**
     * Black fill color button's callback function
     */
    public void onDark() { controller.fillColor = 0;/*(int)0xFF000000;*/ }

    /**
     * White fill color button's callback function
     */
    public void onBright() { controller.fillColor = 1;/*(int)0xFFFFFFFF;*/ }

    /**
     * 4 link fill type button's callback function
     */
    public void onFourLink() { controller.fillType = 4; }

    /**
     * 8 link fill type button's callback function
     */
    public void onEightLink() { controller.fillType = 8; }

    /**
     * About button's callback function
     */
    public void onAbout() { controller.showAbout(); }

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
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/New", "New file", KeyEvent.VK_N, "New_document.gif", "onNewFile");
            addMenuItem("File/Save", "Save file", KeyEvent.VK_S, "Save.gif", "onSave");
            addMenuItem("File/Open", "Open file", KeyEvent.VK_O, "Load.gif", "onOpen");
            addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, "Exit.gif", "onExit");

            addSubMenu("Edit", KeyEvent.VK_E);
            addMenuItem("Edit/Undo", "Undo", KeyEvent.VK_D, "Undo.gif", "onDelete");

            addSubMenu("Tools", KeyEvent.VK_T);
            addMenuItem("Tools/Polygon", "Polygon", KeyEvent.VK_P, "Polygon.png", "onPolygon");
            addMenuItem("Tools/Circle", "Circle", KeyEvent.VK_C, "Circle.png", "onOval");

            addSubMenu("Tools/Fill", KeyEvent.VK_F);
            addMenuItem("Tools/Fill/Fill", "Fill", KeyEvent.VK_F, "Fill.png", "onFill");

            addSubMenu("Tools/Fill/Color", KeyEvent.VK_C);
            addMenuItem("Tools/Fill/Color/Black", "Dark fill color", KeyEvent.VK_B, "Blue.gif", "onDark");
            addMenuItem("Tools/Fill/Color/White", "Bright fill color", KeyEvent.VK_Y, "Yellow.gif", "onBright");

            addSubMenu("Tools/Fill/Type", KeyEvent.VK_T);
            addMenuItem("Tools/Fill/Type/4 link", "4 link fill", KeyEvent.VK_4, "4link.gif", "onFourLink");
            addMenuItem("Tools/Fill/Type/8 link", "8 link fill", KeyEvent.VK_8, "8link.gif", "onEightLink");

            addSubMenu("Tools/Line Width", KeyEvent.VK_W);
            addMenuItem("Tools/Line Width/1 px", "1 px", KeyEvent.VK_1, "1_px.png", "onOnePx");
            addMenuItem("Tools/Line Width/2 px", "2 px", KeyEvent.VK_2, "2_px.png", "onTwoPx");
            addMenuItem("Tools/Line Width/3 px", "3 px", KeyEvent.VK_3, "3_px.png", "onThreePx");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About", "About", KeyEvent.VK_A, "About.gif", "onAbout");

            addToolBarButton("File/New");
            addToolBarButton("File/Save");
            addToolBarButton("File/Open");
            addToolBarSeparator();
            addToolBarButton("Edit/Undo");
            addToolBarSeparator();
            addToolBarButton("Tools/Polygon");
            addToolBarButton("Tools/Circle");
            addToolBarButton("Tools/Fill/Fill");
            addToolBarSeparator();
            addToolBarButton("Tools/Line Width/1 px");
            addToolBarButton("Tools/Line Width/2 px");
            addToolBarButton("Tools/Line Width/3 px");
            addToolBarSeparator();
            addToolBarButton("Tools/Fill/Color/Black");
            addToolBarButton("Tools/Fill/Color/White");
            addToolBarSeparator();
            addToolBarButton("Tools/Fill/Type/4 link");
            addToolBarButton("Tools/Fill/Type/8 link");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarButton("File/Exit");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
