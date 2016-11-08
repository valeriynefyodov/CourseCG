package FF_13312i_Nefedov_IS.controller;

import FF_13312i_Nefedov_IS.model.Function;

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
                if(controller.isDrawIsoline) {
                    if (0 < e.getX() && e.getX() < controller.view.buffImage.getWidth()
                            && 0 < e.getY() && e.getY() < controller.view.buffImage.getHeight()) {
                        double[] coord = controller.pixToCoord(e.getPoint().x, e.getPoint().y);
                        double value = Function.circle(coord[0], coord[1]);
                        controller.func_vals.add(value);
                    }
                }

                controller.view.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

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

                if (0 < e.getX() && e.getX() < controller.view.buffImage.getWidth()
                        && 0 < e.getY() && e.getY() < controller.view.buffImage.getHeight()) {
                    controller.curr_point = e.getPoint();
                }
                else
                    controller.curr_point = null;

                controller.view.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }
        };

        return listener;
    }
}
