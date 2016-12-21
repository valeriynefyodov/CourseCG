package FF_13312i_Nefedov_WF.controller;

import FF_13312i_Nefedov_WF.MainWindow;
import FF_13312i_Nefedov_WF.model.Matrix;
import FF_13312i_Nefedov_WF.model.Point3D;
import FF_13312i_Nefedov_WF.model.Spline;
import FF_13312i_Nefedov_WF.view.SplineView;
import FF_13312i_Nefedov_WF.view.WFView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {
    public File initFile;

    public MainWindow mainView;
    public SplineView splineView;
    public WFView wfView;

    public List<Spline> splines;

    public boolean isNewSpline;
    public boolean deletePoint;

    private Matrix splineMatrix;
    private Matrix tVector;
    private Matrix gMatrix;

    private int xc;
    private int yc;
    private int zc;

    private int xv;
    private int yv;
    private int zv;

    private int xu;
    private int yu;
    private int zu;

    public int n;
    public int m;
    public int k;
    public int a;
    public int b;
    public int c;
    public int d;

    public double sw;
    public double sh;
    public double zn;
    public double zf;

    public double thetaX;
    public double thetaY;
    public double thetaZ;

    public Controller() {
        this.splineView = new SplineView(this);
        this.wfView = new WFView(this);

        this.splines = new ArrayList();
        this.splines.add(new Spline());

        this.isNewSpline = false;
        this.deletePoint = false;

        this.defineMatrix();

        this.thetaX = Math.PI / 6;
        this.thetaY = 0;
        this.thetaZ = Math.PI / 6;

        this.loadSettings();
        this.mainView = new MainWindow(this);
    }

    private void loadSettings() {
        this.initFile = new File("data/init.wf");

        this.xc = -10;
        this.yc = 0;
        this.zc = 0;

        this.xv = 10;
        this.yv = 0;
        this.zv = 0;

        this.xu = 0;
        this.yu = 1;
        this.zu = 0;

        this.openFile(this.initFile);
    }

    private void defineMatrix() {
        this.tVector = null;
        this.gMatrix = null;

        double[][] splineMatrixElements = new double[][] {
            {-1., 3., -3., 1.},
            { 3., -6., 3., 0.},
            {-3.,  0., 3., 0.},
            { 1.,  4., 1., 0.}
        };

        this.splineMatrix = new Matrix(splineMatrixElements);
        MatrixMath.multiplyByScalar(splineMatrix, 1./6.);
    }

    /**
     * Loads an information about the program and shows it
     */
    public void showAbout() {
        File about = new File("data/About.txt");
        String fileLine;
        DefaultListModel aboutText = new DefaultListModel();

        if (about != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(about));

                while((fileLine = reader.readLine()) != null) {
                    aboutText.addElement(fileLine + "\n");
                }

                MainWindow.showScrollableMessageDialog(aboutText, "About");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Point getSplinePoint(Spline s, int i, double t){
        double[][] tVectorElements = new double[][] { { Math.pow(t,3), Math.pow(t,2), Math.pow(t,1), 1 } };
        tVector = new Matrix(tVectorElements); //new Matrix(tVectorElements);

        tVector = MatrixMath.multiplyMatrix(tVector, splineMatrix);

        double[][] gMatrixElements = new double[][] {
                {s.points.get(i-1).getX(), s.points.get(i-1).getY()},
                {s.points.get(i  ).getX(), s.points.get(i  ).getY()},
                {s.points.get(i+1).getX(), s.points.get(i+1).getY()},
                {s.points.get(i+2).getX(), s.points.get(i+2).getY()},
        };

        gMatrix = new Matrix(gMatrixElements);

        tVector = MatrixMath.multiplyMatrix(tVector, gMatrix);
        double[][] new_coordinates = tVector.getElements();

        return new Point((int)new_coordinates[0][0], (int)new_coordinates[0][1]);
    }

    public Point3D getProjectionPoint(Spline s, int i, double t, double phi) {
        Point p = getSplinePoint(s, i, t);

        double new_x = p.x * Math.cos(phi);
        double new_y = p.x * Math.sin(phi);
        double new_z = p.y;

        double[] coordinateVectorElements = new double[] { new_x, new_y, new_z, 1 };
        Matrix coordinateVector = new Matrix(coordinateVectorElements);

        coordinateVector = scale(coordinateVector, 0.5,0.5, 0.5);

        coordinateVector = rotateZCoordinate(coordinateVector, thetaZ);
        coordinateVector = rotateXCoordinate(coordinateVector, thetaX);
        coordinateVector = rotateYCoordinate(coordinateVector, thetaY);

        double[][] projectionMatrixElements = new double[][] {
            {2 / sw, 0, 0, 0},
            {0, 2 / sh, 0, 0},
            {0, 0, 1 / (zf - zn), -zn / (zf - zn)},
            {0, 0, 0, 1}
        };
        Matrix projectionMatrix = new Matrix(projectionMatrixElements);

        coordinateVector = MatrixMath.multiplyMatrix(projectionMatrix, coordinateVector);

        double[] newCoordinateVectorElements = coordinateVector.getColumn(0);
        double absolute = newCoordinateVectorElements[3];

        return new Point3D(newCoordinateVectorElements[0] / absolute, newCoordinateVectorElements[1] / absolute, newCoordinateVectorElements[2] / absolute);
    }

    private Matrix rotateXCoordinate(Matrix matrix, double theta){
        double[][] rotationMatrixElements = new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(theta), -1 * Math.sin(theta), 0},
                {0, Math.sin(theta), Math.cos(theta), 0},
                {0, 0, 0, 1},
        };

        Matrix rotationMatrix = new Matrix(rotationMatrixElements);

        return MatrixMath.multiplyMatrix(rotationMatrix, matrix);
    }

    private Matrix rotateYCoordinate(Matrix matrix, double theta){
        double[][] rotationMatrixElements = new double[][]{
                {Math.cos(theta), -1 * Math.sin(theta), 0, 0},
                {Math.sin(theta), Math.cos(theta), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        };

        Matrix rotationMatrix = new Matrix(rotationMatrixElements);

        return MatrixMath.multiplyMatrix(rotationMatrix, matrix);
    }

    private Matrix rotateZCoordinate(Matrix matrix, double theta){
        double[][] rotationMatrixElements = new double[][]{
                {Math.cos(theta), 0, Math.sin(theta), 0},
                {0, 1, 0, 0},
                {-1 * Math.sin(theta), 0, Math.cos(theta), 0},
                {0, 0, 0, 1},
        };

        Matrix rotationMatrix = new Matrix(rotationMatrixElements);

        return MatrixMath.multiplyMatrix(rotationMatrix, matrix);
    }

    private Matrix scale(Matrix matrix, double x, double y, double z) {
        double[][] scaleMatrixElements = new double[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1},
        };

        Matrix scaleMatrix = new Matrix(scaleMatrixElements);

        return MatrixMath.multiplyMatrix(scaleMatrix, matrix);
    }

    private Matrix translate(Matrix matrix, double x, double y, double z) {
        double[][] translateMatrixElements = new double[][]{
                {1,0,0,x},
                {0,1,0,y},
                {0,0,1,z},
                {0,0,0,1},
        };

        Matrix translateMatrix = new Matrix(translateMatrixElements);

        return MatrixMath.multiplyMatrix(translateMatrix, matrix);
    }


    public Point getNearestPoint(Point m_point) {
        for (Spline s : splines) {
            for (Point p : s.points) {
                if ((p.x - 10 <= m_point.x && m_point.x <= p.x + 10)
                        && (p.y - 10 <= m_point.y && m_point.y <= p.y + 10))
                    return p;
            }
        }

        return null;
    }

    public Spline findSplineByPoint(Point point) {
        for (Spline s : splines) {
            for (Point p : s.points) {
                if (p.equals(point))
                    return s;
            }
        }

        return null;
    }

    public void deleteSpline() {
        splines.remove(splines.size() - 1);
        mainView.repaint();
    }

    /**
     * Opens the scene from a .wf file
     */
    public void openFile(File fileToOpen) {
        if(fileToOpen != null) {
            try {
                BufferedReader fr = new BufferedReader(new FileReader(fileToOpen));
                String fileLine;

                splines.clear();

                fileLine = fr.readLine();
                String[] params = fileLine.split(" ");

                this.n = Integer.parseInt(params[0]);
                this.m = Integer.parseInt(params[1]);
                this.k = Integer.parseInt(params[2]);
                this.a = Integer.parseInt(params[3]);
                this.b = Integer.parseInt(params[4]);
                this.c = Integer.parseInt(params[5]);
                this.d = Integer.parseInt(params[6]);

                this.zn = Double.parseDouble(params[7]);
                this.zf = Double.parseDouble(params[8]);
                this.sw = Double.parseDouble(params[9]);
                this.sh = Double.parseDouble(params[10]);

                int shapesAmount = Integer.parseInt(fr.readLine());
                int counter = 0;

                while ((fileLine = fr.readLine()) != null && counter < shapesAmount) {
                    splines.add(new Spline());

                    int pointsAmount = Integer.parseInt(fileLine);

                    for (int i = 0; i < pointsAmount; i++) {
                        Point point = new Point();

                        if ((fileLine = fr.readLine()) != null) {
                            String[] pointCoordinates = fileLine.split(" ");

                            point.setLocation(Integer.parseInt(pointCoordinates[0]), Integer.parseInt(pointCoordinates[1]));
                            splines.get(splines.size() - 1).points.add(point);
                        }
                    }
                }

                splineView.repaint();
                wfView.repaint();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setParams(int answer) {
        if (answer == JOptionPane.OK_OPTION) {
            this.n = Integer.parseInt(mainView.params.get(0).getText());
            this.m = Integer.parseInt(mainView.params.get(1).getText());
            this.k = Integer.parseInt(mainView.params.get(2).getText());
            this.zn = Double.parseDouble(mainView.params.get(3).getText());
            this.zf = Double.parseDouble(mainView.params.get(4).getText());
            this.sw = Double.parseDouble(mainView.params.get(5).getText());
            this.sh = Double.parseDouble(mainView.params.get(6).getText());

            this.wfView.repaint();
        }
    }
}