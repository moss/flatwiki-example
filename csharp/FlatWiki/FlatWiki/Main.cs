using System.Collections.Generic;
using System.IO;

namespace FlatWiki
{


public class Main {
    private readonly string inputDirectory;
    private readonly OutputDirectory outputDirectory;

	
    public static void main(params string[] args)  {
        new Main(args[0], new OutputDirectory(args[1])).run();
    }

    public Main(string inputDirectory, OutputDirectory outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    private void run() {
        foreach (var filename in listInputFiles())
        {
        	var pageName = filename.Substring(0, filename.Length -".wiki".Length);
            WikiPage page = new WikiPage(pageName, readInputFile(filename), modifiedTime(filename));
            outputDirectory.writeFile(page.getOutputFilename(), page.asHtml());
        }
    }

    // TODO extract a delegate for inputDirectory business?
    private IEnumerable<string> listInputFiles() {
        return Directory.EnumerateFiles(inputDirectory, "*.wiki");
    }

    private long modifiedTime(string filename) {
        return File.GetLastWriteTime(inputFile(filename)).Ticks;
    }

    private string readInputFile(string filename)  {
        return File.ReadAllText(inputFile(filename));
    }

    private string inputFile(string filename) {
        return inputDirectory + '\\' + filename;
    }
}

}
