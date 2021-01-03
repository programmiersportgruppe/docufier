package org.buildobjects.doctest;

import com.github.javaparser.TokenRange;

public class Replacement {
    final TokenRange tokenRange;
    final String replacement;

    public Replacement(TokenRange tokenRange, String replacement) {
        this.tokenRange = tokenRange;
        this.replacement = replacement;
    }

    public TokenRange getTokenRange() {
        return tokenRange;
    }

    public String getReplacement() {
        return replacement;
    }
}
