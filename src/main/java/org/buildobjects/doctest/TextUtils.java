package org.buildobjects.doctest;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {


    public static String unindent(String source) {
        return reindent(source, "");
    }

    public static String reindent(String source, String prefix) {
        String[] lines = source.split("\n");
        int minIndent = Integer.MAX_VALUE;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (line.trim().length() < 1) {
                lines[i] = "";
                continue;
            }

            int c = countLeadingSpaces(line);
            if (c < minIndent){
                minIndent = c;
            }
        }
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > minIndent)
                lines[i] = prefix + lines[i].substring(minIndent);
        }

        return org.apache.commons.lang3.StringUtils.join(lines, "\n");
    }

    private static int countLeadingSpaces(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) == ' '){
            i++;
        }
        return i;
    }

    public String underline(String s, Character underlineCharacter) {
        return s + "\n" + StringUtils.repeat(underlineCharacter.toString(), s.length());
    }

}
