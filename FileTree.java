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
    dirs = new HashMap<>();
  }

  /**
   * Adds a new directory to the dir list
   * 
   * @param dir the directory to be added
   */
  public void addDir(Directory dir) {
    dirs.put(dir.getName(), dir);
  }

  /**
   * Gets the size of the directory list
   * 
   * @return the size of the directory list
   */
  public int getDirSize() {
    return dirs.size();
  }

  /**
   * Checks if the directory exists
   * 
   * @param name the name of the directory
   * @return true if the directory exists, false otherwise
   */
  public boolean dirExists(String name) {
    return dirs.containsKey(name);
  }

  /**
   * Add file to directory
   * 
   * @param file    the file to be added
   * @param dirName the name of the directory
   */
  public void addFileToDir(File file, String dirName) {
    Directory dir = (Directory) dirs.get(dirName);
    dir.add(file);
  }

  /**
   * List all the directories in the directory list
   */
  public void listDir() {
    for (String dir : dirs.keySet()) {
      System.out.println(dir);
    }
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

  /**
   * Checks if a file exists in the file system.
   * 
   * @param name the name of the file.
   * @return true if the file exists, false otherwise.
   * @throws Exception if the file is not found.
   */
  public boolean chkFileExists(String name) throws Exception {
    for (T file : tree.values()) {
      if (dfsSearch(file, name, null)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Moves a file/directory from one directory to another.
   * 
   * @param filename the name of the file/directory to move.
   * @param dirName  the name of the destination directory.
   * @throws Exception if the file/directory does not exist.
   */
  public void move(String filename, String dirName) throws Exception {
    if (!chkFileExists(filename)) {
      throw new Exception("File does not exist");
    }

    // check if the directory exists
    if (!dirExists(dirName)) {
      throw new Exception("Directory does not exist");
    }

    // remove the file/directory from the file system
    delete(filename);

    // add the file/directory to the destination directory
    Directory dir = (Directory) dirs.get(dirName);
    dir.add(new File(filename));
  }

  /**
   * Prints the files/directories with their respective
   * indentation in the file system tree as well as their metadata.
   * 
   * @param file   the file/directory to be displayed
   * @param indent the indentation level of the file/directory
   */
  private void printMeta(File file, String indent) {
    if (indent.length() > 0) {
      System.out.print("|");
    }

    System.out.print(indent + "|-- ");
    file.info();

    // recursively print the files/directories in the current directory
    if (file.Is_dir()) {
      for (File f : ((Directory) file).getContainer()) {
        printMeta(f, indent + " ".repeat(4));
      }
    }
  }

  /**
   * Prints the file system tree as well as the metadata of the files/directories.
   */
  public void printDetailedFileTree() {
    for (File file : tree.values()) {
      printMeta(file, "");
    }
  }
}