package org.buildobjects.doctest;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.io.FileUtils;
import org.buildobjects.doctest.pretty.JavaPrettyPrint;
import org.buildobjects.doctest.runtime.MultiCounter;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.buildobjects.doctest.TextUtils.unindent;


public class Docufier {

    private static final Pattern CLASS_TAG = Pattern.compile("\\[DOC file=(.+?)\\]");

    private final File targetDirectory;

    private final MultiCounter runtimeSnippetCounter = new MultiCounter();

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

    private void processClasses(JavaClass[] classes) throws IOException {
        for (JavaClass aClass : classes) {
            String comment = aClass.getComment();
            if (comment != null) {
                Matcher matcher = CLASS_TAG.matcher(comment);
                if (matcher.find()) {
                    String filename = matcher.group(1);
                    final URL classUrl = aClass.getSource().getURL();

                    CompilationUnit cu = StaticJavaParser.parse(FileUtils.readFileToString(new File(classUrl.getFile()), StandardCharsets.UTF_8));
                    ClassOrInterfaceDeclaration pC = cu.getClassByName(aClass.getName()).get();

                    try (
                        Writer writer = new OutputStreamWriter(new FileOutputStream(new File(targetDirectory, filename)), "UTF-8");
                        MarkdownWriter markdownWriter = new MarkdownWriter(writer)
                    ) {
                        markdownWriter.testPreamble(matcher.replaceAll(""));
                        processClass(markdownWriter, aClass, pC);
                    } catch (IOException e) {
                        throw new RuntimeException("Couldn't open output file '" + filename + "'");
                    }
                }
            }
        }
    }

    private void processClass(MarkdownWriter markdownWriter, JavaClass aClass, ClassOrInterfaceDeclaration pC) {
        for (JavaMethod method : aClass.getMethods()) {
            MethodDeclaration parsedMethod = pC.getMethodsByName(method.getName()).get(0);
            processTestMethod(markdownWriter, method, parsedMethod);
        }
    }

    private void processTestMethod(MarkdownWriter markdownWriter, JavaMethod method, MethodDeclaration parsedMethod) {
        String comment = method.getComment();

        if (comment != null && comment.contains("[NO-DOC]"))
            return;

        if (comment != null)
            markdownWriter.paragraph(unindent(comment));

        String methodSource;
        if (hasAnnotation("Test", method)) {
            ExpressionVisitor visitor = new ExpressionVisitor(markdownWriter);
            visitor.visit(parsedMethod, null);

            final BlockStmt blockStmt = parsedMethod.getBody().get();

            final JavaPrettyPrint javaPrettyPrint = new JavaPrettyPrint(visitor.getReplacements());
            methodSource = unindent(blockStmt
                .getStatements()
                .stream()
                .map(node -> {
                    return javaPrettyPrint.highlight(node);
                })
                .collect(Collectors.joining("\n"))
            );
        } else {
            String simplifiedDeclaration = method.getDeclarationSignature(false).replaceAll("\\w*\\.", "");
            methodSource = simplifiedDeclaration + " {" + TextUtils.reindent(method.getSourceCode(), "    ") + "}";
        }

        /*
        todo: fiddle output
        for (String s : replacements.keySet()) {
                output = output.replace(s, replacements.get(s));
            }
        */
        markdownWriter.javaCodeBlock(methodSource);
    }

    private class ExpressionVisitor extends VoidVisitorAdapter<Void> {

        private List<Replacement> replacements = new ArrayList<>();

        public ExpressionVisitor(MarkdownWriter markdownWriter) {
        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {
            if (n.getName().getIdentifier().equals("tap")) {

                MethodDeclaration method = getParent(n, MethodDeclaration.class);

                ClassOrInterfaceDeclaration clazz = getParent(n, ClassOrInterfaceDeclaration.class);

                final String methodName = method.asMethodDeclaration().getNameAsString();
                final String className = clazz.getFullyQualifiedName().get();
                String baseName = className + "-" + methodName;


                final String fileName = baseName + "-" + runtimeSnippetCounter.nextNumber(baseName);

                final File path = new File("target/docufier-snippets/" + fileName);
                try {
                    replacements.add(new Replacement(n.getTokenRange().get(), "«" + FileUtils.readFileToString(path, StandardCharsets.UTF_8) + "»"));
                } catch (IOException e) {
                    replacements.add(new Replacement(n.getTokenRange().get(), "Failed to read replacement from '" + path.getAbsolutePath() + "'"));
                }
            }
        }


        private <T extends Node> T getParent(Node n, Class<T> methodDeclarationClass) {
            Node parent = n.getParentNode().get();
            while (!(methodDeclarationClass.isAssignableFrom(parent.getClass()))) {
                parent = parent.getParentNode().get();
            }
            return (T)parent;
        }

        public List<Replacement> getReplacements() {
            return replacements;
        }
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
