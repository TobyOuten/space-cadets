import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class SpaceCadets1 {

  public static void main(String[] args) throws IOException{
    // Create a BufferedReader to read the input from the user.
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    // Accept user input for the ID.
    System.out.println("Enter the ID you want to search for or Q to quit:");
    String id = "";
    id = in.readLine();
    
    // Loop so that the user can enter multiple IDs.
    while (!id.equals("Q")) {
      
      // Set up variables to begin searching the webpage.
      String inputLine = "";
      boolean found = false;
      int index = 0;
      int endIndex = 0;
      String name = "";
        
      // Create a URL object from the ID.
      URL myURL = new URL("https://www.ecs.soton.ac.uk/people/"+id);
      System.out.println("Created URL object.");
      
      if (myURL != null) {
        // Create a reader to read the webpage.
        BufferedReader URLReader = new BufferedReader(new InputStreamReader(myURL.openStream()));
        inputLine = URLReader.readLine();
        System.out.println("Created the reader.");

        // Loop through the webpage until the name is found or the end is reached.
        while (!found && inputLine != null) {
          
          // Search the line of HTML for the property name tag.
          index = inputLine.indexOf("property=\"name\"");
          
          // If the indexOf function finds the tag get the name.
          if (index != -1) {
            found = true;
            inputLine = inputLine.substring(index+16);
            endIndex = inputLine.indexOf("<");
            name = inputLine.substring(0, endIndex);
          }
          inputLine = URLReader.readLine();
        }
      }
      
      // Check to see whether a name was found.
      if (name.equals("")) {
        System.out.println("ID: " + id + " does not have a webpage.");
      } else {
        System.out.println("ID: " + id + " belongs to " + name);
      }
      
      // Setup for another ID entry.
      System.out.println("\n##################################\n");
      System.out.println("Enter the ID you want to search for or Q to quit:");
      id = in.readLine();
    }
  }
}

