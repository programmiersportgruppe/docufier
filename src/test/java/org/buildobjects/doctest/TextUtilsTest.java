package org.buildobjects.doctest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextUtilsTest {

    @Test
    public void testUnindent(){
        assertEquals("asdas", TextUtils.unindent("  asdas"));
        assertEquals("a\n b", TextUtils.unindent(" a\n  b"));
        assertEquals(" a\n b\nc", TextUtils.unindent("    a\n    b\n   c"));
        assertEquals(" a\n\nc", TextUtils.unindent("    a\n\n   c"));
    }

}
