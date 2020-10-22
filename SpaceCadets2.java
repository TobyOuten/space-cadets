import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class SpaceCadets2 {

  // Hashmap for storing variables.
  static HashMap<String, Integer> variables = new HashMap<>();

  // Hashmap for the indexes of while loops
  static HashMap<Integer, Integer> whileEnds = new HashMap<>();

  // Create a scanner object to read user input.
  static Scanner cmdInput = new Scanner(System.in);

  public static void main(String[] args){

    // List to hold the commands
    ArrayList<String> commands = new ArrayList<>();

    // Loop until the user chooses to quit.
    String choice = "";
    while (!choice.equals("5")) {

      // Print a menu and accept user's choice as input.
      System.out.println("*****************************");
      System.out.println("Enter a number 1-5: ");
      System.out.println("1: Enter the name of a file\n" +
              "2: Print current loaded file\n" +
              "3: Run the file\n" +
              "4: Step through the file\n" +
              "5: Quit");
      choice = cmdInput.nextLine();

      // Selection for the user's choice.
      switch (choice) {
        case "1":
          commands = loadFile();
          break;
        case "2":
          if (commands.size() == 0) {
            System.out.println("No file loaded.");
          } else {
            for (String line : commands) {
              System.out.println(line);
            }
          }
          break;
        case "3":
        case "4":
          if (commands.size() == 0) {
            System.out.println("No file loaded.");
          } else {
            runFile(commands, choice.equals("4"));
          }
          break;
        case "5":
          System.out.println("Goodbye!");
          break;
        default:
          System.out.println("Invalid input");
      }
    }
  }

  public static ArrayList<String> loadFile() {

    // Create an output list
    ArrayList<String> output = new ArrayList<>();

    // Read user input for the file name.
    System.out.println("Enter the name of the Text File:");
    File file = new File(cmdInput.nextLine() + ".txt");
    Scanner fileInput;
    try {
      // Read each line of the sample file to a list.
      fileInput = new Scanner(file);
      while (fileInput.hasNext()) {
        output.add(fileInput.nextLine());
      }
    } catch (FileNotFoundException e) {
      // If the
      System.out.println("File not found.");
    }

    return output;
  }

  public static void runFile(ArrayList<String> commands, boolean step) {
    // Loop through the list using index i.
    String command;
    for (int i = 0; i < commands.size(); i++) {

      // Create a String for each command and call the interpret function.
      command = commands.get(i).trim();
      if (!command.startsWith("//")) {
        i = interpret(command, i);

        if (step) {
          // After each line is interpreted, print a table of variables and their values.
          for (String key : variables.keySet()) {
            System.out.println(key + ": " + variables.get(key));
          }
          cmdInput.nextLine();
        }
      }
    }
    if (!step) {
      // After each line is interpreted, print a table of variables and their values.
      for (String key : variables.keySet()) {
        System.out.println(key + ": " + variables.get(key));
      }
    }
  }

  public static int interpret(String command, int lineNum) {

    // Setup for interpreting the line.
    System.out.println(command);
    String[] splitCmd = command.split(" ");

    // Selection for getting the variable name.
    String varName = "";
    if (splitCmd[0].equals("while")) {
      varName = splitCmd[1];
    } else if (!splitCmd[0].equals("end;")) {
      varName = splitCmd[1].substring(0, splitCmd[1].length()-1);
    }

    // Selection for which command is called.
    switch (command.split(" ")[0]) {

      case "clear":
        // Put the variable name into the hashmap with value 0.
        System.out.println("Set value of variable " + varName + " to 0");
        variables.put(varName, 0);
        break;

      case "incr":
        if (variables.containsKey(varName)) {
          System.out.println("Incremented variable "+ varName);
          variables.put(varName, variables.get(varName) + 1);
        } else {
          System.out.println("Attempted to increment a non-existent variable");
        }
        break;

      case "decr":
        if (variables.containsKey(varName)) {
          if (variables.get(varName) == 0) {
            System.out.println("Cannot store a negative value.");
          } else {
            System.out.println("Decremented variable " + varName);
            variables.put(varName, variables.get(varName) - 1);
          }
        } else {
          System.out.println("Attempted to decrement a non-existent variable");
        }
        break;

      case "while":
        if (whileEnds.containsKey(lineNum)) {
          System.out.println(varName);
          if (variables.get(varName) == 0) {
            System.out.println("Exited the while loop");
            System.out.println(whileEnds.get(lineNum));
            return whileEnds.get(lineNum);
          }
        } else {
          System.out.println("Started the while loop");
          whileEnds.put(lineNum, 0);
        }
        break;

      case "end;":
        /* Testing for the while loop jumping.
        for (int key : whileEnds.keySet()) {
          System.out.println(key + ": " + whileEnds.get(key));
        }
        */
        // Loops through all the while indexes to find the largest empty one.
        // Links end statements to their corresponding while statement.
        int highestEmptyIndex = 0;
        for (int key : whileEnds.keySet()) {
          // If this end statement is already linked with a while statement.
          if (whileEnds.get(key) == lineNum) {
            System.out.println("Looped back to line " + (key));
            return key - 1;
          }
          if (whileEnds.get(key) == 0 && key > highestEmptyIndex) {
            highestEmptyIndex = key;
          }
        }
        whileEnds.put(highestEmptyIndex, lineNum);
        return highestEmptyIndex - 1;
    }
    return lineNum;
  }
}
