import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    private static final String CONFIG_FILE = "config.json";

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public Configuration() {}

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Load configuration interactively from user
    public void loadConfiguration() {
        System.out.println("***** Real-time Event Ticketing System *****");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Enter the total number of tickets(Current number of tickets in TicketPool): ");
                totalTickets = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Enter the ticket release rate (Number of tickets release per second): ");
                ticketReleaseRate = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Enter the customer retrieval rate (Number of tickets purchase per second): ");
                customerRetrievalRate = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("Enter the maximum ticket capacity: ");
                maxTicketCapacity = Integer.parseInt(scanner.nextLine().trim());

                if (totalTickets > maxTicketCapacity) {
                    throw new IllegalArgumentException("Total tickets cannot exceed maximum ticket capacity.");
                }
                if (totalTickets < 0 || ticketReleaseRate < 0 || customerRetrievalRate < 0 || maxTicketCapacity < 0) {
                    throw new IllegalArgumentException("Values must be non-negative.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Save configuration to JSON file after input
        try {
            saveToJson();
            System.out.println("Configuration saved to " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    // Save configuration to JSON file
    public void saveToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(CONFIG_FILE), this);
    }

    // Load configuration from JSON file
    public static Configuration loadFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            throw new IOException("Configuration file not found.");
        }
        return objectMapper.readValue(configFile, Configuration.class);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
