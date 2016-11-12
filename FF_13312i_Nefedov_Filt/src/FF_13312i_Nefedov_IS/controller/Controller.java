package FF_13312i_Nefedov_IS.controller;

import FF_13312i_Nefedov_IS.view.View;

import javax.naming.SizeLimitExceededException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {
    private double  gamma;
    private boolean isAqua;

    private List<Integer> stampingMatrix;
    private List<Integer> blurMatrix;
    private List<Integer> sharpenMatrix;
    private List<Integer> contourMatrix;

    private static int MATRIX_3x3 = 9;
    private static int MATRIX_5x5 = 25;


    public View originalView;
    public View filteredView;
    public List<BufferedImage> backup;

    public Controller() {
        this.gamma = 1.1;
        this.isAqua = false;

        this.originalView = new View(this, new File("data/1.png"));
        this.filteredView = new View(this, originalView.canvas);

        this.stampingMatrix = new ArrayList<>();
        this.contourMatrix = new ArrayList<>();
        this.sharpenMatrix = new ArrayList<>();
        this.blurMatrix = new ArrayList<>();

        this.backup = new ArrayList<>();

        fillMatrix();
    }

    private void fillMatrix() {
        int[] stampingArray = {0, 1, 0, -1, 0, 1, 0, -1, 0};
        int[] contourArray  = {0, -1, 0, -1, 4, -1, 0, -1, 0};
        int[] sharpenArray  = {0, -1, 0, -1, 5, -1, 0, -1, 0};
        int[] blurArray     = {0, 1, 0, 1, 2, 1, 0, 1, 0};

        for (int element : stampingArray)
            this.stampingMatrix.add(element);

        for (int element : contourArray)
            this.contourMatrix.add(element);

        for (int element : sharpenArray)
            this.sharpenMatrix.add(element);

        for (int element : blurArray)
            this.blurMatrix.add(element);
    }

    public void setGamma(double value) {
        this.gamma = value;
    }

    public void copySRCToDST() {
        backup.clear();
        filteredView.clearImage();
        filteredView.g2d.drawImage(originalView.canvas, 0, 0, null);
        filteredView.repaint();
    }

    public void copyDSTToSRC() {
        backup.clear();
        originalView.clearImage();
        originalView.g2d.drawImage(filteredView.canvas, 0, 0, null);
        originalView.repaint();
    }

    private Color fitColors(int r, int g, int b) {
        if (r < 0)
            r = 0;
        else if (r > 255)
            r = 255;
        if (g < 0)
            g = 0;
        else if (g > 255)
            g = 255;
        if (b < 0)
            b = 0;
        else if (b > 255)
            b = 255;

        return new Color(r, g, b);
    }

    public void doBWConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = copyImage(originalView.canvas);

        filteredView.clearImage();

        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                Color old_color = new Color(buffer.getRGB(x, y));
                int tmp_color = (int)(old_color.getRed() * 0.299) + (int)(old_color.getGreen() * 0.587) + (int)(old_color.getBlue() * 0.114);

                if (tmp_color < 0)
                    tmp_color = 0;
                if (tmp_color > 255)
                    tmp_color = 255;

                Color new_color = new Color(tmp_color, tmp_color, tmp_color);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x, y);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doNegativeConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = copyImage(originalView.canvas);

        filteredView.clearImage();

        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                Color old_color = new Color(buffer.getRGB(x, y));
                Color new_color = new Color(255 - old_color.getRed(), 255 - old_color.getGreen(), 255 - old_color.getBlue());

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x, y);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doStampingConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = expandImage(originalView.canvas, Controller.MATRIX_3x3);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                try {
                    Color new_color = doMatrixColorConversion(buffer, stampingMatrix, Controller.MATRIX_3x3, x, y, 128);

                    filteredView.g2d.setColor(new_color);
                    View.setPoint(filteredView.g2d, x - 1, y - 1);
                } catch (SizeLimitExceededException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doBlurConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = expandImage(originalView.canvas, Controller.MATRIX_3x3);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                try {
                    Color new_color = doMatrixColorConversion(buffer, blurMatrix, Controller.MATRIX_3x3, x, y, 0);

                    filteredView.g2d.setColor(new_color);
                    View.setPoint(filteredView.g2d, x - 1, y - 1);
                } catch (SizeLimitExceededException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doSharpenConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = null;

        if (isAqua)
            buffer = expandImage(filteredView.canvas, Controller.MATRIX_3x3);
        else
            buffer = expandImage(originalView.canvas, Controller.MATRIX_3x3);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                try {
                    Color new_color = doMatrixColorConversion(buffer, sharpenMatrix, Controller.MATRIX_3x3, x, y, 0);

                    filteredView.g2d.setColor(new_color);
                    View.setPoint(filteredView.g2d, x - 1, y - 1);
                } catch (SizeLimitExceededException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    private BufferedImage expandImage(BufferedImage src, int matrix_type) {
        int expandSize = 0;

        if (matrix_type == this.MATRIX_3x3)
            expandSize = 2;
        else if (matrix_type == this.MATRIX_5x5)
            expandSize = 4;

        BufferedImage res = new BufferedImage(src.getWidth() + expandSize, src.getHeight() + expandSize, TYPE_INT_ARGB);
        Graphics2D    gg  = (Graphics2D) res.getGraphics();

        for (int x = 0; x < res.getWidth(); x++) {
            for (int y = 0; y < res.getHeight(); y++) {
                int curr_x = x - expandSize / 2;
                int curr_y = y - expandSize / 2;

                if (x == 0)
                    curr_x += expandSize / 2;
                if (y == 0)
                    curr_y += expandSize / 2;

                if (x == 1)
                    curr_x += (int)((double)expandSize / 4.);
                if (y == 1)
                    curr_y += (int)((double)expandSize / 4.);

                if (x == res.getWidth() - 2)
                    curr_x -= (int)((double)expandSize / 4.);
                if (y == res.getHeight() - 2)
                    curr_y -= (int)((double)expandSize / 4.);

                if (x == res.getWidth() - 1)
                    curr_x -= expandSize / 2;
                if (y == res.getHeight() - 1)
                    curr_y -= expandSize / 2;

                gg.setColor(new Color(src.getRGB(curr_x, curr_y)));
                View.setPoint(gg, x, y);
            }
        }

        return res;
    }

    private Color doMatrixColorConversion(BufferedImage image, List<Integer> matrix, int matrix_type, int x, int y, int offset) throws SizeLimitExceededException {
        int   new_r    = 0;
        int   new_g    = 0;
        int   new_b    = 0;
        int   divider  = 0;
        int[] dx_array = new int[(int)Math.sqrt(matrix_type)];
        int[] dy_array = new int[(int)Math.sqrt(matrix_type)];

        List<Color>  colors = new ArrayList<>();

        if (matrix.size() != matrix_type) {
            throw new SizeLimitExceededException("Matrix actual size doesn't match the specified type");
        }

        for (int i = 0; i < dx_array.length; i++) {
            if (matrix_type == Controller.MATRIX_3x3) {
                dx_array[i] = i - 1;
                dy_array[i] = i - 1;
            }
            else if (matrix_type == Controller.MATRIX_5x5) {
                dx_array[i] = i - 2;
                dy_array[i] = i - 2;
            }
        }

        for (int dy : dy_array)
            for (int dx : dx_array)
                colors.add(new Color(image.getRGB(x + dx, y + dy)));

        for (int i = 0; i < matrix_type; i++) {
            new_r += colors.get(i).getRed() * matrix.get(i);
            new_g += colors.get(i).getGreen() * matrix.get(i);
            new_b += colors.get(i).getBlue() * matrix.get(i);

            divider += matrix.get(i);
        }

        if (divider <= 0)
            divider = 1;

        new_r /= divider;
        new_g /= divider;
        new_b /= divider;

        new_r += offset;
        new_g += offset;
        new_b += offset;

        Color newColor = fitColors(new_r, new_g, new_b);

        return newColor;
    }

    private Color doMedianColorConversion(BufferedImage image, int x, int y) {
        int[] dx_array = {-2, -1, 0, 1, 2};
        int[] dy_array = {-2, -1, 0, 1, 2};

        List<Integer> colors = new ArrayList<>();

        for (int dy : dy_array)
            for (int dx : dx_array)
                colors.add(image.getRGB(x + dx, y + dy));

        Collections.sort(colors);

        return new Color(colors.get(13));
    }

    public void doMedianConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = expandImage(originalView.canvas, Controller.MATRIX_5x5);

        filteredView.clearImage();

        for (int x = 2; x < buffer.getWidth() - 2; x++) {
            for (int y = 2; y < buffer.getHeight() - 2; y++) {
                Color new_color = doMedianColorConversion(buffer, x, y);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x - 2, y - 2);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doAquarelizationConversion() {
        backup.add(filteredView.canvas);
        backup.remove(backup.size() - 3);
        backup.remove(backup.size() - 2);

        this.isAqua = true;

        doMedianConversion();
        doSharpenConversion();

        this.isAqua = false;

        filteredView.repaint();
    }

    public void doGammaCorrection() {
        backup.add(filteredView.canvas);

        for (int x = 0; x < originalView.canvas.getWidth(); x++) {
            for (int y = 0; y < originalView.canvas.getHeight(); y++) {
                Color old_color = new Color(originalView.canvas.getRGB(x, y));

                int new_r = (int)Math.pow((double)old_color.getRed(), this.gamma);
                int new_g = (int)Math.pow((double)old_color.getGreen(), this.gamma);
                int new_b = (int)Math.pow((double)old_color.getBlue(), this.gamma);

                Color new_color = fitColors(new_r, new_g, new_b);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x, y);
            }
        }

        filteredView.repaint();
    }

    public void doContourSelection() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = expandImage(originalView.canvas, Controller.MATRIX_3x3);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                try {
                    Color new_color = doMatrixColorConversion(buffer, contourMatrix, Controller.MATRIX_3x3, x, y, 0);

                    filteredView.g2d.setColor(new_color);
                    View.setPoint(filteredView.g2d, x - 1, y - 1);
                } catch (SizeLimitExceededException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    private void copyImage(BufferedImage src, BufferedImage dst) {
        dst.getGraphics().drawImage(src, 0, 0, null);
    }

    private BufferedImage copyImage(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), TYPE_INT_ARGB);
        copyImage(src, dst);

        return dst;
    }

    public void undo() {
        if (!backup.isEmpty()) {
            filteredView.clearImage();
            copyImage(backup.get(backup.size() - 1), filteredView.canvas);
            backup.remove(backup.size() - 1);
            filteredView.repaint();
        }
    }


    /**
     * Loads an information about the program and shows it
     */
    /*
    public void showAbout() {
        File about = new File("FF_13312i_Nefedov_IS_Data/About.txt");
        String fileLine;
        DefaultListModel aboutText = new DefaultListModel();

        if (about != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(about));

                while((fileLine = reader.readLine()) != null) {
                    aboutText.addElement(fileLine + "\n");
                }

                originalView.showScrollableMessageDialog(aboutText, "About");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}