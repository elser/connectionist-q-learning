package pl.gdan.elsy.tool;

/**
 * TODO: Document this class / interface here
 *
 * @since v7.0
 */
public class Average {
    private final int over;
    private Double cumulativeValue = null;

    public Average(int over) {
        this.over = over;
    }

    public void put(double d) {
        if (cumulativeValue == null) {
            cumulativeValue = d;
        } else {
            cumulativeValue = (cumulativeValue * (over - 1) + d) / over;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(cumulativeValue);
    }

    public Double get() {
        return cumulativeValue;
    }
}
