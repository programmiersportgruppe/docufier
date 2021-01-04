package org.buildobjects.doctest.pretty;

import com.github.javaparser.JavaToken;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import org.buildobjects.doctest.Replacement;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.buildobjects.doctest.TextUtils.unindent;

public class JavaPrettyPrint {


    final static Map<JavaToken.Kind, String> kindStyleMapping = new HashMap() {{
        put(JavaToken.Kind.STRING_LITERAL, "color:green");
    }};

    final static Map<JavaToken.Category, String> categoryStyleMapping = new HashMap() {{
        put(JavaToken.Category.LITERAL, "color:blue");
        put(JavaToken.Category.COMMENT, "color:gray");
        put(JavaToken.Category.KEYWORD, "color:red");
    }};
    private final List<Replacement> replacements;

    public JavaPrettyPrint(List<Replacement> replacements) {
        this.replacements = replacements;
    }


    private String stripOuterCurlies(String unindent) {

        if (unindent.startsWith("{\n")) {
            unindent = unindent.substring(2);
        }
        if (unindent.endsWith("}")) {
            unindent = unindent.substring(0, unindent.length() - 1);
        }
        return unindent;
    }


    public String highlight(Node node) {
        List<Replacement> localReplacements = new ArrayList<>(replacements);
        int tokenCount = 0;

        Boolean currentlySkipping = false;

        StringWriter out = new StringWriter();
        TokenRange tokens = node.getTokenRange().get();


        for (JavaToken token : tokens) {
            final JavaToken.Kind tokenKind = JavaToken.Kind.valueOf(token.getKind());
            if (!localReplacements.isEmpty() && !currentlySkipping) {
                if (localReplacements.get(0).getTokenRange().getBegin() == token) {
                    out.write(magicToken(tokenCount));
                    tokenCount ++;
                    currentlySkipping = true;
                    continue;
                }
            }

            if (currentlySkipping) {
                if (localReplacements.get(0).getTokenRange().getEnd() == token) {
                    currentlySkipping = false;
                    localReplacements.remove(0);
                    continue;
                }
                continue;
            }

            if (kindStyleMapping.containsKey(tokenKind)) {
                out.write(new SpanBuilder(token.asString()).withStyle(kindStyleMapping.get(tokenKind)).build());
            } else if (categoryStyleMapping.containsKey(token.getCategory())) {
                out.write(new SpanBuilder(token.asString()).withStyle(categoryStyleMapping.get(token.getCategory())).build());

            } else {
                out.write(token.getText());
            }
        }

        String raw = unindent(stripOuterCurlies(out.getBuffer().toString()));
        for (int i = 0; i < tokenCount; i++) {
            raw = raw.replace(magicToken(i), replacements.get(i).getReplacement());
        }

        return raw;
    }

    private String magicToken(int tokenCount) {
        return "__TOKEN__" + tokenCount;
    }
}
