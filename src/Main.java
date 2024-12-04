import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Load configuration
        Configuration configuration = null;
        try {
            configuration = new Configuration();
            configuration.loadConfiguration();
        } catch (Exception e) {
            System.out.println("Error loading configuration");
            System.exit(1);
        }

        // Create ticket pool
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity());

        // Lists to hold vendor and customer threads
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        // Create and start vendors
        for (int i = 1; i <= configuration.getTicketReleaseRate(); i++) { // Example: 2 vendors
            Vendor vendor = new Vendor(i, ticketPool, configuration.getTicketReleaseRate(), configuration.getTotalTickets());
            vendors.add(vendor);
            vendor.start();
        }

        // Create and start customers
        for (int i = 1; i <= configuration.getCustomerRetrievalRate(); i++) { // Example: 5 customers
            Customer customer = new Customer(i, ticketPool, configuration.getCustomerRetrievalRate());
            customers.add(customer);
            customer.start();
        }

        // Add a shutdown hook to gracefully stop threads
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down the system...");
            vendors.forEach(Vendor::stopRunning);
            customers.forEach(Customer::stopRunning);
            System.out.println("All threads stopped. Goodbye!");
        }));

        // Keep the main thread alive to allow interaction
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            System.out.println("\nPress ENTER to stop the system...");
            scanner.nextLine();
        }

        // Stop all threads when the user exits
        vendors.forEach(Vendor::stopRunning);
        customers.forEach(Customer::stopRunning);

        System.out.println("System stopped.");
    }
}
