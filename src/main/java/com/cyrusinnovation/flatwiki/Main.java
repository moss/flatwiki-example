package com.cyrusinnovation.flatwiki;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

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
        Map<String, String> files = new HashMap<String, String>();
        for (String filename : inputDirectory.list()) {
            if (!filename.endsWith(".wiki")) continue;
            files.put(filename, FileUtils.readFileToString(new File(inputDirectory, filename)));
        }
        for (Map.Entry<String, String> nameToContent : files.entrySet()) {
            String filename = nameToContent.getKey().replace(".wiki", ".html");
            String content = nameToContent.getValue();
            FileUtils.writeStringToFile(new File(outputDirectory, filename), content);
        }
    }
}
