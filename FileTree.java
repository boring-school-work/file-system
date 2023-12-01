import java.util.Stack;
import java.util.TreeMap;
import java.util.HashMap;

/**
 * FileTree class simulates a file system. The file system stores file and
 * directories in a tree binary. It also provides methods to perform
 * operations on the file system.
 * 
 * @author Tchio Sekale Thierry Steve
 * @author Priscile Nkenmeza Nzonbi
 * @author David Saah Abeku
 * @author John Anatsui Edem
 * 
 * @since 2023-12-01
 * @version 1.0
 */
class FileTree<T extends File> {
  private TreeMap<String, T> tree; // the file system tree
  private HashMap<String, File> dirs; // stores files and directories

  /**
   * Constructor
   */
  public FileTree() {
    tree = new TreeMap<>();
  }

  /**
   * Adds a file/directory to the file system
   * 
   * @param file the file/directory to be added
   */
  public void add(T file) {
    tree.put(file.getName(), file);
  }

  /**
   * Prints the files/directories with their respective
   * indentation in the file system tree.
   * 
   * @param file   the file/directory to be displayed
   * @param indent the indentation level of the file/directory
   */
  private void print(File file, String indent) {
    if (indent.length() > 0) {
      System.out.print("|");
    }

    System.out.print(indent + "|-- ");
    System.out.println(file.getName());

    // recursively print the files/directories in the current directory
    if (file.Is_dir()) {
      for (File f : ((Directory) file).getContainer()) {
        print(f, indent + " ".repeat(4));
      }
    }
  }

  /**
   * Prints the file system tree
   */
  public void printFileTree() {
    for (File file : tree.values()) {
      print(file, "");
    }
  }

  /**
   * Performs a depth-first search to find the filepath of a file/directory
   * when path not null. When path is null, it checks if the file/directory
   * exists in the file system.
   * 
   * @param file the file/directory being examined.
   * @param name the name of the file/directory to find.
   * @param path a stack of explored directories.
   * @return true if the file is found, false otherwise.
   */
  private boolean dfsSearch(File file, String name, Stack<String> path) {
    // add the current file/directory to the path list
    if (path != null) {
      path.push(file.getName());
    }

    // check if the current file/directory is the file/directory we are looking for
    if (file.getName().equals(name)) {
      return true; // File found
    }

    // recursively search the files/directories in the current directory
    if (file.Is_dir()) {
      for (File f : ((Directory) file).getContainer()) {
        if (dfsSearch(f, name, path)) {
          return true;
        }
      }
    }

    // Backtrack (remove last dir)
    if (path != null) {
      path.pop();
    }

    return false;
  }

  /**
   * Finds the path of a file in the file system.
   *
   * @param name the name of the file.
   * 
   * @return the file/directory path if found.
   * @throws Exception if the file is not found.
   */
  public String findFilePath(String name) throws Exception {
    Stack<String> path = new Stack<>();
    for (T file : tree.values()) {
      if (dfsSearch(file, name, path)) {
        break;
      }
    }

    if (path.isEmpty()) {
      throw new Exception("File/directory not found");
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append("/");
      for (String dir : path) {
        sb.append(dir).append("/");
      }

      sb.replace(sb.length() - 1, sb.length(), ""); // remove the last slash
      return sb.toString();
    }
  }

  /**
   * Removes a file or directory with the specified name from a given directory.
   *
   * @param directory The directory from which to remove the file or directory.
   * @param name      The name of the file or directory to remove.
   * 
   * @throws Exception if the file or directory is not found.
   */
  private void removeFileOrDirectory(Directory directory, String name) throws Exception {
    for (File file : directory.getContainer()) {
      if (file.getName().equals(name)) {
        directory.delete(file.getName());
        return;
      } else if (file.Is_dir()) {
        removeFileOrDirectory((Directory) file, name);
      }
    }
  }

  /**
   * Deletes a file/directory with the specified name from the file system.
   *
   * @param name the name of the file/directory to delete.
   * @throws Exception if the file/directory is not found.
   */
  public void delete(String name) throws Exception {
    for (T file : tree.values()) {

      if (dfsSearch(file, name, null)) {
        if (file.getName().equals(name)) {
          tree.remove(name);
          return;
        } else {
          removeFileOrDirectory((Directory) file, name);
        }
      }
    }
  }

  /*
   * -----------------------------------------------------------------------------
   */

  /**
   * Gets the Directory object with the specified name from the file system.
   *
   * @param directoryName The name of the directory to retrieve.
   * @return The Directory object if found, or null if the directory is not found.
   * @throws Exception if the directory is not found.
   */
  public Directory getDirectory(String directoryName) throws Exception {
    for (T file : tree.values()) {
      if (dfsSearch(file, directoryName, null) && file.Is_dir() && file.getName().equals(directoryName)) {
        return (Directory) file;
      }
     else{
      
    }
    }

    throw new Exception("Directory not found");
  }

  private File findFile(File dir, String name) throws Exception {
    // base case
    // check if the current directory (also file) is the file we are looking for
    if (dir.getName().equals(name)) {
      return dir;
    }

    if (dir.Is_dir()) {
      for (File f : ((Directory) dir).getContainer()) {
       findFile(f, name);
      }
    }

    throw new Exception("File not found");
  }

  /**
   * Gets the file object with the specified name from the file system.
   *
   * @param fileName The name of the file to retrieve.
   * @return The file object if found, or null if the directory is not found.
   * @throws Exception if the file is not found.
   */
  public File getFile(String fileName) throws Exception {
    // go through all the files in the file system
    for (File file : tree.values()) {

      // check if file exists
      if (dfsSearch(file, fileName, null)) {
        if (file.getName().equals(fileName)) {
          return file;
        } else {
          // recursively search directory
          return findFile(file, fileName);
        }
      }
    }
    throw new Exception("File not found");
  }

  /**
   * Moves a file or directory to a new directory in the file system.
   *
   * @param movingFile     The file or directory to move.
   * @param destinationDir The directory to which the file or directory will be
   *                       moved to.
   * @throws Exception if the file or directory is not found.
   */
  public void move(String movingFile, String destinationDir) throws Exception {
    File moveItem = getFile(movingFile);
    Directory destination = getDirectory(destinationDir);
    this.delete(moveItem.getName());
    destination.add(moveItem);
  }

  /**
   * Checks if a directory with the specified name exists in the file system.
   *
   * @param directoryName The name of the directory to check.
   * @return True if the directory exists, false otherwise.
   */
  public boolean doesDirectoryExist(String directoryName) {
    for (T file : tree.values()) {
      if (file.Is_dir() && file.getName().equals(directoryName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a file with the specified name exists in the file system.
   *
   * @param directoryName The name of the file to check.
   * @return True if the file exists, false otherwise.
   */
  public boolean doesFileExist(String directoryName) {
    for (File file : tree.values()) {
      if (dfsSearch(file, directoryName, null) && file.getName().equals(directoryName)) {
        return true;
      }
    }
    return false;
  }
}