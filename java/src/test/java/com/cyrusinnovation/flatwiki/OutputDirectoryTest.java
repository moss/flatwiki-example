package com.cyrusinnovation.flatwiki;

import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.*;

public class OutputDirectoryTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private OutputDirectory outputDirectory;

    @Before public void setUp() {
        outputDirectory = new OutputDirectory(folder.getRoot());
    }

    @Test public void writesFilesToFolder() throws IOException {
        outputDirectory.writeFile("somefile.txt", "some text");
        String content = readFileToString(new File(folder.getRoot(), "somefile.txt"));
        assertEquals("some text", content);
    }
}
