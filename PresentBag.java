import java.util.*;
import java.io.*;
import java.io.FileWriter;

public class PresentBag {
    // ArrayDeque representing contents of bag
    private ArrayDeque<Present> presents;
    // FileWriter to write to file
    FileWriter writer;

    // Constructor
    public PresentBag(FileWriter writer) {
        this.presents = new ArrayDeque<Present>();
        this.writer = writer;
    }

    // Method to fill the bag with numPresents presents in a random order
    public void fillBag(int numPresents) {
        // Shuffle numbers 0 to numPresents-1 using an ArrayList
        ArrayList<Integer> presentNums = new ArrayList<>();
        for (int i = 0; i < numPresents; i++) {
            presentNums.add(i);
        }
        Collections.shuffle(presentNums);

        // Put shuffled list in ArrayDeque
        for (int i : presentNums) {
            presents.add(new Present(i, writer));
        }
    }

    // Method to remove the next present from the bag
    public synchronized Present removePresent() {
        if (presents.size() == 0) return null;
        return presents.poll();
    }

    // Helper method to get size
    public synchronized int getSize() {
        return presents.size();
    }

    // Helper method to represent the contents of the bag as a string
    @Override
    public synchronized String toString() {
        String s = "";
        for (Present present : presents) {
            s += present.getNum() + " ";
        }
        return s;
    }
}
