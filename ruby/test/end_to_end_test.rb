require 'test/unit'
require 'pathname'
require 'flatwiki'

class EndToEndTests < Test::Unit::TestCase
  INPUT_DIRECTORY = Pathname.new("test-input-dir")
  OUTPUT_DIRECTORY = Pathname.new("test-output-dir")

  def test_generates_an_html_page_for_every_wiki_page
    given_input_file "StoryZeroExample.wiki", "Some text in a file."
    when_i_translate_the_folder_to_html
    check_output_file_contains "StoryZeroExample.html", "Some text in a file."
  end

  def test_only_processes_source_files_with_the_dot_wiki_extension
    given_input_file "StoryOneExample.txt", "Some text in a file."
    when_i_translate_the_folder_to_html
    output_directory_should_be_empty
  end

  def test_turns_WordsSmashedTogetherLikeSo_into_links
    given_input_file "StoryTwoExample.wiki", "Link to StoryTwoExampleTarget"
    given_input_file "StoryTwoExampleTarget.wiki", "Here's the target of that link."
    when_i_translate_the_folder_to_html
    check_output_file_contains "StoryTwoExample.html",
      '<a href="StoryOneExampleTarget.html">StoryOneExampleTarget</a>'
  end

  def test_shows_updated_date_at_the_end_of_each_page
    given_input_file "StoryThreeExample.wiki", "Just some page. Whatever."
    file_last_modified "StoryThreeExample.wiki", 2010, 3, 25, 16, 28
    when_i_translate_the_folder_to_html
    check_output_file_contains "StoryThreeExample.html",
      "<i>Last Updated: 3/25/2010 4:28 PM</i>"
  end

  def PENDING_test_shows_a_recent_changes_page
    given_file_created_on "NewestPage.wiki", 2012, 5, 16
    given_file_created_on "OldestPage.wiki", 1996, 1, 12
    given_file_created_on "MiddlePage.wiki", 2008, 7, 25
    when_i_translate_the_folder_to_html
    check_output_file_contains "RecentChanges.html", <<HERE
<li><a href="NewestPage.html">NewestPage</a>, created 5/16/2012</li>
<li><a href="MiddlePage.html">MiddlePage</a>, created 7/25/2008</li>
<li><a href="OldestPage.html">OldestPage</a>, created 1/12/1996</li>
HERE
  end

  def PENDING_test_turns_starred_words_into_boldface
    given_input_file "StoryFiveExample.wiki", "Some *boldface* text"
    when_i_translate_the_folder_to_html
    check_output_file_contains "StoryFiveExample.html", "<b>boldface</b>"
  end

  def PENDING_test_turns_double_line_breaks_into_paragraph_breaks
    given_input_file "StorySixExample.wiki", "Paragraph One\n\nParagraph Two"
    when_i_translate_the_folder_to_html
    check_output_file_contains "StorySixExample.html", "Paragraph One<p>Paragraph Two"
  end

  def PENDING_test_links_to_nonexistent_pages_should_not_format_as_links
    given_input_file("StorySevenExample.wiki", "Link to NonexistentPage");
    when_i_translate_the_folder_to_html();
    # note the lack of an <a> tag
    check_output_file_contains "StorySevenExample.html", "Link to NonexistentPage"
  end

  #vocabulary
  def given_input_file name, content
    File.open(INPUT_DIRECTORY + name, 'w') {|f| f.write content }
  end

  def file_last_modified filename, year, month, day, hours, minutes
    path = INPUT_DIRECTORY + filename
    mtime = Time.new(year, month, day, hours, minutes)
    File.utime(File.atime(path), mtime, path)
  end

  def given_file_created_on filename, year, month, day
    given_input_file filename, "Some text."
    file_last_modified filename, year, month, day, 5, 25
  end

  def when_i_translate_the_folder_to_html
    FlatWiki.main(INPUT_DIRECTORY.realpath, OUTPUT_DIRECTORY.realpath)
  end

  def check_output_file_contains filename, expected_content
    File.open(OUTPUT_DIRECTORY + filename, 'r') do |f|
      actual_content = f.read
      assert_match expected_content, actual_content
    end
  end

  def check_output_file_does_not_contain filename, expected_content
    File.open(OUTPUT_DIRECTORY + filename, 'r') do |f|
      actual_content = f.read
      assert_no_match expected_content, actual_content
    end
  end

  def output_directory_should_be_empty
    assert_equal [], OUTPUT_DIRECTORY.children
  end

  def setup
    INPUT_DIRECTORY.mkdir unless INPUT_DIRECTORY.exist?
    OUTPUT_DIRECTORY.mkdir unless OUTPUT_DIRECTORY.exist?
  end

  def teardown
    INPUT_DIRECTORY.rmtree if INPUT_DIRECTORY.exist?
    OUTPUT_DIRECTORY.rmtree if OUTPUT_DIRECTORY.exist?
  end
end
