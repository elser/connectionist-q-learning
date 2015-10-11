package tool;

import org.junit.Test;

/**
 * TODO: Document this file here
 */
public class MatTest {

    @Test
    public void testSigmoidBi() throws Exception {
        for (int i = -20; i < 20; i++) {
            double d = 0.01 * i;
            System.out.println(d + " " + Mat.sigmoidBi(d));
        }
    }
}