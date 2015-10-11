/* Created on 1990-12-23 */
package tool;

import pl.gdan.elsy.tool.Evaluable;
import pl.gdan.elsy.tool.Evaluator;


/**
 * This class is used to choose random element from array
 * proportionally to the value of the element
 *
 * @author Elser
 */
public class RR {
    public static Object pickWorst(Object[] arr, final Evaluator ev) {
        return pickBest(arr, new Evaluator() {
            public double evaluate(Object o) {
                return 1.0 / (ev.evaluate(o) + 0.001);
            }
        });
    }

    public static Evaluable pickWorst(Evaluable arr[]) {
        return (Evaluable) pickBest(arr, new Evaluator() {
            public double evaluate(Object o) {
                return 1.0 / (((Evaluable) o).evaluate() + 0.001);
            }
        });
    }

    public static Evaluable pickBest(Evaluable arr[]) {
        return (Evaluable) pickBest(arr, new Evaluator() {
            public double evaluate(Object o) {
                return ((Evaluable) o).evaluate();
            }
        });
    }

    public static void main(String[] args) {
        EvTest arr[] = new EvTest[20];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new EvTest();
        }
        for (int i = 0; i < 1000; i++) {
            ((EvTest) pickWorst(arr)).hitCount++;
        }
        System.out.println("Value of element : times hit");
        for (int i = 0; i < arr.length; i++) {
            System.out.println((int) arr[i].ret + " : " + (arr[i]).hitCount);
        }
    }

    public static Object pickRandom(Object[] arr) {
        return arr[Rand.i(arr.length)];
    }

    /**
     * @param teams
     * @param evaluator
     * @return
     */
    public static Object pickBest(Object[] arr, Evaluator evaluator) {
        return arr[pickBestIndex(arr, evaluator)];
    }

    public static int pickBestIndex(Object[] arr, Evaluator evaluator) {
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            double evaluate = evaluator.evaluate(arr[i]);
            if (evaluate < 0) {
                new Exception("arr[i].evaluate() < 0").printStackTrace();
            }
            sum += evaluate;
        }
        double randomPick = Rand.d(sum);
        sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum += evaluator.evaluate(arr[i]);
            if (randomPick <= sum) {
                return i;
            }
        }
        return Rand.i(arr.length);
    }

    public static int pickBestIndex(double[] arr) {
        double sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            double evaluate = arr[i];
            if (evaluate < 0) {
                new Exception("arr[i].evaluate() < 0").printStackTrace();
            }
            sum += evaluate;
        }
        double randomPick = Rand.d(sum);
        sum = 0.0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (randomPick <= sum) {
                return i;
            }
        }
        return Rand.i(arr.length);
    }
}

class EvTest implements Evaluable {
    double ret = Rand.d(100);
    int hitCount = 0;

    public double evaluate() {
        return ret;
    }
}
