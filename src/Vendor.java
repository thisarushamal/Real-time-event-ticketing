public class Vendor extends Thread {
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int releaseRate;
    private volatile boolean running = true;

    public Vendor(int vendorId, TicketPool ticketPool, int releaseRate) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        System.out.println("Vendor " + vendorId + " started");
        while (running) {
            try {
                Thread.sleep(1000); // Simulate one second delay
                ticketPool.addTickets(releaseRate, vendorId);
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
        this.interrupt();
    }
}
