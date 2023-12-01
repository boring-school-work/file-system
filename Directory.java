import java.util.HashMap;
import java.io.PrintStream;
import java.util.Collection;

/**
 * Directory class simulates a directory in a file system.
 * A directory is a file that contains other file and directory
 * 
 * @author Tchio Sekale Thierry Steve
 * @author Priscile Nkenmeza Nzonbi
 * @author David Saah
 * @author John Anatsui Edem
 * 
 * @since 2023-12-01
 * @version 1.0
 */
class Directory extends File {
  private HashMap<String, File> container; // stores files and directories
  private int count; // tracks the items in the current directory

  /**
   * Directory constructor
   * 
   * @param name the name of the Directory
   */
  public Directory(String name) {
    super(name);
    setIs_dir(true);
    container = new HashMap<>();
    count = 0;
  }

  /**
   * Add files/directory
   * 
   * @param file the file/directory to be added
   */
  public void add(File file) {
    container.put(file.getName(), file);
    count++;
  }

  /**
   * Deletes a file/directory
   * 
   * @param name the file/directory to be deleted
   * @throws Exception if the file/directory is not found
   */
  public void delete(String name) throws Exception {
    if (container.containsKey(name)) {
      container.remove(name);
      count--;
    } else {
      throw new Exception("file/directory not found");
    }
  }

  /**
   * Finds a file in the directory
   * 
   * @param name the name of the file/directory to be found
   * 
   * @return the file/directory if found
   * @throws Exception if the file/directory is not found
   */
  public File find(String name) throws Exception {
    if (container.containsKey(name)) {
      return container.get(name);
    } else {
      throw new Exception("file/directory not found");
    }
  }

  /**
   * Displays information about every file/directory
   */
  public void print() {
    for (File file : container.values()) {
      file.info();
    }
  }

  /**
   * Returns a formatted output of the directory's metadata
   * 
   * @return the directory's metadata: name, type, modified time, number of items.
   */
  public PrintStream info() {
    return System.out.format("%-5s %-20s %-3s %s\n", "dir", getModified_time(), count, getName());
  }

  /**
   * Returns all the files/directories
   * 
   * @return the files/directories in a directory.
   */
  public Collection<File> getContainer() {
    return container.values();
  }
}