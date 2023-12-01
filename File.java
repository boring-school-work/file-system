import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * File class simulates a file in a file system.
 * 
 * @author Tchio Sekale Thierry Steve
 * @author Priscile Nkenmeza Nzonbi
 * @author David Saah
 * @author John Anatsui Edem
 * 
 * @since 2023-12-01
 * @version 1.0
 */
class File {
  private String modified_time; // the last time the file was modified
  private String created_time; // the time the file was created
  private String name; // the name of the file
  private boolean is_dir; // true if the file is a directory, false otherwise

  /**
   * File constructor
   * 
   * @param name the name of the File
   */
  public File(String name) {
    this.name = name;
    this.is_dir = false;
    this.created_time = getDateTime();
    this.modified_time = getDateTime();
  }

  // TODO: Check the display info of the time

  /**
   * Gets the time a file is created
   * 
   * @return the time the file was created
   */
  private String getDateTime() {
    String time = LocalTime.now().toString(); // HH:mm:ss.SSS
    time = time.substring(0, time.indexOf(".")); // remove the milliseconds
    return LocalDate.now().toString() + " " + time; // HH:mm:ss
  }

  /**
   * Gets the modified time of the file
   * 
   * @return the time a file was modified
   */
  public String getModified_time() {
    return modified_time;
  }

  /**
   * Updates the modified time of the file
   */
  public void upModified_time() {
    this.modified_time = getDateTime();
  }

  /**
   * Gets the created time of the file
   * 
   * @return the time a file is created
   */
  public String getCreated_time() {
    return created_time;
  }

  /**
   * Gets the name of the file
   * 
   * @return the name of the file
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of a file
   * 
   * @param name the name of file
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Checks whether the file is a directory or not
   * 
   * @return true if the file is a directory, false otherwise
   */
  public boolean Is_dir() {
    return is_dir;
  }

  /**
   * Sets the file type: whether a directory or not
   * 
   * @param is_dir true if the file is a directory, false otherwise
   */
  protected void setIs_dir(boolean is_dir) {
    this.is_dir = is_dir;
  }

  /**
   * Returns information about the file
   * 
   * @return the file's metadata: name, type, modified time.
   */
  public PrintStream info() {
    return System.out.format("%-5s %-20s %-3s %s\n", "file", modified_time, "", name);
  }
}
