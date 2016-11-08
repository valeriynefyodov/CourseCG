package FF_13312i_Nefedov_IS.model;

public class Function {
    public int min;
    public int max;

    public Function(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Calculates a circle function
     * @param x - x variable in integer format
     * @param y - y variable in integer format
     * @return - function value in (x,y)
     */
    public static double circle(int x, int y) {
        return Math.pow((double) x, 2) + Math.pow((double) y, 2);
    }

    /**
     * Calculates a circle function
     * @param x - x variable in double format
     * @param y - y variable in double format
     * @return - function value in (x,y)
     */
    public static double circle(double x, double y) {
        return Math.pow(x, 2) + Math.pow(y, 2);
    }
}
