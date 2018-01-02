package ticketingsystem;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketingDS implements TicketingSystem {
	public int routenum;
	public int coachnum;
	public int seatnum;
	public int stationnum;
	public int threadnum;
	public int totalseatnum;
	public int sum_seats;
	
	//a random start point select
	//ThreadLocalRandom rand = new ThreadLocalRandom();
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
		sum_seats = coachnum * seatnum;
		this.trains = new Train[routenum];
		for (int i = 0;i < routenum;++i ) {
			trains[i] = new Train(coachnum, seatnum, stationnum);
		}
	}
	
	TicketingDS(int routenum,int coachnum,int seatnum,int stationnum,int threadnum){
		this.routenum = routenum;
		this.coachnum = coachnum;
		this.seatnum = seatnum;
		this.stationnum = stationnum;
		this.threadnum = threadnum;
		sum_seats = coachnum * seatnum;
		this.trains = new Train[routenum];
		for (int i = 0;i < routenum;++i ) {
			trains[i] = new Train(coachnum, seatnum, stationnum);
		}
	}
	
	public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
		long obj_ticket = (((1 << departure) - 1) ^ ((1 << arrival) - 1) );
		Ticket ticket = new Ticket();
		//buy tickets in the route you want
		Train train = trains[route - 1];
		//according to thread id to select a start point
		//long idx = Thread.currentThread().getId();
		//int start_point = (((int)idx & 0x40));				//select a start point to buy ticket
		//int start_point = rand.nextInt(0, sum_seats);
		long tm = System.nanoTime();
		int start_point = (int)(tm & 0x1ff);
		int[] ticketinfo = train.buyTicket(start_point, obj_ticket);
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
		long obj_ticket = (((1 << departure) - 1) ^ ((1 << arrival) - 1) );
		return trains[route-1].inquiry(obj_ticket);
	}
	
	public boolean refundTicket(Ticket ticket) {
		if(ticket != null) {	
			int seatindex = ticket.seat - 1;
			int coachindex = ticket.coach - 1;
			int departure = ticket.departure;
			int arrival = ticket.arrival;
			long obj_ticket = (((1 << departure) - 1) ^ ((1 << arrival) - 1) );
			int route = ticket.route - 1;
			return trains[route].refund(coachindex, seatindex , obj_ticket);
		}
		else
			return false;
	}

}
