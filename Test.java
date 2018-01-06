package ticketingsystem;

import java.util.ArrayList;
import java.util.Random;

public class Test {
	static int retpc = 10; // return ticket operation is 10% percent
    static int buypc = 40; // buy ticket operation is 30% percent
    static int inqpc = 100; //inquiry ticket operation is 60% percent
	public static void main(String[] args) throws InterruptedException {
		
		
		final int threadnum = Integer.valueOf(args[0]);
		final int routenum = Integer.valueOf(args[1]);
		final int coachnum = Integer.valueOf(args[2]);
		final int seatnum = Integer.valueOf(args[3]);
		final int stationnum = Integer.valueOf(args[4]);
		final int testnum = Integer.valueOf(args[5]);
		
		Thread[] threads = new Thread[threadnum];
		final TicketingDS tds = new TicketingDS(routenum, coachnum, seatnum, stationnum, threadnum);
		final double[] latency = new double[256];
		for(int i = 0; i < 256; ++i) {
			latency[i] = 0.0;
		}
		//begin timing
		long starttime = System.nanoTime();		
		for (int i = 0; i< threadnum; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    Random rand = new Random();
                    Ticket ticket = new Ticket();
                    ArrayList<Ticket> soldTicket = new ArrayList<Ticket>();
                    long local_start = System.nanoTime();
                    for (int i = 0; i < testnum; i++) {
                        int sel = rand.nextInt(inqpc);
                        if (0 <= sel && sel < retpc && soldTicket.size() > 0) { // refund ticket
                            int select = rand.nextInt(soldTicket.size());
                            if ((ticket = soldTicket.remove(select)) != null) {
                            	tds.refundTicket(ticket);
                            } 
                        } 
                        else if (retpc <= sel && sel < buypc) { 				// buy ticket
                            String passenger = ThreadId.get() + "passenger" + i;
                            int route = rand.nextInt(routenum)+ 1;
                            int departure = rand.nextInt(stationnum - 1) + 1;
                            int arrival = departure + rand.nextInt(stationnum - departure) + 1; // arrival is always greater than departure
                            ticket = tds.buyTicket(passenger, route, departure, arrival);
                            if (ticket != null) 
                                soldTicket.add(ticket);          
                        } 
                        else if (buypc <= sel && sel < inqpc) { 				// inquiry ticket
                            int route = rand.nextInt(routenum) + 1;
                            int departure = rand.nextInt(stationnum - 1) + 1;
                            int arrival = departure + rand.nextInt(stationnum - departure) + 1; // arrival is always greater than departure
                            tds.inquiry(route, departure, arrival);
                        }
                    }
                    long idx = Thread.currentThread().getId();
                    latency[(int)idx] = (System.nanoTime()-local_start)/1000000000.0;
                }
            });
            threads[i].start();
        }
        for (int i = 0; i< threadnum; i++) {
            threads[i].join();
        }
        double run_time = (System.nanoTime()-starttime)/1000000000.0;								//total time
        double throughput = (threadnum * testnum) / run_time;
        double total_time = 0.0;
        for(int i = 0; i < 256; ++i) {
        	total_time += latency[i];
        }
        double call_latency = total_time / (threadnum * testnum );
        System.out.println("total time(s):" + run_time);
        System.out.println("sold-tickets:" + tds.tid);
        System.out.println("average latency(s/call):" + call_latency);
        System.out.println("throughtput(call/s):"+  throughput );
	}

}
