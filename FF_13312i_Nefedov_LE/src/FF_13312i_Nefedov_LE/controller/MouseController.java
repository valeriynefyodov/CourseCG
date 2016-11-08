package FF_13312i_Nefedov_LE.controller;

import FF_13312i_Nefedov_LE.model.Line;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Mouse controller class
 */
public class MouseController {

    private Controller controller;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public MouseController(Controller cntrl) { controller = cntrl; }

    /**
     * Creates a new mouse listener, handles mouse events and returns the listener
     * @return a created mouse listener
     */
    public MouseListener createMouseListener(){

        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (controller.lines.size() == 0 || controller.isNewLine) {
                        controller.lines.add(new Line());
                        controller.isNewLine = false;
                        controller.isDragging = true;
                    }

                    controller.lines.get(controller.lines.size() - 1).points.add((controller.marker = e.getPoint()));
                    controller.view.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    controller.isNewLine = true;
                    controller.isDragging = false;
                    controller.fakePoint = null;
                    controller.marker = null;
                    controller.view.repaint();
                }
            }
        };

        return listener;
    }

    /**
     * Creates a new mouse motion listener, handles mouse motion events and returns the listener
     * @return a created mouse motion listener
     */
    public MouseMotionListener createMouseMotionListener(){

        MouseMotionListener listener = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (controller.isDragging) {
                    controller.fakePoint = e.getPoint();
                    controller.view.repaint();
                }
            }
        };

        return listener;
    }
}
