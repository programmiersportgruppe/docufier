package org.buildobjects.doctest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class MarkdownWriter implements AutoCloseable {

    private final PrintWriter out;

    public MarkdownWriter(Writer writer) {
        out = new PrintWriter(writer);
    }

    public void javaCodeBlock(String source) {
        out.println("<pre>");
        out.println(source.trim());
        out.println("</pre>");
    }

    public void paragraph(String comment) {
        out.println("\n" + comment + "\n");
    }

    public void testPreamble(String text) {
        out.println(text.trim());
    }

    @Override
    public void close() {
        // nada
    }
}
