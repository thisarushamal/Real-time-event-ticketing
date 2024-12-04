import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;


    private int maxTicketCapacity;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public Configuration() {

    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void loadConfiguration() {

        System.out.println("*****Real time event ticketing system*****");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the total number of tickets: ");
            totalTickets = scanner.nextInt();
            if (totalTickets > 0) {
                break;
            }
            System.out.println("Total tickets must be greater than 0");
        }
        while (true) {
            System.out.println("Enter the ticket release rate: ");
            ticketReleaseRate = scanner.nextInt();
            if (ticketReleaseRate > 0) {
                break;
            }
            System.out.println("Ticket release rate must be greater than 0");
        }
        while (true) {
            System.out.println("Enter the customer retrieval rate: ");
            customerRetrievalRate = scanner.nextInt();
            if (customerRetrievalRate > 0) {
                break;
            }
            System.out.println("Customer retrieval rate must be greater than 0");
        }
        while (true) {
            System.out.println("Enter the maximum ticket capacity: ");
            maxTicketCapacity = scanner.nextInt();
            if (maxTicketCapacity > 0) {
                break;
            }
            System.out.println("Maximum ticket capacity must be greater than 0");
        }

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
