import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class SensorThread extends Thread {
    // Shared array to store temps
    private double[] temps;
    // Time that the "hour" started
    private long startTime;
    // Time that the "hour" should end
    private long endTime;
    // How many milliseconds 1 simulated minute takes
    private int msPerMinute;
    // Shared semaphore
    private Semaphore semaphore;
    // Shared flag to indicate whether the current minute has been recorded yet or not
    AtomicBoolean shouldRecord;

    // Constructor
    public SensorThread(double[] temps, long startTime, int msPerMinute, Semaphore semaphore, AtomicBoolean shouldRecord) {
        this.temps = temps;
        this.startTime = startTime;
        this.endTime = startTime + (msPerMinute * 60); // one hour = msPerMinute * 60
        this.msPerMinute = msPerMinute;
        this.semaphore = semaphore;
        this.shouldRecord = shouldRecord;
    }

    // Method to record the current temp
    private void recordTemp(int minute) {
        // Generate a random temp between -100 and 70
        double temp = (Math.random() * 170) - 100;
        // Record temp in the data
        temps[minute] = temp;
    }

    // Helper method to determine if current thread should run recordTemp()
    private boolean shouldRecord() {
        return shouldRecord.getAndSet(false);
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() < endTime) {
            try {
                // Acquire the permit from the semaphore
                semaphore.acquire();
                // Check again after acquiring the permit to make sure it's still needed
                int minute = (int) ((System.currentTimeMillis() - startTime) / msPerMinute);
                if (minute >= 0 && minute < 60 && shouldRecord()) {
                    recordTemp(minute);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Release the permit back to the semaphore
                semaphore.release();
            }
        }
    }
}
