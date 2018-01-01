package ticketingsystem;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketingDS implements TicketingSystem {
	public int routenum;
	public int coachnum;
	public int seatnum;
	public int stationnum;
	public int threadnum;
	public int totalseatnum;
	
	//use to produce ticketid
	public AtomicInteger tid = new AtomicInteger(0);
	//trains
	public Train[] trains; 
	

	TicketingDS(){
		this.routenum = 5;
		this.coachnum = 8;
		this.seatnum = 100;
		this.stationnum = 10;
		this.threadnum = 16;
		this.trains = new Train[routenum];
		for (int i = 0;i < routenum;++i ) {
			//System.out.println(totalseatnum);
			trains[i] = new Train(coachnum, seatnum, stationnum);
		}
	}
	
	TicketingDS(int routenum,int coachnum,int seatnum,int stationnum,int threadnum){
		this.routenum = routenum;
		this.coachnum = coachnum;
		this.seatnum = seatnum;
		this.stationnum = stationnum;
		this.threadnum = threadnum;
		this.trains = new Train[routenum];
		for (int i = 0;i < routenum;++i ) {
			trains[i] = new Train(coachnum, seatnum, stationnum);
		}
	}
	
	public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
		Ticket ticket = new Ticket();
		
		//buy tickets in the route you want
		Train train = trains[route - 1];
		int[] ticketinfo = train.tryBuyTicket(departure, arrival);
		if(ticketinfo != null) {
			ticket.arrival = arrival;
			
			ticket.coach = ticketinfo[0];
			ticket.departure = departure;
			ticket.passenger = passenger;
			ticket.route = route;
			ticket.seat = ticketinfo[1];
			ticket.tid = (long) tid.getAndIncrement();
			return ticket;
		}
		return null;
	}
	
	public int inquiry(int route, int departure, int arrival) {
		return trains[route-1].inquiry(departure, arrival);
	}
	
	public boolean refundTicket(Ticket ticket) {
		if(ticket != null) {
			int seatindex = ticket.seat - 1;
			int coachindex = ticket.coach - 1;
			int departure = ticket.departure;
			int arrival = ticket.arrival;
			int route = ticket.route - 1;
			return trains[route].refundTicket(coachindex, seatindex , departure, arrival);
		}
		else
			return false;
	}

}
