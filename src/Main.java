import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        // Load configuration
        Configuration configuration = null;

        try {
            // Try to load configuration from JSON
            configuration = Configuration.loadFromJson();
            System.out.println("Loaded configuration from JSON: " + configuration);
        } catch (IOException e) {
            // If loading fails, prompt the user for inputs
            System.out.println("Configuration file not found or invalid. Please provide configuration inputs.");
            configuration = new Configuration();
            configuration.loadConfiguration();
        }

        // Create ticket pool
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity());

        // Lists to hold vendor and customer threads
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        // Create and start vendors
        for (int i = 1; i <= configuration.getTicketReleaseRate(); i++) {
            Vendor vendor = new Vendor(i, ticketPool, configuration.getTicketReleaseRate(), configuration.getTotalTickets());
            vendors.add(vendor);
            vendor.start();
        }

        // Create and start customers
        for (int i = 1; i <= configuration.getCustomerRetrievalRate(); i++) {
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
            String input;
            boolean running = true;
            System.out.println("\nEnter 3 to stop, 2 to pause, and 1 to resume the system...");
            while (running) {
                input = scanner.nextLine();
                switch (input) {
                    case "3":
                        System.out.println("Stopping the system...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid input. Enter 3 to stop, 2 to pause, and 1 to resume.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error during system interaction: " + e.getMessage());
        }

        // Stop all threads when the user exits
        vendors.forEach(Vendor::stopRunning);
        customers.forEach(Customer::stopRunning);

        // Save configuration back to JSON for future use
        try {
            configuration.saveToJson();
            System.out.println("Configuration saved to config.json.");
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }

        System.out.println("System stopped.");
    }
}
