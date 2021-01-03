package org.buildobjects.doctest.pretty;

import java.util.ArrayList;
import java.util.List;

public class SpanBuilder {
    final List<String> styles = new ArrayList<>();
    final List<String> classes = new ArrayList<>();

    final String content;

    public SpanBuilder(String content) {
        this.content = content;
    }

    public SpanBuilder withClass(String clazz) {
        this.classes.add(clazz);
        return this;
    }

    public SpanBuilder withStyle(String style) {
        this.styles.add(style);
        return this;
    }

    private String style() {
        if (styles.isEmpty()) return "";
        return " style=\"" + String.join(";", styles)+ "\"";
    }

    private String clazz() {
        if (classes.isEmpty()) return "";
        return " class=\"" + String.join(" ", classes)+ "\"";
    }
    public String build() {
        return "<span"+style()+clazz()+">" + content + "</span>";
    }

}
