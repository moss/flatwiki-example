package com.cyrusinnovation.flatwiki;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class Main {
    private final File inputDirectory;
    private final File outputDirectory;

    public static void main(String... args) throws IOException {
        File inputDirectory = new File(args[0]);
        File outputDirectory = new File(args[1]);
        new Main(inputDirectory, outputDirectory).run();
    }

    public Main(File inputDirectory, File outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    private void run() throws IOException {
        for (String filename : inputDirectory.list()) {
            if (!filename.endsWith(".wiki")) continue;
            String content = FileUtils.readFileToString(new File(inputDirectory, filename));
            String outputFilename = filename.replace(".wiki", ".html");
            FileUtils.writeStringToFile(new File(outputDirectory, outputFilename), content);
        }
    }
}
