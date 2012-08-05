using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace FlatWiki
{


public class FlatWiki {
    private readonly string inputDirectory;
    private readonly OutputDirectory outputDirectory;

	
    public static void Main(params string[] args)  {
        new FlatWiki(args[0], new OutputDirectory(args[1])).Run();
    }

    public FlatWiki(string inputDirectory, OutputDirectory outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    private void Run() {
        foreach (var filename in ListInputFiles())
        {
        	var pageName = filename.Substring(0, filename.Length -".wiki".Length);
            WikiPage page = new WikiPage(pageName, ReadInputFile(filename), ModifiedTime(filename));
            outputDirectory.WriteFile(page.GetOutputFilename(), page.AsHtml());
        }
    }

    // TODO extract a delegate for inputDirectory business?
    private IEnumerable<string> ListInputFiles() {
			return Directory.EnumerateFiles(inputDirectory, "*.wiki").Select(f => new FileInfo(f).Name);
    }

    private long ModifiedTime(string filename) {
        return File.GetLastWriteTime(InputFile(filename)).Ticks;
    }

    private string ReadInputFile(string filename)  {
        return File.ReadAllText(InputFile(filename));
    }

    private string InputFile(string filename) {
        return inputDirectory + '\\' + filename;
    }
}

}
