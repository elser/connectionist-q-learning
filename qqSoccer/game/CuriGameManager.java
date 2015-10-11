/**
 *
 */
package game;

/**
 * @author dkapusta
 */
public class CuriGameManager extends GameManager {
    private static final long serialVersionUID = 1L;
    public static final int PLAYERS_NO = 4;

    public CuriGameManager() {
        super();
        start();
    }

    public Team newTeam() {
        return new CuriTeam(PLAYERS_NO);
    }

    public int getTeamsNo() {
        return 2;
    }

}
