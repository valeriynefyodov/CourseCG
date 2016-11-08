package FF_13312i_Nefedov_LE.model;

import java.util.*;
import java.awt.Point;

/**
 * The main model of the program - the line described by it's points
 */
public class Line {

    public List<Point> points;

    /**
     * Default class constructor
     * Initializes array of line's points
     */
    public Line(){
        points = new ArrayList<>();
    }
}
