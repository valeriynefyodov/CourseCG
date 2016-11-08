package FF_13312i_Nefedov_Span.controller;

import FF_13312i_Nefedov_Span.model.*;
import FF_13312i_Nefedov_Span.model.Shape;
import FF_13312i_Nefedov_Span.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {

    public List<Shape>         shapes;
    public Line                fakePolygon;
    public Oval                fakeOval;
    public View                view;
    public Point               marker;
    public Point               fakePoint;
    public String              mode;
    public boolean             isDragging;
    public float               lineWidth;
    public int                 fillColor;
    public int                 fillType;

    /**
     * Default class constructor
     * Initializes properties: creates new view, array of the lines and sets up flags
     */
    public Controller() {
        shapes = new ArrayList<>();
        fakeOval = new Oval();
        fakePolygon = new Line();
        view = new View(this);
        isDragging = false;
        mode = "init";
        lineWidth = 1.0f;
        fillColor = 0;
        fillType = 4;
    }

    /**
     * Deletes the previous drawn shape
     */
    public void deleteShape(){
        if(isDragging)
            return;

        if (shapes.size() != 0) {
            shapes.remove(shapes.size() - 1);
            fakePolygon.delete();
            marker = null;

            view.repaint();
        }
        else
            view.showErrorDialog("There is no any drawn shapes!", "Error");
    }

    /**
     * Creates new empty document
     */
    public void newFile(){

        setDefaults(true);

        view.repaint();
    }

    /**
     * Loads an information about the program and shows it
     */
    public void showAbout() {
        File about = new File("FF_13312i_Nefedov_Span_Data/About.txt");
        String fileLine;
        DefaultListModel aboutText = new DefaultListModel();

        if (about != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(about));

                while((fileLine = reader.readLine()) != null) {
                    aboutText.addElement(fileLine + "\n");
                }

                view.showScrollableMessageDialog(aboutText, "About");

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

                for (Shape s : shapes){

                    String shapeName = s.getShapeName();

                    fw.write(shapeName + "\n");

                    switch(shapeName) {
                        case "POLYGON":
                            List<Point> points  = s.getPoints();
                            fw.write(points.size() + " " + s.getLineWidth() + "\n");

                            for(Point p : points) {
                                fw.write(p.x + " " + p.y + "\n");
                            }

                            break;

                        case "CIRCLE":
                            fw.write(s.getCenter().x + " " + s.getCenter().y + " " + s.getRadius() + " " + s.getLineWidth() + "\n");

                            break;

                        case "FILL":
                            fw.write(s.getSeed().x + " " + s.getSeed().y + " " + s.getFillColor() + " " + s.getPC() + "\n");

                            break;
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

        this.newFile();

        try {
            BufferedReader fr = new BufferedReader(new FileReader(fileToOpen));
            String fileLine;

            while ((fileLine = fr.readLine()) != null) {
                switch(fileLine) {
                    case "POLYGON":
                        fakePolygon = new Line();

                        fileLine = fr.readLine();
                        String[] properties = fileLine.split(" ");
                        this.lineWidth = Float.parseFloat(properties[1]);

                        while ((fileLine = fr.readLine()).trim().length() != 0) {
                            Point point = new Point();
                            String[] pointCoordinates = fileLine.split(" ");

                            point.setLocation(Integer.parseInt(pointCoordinates[0]), Integer.parseInt(pointCoordinates[1]));
                            fakePolygon.points.add(point);
                        }

                        shapes.add(new CustomPolygon(fakePolygon, lineWidth));
                        view.placeShapeOnImage((Graphics2D) view.buffImage.getGraphics(), shapes.get(shapes.size() - 1));

                        break;

                    case "CIRCLE":
                        fileLine = fr.readLine();
                        String[] propertiesCircle = fileLine.split(" ");

                        shapes.add(new Oval(new Point(Integer.parseInt(propertiesCircle[0]), Integer.parseInt(propertiesCircle[1])), Integer.parseInt(propertiesCircle[2]), Float.parseFloat(propertiesCircle[3])));
                        view.placeShapeOnImage((Graphics2D) view.buffImage.getGraphics(), shapes.get(shapes.size() - 1));

                        break;

                    case "FILL":
                        fileLine = fr.readLine();
                        String[] propertiesFill = fileLine.split(" ");
                        Point seed = new Point(Integer.parseInt(propertiesFill[0]), Integer.parseInt(propertiesFill[1]));

                        shapes.add(new Fill(view.buffImage, seed, Integer.parseInt(propertiesFill[3]), Integer.parseInt(propertiesFill[2])));
                        view.placeShapeOnImage((Graphics2D) view.buffImage.getGraphics(), shapes.get(shapes.size() - 1));

                        break;

                    default:
                        break;
                }
            }
        } catch (FileNotFoundException ex1) {
            this.view.showErrorDialog("File not found!", "Error");
            ex1.printStackTrace();
        } catch (IOException ex2) {
            this.view.showErrorDialog("Cannot open the file!", "Error");
            ex2.printStackTrace();
        }
        finally {
            setDefaults(false);
            view.repaint();
        }
    }

    /**
     * Sets the default settings
     * @param onNewFile - flag that shows if setting defaults caused by creating a new file
     */
    public void setDefaults(boolean onNewFile) {

        if(onNewFile) {
            if(shapes != null)
                shapes.clear();

            this.view.buffImage = new BufferedImage(3072, 2048, TYPE_INT_ARGB);

            mode = "init";
            lineWidth = 1.0f;
            fillColor = 0;
            fillType = 4;
        }

        if(fakePolygon != null)
            fakePolygon.delete();

        if(fakeOval != null)
            fakeOval.delete();

        isDragging = false;
        fakePoint = null;
        marker = null;
    }
}