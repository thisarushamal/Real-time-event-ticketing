import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Load configuration
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
                    System.out.println("Continuing with existing configuration...");
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

        // Create a separate thread for user interaction
        Thread interactionThread = new Thread(() -> {
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                boolean running = true;
                System.out.println("\nEnter 3 to stop, 2 to pause, and 1 to resume the system...");

                while (running) {
                    String input = scanner.nextLine().trim();
                    switch (input) {
                        case "3":
                            System.out.println("Stopping the system...");
                            running = false;
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

        // Start the interaction thread
        interactionThread.start();

        // Wait for the interaction thread to finish
        try {
            interactionThread.join();

            // Stop all threads and wait for them to finish
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
            System.out.println("Main thread interrupted: " + e.getMessage());
        }
    }
}
