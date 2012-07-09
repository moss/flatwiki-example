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
}
