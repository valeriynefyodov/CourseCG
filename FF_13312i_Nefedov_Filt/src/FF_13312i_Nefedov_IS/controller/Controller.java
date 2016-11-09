package FF_13312i_Nefedov_IS.controller;

import FF_13312i_Nefedov_IS.view.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {
    private List<Integer> stampingMatrix;
    private List<Integer> blurMatrix;
    private List<Integer> sharpenMatrix;

    public View originalView;
    public View filteredView;
    public List<BufferedImage> backup;

    public Controller() {
        this.originalView = new View(this, new File("data/1.png"));
        this.filteredView = new View(this, originalView.canvas);

        this.stampingMatrix = new ArrayList<>();
        this.sharpenMatrix = new ArrayList<>();
        this.blurMatrix = new ArrayList<>();

        this.backup = new ArrayList<>();

        fillMatrix();
    }

    private void fillMatrix() {
        this.stampingMatrix.add(0);
        this.stampingMatrix.add(1);
        this.stampingMatrix.add(0);
        this.stampingMatrix.add(-1);
        this.stampingMatrix.add(0);
        this.stampingMatrix.add(1);
        this.stampingMatrix.add(0);
        this.stampingMatrix.add(-1);
        this.stampingMatrix.add(0);

        this.sharpenMatrix.add(0);
        this.sharpenMatrix.add(-1);
        this.sharpenMatrix.add(0);
        this.sharpenMatrix.add(-1);
        this.sharpenMatrix.add(5);
        this.sharpenMatrix.add(-1);
        this.sharpenMatrix.add(0);
        this.sharpenMatrix.add(-1);
        this.sharpenMatrix.add(0);

        this.blurMatrix.add(0);
        this.blurMatrix.add(1);
        this.blurMatrix.add(0);
        this.blurMatrix.add(1);
        this.blurMatrix.add(2);
        this.blurMatrix.add(1);
        this.blurMatrix.add(0);
        this.blurMatrix.add(1);
        this.blurMatrix.add(0);
    }

    public void doBWConversion() {
        BufferedImage buffer = copyImage(filteredView.canvas);
        filteredView.clearImage();
        backup.add(buffer);

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
        BufferedImage buffer = copyImage(filteredView.canvas);
        filteredView.clearImage();
        backup.add(buffer);

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

    public void copySRCToDST() {
        backup.clear();
        filteredView.clearImage();
        filteredView.g2d.drawImage(originalView.canvas, 0, 0, null);
        filteredView.repaint();
    }

    public void doStampingConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = apodizatateImage(filteredView.canvas);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                Color new_color = doMatrixColorConversion(buffer, stampingMatrix, x, y, 128);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x - 1, y - 1);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doBlurConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = apodizatateImage(filteredView.canvas);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                Color new_color = doMatrixColorConversion(buffer, blurMatrix, x, y, 0);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x - 1, y - 1);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    public void doSharpenConversion() {
        backup.add(filteredView.canvas);

        BufferedImage buffer = apodizatateImage(filteredView.canvas);

        filteredView.clearImage();

        for (int x = 1; x < buffer.getWidth() - 1; x++) {
            for (int y = 1; y < buffer.getHeight() - 1; y++) {
                Color new_color = doMatrixColorConversion(buffer, sharpenMatrix, x, y, 0);

                filteredView.g2d.setColor(new_color);
                View.setPoint(filteredView.g2d, x - 1, y - 1);
            }
        }

        buffer.getGraphics().dispose();
        filteredView.repaint();
    }

    private BufferedImage apodizatateImage(BufferedImage src) {
        BufferedImage res = new BufferedImage(src.getWidth() + 2, src.getHeight() + 2, TYPE_INT_ARGB);
        Graphics2D    gg  = (Graphics2D) res.getGraphics();

        for (int x = 0; x < res.getWidth(); x++) {
            for (int y = 0; y < res.getHeight(); y++) {
                int curr_x = x - 1;
                int curr_y = y - 1;

                if (x == 0)
                    curr_x += 1;
                if (y == 0)
                    curr_y += 1;
                if (x == res.getWidth() - 1)
                    curr_x -= 1;
                if (y == res.getHeight() - 1)
                    curr_y -= 1;

                gg.setColor(new Color(src.getRGB(curr_x, curr_y)));
                View.setPoint(gg, x, y);
            }
        }

        return res;
    }

    private Color doMatrixColorConversion(BufferedImage image, List<Integer> matrix, int x, int y, int offset) {
        int new_r   = 0;
        int new_g   = 0;
        int new_b   = 0;
        int divider = 0;

        List<Color>   colors = new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++)
            for (int dx = -1; dx <= 1; dx++)
                colors.add(new Color(image.getRGB(x + dx, y + dy)));

        for (int i = 0; i < 9; i++) {
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

        if (new_r < 0)
            new_r = 0;
        else if (new_r > 255)
            new_r = 255;
        if (new_g < 0)
            new_g = 0;
        else if (new_g > 255)
            new_g = 255;
        if (new_b < 0)
            new_b = 0;
        else if (new_b > 255)
            new_b = 255;

        Color newColor = new Color(new_r, new_g, new_b);

        return newColor;
    }

    private Color stampingMatrix(BufferedImage image, int x, int y, int divider, int offset) {
        int new_r = 0;
        int new_g = 0;
        int new_b = 0;

        List<Color>   colors = new ArrayList<>();
        List<Integer> matrix = new ArrayList<>();

        Color color_cc = new Color(image.getRGB(x, y));
        Color color_cl = new Color(image.getRGB(x - 1, y));
        Color color_cr = new Color(image.getRGB(x + 1, y));
        Color color_tc = new Color(image.getRGB(x, y - 1));
        Color color_bc = new Color(image.getRGB(x, y + 1));

        colors.add(color_tc);
        colors.add(color_cl);
        colors.add(color_cc);
        colors.add(color_cr);
        colors.add(color_bc);

        matrix.add(1);
        matrix.add(-1);
        matrix.add(0);
        matrix.add(1);
        matrix.add(-1);

        for (int i = 0; i < 5; i++) {
            new_r += colors.get(i).getRed() * matrix.get(i);
            new_g += colors.get(i).getGreen() * matrix.get(i);
            new_b += colors.get(i).getBlue() * matrix.get(i);
        }

        new_r /= divider;
        new_g /= divider;
        new_b /= divider;

        new_r += offset;
        new_g += offset;
        new_b += offset;

        if (new_r < 0)
            new_r = 0;
        else if (new_r > 255)
            new_r = 255;
        if (new_g < 0)
            new_g = 0;
        else if (new_g > 255)
            new_g = 255;
        if (new_b < 0)
            new_b = 0;
        else if (new_b > 255)
            new_b = 255;

        Color newColor = new Color(new_r, new_g, new_b);

        return newColor;
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