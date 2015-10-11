/**
 *
 */
package game;

/**
 * @author dkapusta
 */
public class CuriTeam extends Team {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param players
     */
    public CuriTeam(int players) {
        super(players);
        // TODO Auto-generated constructor stub
    }

    /**
     * @see game.Team#newPlayer()
     */
    protected Player newPlayer() {
        return new CuriPlayer(this);
    }

}
