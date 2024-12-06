import java.util.List;
import java.util.Scanner;

public class ControlManager {
    private volatile boolean isPaused = false;
    private volatile boolean isRunning = true;

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void controlThreads(List<Vendor> vendors, List<Customer> customers) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (isRunning) {
                System.out.println("Enter 1 to start/resume, 2 to pause, 3 to exit:");
                int command = scanner.nextInt();

                switch (command) {
                    case 1:
                        System.out.println("Resuming system...");
                        resumeThreads(vendors, customers);
                        break;
                    case 2:
                        System.out.println("Pausing system...");
                        pauseThreads(vendors, customers);
                        break;
                    case 3:
                        System.out.println("Exiting system...");
                        stopThreads(vendors, customers);
                        break;
                    default:
                        System.out.println("Invalid command. Please enter 1, 2, or 3.");
                }
            }
        }
    }

    private void pauseThreads(List<Vendor> vendors, List<Customer> customers) {
    }

    private void resumeThreads(List<Vendor> vendors, List<Customer> customers) {
    }

    private void pauseThreads() {
        isPaused = true;
    }

    private void resumeThreads() {
        isPaused = false;
        synchronized (this) {
            notifyAll(); // Notify threads waiting for resume
        }
    }

    private void stopThreads(List<Vendor> vendors, List<Customer> customers) {
        isRunning = false;
        vendors.forEach(Vendor::stopRunning);
        customers.forEach(Customer::stopRunning);
    }

    public synchronized void waitForResume() {
        while (isPaused) {
            try {
                wait(); // Wait until notified to resume
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted while paused.");
            }
        }
    }
}
