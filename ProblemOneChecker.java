import java.util.*;
import java.io.*;

public class ProblemOneChecker {
    public static void main(String[] args) {
        try {
            File file = new File("output.txt");
            Scanner scanner = new Scanner(file);

            // Create TreeSet to keep track of what's in the list
            TreeSet<Integer> ts = new TreeSet<>();

            // Keep track of if output is consistent so far
            boolean consistent = true;

            while (scanner.hasNextInt()) {
                int query = scanner.nextInt();
                if (query == 0) { // Added number to list
                    int added = scanner.nextInt();
                    ts.add(added);
                }
                if (query == 1) { // Removed number from list
                    int removed = scanner.nextInt();
                    ts.remove(removed);
                }
                if (query == 2) { // Asked if number is in list
                    int asked = scanner.nextInt();
                    int response = scanner.nextInt();
                    boolean contained = ts.contains(asked);
                    // If contradiction, output is not internally consistent
                    if ((contained && (response == 0)) || (!contained && (response == 1))) consistent = false;
                }
                if (!consistent) break;
            }

            if (!consistent) System.out.println("ProblemOne output is not internally consistent.");
            else System.out.println("ProblemOne output is internally consistent.");

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Output file doesn't exist, run ProblemOne first.");
            e.printStackTrace();
        }
    }
}
