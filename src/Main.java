import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = null;
        File configFile = new File("config.json");

        if (configFile.exists()) {
            try {
                Configuration existingConfig = Configuration.loadFromJson();
                System.out.println("Found existing configuration: " + existingConfig);
                System.out.println("Do you want to continue with current config? (Y/N)");

                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String choice = scanner.nextLine().trim();

                if (choice.equalsIgnoreCase("Y")) {
                    configuration = existingConfig;
                } else {
                    System.out.println("Please enter new configuration values:");
                    configuration = new Configuration();
                    configuration.loadConfiguration();
                }
            } catch (IOException e) {
                System.out.println("Error reading configuration file. Starting with new configuration.");
                configuration = new Configuration();
                configuration.loadConfiguration();
            }
        } else {
            System.out.println("No existing configuration found. Please enter new configuration values:");
            configuration = new Configuration();
            configuration.loadConfiguration();
        }

        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());

        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i <= configuration.getTicketReleaseRate(); i++) {
            Vendor vendor = new Vendor(i, ticketPool, configuration.getTicketReleaseRate());
            vendors.add(vendor);
            vendor.start();
        }

        for (int i = 1; i <= configuration.getCustomerRetrievalRate(); i++) {
            Customer customer = new Customer(i, ticketPool, configuration.getCustomerRetrievalRate());
            customers.add(customer);
            customer.start();
        }

        // Shared flag to signal when to stop the system
        final boolean[] running = {true};

        // Interaction thread to listen for user input
        Thread interactionThread = new Thread(() -> {
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                System.out.println("\nEnter 3 to stop, 2 to pause, and 1 to resume the system...");

                while (running[0]) {
                    String input = scanner.nextLine().trim();
                    switch (input) {
                        case "3":
                            System.out.println("Stopping the system...");
                            running[0] = false; // Signal to stop
                            break;
                        case "2":
                            System.out.println("System paused. Press 1 to resume or 3 to stop.");
                            break;
                        case "1":
                            System.out.println("System resumed.");
                            break;
                        default:
                            System.out.println("Invalid input. Enter 3 to stop, 2 to pause, and 1 to resume.");
                            break;
                    }
                }
            }
        });

        interactionThread.start();

        // Main thread waits for the interaction thread to stop
        try {
            interactionThread.join();

            // Stop all vendor and customer threads
            vendors.forEach(vendor -> {
                vendor.stopRunning();
                try {
                    vendor.join();
                } catch (InterruptedException e) {
                    System.out.println("Error waiting for vendor to stop: " + e.getMessage());
                }
            });

            customers.forEach(customer -> {
                customer.stopRunning();
                try {
                    customer.join();
                } catch (InterruptedException e) {
                    System.out.println("Error waiting for customer to stop: " + e.getMessage());
                }
            });

            // Save the final configuration
            try {
                configuration.saveToJson();
                System.out.println("Configuration saved to config.json");
            } catch (IOException e) {
                System.out.println("Failed to save configuration: " + e.getMessage());
            }

            System.out.println("System stopped.");
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
    }
}
