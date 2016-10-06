package org.buildobjects.doctest;

import java.io.PrintWriter;
import java.io.Writer;


public class MarkdownWriter {

    private final PrintWriter out;

    public MarkdownWriter(Writer writer) {
        out = new PrintWriter(writer);
    }

    public void javaCodeBlock(String source) {
        out.println("~~~ .java");
        out.println(source.trim());
        out.println("~~~");
    }

    public void paragraph(String comment) {
        out.println("\n" + comment + "\n");
    }

    public void testPreamble(String text) {
        out.println(text.trim());
    }

}
