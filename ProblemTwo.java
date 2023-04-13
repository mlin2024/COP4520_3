import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProblemTwo {
    // Number of sensors
    private static int numSensors = 8;
    // How many milliseconds 1 simulated minute takes
    private static int msPerMinute = 100;

    public static void main(String[] args) {
        // Number of "hours" to record data for
        int hours = Integer.parseInt(args[0]);

        for (int hour = 0; hour < hours; hour++) {
            System.out.println("Hour " + (hour + 1) + ":");

            // Create a semaphore with permit count of 1
            Semaphore semaphore = new Semaphore(1);

            // Create a flag to indicate whether the current minute has been recorded yet or not
            AtomicBoolean shouldRecord = new AtomicBoolean(false);

            // Initalize an array to store hourly data
            double[] temps = new double[60];

            // Make a thread for each sensor
            long startTime = System.currentTimeMillis();
            SensorThread[] threads = new SensorThread[numSensors];
            for (int i = 0; i < numSensors; i++) {
                threads[i] = new SensorThread(temps, startTime, msPerMinute, semaphore, shouldRecord);
            }

            // Start all threads
            for (int i = 0; i < numSensors; i++) {
                threads[i].start();
            }

            // Simulate an hour
            for (int i = 0; i < 60; i++) {
                try {
                    // Set flag to true to allow one thread to record this minute
                    shouldRecord.getAndSet(true);
                    // Release a permit to wake up one thread
                    semaphore.release();
                    // Wait for 1 "minute"
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Perform analytics
            performHourlyAnalytics(temps);
            System.out.println();
        }
    }

    // Method to perform all analytics
    public static void performHourlyAnalytics(double[] temps) {
        calculateLargestTempDiff(temps);
        calculateExtremes(temps);
    }

    // Method to calculate 10-minute interval of largest temp difference
    private static void calculateLargestTempDiff(double[] temps) {
        // Pointers to store beginning and end of interval
        int a = 0, b = 0;

        // Keep track of both actual and absolute value, absolute value used to compare actual magnitude
        double maxDiff = 0;
        double absMaxDiff = 0;

        for (int i = 0; i < 50; i++) {
            double diff = temps[i] - temps[i+10];
            double absDiff = Math.abs(diff);
            // If we find an interval with a greater differential than we currently have stored, make this the new interval of greatest change
            if (absDiff > absMaxDiff) {
                a = i;
                b = i + 10;
                maxDiff = diff;
                absMaxDiff = absMaxDiff;
            }
        }
        System.out.printf("   The largest 10-minute temperature difference was %.3fF, which occurred between %d and %d minutes.\n\n", maxDiff, a, b);
    }

    // Method to calculate 5 hottest and coldest temps
    private static void calculateExtremes(double[] temps) {
        // Sort a clone of the array
        double[] sorted = temps.clone();
        Arrays.sort(sorted);

        System.out.println("   Top 5 Coldest Temperatures:");
        for (int i = 4; i >= 0; i--) {
            System.out.println("\t" + sorted[i]);
        }

        System.out.println("\n   Top 5 Hottest Temperatures:");
        for (int i = 55; i < 60; i++) {
            System.out.println("\t" + sorted[i]);
        }
    }
}
