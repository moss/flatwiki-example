package com.cyrusinnovation.flatwiki_endtoendtests;

import com.cyrusinnovation.flatwiki.Main;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.junit.*;

import java.io.*;

import static org.apache.commons.io.FileUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.*;

public class EndToEndTest {
    private static final File INPUT_DIRECTORY = new File("test-input-dir");
    private static final File OUTPUT_DIRECTORY = new File("test-output-dir");

    @Test public void shouldGenerateAnHtmlPageForEveryWikiPage() throws IOException {
        givenInputFile("StoryZeroExample.wiki", "Some text in a file");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryZeroExample.html", containsString("Some text in a file"));
    }

    // Story 1: It should turn WordsSmashedTogetherLikeSo into links
    @Test public void shouldTurn_WordsSmashedTogetherLikeSoInto_Links() throws IOException {
        givenInputFile("StoryOneExample.wiki", "Link to StoryOneExampleTarget");
        givenInputFile("StoryOneExampleTarget.wiki", "Here's the target of the link.");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryOneExample.html",
                containsString("<a href=\"StoryOneExampleTarget.html\">StoryOneExampleTarget</a>"));
    }

    // Story 2: It should turn *starred* words into <b>boldface</b>
    @Test public void shouldTurnStarredWordsIntoBoldface() throws IOException {
        givenInputFile("StoryTwoExample.wiki", "Some *boldface* text");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryTwoExample.html", containsString("<b>boldface</b>"));
    }

    // Story 3: Double line breaks should act as paragraph separators
    @Test public void shouldTurnDoubleLineBreaksIntoParagraphBreaks() throws IOException {
        givenInputFile("StoryThreeExample.wiki", "Paragraph One\n\nParagraph Two");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryThreeExample.html", containsString("Paragraph One<p>Paragraph Two"));
    }

    // Story 4: Links to nonexistent pages should format differently
    @Ignore // WIP
    @Test public void shouldLinksToNonExistentPagesShouldNotFormatAsLinks() throws IOException {
        givenInputFile("StoryFourExample.wiki", "Link to NonexistentPage");
        whenITranslateTheInputFolderToHtml();
        checkOutputFile("StoryFourExample.html", not(containsString("<a")));
        checkOutputFile("StoryFourExample.html", containsString("Link to NonexistentPage"));
    }

    // vocabulary
    public void givenInputFile(String filename, String content) throws IOException {
        File path = new File(INPUT_DIRECTORY, filename);
        FileUtils.writeStringToFile(path, content);
    }

    private void whenITranslateTheInputFolderToHtml() throws IOException {
        Main.main(INPUT_DIRECTORY.getPath(), OUTPUT_DIRECTORY.getPath());
    }

    private void checkOutputFile(String filename, Matcher<String> matcher) throws IOException {
        File path = new File(OUTPUT_DIRECTORY, filename);
        String output = readFileToString(path);
        assertThat(output, matcher);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before public void createInputDirectory() {
        if (!INPUT_DIRECTORY.exists()) INPUT_DIRECTORY.mkdir();
    }

    @After public void cleanUpResultsForRenderingInputFolder() throws IOException {
        if (OUTPUT_DIRECTORY.exists()) deleteDirectory(OUTPUT_DIRECTORY);
        if (INPUT_DIRECTORY.exists()) deleteDirectory(INPUT_DIRECTORY);
    }
}
