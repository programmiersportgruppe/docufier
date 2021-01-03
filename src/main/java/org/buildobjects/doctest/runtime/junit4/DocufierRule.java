package org.buildobjects.doctest.runtime.junit4;

import org.apache.commons.io.FileUtils;
import org.buildobjects.doctest.runtime.MultiCounter;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DocufierRule implements TestRule {

    private Description description;
    private MultiCounter counters = new MultiCounter();

    @Override
    public Statement apply(Statement base, Description description) {
        this.description = description;
        return base;
    }

    public <T> T tap(T t, Function<T, String> formatter) {
        try {
            final File file = new File("target/docufier-snippets/");
            String baseName = description.getClassName() + "-" + description.getMethodName();
            String fullBaseName = baseName + "-" + counters.nextNumber(baseName);

            FileUtils.forceMkdir(file);

            FileUtils.write(new File(file, fullBaseName), formatter.apply(t), UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return t;
    }
}
