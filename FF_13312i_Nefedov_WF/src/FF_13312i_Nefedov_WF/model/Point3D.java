package FF_13312i_Nefedov_WF.model;

/**
 * Created by Валерий on 20.12.2016.
 */
public class Point3D {
    public double x;
    public double y;
    public double z;

    public Point3D(int x, int y, int z) {
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
    }

    public Point3D(double x, double y, double z) {
        this.x = x + 0.5;
        this.y = y + 0.5;
        this.z = z + 0.5;
    }
}
