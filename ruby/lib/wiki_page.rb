class WikiPage
    WIKI_WORD_PATTERN = /(?<=\W|^)[A-Z][a-z]+([A-Z][a-z]+)+/

    def initialize name, source, updated_time
      @name = name
      @source = source
      # TODO replace this with a value object. FormattableTime?
      @updated_time = updated_time
    end

    def output_filename
      "#{@name}.html"
    end

    def as_html
      <<HERE
<h1>#{@name}</h1>
<div>#{wikify_source}</div>
<i>Last Updated: #{format_updated_time}</i>
HERE
    end

    private

    def wikify_source
      #TODO extract a method object. WikiText? WikiScanner?
      @source.gsub(WIKI_WORD_PATTERN) {|match| %[<a href="#{match}.html">#{match}</a>] }
    end

    def format_updated_time
      @updated_time.strftime("%-m/%-d/%Y %l:%M %p").gsub(/\s+/, ' ')
    end
end
