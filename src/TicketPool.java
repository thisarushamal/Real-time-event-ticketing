import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets; // List to hold tickets
    private final int maxCapacity;     // Maximum number of tickets

    public TicketPool(int maxCapacity, int initialTickets) {
        this.tickets = new ArrayList<>();
        this.maxCapacity = maxCapacity;

        // Initialize the ticket pool with the initial tickets
        for (int i = 0; i < initialTickets; i++) {
            tickets.add("Ticket" + i);
        }
    }

    // Adds tickets to the pool if there's room
    public synchronized void addTickets(int count, int vendorId) {
        int addedCount = 0;
        while (tickets.size() < maxCapacity && addedCount < count) {
            String ticket = "Ticket" + (tickets.size() + 1);
            tickets.add(ticket);
            addedCount++;
        }
        System.out.println("Vendor-" + vendorId + " added " + addedCount + " tickets. Total tickets: " + tickets.size());
        notifyAll(); // Notify waiting customers
    }

    // Removes tickets from the pool if available
    public synchronized List<String> removeTickets(int count, int customerId) {
        List<String> purchasedTickets = new ArrayList<>();
        while (tickets.isEmpty()) {
            try {
                wait(); // Wait until tickets are available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer interrupted while waiting to retrieve tickets.");
                return purchasedTickets;
            }
        }

        for (int i = 0; i < count && !tickets.isEmpty(); i++) {
            purchasedTickets.add(tickets.remove(0));
        }

        System.out.println("Customer-" + customerId + " purchased " + purchasedTickets.size() + " tickets: " +
                String.join(", ", purchasedTickets) + ". Remaining tickets: " + tickets.size());
        notifyAll(); // Notify waiting vendors
        return purchasedTickets;
    }

    // Gets the current number of available tickets
    public synchronized int getAvailableTickets() {
        return tickets.size();
    }
}

