package com.cyrusinnovation.flatwiki;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.*;

public class FlatWiki {
    private final File inputDirectory;
    private final OutputDirectory outputDirectory;

    public static void main(String... args) throws IOException {
        File inputDirectory = new File(args[0]);
        OutputDirectory outputDirectory = new OutputDirectory(args[1]);
        new FlatWiki(inputDirectory, outputDirectory).run();
    }

    public FlatWiki(File inputDirectory, OutputDirectory outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    private void run() throws IOException {
        for (String filename : listInputFiles()) {
            String pageName = filename.replaceFirst("\\.wiki$", "");
            WikiPage page = new WikiPage(pageName, readInputFile(filename), modifiedTime(filename));
            outputDirectory.writeFile(page.getOutputFilename(), page.asHtml());
        }
    }

    // TODO extract a delegate for inputDirectory business?
    private String[] listInputFiles() {
        return inputDirectory.list(new SuffixFileFilter(".wiki"));
    }

    private long modifiedTime(String filename) {
        return inputFile(filename).lastModified();
    }

    private String readInputFile(String filename) throws IOException {
        return FileUtils.readFileToString(inputFile(filename));
    }

    private File inputFile(String filename) {
        return new File(inputDirectory, filename);
    }
}
