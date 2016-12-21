package FF_13312i_Nefedov_WF.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Валерий on 19.12.2016.
 */
public class Spline {
    public List<Point> points;
    public List<Ring>  rings;

    public Spline() {

        this.points = new ArrayList();
        this.rings  = new ArrayList<>();
    }
}
