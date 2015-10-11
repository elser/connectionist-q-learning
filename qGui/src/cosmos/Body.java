/* Created on 1990-12-21 */
package cosmos;

import java.io.Serializable;

/**
 * Class that represents one 3D object that doubles in 3D world.
 *
 * @author Elser
 */
public class Body extends PosXY implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    private static int idCounter;

    public Body() {
        super();
        this.id = idCounter++;
    }
}
