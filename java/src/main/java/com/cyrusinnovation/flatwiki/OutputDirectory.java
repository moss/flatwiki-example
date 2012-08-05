package com.cyrusinnovation.flatwiki;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class OutputDirectory {
    private final File outputDirectory;

    public OutputDirectory(String outputDirectory) {
        this(new File(outputDirectory));
    }

    public OutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    // TODO introduce a parameter object for filename and content. Document?
    public void writeFile(String filename, String content) throws IOException {
        File path = new File(outputDirectory, filename);
        FileUtils.writeStringToFile(path, content);
    }
}