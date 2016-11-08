package FF_13312i_Nefedov_LE;

import FF_13312i_Nefedov_LE.controller.Controller;
import ru.nsu.cg.MainFrame;
import java.awt.event.KeyEvent;

/**
 * A main app's window
 * Extends MainFrame class
 */
public class MainWindow extends MainFrame {

    private Controller controller;

    /**
     * Class constructor where you can specify the main controller
     * Creates a main window and adds a view to it
     * @param cntrl - specified controller
     */
    public MainWindow(Controller cntrl){
        super(600, 400, "Simple line editor");

        try{
            createMenu();
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        controller = cntrl;
        add(controller.view);
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
    public void onSave() { controller.saveFile(getSaveFileName("le", "LE Files")); }

    /**
     * Open file button's callback function
     */
    public void onOpen(){ controller.openFile(getOpenFileName("le", "LE Files")); }

    /**
     * Delete line button's callback function
     */
    public void onDelete(){
        controller.deleteLine();
    }

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
            addMenuItem("Edit/Delete the last line", "Delete Line", KeyEvent.VK_D, "Delete.gif", "onDelete");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About", "About", KeyEvent.VK_A, "About.gif", "onAbout");

            addToolBarButton("File/New");
            addToolBarButton("File/Save");
            addToolBarButton("File/Open");
            addToolBarSeparator();
            addToolBarButton("Edit/Delete the last line");
            addToolBarSeparator();
            addToolBarButton("Help/About");
            addToolBarButton("File/Exit");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
