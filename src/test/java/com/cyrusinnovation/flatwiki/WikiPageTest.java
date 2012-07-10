package com.cyrusinnovation.flatwiki;

import org.junit.*;

import static com.cyrusinnovation.flatwiki.TimeUtils.timeInMillis;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.*;

public class WikiPageTest {
    @Test public void outputFilenameIsPageNamePlusHtmlExtension() {
        WikiPage page = new WikiPage("SomePage", "content ignored", 0);
        assertThat(page.getOutputFilename(), is("SomePage.html"));
    }

    @Test public void htmlContentIncludesTitleAndText() {
        WikiPage page = new WikiPage("ThePageName", "Some random text.", 0);
        assertThat(page.asHtml(), containsString("<h1>ThePageName</h1>"));
        assertThat(page.asHtml(), containsString("Some random text."));
    }

    @Test public void transformsWordsSmashedTogetherIntoWikiLinks() {
        WikiPage page = new WikiPage("PageName", "Link to AnotherPage.", 0);
        assertThat(page.asHtml(), containsString(
                "Link to <a href=\"AnotherPage.html\">AnotherPage</a>."
        ));
    }

    @Test public void wikiLinks_Cannot_AppearInTheMiddleOfOtherWords() {
        WikiPage page = new WikiPage("PageName", "This should notBeLinked.", 0);
        assertThat(page.asHtml(), containsString("should notBeLinked."));
        assertThat(page.asHtml(), not(containsString("<a")));
    }

    @Test public void wikiLinks_Can_AppearAtTheStartOfTheContent() {
        WikiPage page = new WikiPage("PageName", "ThisLink should be a link.", 0);
        assertThat(page.asHtml(), containsString("<a href=\"ThisLink.html\">ThisLink</a>"));
    }

    @Test public void showsTheLastUpdatedTime() {
        WikiPage page = new WikiPage("PageName", "Some text.", timeInMillis(2010, 4, 25, 13, 17));
        assertThat(page.asHtml(), containsString("<i>Last Updated: 4/25/2010 1:17 PM</i>"));
    }
}
