import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = null;
        File configFile = new File("config.json");

        try {
            if (configFile.exists()) {
                try {
                    Configuration existingConfig = Configuration.loadFromJson();
                    System.out.println("Found existing configuration: " + existingConfig);

                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    String choice = "";

                    // Loop until the user enters a valid response
                    while (true) {
                        System.out.println("Do you want to continue with current config? (Y/N)");
                        choice = scanner.nextLine().trim();

                        if (choice.equalsIgnoreCase("Y")) {
                            configuration = existingConfig;
                            System.out.println("Continuing with existing configuration...");
                            break; // Exit the loop on valid input
                        } else if (choice.equalsIgnoreCase("N")) {
                            System.out.println("Please enter new configuration values:");
                            configuration = new Configuration();
                            configuration.loadConfiguration();
                            break; // Exit the loop on valid input
                        } else {
                            System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading configuration file: " + e.getMessage());
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

            final boolean[] running = {true};

            Thread interactionThread = new Thread(() -> {
                try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                    System.out.println("\nEnter 3 to stop the system...");

                    while (running[0]) {
                        String input = scanner.nextLine().trim();
                        switch (input) {
                            case "3":
                                System.out.println("Stopping the system...");
                                running[0] = false;
                                break;
                            default:
                                System.out.println("Invalid input. Enter only 3 to stop the system.");
                                break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error in interaction thread: " + e.getMessage());
                }
            });

            interactionThread.start();

            try {
                interactionThread.join();
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

                configuration.saveToJson();
                System.out.println("Configuration saved to config.json");
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted.");
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        System.out.println("System stopped.");
    }
}
