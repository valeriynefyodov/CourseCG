package FF_13312i_Nefedov_WF.controller;

import FF_13312i_Nefedov_WF.model.Spline;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Валерий on 21.12.2016.
 */
public class WFMouseController {
    private Controller controller;
    private Point basePoint;
    private boolean isDrag;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public WFMouseController(Controller cntrl) { controller = cntrl; isDrag = false; }

    /**
     * Creates a new mouse listener, handles mouse events and returns the listener
     * @return a created mouse listener
     */
    public MouseListener createMouseListener(){

        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isDrag = true;
                    basePoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    basePoint = null;
                    isDrag = false;
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
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrag && basePoint != null) {
                    int dx = basePoint.x - e.getX();
                    int dy = basePoint.y - e.getY();

                    controller.thetaX -= dx / 1000.;
                    controller.thetaY -= dy / 1000.;

                    controller.splineView.repaint();
                    controller.wfView.repaint();
                }
            }
        };

        return listener;
    }
}
