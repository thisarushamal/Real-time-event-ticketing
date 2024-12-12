import java.util.List;

public class Customer extends Thread {
    private final int customerId;
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private volatile boolean running = true;

    public Customer(int customerId, TicketPool ticketPool, int retrievalRate) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        System.out.println("Customer " + customerId + " started");
        while (running) {
            try {
                Thread.sleep(1000); // Simulate one second delay
                ticketPool.removeTickets(retrievalRate, customerId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " interrupted");
                break;
            }
        }
        System.out.println("Customer " + customerId + " stopped");
    }

    public void stopRunning() {
        running = false;
        this.interrupt();
    }
}
