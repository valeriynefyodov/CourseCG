package FF_13312i_Nefedov_WF.controller;

import FF_13312i_Nefedov_WF.model.Matrix;

public class MatrixMath {

    public static Matrix multiplyMatrix(Matrix matrix1, Matrix matrix2){
        double[][] resultMatrixElements = new double[matrix1.rows][matrix2.cols];
        double[][] matrix1Elements = matrix1.getElements();
        double[][] matrix2Elements = matrix2.getElements();

        if (matrix1.cols != matrix2.rows)
            throw new IllegalArgumentException("The number of colums in first matrix not equals the number of columns in second matrix");

        for (int i = 0; i < matrix1.rows; i++) {
            for (int j = 0; j < matrix2.cols; j++) {
                double cell = 0;

                for (int m = 0; m < matrix1.cols; m++) {
                    cell += matrix1Elements[i][m] * matrix2Elements[m][j];
                }

                resultMatrixElements[i][j] = cell;
            }
        }

        return new Matrix(resultMatrixElements);
    }

    public static void multiplyByScalar(Matrix matrix, double multiplier) {
        double[][] matrixElements = matrix.getElements();

        for (int j = 0; j < matrix.rows; j++)
            for (int i = 0; i < matrix.cols; i++)
                matrixElements[i][j] *= multiplier;

        matrix.setElements(matrixElements);
    }
}
