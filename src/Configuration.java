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
            System.out.println("Enter the total number of tickets: ");
            totalTickets = scanner.nextInt();
            System.out.println("Enter the ticket release rate (tickets per second): ");
            ticketReleaseRate = scanner.nextInt();
            System.out.println("Enter the customer retrieval rate (tickets per second): ");
            customerRetrievalRate = scanner.nextInt();
            System.out.println("Enter the maximum ticket capacity: ");
            maxTicketCapacity = scanner.nextInt();

            if (totalTickets > maxTicketCapacity) {
                System.out.println("Error: Total tickets cannot exceed maximum ticket capacity.");
                System.out.println("Please re-enter the configurations.");
            } else {
                break;
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
        return objectMapper.readValue(new File(CONFIG_FILE), Configuration.class);
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
