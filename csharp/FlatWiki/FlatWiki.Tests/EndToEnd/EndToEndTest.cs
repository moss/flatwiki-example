using System;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Linq;

namespace FlatWiki.Tests.EndToEnd
{
	[TestClass]
	public class EndToEndTest
	{
		private const string InputDirectory = "test-input-dir";
		private const string OutputDirectory = "test-output-dir";

		[TestMethod]
		public void ShouldGenerateAnHtmlPageForEveryWikiPage()
		{
			GivenInputFile("StoryZeroExample.wiki", "Some text in a file");
			WhenITranslateTheInputFolderToHtml();
			CheckOutputFile("StoryZeroExample.html", "Some text in a file");
		}

		[TestMethod]
		public void ShouldOnlyProcessSourceFilesWithTheDotWikiExtension()
		{
			GivenInputFile("StoryOneExample.txt", "Some text in a file.");
			WhenITranslateTheInputFolderToHtml();
			OutputDirectoryShouldBeEmpty();
		}

		[TestMethod]
		public void ShouldTurnWordsSmashedTogetherLikeSoIntoLinks()
		{
			GivenInputFile("StoryTwoExample.wiki", "Link to StoryTwoExampleTarget");
			GivenInputFile("StoryTwoExampleTarget.wiki", "Here's the target of the link.");
			WhenITranslateTheInputFolderToHtml();
			CheckOutputFile("StoryTwoExample.html",
			                "<a href=\"StoryTwoExampleTarget.html\">StoryTwoExampleTarget</a>");
		}

		[TestMethod]
		public void ShouldShowUpdatedDateAtTheEndOfEachPage()
		{
			GivenInputFile("StoryThreeExample.wiki", "Just some page. Whatever.");
			fileLastModified("StoryThreeExample.wiki", 2010, 3, 25, 16, 28);
			WhenITranslateTheInputFolderToHtml();
			CheckOutputFile("StoryThreeExample.html", "<i>Last Updated: 3/25/2010 4:28 PM</i>"
				);
		}

//    [TestMethod] -- Future requirement.
		public void ShouldShowARecentChangesPage()
		{
			GivenFileCreatedOn("NewestPage.wiki", 2012, 5, 16);
			GivenFileCreatedOn("OldestPage.wiki", 1996, 1, 12);
			GivenFileCreatedOn("MiddlePage.wiki", 2008, 7, 25);
			CheckOutputFile("RecentChanges.html",
			                "<li><a href=\"NewestPage.html\">NewestPage</a>, created 5/16/2012</li>" +
			                "<li><a href=\"MiddlePage.html\">MiddlePage</a>, created 7/25/2008</li>" +
			                "<li><a href=\"OldestPage.html\">OldestPage</a>, created 1/12/1996</li>"
				);
		}

//    [TestMethod] -- Future requirement.
		public void ShouldTurnStarredWordsIntoBoldface()
		{
			GivenInputFile("StoryFiveExample.wiki", "Some *boldface* text");
			WhenITranslateTheInputFolderToHtml();
			CheckOutputFile("StoryFiveExample.html", "<b>boldface</b>");
		}

//    [TestMethod] -- Future requirement.
		public void ShouldTurnDoubleLineBreaksIntoParagraphBreaks()
		{
			GivenInputFile("StorySixExample.wiki", "Paragraph One\n\nParagraph Two");
			WhenITranslateTheInputFolderToHtml();
			CheckOutputFile("StorySixExample.html", "Paragraph One<p>Paragraph Two");
		}

		private void GivenInputFile(string filename, string content)
		{
			var path = GetInputFile(filename);
			

			File.WriteAllText(path, content);
		}

		private static string GetInputFile(string filename)
		{
			return InputDirectory + "\\" + filename;
		}

		private void fileLastModified(string filename,
		                              int year, int month, int day, int hours, int minutes)
		{
			var time = new DateTime(year, month, day, hours, minutes, 0);
			File.SetLastWriteTime(GetInputFile(filename), time);
		}

		private void GivenFileCreatedOn(string name, int year, int month, int day)
		{
			GivenInputFile(name, "Some text.");
			fileLastModified(name, year, month, day, 5, 25);
		}

		private void WhenITranslateTheInputFolderToHtml()
		{
			FlatWiki.Main(InputDirectory, OutputDirectory);		}

		private void CheckOutputFile(string filename, string contains)
		{
			var fullPath = OutputDirectory + "\\" + filename;
			var text = File.ReadAllText(fullPath);
			Assert.IsTrue(text.Contains(contains), string.Format("Didn't find '{0}' in :\r\n{1}", contains, text));
		}

		private void OutputDirectoryShouldBeEmpty()
		{
			Assert.AreEqual(0, Directory.EnumerateFiles(OutputDirectory).Count(), "Output directory should be empty.");
		}

		[TestInitialize]
		public void CreateInputDirectory()
		{
			if (!Directory.Exists(InputDirectory))
			{
				Directory.CreateDirectory(InputDirectory);
			}
			if (!Directory.Exists(OutputDirectory))
			{
				Directory.CreateDirectory(OutputDirectory);
			}
			
		}

		[TestCleanup]
		public void CleanUpResultsForRenderingInputFolder()
		{
			if (Directory.Exists(OutputDirectory))
			{
				Directory.Delete(OutputDirectory, true);
			}
			if (Directory.Exists(InputDirectory))
			{
				Directory.Delete(InputDirectory, true);
			}
		}
	}
}