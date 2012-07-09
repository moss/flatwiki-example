package com.cyrusinnovation.flatwiki_endtoendtests;

import com.cyrusinnovation.flatwiki.Main;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.junit.*;

import java.io.*;
import java.util.Calendar;

import static org.apache.commons.io.FileUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.*;

@SuppressWarnings({"ResultOfMethodCallIgnored", "MagicConstant"})
public class EndToEndTest {
    private static final File INPUT_DIRECTORY = new File("test-input-dir");
    private static final File OUTPUT_DIRECTORY = new File("test-output-dir");

    @Test public void shouldGenerateAnHtmlPageForEveryWikiPage() throws IOException {
        givenInputFile("StoryZeroExample.wiki", "Some text in a file");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryZeroExample.html", containsString("Some text in a file"));
    }

    @Test public void shouldTurn_WordsSmashedTogetherLikeSoInto_Links() throws IOException {
        givenInputFile("StoryOneExample.wiki", "Link to StoryOneExampleTarget");
        givenInputFile("StoryOneExampleTarget.wiki", "Here's the target of the link.");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryOneExample.html",
                containsString("<a href=\"StoryOneExampleTarget.html\">StoryOneExampleTarget</a>"));
    }

    @Ignore
    @Test public void shouldShowUpdatedDateAtTheEndOfEachPage() throws IOException {
        givenInputFile("StoryFourExample.wiki", "Just some page. Whatever.");
        fileLastModified("StoryFourExample.wiki", 2010, 3, 25, 16, 28);
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryFourExample.html", containsString(
                "<i>Last Updated: 3/25/2010 4:28 PM</i>"
        ));
    }

    @Ignore
    @Test public void shouldShowARecentChangesPage() throws IOException {
        givenFileCreatedOn("NewestPage.wiki", 2012, 5, 16);
        givenFileCreatedOn("OldestPage.wiki", 1996, 1, 12);
        givenFileCreatedOn("MiddlePage.wiki", 2008, 7, 25);
        checkOutputFile("RecentChanges.html", containsString(
                "<li><a href=\"NewestPage.html\">NewestPage</a>, created 5/16/2012</li>" +
                        "<li><a href=\"MiddlePage.html\">MiddlePage</a>, created 7/25/2008</li>" +
                        "<li><a href=\"OldestPage.html\">OldestPage</a>, created 1/12/1996</li>"
        ));
    }

    @Ignore
    @Test public void shouldTurnStarredWordsIntoBoldface() throws IOException {
        givenInputFile("StoryTwoExample.wiki", "Some *boldface* text");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryTwoExample.html", containsString("<b>boldface</b>"));
    }

    @Ignore
    @Test public void shouldTurnDoubleLineBreaksIntoParagraphBreaks() throws IOException {
        givenInputFile("StoryThreeExample.wiki", "Paragraph One\n\nParagraph Two");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryThreeExample.html", containsString("Paragraph One<p>Paragraph Two"));
    }

    @Ignore
    @Test public void shouldLinksToNonExistentPagesShouldNotFormatAsLinks() throws IOException {
        givenInputFile("StoryFourExample.wiki", "Link to NonexistentPage");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryFourExample.html", not(containsString("<a")));
        checkOutputFile("StoryFourExample.html", containsString("Link to NonexistentPage"));
    }

    // vocabulary
    private void givenInputFile(String filename, String content) throws IOException {
        File path = new File(INPUT_DIRECTORY, filename);
        FileUtils.writeStringToFile(path, content);
    }

    private void fileLastModified(String filename,
                                  int year, int month, int day, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hours, minutes);
        new File(INPUT_DIRECTORY, filename).setLastModified(calendar.getTimeInMillis());
    }

    private void givenFileCreatedOn(String name, int year, int month, int day) throws IOException {
        givenInputFile(name, "Some text.");
        fileLastModified(name, year, month, day, 5, 25);
    }

    private void whenITranslateTheInputFolderToHtml() throws IOException {
        Main.main(INPUT_DIRECTORY.getPath(), OUTPUT_DIRECTORY.getPath());
    }

    private void checkOutputFile(String filename, Matcher<String> matcher) throws IOException {
        File path = new File(OUTPUT_DIRECTORY, filename);
        String output = readFileToString(path);
        assertThat(output, matcher);
    }

    @Before public void createInputDirectory() {
        if (!INPUT_DIRECTORY.exists()) INPUT_DIRECTORY.mkdir();
    }

    @After public void cleanUpResultsForRenderingInputFolder() throws IOException {
        if (OUTPUT_DIRECTORY.exists()) deleteDirectory(OUTPUT_DIRECTORY);
        if (INPUT_DIRECTORY.exists()) deleteDirectory(INPUT_DIRECTORY);
    }
}
