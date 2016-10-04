package org.buildobjects.doctest;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.Writer;


public class DefaultOutputAdapter {

    private final PrintWriter out;

    public DefaultOutputAdapter(Writer writer) {
        out = new PrintWriter(writer);
    }

    public void methodBody(String source) {
        out.println("~~~ .java");
        out.println(unindent(source).trim());
        out.println("~~~");

    }

    public static String unindent(String source) {
        String[] lines = source.split("\n");
        int minIndent = Integer.MAX_VALUE;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().length() < 1)
              continue;
            int c = countLeadingSpaces(line);
            if (c < minIndent){
                minIndent = c;
            }
        }
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length()>minIndent)
            lines[i] = lines[i].substring(minIndent);
        }

        return StringUtils.join(lines, "\n");
    }

    private static int countLeadingSpaces(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) == ' '){
            i++;
        }
        return i;
    }

    public void methodPreamble(String comment) {
        out.println("\n" + unindent(comment) + "\n");

    }

    private String underline(String s, Character underlineCharacter) {
        return s + "\n" + StringUtils.repeat(underlineCharacter.toString(), s.length());
    }

    public void heading1(String s) {
        out.println(underline(s, '='));
    }

    public void testPreamble(String text) {
        out.println(text.trim());
    }
}
