package FF_13312i_Nefedov_IS.view;

import FF_13312i_Nefedov_IS.controller.Controller;
import FF_13312i_Nefedov_IS.controller.MouseController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Custom view class extended from JPanel class
 */
public class View extends JPanel {

    private int SIZE_W;
    private int SIZE_H;

    public Controller controller;

    public BufferedImage orig_canvas;
    public BufferedImage filt_canvas;
    public BufferedImage splt_canvas;

    private Graphics2D orig_g;
    private Graphics2D filt_g;
    private Graphics2D splt_g;

    /**
     * Class constructor where you can specify the main controller and size
     * @param cntrl - specified controller
     * @param width - preferred width
     * @param height - preferred height
     */
    public View(Controller cntrl, int width, int height){
        SIZE_W = width;
        SIZE_H = height;

        this.setPreferredSize(new Dimension(SIZE_W, SIZE_H));

        orig_canvas = new BufferedImage(SIZE_W, SIZE_H, TYPE_INT_ARGB);
        filt_canvas = new BufferedImage(SIZE_W, SIZE_H, TYPE_INT_ARGB);
        splt_canvas = new BufferedImage(SIZE_W, SIZE_H, TYPE_INT_ARGB);

        orig_g = orig_canvas.createGraphics();
        filt_g = filt_canvas.createGraphics();
        splt_g = splt_canvas.createGraphics();

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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        clearScreen();

        int drawingStartPointX = this.getWidth() / 2 - SIZE_W / 2;
        int drawingStartPointY = this.getHeight() / 2 - SIZE_H / 2;

        if (!controller.isSplitted) {
            if (controller.showFilters)
                g.drawImage(filt_canvas, drawingStartPointX, drawingStartPointY, null);
            else
                g.drawImage(orig_canvas, drawingStartPointX, drawingStartPointY, null);

        }
        else {
            splitImage();
            g.drawImage(splt_canvas, drawingStartPointX, drawingStartPointY, null);
        }
    }

    /**
     * Clears the screen - sets default background color to the canvases
     */
    private void clearScreen() {
        orig_g.setColor(Color.GREEN);
        filt_g.setColor(Color.BLUE);
        splt_g.setColor(Color.WHITE);

        orig_g.fillRect(0, 0, orig_canvas.getWidth(), orig_canvas.getHeight());
        filt_g.fillRect(0, 0, filt_canvas.getWidth(), filt_canvas.getHeight());
        splt_g.fillRect(0, 0, splt_canvas.getWidth(), splt_canvas.getHeight());

        orig_g.setColor(Color.BLACK);
        filt_g.setColor(Color.BLACK);
        splt_g.setColor(Color.BLACK);
    }

    /**
     * Creates a new splitted image from original and filtered ones
     * Left half - original, right - filtered
     */
    private void splitImage() {
        /*for (int x = 0; x < SIZE_W / 2; x++) {
            for (int y = 0; y < SIZE_H; y++) {
                Color curr_color = new Color(orig_canvas.getRGB(x, y));
                splt_g.setColor(curr_color);
                setPoint(splt_g, x, y);
            }
        }

        for (int x = SIZE_W / 2; x < SIZE_W; x++) {
            for (int y = 0; y < SIZE_H; y++) {
                Color curr_color = new Color(filt_canvas.getRGB(x, y));
                splt_g.setColor(curr_color);
                setPoint(splt_g, x, y);
            }
        }*/


    }

    /**
     * Draws a pixel
     * @param g - graphics component
     * @param x - pixel's X-coordinate
     * @param y - pixel's Y-coordinate
     */
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




















