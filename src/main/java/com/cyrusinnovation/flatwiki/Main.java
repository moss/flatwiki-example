package com.cyrusinnovation.flatwiki;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class Main {
    private final File inputDirectory;
    private final OutputDirectory outputDirectory;

    public static void main(String... args) throws IOException {
        File inputDirectory = new File(args[0]);
        OutputDirectory outputDirectory = new OutputDirectory(args[1]);
        new Main(inputDirectory, outputDirectory).run();
    }

    public Main(File inputDirectory, OutputDirectory outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    private void run() throws IOException {
        for (String filename : inputDirectory.list()) {
            if (!filename.endsWith(".wiki")) continue;
            String pageName = filename.replaceFirst("\\.wiki$", "");
            WikiPage page = new WikiPage(pageName, readInputFile(filename));
            outputDirectory.writeFile(page.getOutputFilename(), page.asHtml());
        }
    }

    // TODO extract a delegate for inputDirectory business?
    private String readInputFile(String filename) throws IOException {
        return FileUtils.readFileToString(new File(inputDirectory, filename));
    }
}
