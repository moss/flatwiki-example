using System;
using System.IO;
using FlatWiki;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace MyNamespace
{


[TestClass]
public class OutputDirectoryTest {
   
    [TestMethod] public void writesFilesToFolder()
    {
    	var temp = Directory.CreateDirectory(Path.GetTempPath() + "\\FlatWiki");
			try
			{
				var file = "somefile.txt";
				var contents = "some text";
				new OutputDirectory(temp.FullName).writeFile(file, contents);
				Assert.AreEqual(contents, File.ReadAllText(temp.FullName + "\\" + file));

			}
			finally
			{
				temp.Delete(true);
			}
    }
}
}
