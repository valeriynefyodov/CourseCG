package FF_13312i_Nefedov_IS.controller;

import FF_13312i_Nefedov_IS.model.Function;
import FF_13312i_Nefedov_IS.view.View;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller class
 * Contains methods to do program's routine
 */
public class Controller {
    public View view;
    public Function function;
    public int k_grid;
    public int m_grid;
    public int n_levels;
    public List<Color> colors;
    public Color is_color;
    public boolean isShowGrid;
    public boolean isDrawIsoline;
    public String  mode;
    public Point   curr_point;
    public ArrayList<Double> func_vals;

    public Controller() {
        this.function = new Function(-5, 5);
        this.view = new View(this);
        this.colors = new ArrayList<>();
        this.readConfig();
        this.isShowGrid = false;
        this.isDrawIsoline = false;
        this.mode = "norm";
        this.func_vals = new ArrayList<>();
        this.curr_point = null;
    }

    private void readConfig() {
        File config = new File("FF_13312i_Nefedov_IS_Data/config.is");

        try {
            BufferedReader fr = new BufferedReader(new FileReader(config));
            String file_line;
            String[] line_args;

            file_line = fr.readLine();
            line_args = file_line.split(" ");
            this.k_grid = Integer.parseInt(line_args[0]);
            this.m_grid = Integer.parseInt(line_args[1]);

            file_line = fr.readLine();
            line_args = file_line.split(" ");
            this.n_levels = Integer.parseInt(line_args[0]);

            while ((file_line = fr.readLine()) != null) {
                int curr_r;
                int curr_g;
                int curr_b;

                line_args = file_line.split(" ");
                curr_r = Integer.parseInt(line_args[0]);
                curr_g = Integer.parseInt(line_args[1]);
                curr_b = Integer.parseInt(line_args[2]);

                this.colors.add(new Color(curr_r, curr_g, curr_b));
            }

            this.is_color = this.colors.get(this.colors.size() - 1);
            this.colors.remove(this.colors.size() - 1);

        } catch (FileNotFoundException ex1) {
            this.view.showErrorDialog("config.is is not found!", "Error");
            ex1.printStackTrace();
        } catch (IOException ex2) {
            this.view.showErrorDialog("Cannot open the config.is!", "Error");
            ex2.printStackTrace();
        }
    }

    public double[] pixToCoord(int x, int y) {
        double[] coordinate = new double[2];

        coordinate[0] = (function.max - function.min) * (double)x / view.buffImage.getWidth() + function.min;
        coordinate[1] = (function.max - function.min) * (double)y / view.buffImage.getHeight() + function.min;

        return coordinate;
    }

    public double getFunctionValue(Point p) {
        double[] coordinates = pixToCoord(p.x, p.y);

        return function.circle(coordinates[0], coordinates[1]);
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

                view.showScrollableMessageDialog(aboutText, "About");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}