package cosmos;

import java.io.Serializable;

/*
 * Created on 1990-12-21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Elser
 *         <p>
 *         Describes either one sector of cosmos or one object
 */
public class FieldDimensions implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    public static final double INNER_X=65, INNER_Y=86;
    public static final double OUTER_X = 75, OUTER_Y = 100;
    public static final double MAX_DISTANCE = Math.sqrt(OUTER_X * OUTER_X + OUTER_Y * OUTER_Y);
    public static final double GOAL_SIZE = INNER_X/2;
}
