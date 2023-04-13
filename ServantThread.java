import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServantThread extends Thread {
    // Shared PresentBag representing the unordered bag of presents
    private PresentBag presentBag;
    // Shared head of the linked list
    private Present head;
    // True if current task is adding presents to list, false if current task is removing presents from list
    private boolean addingPresents = true;
    // Number of thank-you notes this servant has written
    private AtomicInteger numNotes;

    // Constructor
    public ServantThread(PresentBag presentBag, Present head) {
        this.presentBag = presentBag;
        this.head = head;
        this.numNotes = new AtomicInteger(0);
    }

    // Method to take present from unordered bag and add it to the linked list
    private void addPresent() {
        Present present = presentBag.removePresent();
        if (present != null) {
            head.addPresent(present);
        }
    }

    // Method to write a thank-you note and remove present from linked list
    private void removePresent() {
        if (head.getNext() != null) {
            head.writeNoteAndRemovePresent(numNotes);
        }
    }

    // Method to check whether a given present is in the linked list
    // Returns true if the present with the given num is in the list, false otherwise
    public boolean checkForPresent(int target) {
        return head.checkForPresent(target);
    }

    // Helper method to get number of thank-you notes written
    public int getNumNotes() {
        return numNotes.get();
    }

    @Override
    public void run() {
        while (presentBag.getSize() > 0) {
            addPresent();
            removePresent();
        }
    }
}
