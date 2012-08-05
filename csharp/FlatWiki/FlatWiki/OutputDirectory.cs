using System;
using System.IO;

namespace FlatWiki
{
	public class OutputDirectory
	{
		private readonly string outputDirectory;

		public OutputDirectory(string outputDirectory)
		{
			this.outputDirectory = outputDirectory;
		}

		// TODO introduce a parameter object for filename and content. Document?
		public void WriteFile(String filename, String content)
		{
			var path = outputDirectory + "\\" + filename;
			File.WriteAllText(path, content);
		}
	}
}