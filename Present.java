import java.util.*;
import java.lang.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.FileWriter;
import java.io.IOException;

public class Present {
    // Unique number associated with present
    private int num;
    // Pointers to prev and next presents in the linked list
    private Present next;
    // FileWriter to write to file
    FileWriter writer;

    // Constructor
    public Present(int num, FileWriter writer) {
        this.num = num;
        this.next = null;
        this.writer = writer;
    }

    ///////////////////////////
    // NODE UTILITY FUNCTIONS
    ///////////////////////////

    // Helper method to get num
    public int getNum() {
        return this.num;
    }

    // Helper method to set next
    public void setNext(Present newNext) {
        this.next = newNext;
    }

    // Helper method to get next
    public Present getNext() {
        return this.next;
    }

    //////////////////////////////////
    // LINKED LIST UTILITY FUNCTIONS
    //////////////////////////////////

    // Recursive method to add a given present to the list
    // Returns true if the present was successfully placed, false otherwise
    public synchronized boolean addPresent(Present present) {
        // If the number of the present we're at is lower than the number of the present we want to add, keep going
        if (num < present.getNum()) {
            // If we're at the last element, we have to add it here
            if (next == null) {
                next = present;
                try {
                    writer.write("0 " + present.getNum() + "\n");
                } catch (IOException e) {
                    e.getStackTrace();
                }
                return true;
            }

            boolean added = next.addPresent(present);

            // If we fail to add it in the future, it must belong here, add it and return true
            if (!added) {
                Present oldNext = next;
                setNext(present);
                present.setNext(oldNext);
                return true;
            }

            // Otherwise, we successfully added it in the future, return true
            return true;
        }

        // Otherwise, we've gone too far, return false
        return false;
    }

    // Method to write a thank-you note and remove the first present from the list
    public synchronized void writeNoteAndRemovePresent(AtomicInteger numNotes) {
        if (next == null) {
            return;
        }
        Present present = next;

        // Write thank-you note
        numNotes.incrementAndGet();

        // Set the new first present to be the present after the one we're removing
        next = present.getNext();

        try {
            writer.write("1 " + present.getNum() + "\n");
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    // Recursive method to check if a present exists in the list
    // Returns true if the present with the given num is in the sublist with head at, false otherwise
    public synchronized boolean checkForPresent(int target) {
        // If we're already past the target, it's not in the list, so return false
        if (num > target) {
            return false;
        }

        // If we've found the target, return true
        if (num == target) {
            return true;
        }

        // If we're at the last element in the list, our target's not in the list, so return false
        if (next == null) {
            return false;
        }

        // Otherwise, continue to the next element in the list
        return next.checkForPresent(target);
    }

    // Recursive method to convert the linked list that is headed by this present to a string
    @Override
    public synchronized String toString() {
        String s = "";

        // If this isn't the head, add it to the string (the head is just a symbolic placeholder)
        if (num != -1) {
            s += num + " ";
        }

        // If this is the last element in the list, return s
        if (next == null) {
            return s;
        }

        // Otherwise, continue down the list
        return s + next.toString();
    }
}
