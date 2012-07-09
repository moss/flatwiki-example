package com.cyrusinnovation.flatwiki;

import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.*;

public class WikiPageTest {
    @Test public void outputFilenameIsPageNamePlusHtmlExtension() {
        WikiPage page = new WikiPage("SomePage", "content ignored");
        assertThat(page.getOutputFilename(), is("SomePage.html"));
    }

    @Test public void htmlContentIncludesTitleAndText() {
        WikiPage page = new WikiPage("ThePageName", "Some random text.");
        assertThat(page.asHtml(), containsString("<h1>ThePageName</h1>"));
        assertThat(page.asHtml(), containsString("Some random text."));
    }

    @Test public void transformsWordsSmashedTogetherIntoWikiLinks() {
        WikiPage page = new WikiPage("PageName", "Link to AnotherPage.");
        assertThat(page.asHtml(), containsString(
                "Link to <a href=\"AnotherPage.html\">AnotherPage</a>."
        ));
    }

    @Test public void wikiLinks_Cannot_AppearInTheMiddleOfOtherWords() {
        WikiPage page = new WikiPage("PageName", "This should notBeLinked.");
        assertThat(page.asHtml(), containsString("should notBeLinked."));
        assertThat(page.asHtml(), not(containsString("<a")));
    }

    @Test public void wikiLinks_Can_AppearAtTheStartOfTheContent() {
        WikiPage page = new WikiPage("PageName", "ThisLink should be a link.");
        assertThat(page.asHtml(), containsString("<a href=\"ThisLink.html\">ThisLink</a>"));
    }
}
