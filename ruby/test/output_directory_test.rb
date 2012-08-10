require 'test/unit'
require 'output_directory'

class OutputDirectoryTest < Test::Unit::TestCase
  def setup
    @folder = Pathname.new 'output-directory-test-output'
    @folder.mkdir unless @folder.exist?
    @output_directory = OutputDirectory.new(@folder)
  end

  def teardown
    @folder.rmtree if @folder.exist?
  end

  def test_writes_files_to_folder
    @output_directory.write_file("somefile.txt", "some text");
    content = read_file 'somefile.txt'
    assert_equal("some text", content);
  end
  
  def read_file name
    File.open(@folder + name, 'r') {|f| return f.read }
  end
end
