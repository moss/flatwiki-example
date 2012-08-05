using System;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Linq;

namespace FlatWiki.Tests.EndToEnd
{
	[TestClass]
	public class EndToEndTest
	{
		private const string INPUT_DIRECTORY = "test-input-dir";
		private const string OUTPUT_DIRECTORY = "test-output-dir";

		[TestMethod]
		public void shouldGenerateAnHtmlPageForEveryWikiPage()
		{
			givenInputFile("StoryZeroExample.wiki", "Some text in a file");
			whenITranslateTheInputFolderToHtml();
			checkOutputFile("StoryZeroExample.html", "Some text in a file");
		}

		[TestMethod]
		public void shouldOnlyProcessSourceFilesWithTheDotWikiExtension()
		{
			givenInputFile("StoryOneExample.txt", "Some text in a file.");
			whenITranslateTheInputFolderToHtml();
			outputDirectoryShouldBeEmpty();
		}

		[TestMethod]
		public void shouldTurn_WordsSmashedTogetherLikeSo_IntoLinks()
		{
			givenInputFile("StoryTwoExample.wiki", "Link to StoryTwoExampleTarget");
			givenInputFile("StoryTwoExampleTarget.wiki", "Here's the target of the link.");
			whenITranslateTheInputFolderToHtml();
			checkOutputFile("StoryTwoExample.html",
			                "<a href=\"StoryTwoExampleTarget.html\">StoryTwoExampleTarget</a>");
		}

		[TestMethod]
		public void shouldShowUpdatedDateAtTheEndOfEachPage()
		{
			givenInputFile("StoryThreeExample.wiki", "Just some page. Whatever.");
			fileLastModified("StoryThreeExample.wiki", 2010, 3, 25, 16, 28);
			whenITranslateTheInputFolderToHtml();
			checkOutputFile("StoryThreeExample.html", "<i>Last Updated: 3/25/2010 4:28 PM</i>"
				);
		}

//    [TestMethod] -- Future requirement.
		public void shouldShowARecentChangesPage()
		{
			givenFileCreatedOn("NewestPage.wiki", 2012, 5, 16);
			givenFileCreatedOn("OldestPage.wiki", 1996, 1, 12);
			givenFileCreatedOn("MiddlePage.wiki", 2008, 7, 25);
			checkOutputFile("RecentChanges.html",
			                "<li><a href=\"NewestPage.html\">NewestPage</a>, created 5/16/2012</li>" +
			                "<li><a href=\"MiddlePage.html\">MiddlePage</a>, created 7/25/2008</li>" +
			                "<li><a href=\"OldestPage.html\">OldestPage</a>, created 1/12/1996</li>"
				);
		}

//    [TestMethod] -- Future requirement.
		public void shouldTurnStarredWordsIntoBoldface()
		{
			givenInputFile("StoryFiveExample.wiki", "Some *boldface* text");
			whenITranslateTheInputFolderToHtml();
			checkOutputFile("StoryFiveExample.html", "<b>boldface</b>");
		}

//    [TestMethod] -- Future requirement.
		public void shouldTurnDoubleLineBreaksIntoParagraphBreaks()
		{
			givenInputFile("StorySixExample.wiki", "Paragraph One\n\nParagraph Two");
			whenITranslateTheInputFolderToHtml();
			checkOutputFile("StorySixExample.html", "Paragraph One<p>Paragraph Two");
		}

		private void givenInputFile(string filename, string content)
		{
			var path = GetInputFile(filename);
			

			File.WriteAllText(path, content);
		}

		private static string GetInputFile(string filename)
		{
			return INPUT_DIRECTORY + "\\" + filename;
		}

		private void fileLastModified(string filename,
		                              int year, int month, int day, int hours, int minutes)
		{
			var time = new DateTime(year, month, day, hours, minutes, 0);
			File.SetLastWriteTime(GetInputFile(filename), time);
		}

		private void givenFileCreatedOn(string name, int year, int month, int day)
		{
			givenInputFile(name, "Some text.");
			fileLastModified(name, year, month, day, 5, 25);
		}

		private void whenITranslateTheInputFolderToHtml()
		{
			Main.main(INPUT_DIRECTORY, OUTPUT_DIRECTORY);		}

		private void checkOutputFile(string filename, string contains)
		{
			var fullPath = OUTPUT_DIRECTORY + "\\" + filename;
			var text = File.ReadAllText(fullPath);
			Assert.IsTrue(text.Contains(contains), string.Format("Didn't find '{0}' in :\r\n{1}", contains, text));
		}

		private void outputDirectoryShouldBeEmpty()
		{
			Assert.AreEqual(0, Directory.EnumerateFiles(OUTPUT_DIRECTORY).Count(), "Output directory should be empty.");
		}

		[TestInitialize]
		public void createInputDirectory()
		{
			if (!Directory.Exists(INPUT_DIRECTORY))
			{
				Directory.CreateDirectory(INPUT_DIRECTORY);
			}
			if (!Directory.Exists(OUTPUT_DIRECTORY))
			{
				Directory.CreateDirectory(OUTPUT_DIRECTORY);
			}
			
		}

		[TestCleanup]
		public void cleanUpResultsForRenderingInputFolder()
		{
			if (Directory.Exists(OUTPUT_DIRECTORY))
			{
				Directory.Delete(OUTPUT_DIRECTORY, true);
			}
			if (Directory.Exists(INPUT_DIRECTORY))
			{
				Directory.Delete(INPUT_DIRECTORY, true);
			}
		}
	}
}