package org.buildobjects.doctest;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {


    public static String unindent(String source) {
        return reindent(source, "");
    }

    public static String removeIndentation(String source, int indentation) {
        String[] lines = source.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int j = 0;
            while( j < indentation && j < line.length() && line.charAt(j) == ' '){
                j++;
            }
            lines[i] = line.substring(j);
        }
        return StringUtils.join(lines, "\n");
    }


    public static String reindent(String source, String prefix) {
        String[] lines = source.split("\n");

        int minIndent = minindent(lines);

        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].length() <= minIndent ? "" : prefix + lines[i].substring(minIndent);
        }

        return StringUtils.join(lines, "\n");
    }


    public static int minindent(String source) {
        String[] lines = source.split("\n");
        return minindent(lines);
    }
    private static int minindent(String[] lines) {
        int minIndent = Integer.MAX_VALUE;
        for (String line : lines) {
            final int lineIndent = countLeadingSpaces(line);
            if (lineIndent < line.length() && lineIndent < minIndent)
                minIndent = lineIndent;
        }
        return minIndent;
    }

    private static int countLeadingSpaces(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

    public String underline(String s, Character underlineCharacter) {
        return s + "\n" + StringUtils.repeat(underlineCharacter.toString(), s.length());
    }



}
