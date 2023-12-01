import java.util.Scanner;

class Main {

  /**
   * Allows the user to create a file
   * 
   * @param input the scanner object
   * @param tree  the file tree
   */
  public static void createFile(Scanner input, FileTree<File> tree) {
    System.out.print("Enter the name of the file: ");
    File file = new File(input.nextLine());

    // create file at root if the tree has no directory
    if (tree.getDirSize() == 0) {
      tree.add(file);
      System.out.println("File created successfully");
      return;
    }

    System.out.print("Do you want to add the file to a specific directory? (Y/N): ");
    String choice = input.nextLine();

    if (choice.equalsIgnoreCase("Y")) {
      tree.listDir();
      System.out.print("Enter the name of the directory: ");
      String dirName = input.nextLine();

      if (tree.dirExists(dirName)) {
        tree.addFileToDir(file, dirName);
        System.out.println("File created successfully");
      } else {
        System.out.println("Directory does not exist");
      }
    } else {
      tree.add(file);
      System.out.println("File created successfully");
    }
  }

  /**
   * Allows the user to create a folder
   * 
   * @param input the scanner object
   * @param tree  the file tree
   */
  public static void createFolder(Scanner input, FileTree<File> tree) {
    System.out.print("Enter the name of the folder: ");
    Directory dir = new Directory(input.nextLine());

    // create folder at root if the tree has no directory
    if (tree.getDirSize() == 0) {
      tree.addDir(dir);
      tree.add(dir);
      System.out.println("Folder created successfully");
      return;
    }

    System.out.print("Do you want to add the folder to a specific directory? (Y/N): ");
    String choice = input.nextLine();

    if (choice.equalsIgnoreCase("Y")) {
      System.out.println("List of available directories:");
      tree.listDir();
      System.out.print("Enter the name of the directory: ");
      String dirName = input.nextLine();

      if (tree.dirExists(dirName)) {
        tree.addFileToDir(dir, dirName);
        tree.addDir(dir);
        System.out.println("Folder created successfully");
      } else {
        System.out.println("Directory does not exist");
      }
    }

    else {
      tree.add(dir);
      tree.addDir(dir);
      System.out.println("Folder created successfully");
    }
  }

  /**
   * Allows the user to delete a file or folder
   * 
   * @param input the scanner object
   * @param tree  the file tree
   */
  public static void delete(Scanner input, FileTree<File> tree) throws Exception {
    System.out.print("Enter the name of the file/directory to delete: ");
    String fileName = input.nextLine();

    tree.delete(fileName);
    System.out.printf("%s deleted successfully\n", fileName);
  }

  /**
   * Allows the user to move a file or folder to a specific location
   * 
   * @param input the scanner object
   * @param tree  the file tree
   * @throws Exception if the file or folder does not exist
   */
  public static void move(Scanner input, FileTree<File> tree) throws Exception {
    System.out.print("Please enter the name of the file/folder to be moved: ");
    String fileName = input.nextLine();

    System.out.print("Please enter the name of the destination folder: ");
    String directoryName = input.nextLine();

    tree.move(fileName, directoryName);
    System.out.printf("%s moved successfully to %s", fileName, directoryName);
  }

  /**
   * Allows the user to search for a file or folder
   * 
   * @param input the scanner object
   * @param tree  the file tree
   * @throws Exception if the file or folder does not exist
   */
  public static void search(Scanner input, FileTree<File> tree) throws Exception {
    System.out.print("Please enter the name of the file/folder below: ");
    String fileName = input.nextLine();
    System.out.println(tree.findFilePath(fileName));
  }

  public static void main(String[] args) throws Exception {
    // Create a tree to manage files and directories
    FileTree<File> tree = new FileTree<>();

    // Infinite loop for the task management system menu
    while (true) {
      System.out.println();
      System.out.println("--------------------------------------------------------------------------");
      System.out.println("------------------ Virtual File System Organiser --------------");
      System.out.println("--------------------------- 1. Create file -------------------------------");
      System.out.println("--------------------------- 2. Create Folder -----------------------------");
      System.out.println("--------------------------- 3. Delete file or Folder ---------------------");
      System.out.println("--------------------------- 4. Move File or Folder to Specific location --");
      System.out.println("--------------------------- 5. Search File -------------------------------");
      System.out.println("--------------------------- 6. View File tree ----------------------------");
      System.out.println("--------------------------- 7. Print detailed File Tree ------------------");
      System.out.println("--------------------------- 8. Quit---------------------------------------");
      System.out.println("--------------------------------------------------------------------------");
      System.out.println();

      // Get user's choice
      System.out.println("Enter your choice (1/2/3/4/5/6/7/8): ");
      Scanner input = new Scanner(System.in);
      int choice = input.nextInt();
      input.nextLine();
      System.out.println();

      try {
        // Switch based on user's choice
        switch (choice) {
          case 1:
            createFile(input, tree);
            break;

          case 2:
            createFolder(input, tree);
            break;

          case 3:
            delete(input, tree);
            break;

          case 4:
            move(input, tree);
            break;

          case 5:
            search(input, tree);
            break;
          case 6:
            // View the file tree
            tree.printFileTree();
            break;
          case 7:
            tree.printDetailedFileTree();
            break;
          case 8:
            // Exit the program
            input.close();
            System.exit(0);

          default:
            System.out.println("Invalid choice, please select a valid entry between 1 - 7");
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
