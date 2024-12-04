import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets; // List to hold tickets
    private final int maxCapacity;     // Maximum number of tickets

    public TicketPool(int maxCapacity) {
        this.tickets = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    // Adds a ticket to the pool if there's room
    public synchronized void addTicket(String ticket) {
        while (tickets.size() >= maxCapacity) {
            try {
                wait(); // Wait until there's space in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor interrupted while waiting to add ticket.");
                return;
            }
        }
        tickets.add(ticket); // Add ticket to the list
        notifyAll();         // Notify waiting customers
    }

    // Removes a ticket from the pool if available
    public synchronized String removeTicket() {
        while (tickets.isEmpty()) {
            try {
                wait(); // Wait until tickets are available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer interrupted while waiting to retrieve ticket.");
                return null;
            }
        }
        String ticket = tickets.remove(0); // Remove ticket from the front of the list
        notifyAll();                       // Notify waiting vendors
        return ticket;
    }

    // Gets the current number of available tickets
    public synchronized int getAvailableTickets() {
        return tickets.size();
    }
}
