package FF_13312i_Nefedov_LE.controller;

import FF_13312i_Nefedov_LE.model.Line;
import FF_13312i_Nefedov_LE.view.View;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {

    public List<Line>   lines;
    public View         view;
    public boolean      isNewLine;
    public boolean      isDragging;
    public Point        marker;
    public Point        fakePoint;

    /**
     * Default class constructor
     * Initializes properties: creates new view, array of the lines and sets up flags
     */
    public Controller() {
        lines = new ArrayList<>();
        view = new View(this);
        isNewLine = true;
        isDragging = false;
    }

    /**
     * Deletes the last drawn line
     */
    public void deleteLine(){
        if (lines.size() != 0) {
            lines.remove(lines.size() - 1);
            marker = null;
            view.repaint();
        }
        else
            view.showErrorDialog("There is no any drawn lines!");
    }

    /**
     * Creates new empty document
     */
    public void newFile(){
        lines.clear();
        marker = null;
        view.repaint();
    }

    /**
     * Loads an information about the program and shows it
     */
    public void showAbout() {
        File about = new File("FF_13312i_Nefedov_LE_Data/About.txt");
        String fileLine;
        String aboutText = new String();

        if (about != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(about));

                while((fileLine = reader.readLine()) != null) {
                    aboutText += fileLine + "\n";
                }

                view.showMessageDialog(aboutText);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current picture to a .le file
     * @param fileToSave - name of the file to save
     */
    public void saveFile(File fileToSave) {
        if (fileToSave != null) {
            try {
                FileWriter fw = new FileWriter(fileToSave, false);

                fw.write(Integer.toString(lines.size()) + "\n");

                for (Line line : lines){
                    fw.write("Polyline\n");
                    for (Point point : line.points){
                        fw.write(point.x + " " + point.y + "\n");
                    }
                    fw.append('\n');
                }

                fw.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens the picture from a .le file
     * @param fileToOpen - name of the file to open
     */
    public void openFile(File fileToOpen) {
        if(fileToOpen != null) {
            try {
                BufferedReader fr = new BufferedReader(new FileReader(fileToOpen));
                String fileLine;

                lines.clear();

                while ((fileLine = fr.readLine()) != null) {
                    if (fileLine.equals("Polyline")) {
                        lines.add(new Line());

                        while ((fileLine = fr.readLine()).trim().length() != 0) {
                            Point point = new Point();
                            String[] pointCoordinates = fileLine.split(" ");

                            point.setLocation(Integer.parseInt(pointCoordinates[0]), Integer.parseInt(pointCoordinates[1]));
                            lines.get(lines.size() - 1).points.add(point);
                        }
                    }
                }

                view.repaint();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
