using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace FlatWiki.Tests
{
	[TestClass]
	public class WikiPageTest
	{
		[TestMethod]
		public void OutputFilenameIsPageNamePlusHtmlExtension()
		{
			WikiPage page = new WikiPage("SomePage", "content ignored", 0);
			Assert.AreEqual(page.GetOutputFilename(),("SomePage.html"));
		}

		[TestMethod]
		public void HtmlContentIncludesTitleAndText()
		{
			WikiPage page = new WikiPage("ThePageName", "Some random text.", 0);
		AssertContainsString(page.AsHtml(),"<h1>ThePageName</h1>");
			AssertContainsString(page.AsHtml(),"Some random text.");
		}

		[TestMethod]
		public void TransformsWordsSmashedTogetherIntoWikiLinks()
		{
			WikiPage page = new WikiPage("PageName", "Link to AnotherPage.", 0);
			AssertContainsString(page.AsHtml(),"Link to <a href=\"AnotherPage.html\">AnotherPage</a>.");
		}

		[TestMethod]
		public void WikiLinksCannotAppearInTheMiddleOfOtherWords()
		{
			WikiPage page = new WikiPage("PageName", "This should notBeLinked.", 0);
			AssertContainsString(page.AsHtml(),"should notBeLinked.");
			AssertContainsString( false, page.AsHtml(), "<a");
		}

		[TestMethod]
		public void WikiLinksCanAppearAtTheStartOfTheContent()
		{
			WikiPage page = new WikiPage("PageName", "ThisLink should be a link.", 0);
			AssertContainsString(page.AsHtml(), "<a href=\"ThisLink.html\">ThisLink</a>");
		}

		public void AssertContainsString(string text, string contains)
		{
			AssertContainsString(true, text, contains);
		}

		private static void AssertContainsString(bool expected, string text, string contains)
		{
			Assert.AreEqual(expected, text.Contains(contains), string.Format("{0} find '{1}' in :\r\n{2}", expected ? "didn't": "did", contains, text));
		}

		[TestMethod]
		public void ShowsTheLastUpdatedTime()
		{
			WikiPage page = new WikiPage("PageName", "Some text.", new DateTime(2010, 4, 25, 13, 17,0).Ticks);
			AssertContainsString(page.AsHtml(),"<i>Last Updated: 4/25/2010 1:17 PM</i>");
		}

	}
}

