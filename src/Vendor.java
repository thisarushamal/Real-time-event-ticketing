public class Vendor extends Thread {
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final int totalTickets;
    private boolean Running = true;

    public Vendor(int vendorId, TicketPool ticketPool, int releaseRate, int totalTickets) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        System.out.println("Vendor " + vendorId + " started");

        while (Running) {
            try {
                Thread.sleep(1000);
                for (int i = 0; i < releaseRate; i++) {
                    if (ticketPool.getAvailableTickets() < totalTickets) {
                        String ticket = "Ticket" + ticketPool.getAvailableTickets();
                        ticketPool.addTicket(ticket);
                        System.out.println("Vendor " + vendorId + " released ticket: " + ticket);
                    } else {
                        System.out.println("Vendor " + vendorId + " failed to release ticket");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " interrupted");
            }
        }
        System.out.println("Vendor " + vendorId + " stopped");
    }

    public void stopRunning() {
        Running = false;
    }

}
