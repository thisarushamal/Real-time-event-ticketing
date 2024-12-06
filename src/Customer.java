public class Customer extends Thread {

    private final int customerId;
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private boolean Running = true;

    public Customer(int customerId, TicketPool ticketPool, int retrievalRate) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }


    @Override
    public void run() {
        System.out.println("Customer " + customerId + " started");

        while (Running) {
            try {
                Thread.sleep(1000);
                for (int i = 0; i < retrievalRate; i++) {
                    String ticket = ticketPool.removeTicket();
                    if (ticket != null) {
                        System.out.println("Customer " + customerId + " got ticket: " + ticket);
                    }else {
                        System.out.println("Customer" + customerId + "failed to get ticket");
                        break;
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " interrupted");
            }
        }
        System.out.println("Customer " + customerId + " stopped");
    }
    public void stopRunning() {
        Running = false;
    }

}
