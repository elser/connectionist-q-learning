package pl.gdan.elsy.tool;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertTrue;

/**
 * TODO: Document this file here
 */
public class RandTest {

    @Test
    public void testSuccessWithPercent() throws Exception {
        int hits = 0;
        Rand.random = new Random(123);
        final int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            if (Rand.successWithPercent(10)) {
                hits++;
            }
        }
        System.out.println(hits);
        assertTrue(hits > 900);
        assertTrue(hits < 1100);
    }
}