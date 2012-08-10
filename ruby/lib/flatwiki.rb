require 'pathname'

require 'output_directory'
require 'wiki_page'

class FlatWiki
    def self.main *args
        input_directory = Pathname.new(args[0])
        output_directory = OutputDirectory.new(args[1])
        FlatWiki.new(input_directory, output_directory).run
    end

    def initialize input_directory, output_directory
      @input_directory = input_directory
      @output_directory = output_directory
    end

    def run
      list_input_files.each do |filename|
        page_name = filename.sub(/\.wiki$/, '')
        content = read_input_file(filename)
        modified_time = modified_time(filename)
        page = WikiPage.new(page_name, content, modified_time)
        @output_directory.write_file(page.output_filename, page.as_html)
      end
    end

    private

    # TODO extract a delegate for inputDirectory business?
    def list_input_files
      return @input_directory.entries.
        select {|pathname| pathname.extname == '.wiki' }.
        collect {|pathname| pathname.basename}
    end

    def modified_time filename
      File.mtime(input_file(filename))
    end

    def read_input_file filename
      File.open(input_file(filename), 'r') {|f| return f.read }
    end

    def input_file filename
      @input_directory + filename
    end
end
