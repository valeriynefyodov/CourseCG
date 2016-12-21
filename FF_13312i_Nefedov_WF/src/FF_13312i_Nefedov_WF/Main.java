package FF_13312i_Nefedov_WF;

import FF_13312i_Nefedov_WF.controller.Controller;
import FF_13312i_Nefedov_WF.controller.MatrixMath;
import FF_13312i_Nefedov_WF.model.Matrix;

/**
 * The main class of an application
 */
public class Main {

    /**
     * Initializes the app: creates controller and main frame of the app
     */
    private static void initApp(){
        Controller controller = new Controller();
        controller.mainView.setVisible(true);

//        Matrix m1 = new Matrix(new double[] {1, 2, 3, 4});
//        m1.printMatrix();
//
//        double[] qwe = m1.getColumn(0);
//
//        for(int i = 0; i < 4; i++) {
//            System.out.print(qwe[i]);
//            System.out.print(" ");
//        }
//        Matrix m2 = new Matrix(new double[][] {{2, 5, 2, 5}, {2, 5, 2, 5}});
//
//        m2.printMatrix();
//        System.out.println();
//        m1.printMatrix();
//        System.out.println();
////
////        double[][] qwe = m2.getElements();
////
////        for(int i = 0; i < qwe.length; i++) {
////            for (int j = 0; j < qwe[0].length; j++) {
////                System.out.print(qwe[i][j]);
////                System.out.print(" ");
////            }
////
////            System.out.println();
////        }
//
//
//        Matrix m3 = MatrixMath.multiplyMatrix(m2, m1);
//        m3.printMatrix();
    }

    /**
     * Main function
     * Runs the app
     * @param args - main function's arguments
     */
    public static void main(String[] args){
        initApp();
    }

}
