package org.buildobjects.doctest.guineapig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.buildobjects.doctest.runtime.junit4.DocufierRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

/**
 * [DOC file=README.md]
 * The Cool-Library
 * ================
 */
public class CoolTestSuite {

    @Rule
    public DocufierRule docufy = new DocufierRule();

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
     * [NO-DOC]
     * Sometimes we write a test that is not really instructional.
     */
    @Test
    public void ensureEqualityWorks() {
        Cool oneCool = new Cool();
        Cool anotherCool = new Cool();

        assertEquals(oneCool, anotherCool);
    }

    /**
     * Canonical definition of hotness.
     */
    private boolean isHot(int temperature) {
        return temperature >= 5;
    }

    /**
     * New cools are not canonically hot.
     */
    @Test
    public void ensureANewCoolIsNotCanonicallyHot() {
        Cool cool = new Cool();
        assertTrue(!isHot(cool.getTemperature()));
    }

    /**
     * We also want to be able to format some json:
     */
    @Test
    public void ensureWeCanPrettyPrintSomeOutput() {
        String someJson = docufy.tap("{\n" +
            "  \"name\": \"dagobert\",\n" +
            "  \"nephews\": [\"tick\", \"trick\", \"track\"]\n" +
            "}", this::prettyJson);

        String anotherJson = StringUtils.trim(docufy.tap("[\"A\", \"B\", \"C\"]", this::prettyJson));

        // Or a comment?
        assertNotEquals(someJson, anotherJson);
    }

    /** [NO-DOC] */
    private String prettyJson(String t) {
        try {
            return new ObjectMapper()
                .readTree(new ByteArrayInputStream(t.getBytes(UTF_8)))
                .toPrettyString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
