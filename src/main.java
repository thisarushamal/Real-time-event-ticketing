import java.sql.SQLOutput;
import java.util.Scanner;

public class main {
    static Configuration configuration;
    public static void main(String[] args) {
        configurSystem();
    }
    public static void configurSystem(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the total ticket count : ");
        int totalTickets = scanner.nextInt();
        System.out.println("Enter the ticket release rate : ");
        int ticketReleaseRate = scanner.nextInt();
        System.out.println("Enter the customer retrieval rate : ");
        int customerRetrievalRate = scanner.nextInt();
        System.out.println("Enter the maximum ticket capacity : ");
        int maxTicketCapacity = scanner.nextInt();
        Configuration configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        System.out.println("system configured successfully" + configuration);
    }
}
