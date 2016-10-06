package org.buildobjects.doctest;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Docufier {

    private static final Pattern CLASS_TAG = Pattern.compile("\\[DOC file=(.+?)\\]");

    private MarkdownWriter markdownWriter;

    public Docufier(String sourcepath) throws IOException {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(sourcepath));

        JavaClass[] classes = builder.getClasses();
        processClasses(classes);

    }

    private void processClasses(JavaClass[] classes) {
        for (int i = 0; i < classes.length; i++) {
            JavaClass aClass = classes[i];
            String comment = aClass.getComment();
            if (comment != null) {
                Matcher matcher = CLASS_TAG.matcher(comment);
                if (matcher.find()) {
                    String filename = matcher.group(1);
                    try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8")) {
                        markdownWriter = new MarkdownWriter(writer);
                        markdownWriter.testPreamble(matcher.replaceAll(""));
                        processClass(aClass);
                    } catch (IOException e) {
                        throw new RuntimeException("Couldn't open output file '" + filename + "'");
                    }
                }
            }
        }
    }

    private void processClass(JavaClass aClass) {
        JavaMethod[] methods = aClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            JavaMethod method = methods[i];
            if (hasAnnotation("Test", method) || hasAnnotation("Ignore", method)){
                processTestMethod(method);
            }

        }
    }

    private void processTestMethod(JavaMethod method) {
        String comment = method.getComment();
        if (comment != null){
            markdownWriter.paragraph(TextUtils.unindent(comment));
        }

        String methodSource = "";

        boolean isHelperMethod = hasAnnotation("Ignore", method);

        if (isHelperMethod) {
            String declarationSignature = method.getDeclarationSignature(false);
            declarationSignature = declarationSignature.replaceAll("\\w*\\.",""); // simpleName!
            methodSource += declarationSignature + " {";
        }

        methodSource += method.getSourceCode();

        if (isHelperMethod) {
            methodSource += " }";
        }

        markdownWriter.javaCodeBlock(TextUtils.unindent(methodSource));
    }

    private boolean hasAnnotation(String name, JavaMethod method) {
        for (Annotation a : method.getAnnotations()){
            if (a.getType().getFullyQualifiedName().endsWith(name)){
                return true;
            }

        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        new Docufier(args[0]);
    }


}
