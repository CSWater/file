package ticketingsystem;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	static int retpc = 10; // return ticket operation is 10% percent
    static int buypc = 40; // buy ticket operation is 30% percent
    static int inqpc = 100; //inquiry ticket operation is 60% percent
	public static void main(String[] args) throws InterruptedException {
		//get params
		final int threadnum = Integer.valueOf(args[0]);
		final int routenum = Integer.valueOf(args[1]);
		final int coachnum = Integer.valueOf(args[2]);
		final int seatnum = Integer.valueOf(args[3]);
		final int stationnum = Integer.valueOf(args[4]);
		final int test_num = Integer.valueOf(args[5]);
		
		Thread[] threads = new Thread[threadnum];
		final TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);
		
		long starttime = System.nanoTime();	
		for (int i = 0; i< threadnum; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    Random rand = new Random();
                    int call_count = 0;
                    Ticket ticket = new Ticket();
                    ArrayList<Ticket> soldTicket = new ArrayList<Ticket>();
                    while (call_count < test_num) {
                        int sel = rand.nextInt(inqpc);
                        if (0 <= sel && sel < retpc && soldTicket.size() > 0) { 								// refund ticket
                            int select = rand.nextInt(soldTicket.size());
                            if ((ticket = soldTicket.remove(select)) != null) {
                            	tds.refundTicket(ticket);
                            	call_count++;
                            } 
                        } 
                        else if (retpc <= sel && sel < buypc) { 												// buy ticket
                            String passenger = ThreadId.get() + "passenger" + call_count;
                            int route = 1;
                            int departure = rand.nextInt(stationnum - 1) + 1;
                            int arrival = departure + rand.nextInt(stationnum - departure) + 1; 				// arrival is always greater than departure
                            ticket = tds.buyTicket(passenger, route, departure, arrival);
                            call_count++;
                            if (ticket != null) 
                                soldTicket.add(ticket);          
                        } 
                        else if (buypc <= sel && sel < inqpc) {													//query ticket
                            int route = rand.nextInt(routenum) + 1;
                            int departure = rand.nextInt(stationnum - 1) + 1;
                            int arrival = departure + rand.nextInt(stationnum - departure) + 1; 				// arrival is always greater than departure
                            tds.inquiry(route, departure, arrival);
                            call_count++;
                        }
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i< threadnum; i++) {
            threads[i].join();
        }
        double timecount = (System.nanoTime()-starttime)/1000000000.0;//total time
        
        System.out.println("total time(s):"+timecount);
        System.out.println("sold-tickets count(include those have been refund):"+tds.tid);
        System.out.println("throughtput(time/s):"+  ( threadnum * test_num ) / timecount );
	}

}
