package ticketingsystem;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketingDS implements TicketingSystem {
	public int route_num;
	public int coach_num;
	public int seat_num;
	public int station_num;
	public int thread_num;
	public int totalseat_num;
	public int sum_seats;
	
	//a random start point select
	//ThreadLocalRandom rand = new ThreadLocalRandom();
	//use to produce ticketid
	public AtomicInteger tid = new AtomicInteger(0);
	//trains
	public Train[] trains; 
	

	TicketingDS(){
		this.route_num = 5;
		this.coach_num = 8;
		this.seat_num = 100;
		this.station_num = 10;
		this.thread_num = 16;
		sum_seats = coach_num * seat_num;
		this.trains = new Train[route_num];
		for (int i = 0;i < route_num;++i ) {
			trains[i] = new Train(coach_num, seat_num, station_num, thread_num);
		}
	}
	
	TicketingDS(int route_num,int coach_num,int seat_num,int station_num,int thread_num){
		this.route_num = route_num;
		this.coach_num = coach_num;
		this.seat_num = seat_num;
		this.station_num = station_num;
		this.thread_num = thread_num;
		sum_seats = coach_num * seat_num;
		this.trains = new Train[route_num];
		for (int i = 0;i < route_num;++i ) {
			trains[i] = new Train(coach_num, seat_num, station_num, thread_num);
		}
	}
	
	public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
		
		Ticket ticket = new Ticket();
		//buy tickets in the route you want
		Train train = trains[route - 1];
		int[] ticketinfo = train.buyTicket(departure, arrival);
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
			return trains[route].refund(coachindex, seatindex , departure, arrival);
		}
		else
			return false;
	}

}
