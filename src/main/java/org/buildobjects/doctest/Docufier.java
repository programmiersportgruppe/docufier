package org.buildobjects.doctest;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.buildobjects.doctest.TextUtils.unindent;


public class Docufier {

    private static final Pattern CLASS_TAG = Pattern.compile("\\[DOC file=(.+?)\\]");

    private final File targetDirectory;

    public Docufier(String sourcepath, String targetDirectory) throws IOException {
        this(new File(sourcepath), new File(targetDirectory));
    }

    public Docufier(File sourcepath, File targetDirectory) throws IOException {
        FileUtils.forceMkdir(targetDirectory);
        this.targetDirectory = targetDirectory;

        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(sourcepath);

        processClasses(builder.getClasses());
    }

    private void processClasses(JavaClass[] classes) {
        for (JavaClass aClass : classes) {
            String comment = aClass.getComment();
            if (comment != null) {
                Matcher matcher = CLASS_TAG.matcher(comment);
                if (matcher.find()) {
                    String filename = matcher.group(1);
                    try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(targetDirectory, filename)), "UTF-8")) {
                        MarkdownWriter markdownWriter = new MarkdownWriter(writer);
                        markdownWriter.testPreamble(matcher.replaceAll(""));
                        processClass(markdownWriter, aClass);
                    } catch (IOException e) {
                        throw new RuntimeException("Couldn't open output file '" + filename + "'");
                    }
                }
            }
        }
    }

    private void processClass(MarkdownWriter markdownWriter, JavaClass aClass) {
        for (JavaMethod method : aClass.getMethods()) {
            processTestMethod(markdownWriter, method);
        }
    }

    private void processTestMethod(MarkdownWriter markdownWriter, JavaMethod method) {
        String comment = method.getComment();

        if (comment != null && comment.contains("[NO-DOC]"))
            return;

        if (comment != null)
            markdownWriter.paragraph(unindent(comment));

        String methodSource;
        if (hasAnnotation("Test", method)) {
            methodSource = unindent(method.getSourceCode());
        } else {
            String simplifiedDeclaration = method.getDeclarationSignature(false).replaceAll("\\w*\\.", "");
            methodSource = simplifiedDeclaration + " {" + TextUtils.reindent(method.getSourceCode(), "    ") + "}";
        }

        markdownWriter.javaCodeBlock(methodSource);
    }

    private boolean hasAnnotation(String name, JavaMethod method) {
        for (Annotation a : method.getAnnotations()) {
            if (a.getType().getFullyQualifiedName().endsWith(name)) {
                return true;
            }

        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        new Docufier(args[0], args[1]);
    }

}
