package org.buildobjects.doctest.guineapig;

import org.junit.Test;

import static org.junit.Assert.*;

/** [DOC file=README.md]
 The Cool-Library
 ================
 */

public class CoolTestSuite {

    /**
     ### First step

     We can easily create a new cool using a "constructor" and get
     a really cool cool.
     */
    @Test
    public void ensureANewCoolIsNotHot() {
        Cool cool = new Cool();
        assertTrue(cool.getTemperature() < 5);
    }

    /**
     [NO-DOC]
     Sometimes we write a test that is not really instructional.
     */
    @Test
    public void ensureEqualityWorks() {
        Cool oneCool = new Cool();
        Cool anotherCool = new Cool();

        assertEquals(oneCool, anotherCool);
    }

}
