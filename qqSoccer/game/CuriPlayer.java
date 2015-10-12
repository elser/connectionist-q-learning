/**
 *
 */
package game;

import module.brain.perception.MyPerception;
import pl.gdan.elsy.qconf.ErrorBackpropagationNN;
import pl.gdan.elsy.qconf.curiosity.CuriousBrain;
import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

/**
 * @author dkapusta
 */
public class CuriPlayer extends Player {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private MyPerception perception;
    private CuriousBrain brain;

    public CuriPlayer(Team team) {
        super(team);
        perception = new MyPerception(this);
        perception.setAddRandomInput(true);
        brain = new CuriousBrain(perception, vehicle.getActions(), new int[]{20}, new int[]{});
        brain.setAlpha(0.2);
        brain.setGamma(0.99);
        brain.setLambda(0.8);
        brain.setRandActions(1);
        ErrorBackpropagationNN predictionNN = brain.getCuriosity().getNn();
        predictionNN.setAlpha(0.5);
        predictionNN.setMomentum(0.7);
        CuriousPlayerPerception.setRMin(0.014);
        CuriousPlayerPerception.setRMax(0.325);
        CuriousPlayerPerception.setRSlope(650);
        CuriousPlayerPerception.setPMin(0.048);
        CuriousPlayerPerception.setPMax(0.370);
        CuriousPlayerPerception.setPSlope(30);
    }

    /**
     * @see game.Player#live()
     */
    public void live() {
        super.live();
        brain.count();
    }

    @Override
    public void reset() {
        super.reset();
        brain.reset();
    }

    /**
     * @return the brain
     */
    public CuriousBrain getBrain() {
        return brain;
    }

    /**
     * @param brain the brain to set
     */
    public void setBrain(CuriousBrain brain) {
        this.brain = brain;
    }

    public MyPerception getPerception() {
        return perception;
    }

    public void setPerception(MyPerception perception) {
        this.perception = perception;
    }

}
