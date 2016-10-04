package org.buildobjects.doctest;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;


public class DocWriter {
    private final DefaultOutputAdapter outputAdapter;

    public DocWriter(String sourcepath, DefaultOutputAdapter outputAdapter) throws IOException {
        this.outputAdapter = outputAdapter;
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(sourcepath));

        JavaClass[] classes = builder.getClasses();
        processClasses(classes);

    }

    private void processClasses(JavaClass[] classes) {
        for (int i = 0; i < classes.length; i++) {
            JavaClass aClass = classes[i];
            String comment = aClass.getComment();
            if(comment != null && comment.contains("[DOC]")){
                outputAdapter.testPreamble(comment.replace("[DOC]", ""));
                processClass(aClass);

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
            outputAdapter.methodPreamble(comment);
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

        outputAdapter.methodBody(methodSource);
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
        FileWriter writer = new FileWriter("docs.md");
        new DocWriter(args[0], new DefaultOutputAdapter(writer));
        writer.close();

    }


}
