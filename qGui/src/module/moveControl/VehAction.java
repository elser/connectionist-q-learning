package module.moveControl;

import pl.gdan.elsy.qconf.Action;

import java.io.Serializable;

public abstract class VehAction extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean doKick;
    public double kickingHorizForce;
    public double kickingVertForce;

    public VehAction kicker(double force) {
        doKick = true;
        kickingHorizForce = force;
        kickingVertForce = force / 2;
        return this;
    }
}
