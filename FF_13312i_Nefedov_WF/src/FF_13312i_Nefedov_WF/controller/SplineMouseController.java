package FF_13312i_Nefedov_WF.controller;

import FF_13312i_Nefedov_WF.model.Spline;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Mouse controller class
 */
public class SplineMouseController {

    private Controller controller;
    private Point dragPoint;
    private boolean isDrag;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public SplineMouseController(Controller cntrl) { controller = cntrl; isDrag = false; }

    /**
     * Creates a new mouse listener, handles mouse events and returns the listener
     * @return a created mouse listener
     */
    public MouseListener createMouseListener(){

        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (controller.isNewSpline || controller.splines.size() == 0) {
                        controller.splines.add(new Spline());
                        controller.isNewSpline = false;
                    }

                    if (controller.deletePoint) {
                        Point p = controller.getNearestPoint(e.getPoint());
                        Spline s = controller.findSplineByPoint(p);
                        if (s != null) {
                            s.points.remove(p);

                            controller.splineView.repaint();
                            controller.wfView.repaint();

                            controller.deletePoint = false;
                        }

                        return;
                    }

                    controller.splines.get(controller.splines.size() - 1).points.add(e.getPoint());
                    controller.splineView.repaint();
                    controller.wfView.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    isDrag = true;
                    dragPoint = controller.getNearestPoint(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    dragPoint = null;
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
                if (isDrag && dragPoint != null) {
                    dragPoint.setLocation(e.getX(), e.getY());
                    controller.splineView.repaint();
                    controller.wfView.repaint();
                }
            }
        };

        return listener;
    }
}
