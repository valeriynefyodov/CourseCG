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

    public View originalView;
    public View filteredView;
    public List<BufferedImage> backup;

    public Controller() {
        this.originalView = new View(this, new File("data/1.png"));
        this.filteredView = new View(this, originalView.canvas);
        this.backup = new ArrayList<>();
    }

    public void convertToBW() {
        BufferedImage buffer = copyImage(filteredView.canvas);
        filteredView.clearScreen();
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

    public void convertToNegative() {
        BufferedImage buffer = copyImage(filteredView.canvas);
        filteredView.clearScreen();
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
        filteredView.clearScreen();
        filteredView.g2d.drawImage(originalView.canvas, 0, 0, null);
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
            filteredView.clearScreen();
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