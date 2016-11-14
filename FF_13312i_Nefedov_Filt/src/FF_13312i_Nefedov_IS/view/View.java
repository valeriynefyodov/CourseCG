package FF_13312i_Nefedov_IS.view;

import FF_13312i_Nefedov_IS.controller.Controller;
import FF_13312i_Nefedov_IS.controller.MouseController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Custom originalView class extended from JPanel class
 */
public class View extends JPanel {

    private int             IMG_SIZE_W;
    private int             IMG_SIZE_H;
    private Color           bg_color;

    public Controller       controller;
    public BufferedImage    canvas;
    public Graphics2D       g2d;

    /**
     * Class constructor where you can specify the main controller and a file you'd like to create view for
     * @param cntrl - specified controller
     * @param input - file containing an image
     */
    public View(Controller cntrl, File input){

        bg_color = new Color(67, 160, 255);

        try {
            BufferedImage inputImage = ImageIO.read(input);

            IMG_SIZE_W = inputImage.getWidth();
            IMG_SIZE_H = inputImage.getHeight();

            canvas = new BufferedImage(IMG_SIZE_W, IMG_SIZE_H, TYPE_INT_ARGB);
            g2d = canvas.createGraphics();

            g2d.drawImage(inputImage, 0, 0, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(IMG_SIZE_W, IMG_SIZE_H));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(6, 6, 6, 3, bg_color),
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1),
                        "Original",
                        TitledBorder.CENTER,
                        TitledBorder.BELOW_TOP,
                        new Font(Font.SANS_SERIF, Font.BOLD, 20))));

        controller = cntrl;
        MouseController msCntrl = new MouseController(controller);

        addMouseListener(msCntrl.createMouseListener());
        addMouseMotionListener(msCntrl.createMouseMotionListener());
    }

    /**
    * Class constructor where you can specify the main controller and which creates a view with the canvas exactly like the input image's canvas
    * @param cntrl - specified controller
    * @param image - original image
    */
    public View(Controller cntrl, BufferedImage image){

        bg_color = new Color(67, 160, 255);

        IMG_SIZE_W = image.getWidth();
        IMG_SIZE_H = image.getHeight();

        canvas = new BufferedImage(IMG_SIZE_W, IMG_SIZE_H, TYPE_INT_ARGB);
        g2d = canvas.createGraphics();

        this.setPreferredSize(new Dimension(IMG_SIZE_W, IMG_SIZE_H));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(6, 3, 6, 6, bg_color),
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1),
                        "Filtered",
                        TitledBorder.CENTER,
                        TitledBorder.BELOW_TOP,
                        new Font(Font.SANS_SERIF, Font.BOLD, 20))));

        controller = cntrl;
        MouseController msCntrl = new MouseController(controller);

        addMouseListener(msCntrl.createMouseListener());
        addMouseMotionListener(msCntrl.createMouseMotionListener());

        clearImage();
    }

    /**
     * Overrides library's paintComponent function
     * Checks the conditions and draws required graphics
     * @param g - graphic's component
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        clearPanel(g);

        int drawingStartPointX = this.getWidth() / 2 - IMG_SIZE_W / 2;
        int drawingStartPointY = this.getHeight() / 2 - IMG_SIZE_H / 2;

        g.drawImage(canvas, drawingStartPointX, drawingStartPointY, null);
    }

    /**
     * Clears the image - sets default background color to the canvas
     */
    public void clearImage() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMG_SIZE_W, IMG_SIZE_H);
        g2d.setColor(Color.BLACK);
    }

    private void clearPanel(Graphics g) {
        g.setColor(this.bg_color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
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




















