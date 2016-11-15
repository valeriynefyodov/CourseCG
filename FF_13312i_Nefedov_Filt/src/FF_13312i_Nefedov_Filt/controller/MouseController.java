package FF_13312i_Nefedov_Filt.controller;

import FF_13312i_Nefedov_Filt.MainWindow;

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
                int drawingStartPointX = controller.originalView.getWidth() / 2 - controller.originalView.canvas.getWidth() / 2;
                int drawingStartPointY = controller.originalView.getHeight() / 2 - controller.originalView.canvas.getHeight() / 2;

                if ((drawingStartPointX <= e.getX() && e.getX() <= drawingStartPointX + controller.originalView.canvas.getWidth())
                        && (drawingStartPointY <= e.getY() && e.getY() <= drawingStartPointY + controller.originalView.canvas.getHeight())) {
                    MainWindow.showMessageDialog("Inside the picture!", "NOICE!");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int drawingStartPointX = controller.originalView.getWidth() / 2 - controller.originalView.canvas.getWidth() / 2;
                int drawingStartPointY = controller.originalView.getHeight() / 2 - controller.originalView.canvas.getHeight() / 2;

                if ((drawingStartPointX <= e.getX() && e.getX() <= drawingStartPointX + controller.originalView.canvas.getWidth())
                        && (drawingStartPointY <= e.getY() && e.getY() <= drawingStartPointY + controller.originalView.canvas.getHeight())) {
                    controller.isDragging = true;

                    controller.dragOffsetX = e.getX() - drawingStartPointX;
                    controller.dragOffsetY = e.getY() - drawingStartPointY;
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                controller.isDragging = false;
                controller.mainView.getMainPanel().repaint();
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
                if (controller.isDragging)
                    controller.dragPoint = e.getPoint();

                controller.mainView.getMainPanel().repaint();
            }
        };

        return listener;
    }
}
