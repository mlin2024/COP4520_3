import java.util.*;
import java.io.*;
import java.io.FileWriter;

public class ProblemOne {
    // Number of presents received
    public static int numPresents = 500000;
    // Number of servants
    public static int numServants = 4;

    public static void main(String[] args) {
        // Create output file and filewriter
        try {
            FileWriter writer = new FileWriter("output.txt");

            // Initalize and fill PresentBag to represent the unordered bag of presents
            PresentBag presentBag = new PresentBag(writer);
            presentBag.fillBag(numPresents);

            // Initialize the head of the linked list (not representing a real present, only used as head of linked list)
            Present head = new Present(-1, writer);

            // Make a thread for each servant
            ServantThread[] threads = new ServantThread[numServants];
            for (int i = 0; i < numServants; i++) {
                threads[i] = new ServantThread(presentBag, head);
            }

            // Start all threads
            for (int i = 0; i < numServants; i++) {
                threads[i].start();
            }

            while (presentBag.getSize() > 0 || head.getNext() != null) {
                // Minotaur waits a random amount of time from 3 to 12 ms
                try {
                    Thread.sleep((int) ((Math.random() * 12) + 3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Minotaur asks a random servant to check if a random present is in the list
                int randomServant = (int) (Math.random() * numServants);
                int randomPresent = (int) (Math.random() * numPresents);
                boolean inList = threads[randomServant].checkForPresent(randomPresent);
                writer.write("2 " + randomPresent + " " + (inList ? "1" : "0") + "\n");
            }

            // Get the total number of thank-you notes written
            int numNotes = 0;
            for (int i = 0; i < numServants; i++) {
                numNotes += threads[i].getNumNotes();
            }

            System.out.println("Number of presents received:       " + numPresents);
            System.out.println("Number of thank-you notes written: " + numNotes);
            for (int i = 0; i < numServants; i++) {
                System.out.println("\tServant #" + (i+1) + " wrote " + threads[i].getNumNotes() + " thank-you notes.");
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
