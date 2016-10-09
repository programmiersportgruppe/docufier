package org.buildobjects.doctest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DocufierTest {

    @Test
    public void shouldRenderDocumentation() throws IOException {
        new Docufier("src/test/java", "target/doc");
        assertEquals(content("src/test/resources/expected-README.md"), content("target/doc/README.md"));
    }

    private String content(String s) throws IOException {
        return FileUtils.readFileToString(new File(s), "UTF-8");
    }
}
