package FF_13312i_Nefedov_Span.view;

import FF_13312i_Nefedov_Span.controller.Controller;
import FF_13312i_Nefedov_Span.controller.MouseController;
import FF_13312i_Nefedov_Span.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.util.List;

/**
 * Custom view class extended from JPanel class
 */
public class View extends JPanel {

    public Controller controller;
    public BufferedImage buffImage;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public View(Controller cntrl){
        this.setPreferredSize(new Dimension(3072, 2048));

        buffImage = new BufferedImage(3072, 2048, TYPE_INT_ARGB);

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

        Graphics2D gg = (Graphics2D) buffImage.getGraphics();
        gg.setColor(Color.WHITE);
        gg.fillRect(0, 0, buffImage.getWidth(), buffImage.getHeight());
        gg.setColor(Color.BLACK);

        for (Shape s : controller.shapes) {
            s.draw(gg);
        }

        gg.setColor(Color.BLACK);
        gg.setStroke(new BasicStroke(controller.lineWidth));

        if (controller.marker != null)
            gg.drawOval(controller.marker.x - 2, controller.marker.y - 2, 4, 4);

        if (controller.isDragging && controller.fakePoint != null) {
            gg.drawLine(controller.marker.x, controller.marker.y, controller.fakePoint.x, controller.fakePoint.y);

            if (controller.fakePolygon.points.size() > 1) {
                gg.drawLine(controller.fakePolygon.points.get(0).x, controller.fakePolygon.points.get(0).y,
                            controller.fakePoint.x, controller.fakePoint.y);

                controller.fakePolygon.setWidth(controller.lineWidth);
                controller.fakePolygon.draw(gg);
            }

            if (controller.mode.equals("oval")) {
                controller.fakeOval.setWidth(controller.lineWidth);
                controller.fakeOval.draw(gg);
            }
        }

        g.drawImage(buffImage, 0, 0, this);
    }

    public void placeShapeOnImage(Graphics2D imageGraphics, Shape s) {
        s.draw(imageGraphics);
    }

    public static void setPoint(Graphics2D g, int x, int y) {
        g.drawLine(x, y, x, y);
    }

    /**
     * Shows an error dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showErrorDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(this, message, dialogName, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showMessageDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(this, message, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a scrollable message dialog
     * @param message - a list of strings containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showScrollableMessageDialog (DefaultListModel message, String dialogName) {
        JList textArea = new JList(message);
        JScrollPane scrollPane = new JScrollPane(textArea);

        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scrollPane, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }
}




















