require 'pathname'

class OutputDirectory
  def initialize output_directory
    @output_directory = Pathname.new(output_directory.to_s)
  end

  # TODO introduce a parameter object for filename and content. Document?
  def write_file filename, content
    path = @output_directory + filename
    File.open(path, 'w') {|f| f.write content }
  end
end
