using System;
using System.Text;
using System.Text.RegularExpressions;

namespace FlatWiki
{
	public class WikiPage
	{
		private static string WIKI_WORD_PATTERN = 
			"(?<=\\W|^)" + // preceded by a non-word character or the start of the line
			"[A-Z][a-z]+([A-Z][a-z]+)+" // two or more words smashed together
			;

		private string name;
		private string source;
		// TODO replace this with a value object. FormattableTime?
		private long updatedTime;

		public WikiPage(string name, string source, long updatedTime)
		{
			this.name = name;
			this.source = source;
			this.updatedTime = updatedTime;
		}

		public string getOutputFilename()
		{
			return name + ".html";
		}

		public string asHtml()
		{
			return String.Format(
				@"<h1>{0}</h1>
<div>{1}</div>
<i>Last Updated: {2}</i>",
				name, wikifySource(), formatUpdatedTime());
		}

		private string wikifySource()
		{
			// TODO extract a method object. WikiText? WikiScanner?
			

			return Regex.Replace(source, WIKI_WORD_PATTERN, m => string.Format(@"<a href=""{0}.html"">{0}</a>", m.Value));
		}

		private string formatUpdatedTime()
		{
			return new DateTime(updatedTime).ToString("M/d/yyyy h:m tt");
		}
	}
}