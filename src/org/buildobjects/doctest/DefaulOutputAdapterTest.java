package org.buildobjects.doctest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaulOutputAdapterTest {

    @Test
    public void testUnindent(){
        assertEquals("asdas",DefaultOutputAdapter.unindent("  asdas"));
        assertEquals("a\n b",DefaultOutputAdapter.unindent(" a\n  b"));
        assertEquals(" a\n b\nc",DefaultOutputAdapter.unindent("    a\n    b\n   c"));
        assertEquals(" a\n\nc",DefaultOutputAdapter.unindent("    a\n\n   c"));

    }

}
