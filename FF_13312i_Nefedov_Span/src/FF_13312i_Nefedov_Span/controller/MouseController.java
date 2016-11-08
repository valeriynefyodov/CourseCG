package FF_13312i_Nefedov_Span.controller;

import FF_13312i_Nefedov_Span.model.CustomPolygon;
import FF_13312i_Nefedov_Span.model.Fill;
import FF_13312i_Nefedov_Span.model.Oval;

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
                if (controller.mode.equals("polygon")) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        controller.fakePolygon.points.add((controller.marker = e.getPoint()));
                        controller.isDragging = true;

                        controller.view.repaint();

                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        controller.shapes.add(new CustomPolygon(controller.fakePolygon, controller.lineWidth));
                        controller.setDefaults(false);

                        controller.view.repaint();
                    }
                }

                if (controller.mode.equals("fill")) {
                    controller.shapes.add(new Fill(controller.view.buffImage, e.getPoint(), controller.fillType, controller.fillColor));

                    controller.view.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (controller.mode.equals("oval")) {
                    if (e.getButton() == MouseEvent.BUTTON1){
                        controller.isDragging = true;
                        controller.fakeOval.setCenter((controller.marker = e.getPoint()));

                        controller.view.repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (controller.mode.equals("oval")) {
                    if (e.getButton() == MouseEvent.BUTTON1){
                        controller.shapes.add(new Oval(controller.fakeOval.getCenter(), controller.fakeOval.getRadius(), controller.lineWidth));
                        controller.setDefaults(false);

                        controller.view.repaint();
                    }
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
                if (controller.mode.equals("polygon")) {
                    if (controller.isDragging) {
                        controller.fakePoint = e.getPoint();

                        controller.view.repaint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (controller.mode.equals("oval")) {
                    if (controller.isDragging) {
                        controller.fakePoint = e.getPoint();
                        controller.fakeOval.setRadius(e.getPoint());

                        controller.view.repaint();
                    }
                }
            }
        };

        return listener;
    }
}
