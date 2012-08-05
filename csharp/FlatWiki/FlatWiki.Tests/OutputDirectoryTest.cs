using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace FlatWiki.Tests
{
	[TestClass]
	public class OutputDirectoryTest
	{
		[TestMethod]
		public void WritesFilesToFolder()
		{
			var temp = Directory.CreateDirectory(Path.GetTempPath() + "\\FlatWiki");
			try
			{
				var file = "somefile.txt";
				var contents = "some text";
				new OutputDirectory(temp.FullName).WriteFile(file, contents);
				Assert.AreEqual(contents, File.ReadAllText(temp.FullName + "\\" + file));
			}
			finally
			{
				temp.Delete(true);
			}
		}
	}
}