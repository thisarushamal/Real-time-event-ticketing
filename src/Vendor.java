public class Vendor extends Thread {
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final int totalTickets;
    private volatile boolean running = true;

    public Vendor(int vendorId, TicketPool ticketPool, int releaseRate, int totalTickets) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        System.out.println("Vendor " + vendorId + " started");
        while (running) {
            try {
                Thread.sleep(1000); // Simulate some work
                for (int i = 0; i < releaseRate; i++) {
                    if (!running) break;
                    if (ticketPool.getAvailableTickets() < totalTickets) {
                        String ticket = "Ticket" + ticketPool.getAvailableTickets();
                        ticketPool.addTicket(ticket);
                        System.out.println("Vendor " + vendorId + " released ticket: " + ticket);
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " interrupted");
                break;
            }
        }
        System.out.println("Vendor " + vendorId + " stopped");
    }

    public void stopRunning() {
        running = false;
        this.interrupt(); // Interrupt the thread if it's blocked
    }
}
