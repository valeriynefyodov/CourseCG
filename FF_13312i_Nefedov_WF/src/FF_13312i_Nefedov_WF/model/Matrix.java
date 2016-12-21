package FF_13312i_Nefedov_WF.model;

/**
 * Created by Валерий on 21.12.2016.
 */
public class Matrix {
    public double[][] elements;
    public int        rows;
    public int        cols;

    public Matrix(int cols, int rows) {
        this.elements = new double[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                elements[i][j] = 0;
            }
        }

        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(double[][] elements) {
        this.setElements(elements);
    }

    public Matrix(double[] vector) {
        this(1, vector.length);

        for (int i = 0; i < vector.length; i++)
            this.elements[0] = vector;
    }

    public double[][] getElements() {
        double[][] elements = new double[rows][cols];

        for (int j = 0; j < rows; j++)
            for (int i = 0; i < cols; i++)
                elements[j][i] = this.elements[i][j];

        return elements;
    }

    public void setElements(double[][] elements) {
        this.cols = elements[0].length;
        this.rows = elements.length;

        this.elements = new double[cols][rows];

        for (int j = 0; j < rows; j++)
            for (int i = 0; i < cols; i++)
                this.elements[i][j] = elements[j][i];
    }

    public double[] getColumn(int index) {
        return elements[index];
    }

    public void printMatrix() {
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                System.out.print(this.elements[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }
}
