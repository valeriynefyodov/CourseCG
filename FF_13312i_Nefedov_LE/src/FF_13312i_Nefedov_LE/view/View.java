package FF_13312i_Nefedov_LE.view;

import FF_13312i_Nefedov_LE.controller.Controller;
import FF_13312i_Nefedov_LE.controller.MouseController;
import javax.swing.*;
import java.awt.Graphics;

/**
 * Custom view class extended from JPanel class
 */
public class View extends JPanel {

    public Controller controller;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public View(Controller cntrl){
        controller = cntrl;
        MouseController msCntrl = new MouseController(controller);

        addMouseListener(msCntrl.createMouseListener());
        addMouseMotionListener(msCntrl.createMouseMotionListener());
    }

    /**
     * Overrides library's paintComponent function
     * Checks the conditions and draws required graphics
     * @param g - graphic's component
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (controller.marker != null)
            g.drawOval(controller.marker.x - 2, controller.marker.y - 2, 4, 4);

        if (controller.isDragging && controller.fakePoint != null)
            g.drawLine(controller.marker.x, controller.marker.y, controller.fakePoint.x, controller.fakePoint.y);

        for (int j = 0; j < controller.lines.size(); j++) {
            if (controller.lines.get(j).points.size() > 1) {
                for (int i = 0; i < controller.lines.get(j).points.size() - 1; i++) {
                    g.drawLine(controller.lines.get(j).points.get(i).x, controller.lines.get(j).points.get(i).y,
                            controller.lines.get(j).points.get(i + 1).x, controller.lines.get(j).points.get(i + 1).y);
                }
            }
        }
    }

    /**
     * Shows an error dialog
     * @param message - string containing a message to be shown
     */
    public void showErrorDialog (String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message dialog
     * @param message - string containing a message to be shown
     */
    public void showMessageDialog (String message) {
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
