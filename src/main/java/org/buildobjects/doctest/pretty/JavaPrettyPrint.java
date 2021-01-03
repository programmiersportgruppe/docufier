package org.buildobjects.doctest.pretty;

import com.github.javaparser.JavaToken;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import org.buildobjects.doctest.Replacement;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String highlight(Node node) {
        Boolean currentlySkipping = false;
        StringWriter out = new StringWriter();
        TokenRange tokens = node.getTokenRange().get();


        for (JavaToken token : tokens) {
            if (!replacements.isEmpty() && !currentlySkipping) {
                if (replacements.get(0).getTokenRange().getBegin() == token) {
                    out.write(replacements.get(0).getReplacement());
                    currentlySkipping = true;
                    continue;
                }
            }
            if (currentlySkipping) {
                if (replacements.get(0).getTokenRange().getEnd() == token) {
                    currentlySkipping = false;
                    replacements.remove(0);
                    continue;
                }
                continue;
            }

            if (kindStyleMapping.containsKey(JavaToken.Kind.valueOf(token.getKind()))) {
                out.write(new SpanBuilder(token.asString()).withStyle(kindStyleMapping.get(JavaToken.Kind.valueOf(token.getKind()))).build());
            }
            if (categoryStyleMapping.containsKey(token.getCategory())) {
                out.write(new SpanBuilder(token.asString()).withStyle(categoryStyleMapping.get(token.getCategory())).build());

            } else {
                out.write(token.asString());
            }
        }
        return out.getBuffer().toString();
    }
}
