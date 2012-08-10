require 'test/unit'
require 'wiki_page'

class WikiPageTest < Test::Unit::TestCase
    def test_output_filename_is_page_name_plus_html_extension
      page = WikiPage.new("SomePage", "content ignored", Time.new)
      assert_equal "SomePage.html", page.output_filename
    end

    def test_html_content_includes_title_and_text
      page = WikiPage.new("ThePageName", "Some random text.", Time.new)
      assert_match "<h1>ThePageName</h1>", page.as_html
      assert_match "Some random text.", page.as_html
    end

    def test_transforms_WordsSmashedTogether_into_wiki_links
      page = WikiPage.new("PageName", "Link to AnotherPage.", Time.new)
      assert_match "Link to <a href=\"AnotherPage.html\">AnotherPage</a>.", page.as_html
    end

    def test_wiki_links_CANNOT_appear_in_the_middle_of_other_words
      page = WikiPage.new("PageName", "This should notBeLinked.", Time.new)
      assert_match "should notBeLinked.", page.as_html
      assert_no_match /<a/, page.as_html
    end

    def test_wiki_links_CAN_appear_at_the_start_of_the_content
      page = WikiPage.new("PageName", "ThisLink should be a link.", Time.new)
      assert_match "<a href=\"ThisLink.html\">ThisLink</a>", page.as_html
    end

    def test_shows_the_last_updated_time
      page = WikiPage.new("PageName", "Some text.", Time.utc(2010, 4, 25, 13, 17))
      assert_match "<i>Last Updated: 4/25/2010 1:17 PM</i>", page.as_html
    end
end
