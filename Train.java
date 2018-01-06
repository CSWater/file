package ticketingsystem;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Train {
	public int coach_size;			//how many seats per coach
	public int coach_num;			//hou many coaches per train
	public int seat_num;			//how many seats per train
	//private AtomicInteger interval[] ;
	private int interval[];
	private Seat[] seats;
	final Lock train_lock = new ReentrantLock();
	
	public Train(int num_coach_per_train, int num_seat_per_coach, int num_station, int thread_num) {
		coach_size = num_seat_per_coach;
		coach_num = num_coach_per_train;
		seat_num = coach_num * coach_size;
		interval = new int[num_station];
		seats = new Seat[seat_num];
		for(int i = 0; i < seat_num; ++i) {
			seats[i] = new Seat(num_station, thread_num);
		}
		for(int i = 0; i < num_station - 1; i++) {
			interval[i] = seat_num;
		}
	}
	
	public int minmize(int departure, int arrival) {
		int min = 0x7fffffff;					//may be bigger enough
		for(int i = departure; i < arrival; ++i) {
			if(min > interval[i])
				min = interval[i];
		}
		return min;
	}
	
	public int maxmize(int departure, int arrival) {
		int max = -1;
		for(int i = departure; i < arrival; ++i) {
			if(max < interval[i]) 
				max = interval[i];
		}
		return max;
	}
	
	public int generateTicketID(int departure, int arrival) {
		//@TODO
		int ticketID = 0;
		return ticketID;
	}
	
	public void reserveTicket(int departure, int arrival) {
		for(int i = departure; i < arrival; ++i) {
			interval[i] -= 1;
		}
	}
	
	public int[] buyTicket(int departure, int arrival) {
		int ticket = (1 << arrival) - (1 << departure);			//get the ticket
		boolean flag = false;
		for(int i = 0; i < seat_num; ++i) {
			if(seats[i].isEmpty(ticket) ) {
				if(seats[i].seat_lock.lock(ticket) ) {
					try {
						flag = seats[i].isEmpty(ticket);
						if(flag) {
							seats[i].buy(ticket);							
						}
					}	finally {
						seats[i].seat_lock.unlock(ticket);
					}
				}
			}
			else {
				continue;
			}
			if(flag) {					//buy ticket successfully
				for(int j = departure; i < arrival; i++) {
					interval[i]--;
				}
				int[] ticket_info = new int[2];
				ticket_info[0] = i / coach_size + 1;
				ticket_info[1] = i % coach_size + 1;
				return ticket_info;
			}
		}
		return null;
	}
	
	public boolean refund(int coachindex,int seatindex, int departure, int arrival) {
		int ticket = (1 << arrival) - (1 << departure);
		Seat seat = seats[coachindex * coach_size + seatindex];
		seat.seat_lock.lock(ticket);
		try {
			seat.refund(ticket);
		}finally {
			seat.seat_lock.unlock(ticket);
		}
		for(int i = departure; i < arrival; ++i) {
			interval[i]++;
		}
		return true;
	}
		
	public int inquiry(int departure, int arrival) {
		return minmize(departure, arrival);
	}

}
