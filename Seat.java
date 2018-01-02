package ticketingsystem;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Seat {
	volatile int seat_state;
	final Lock seatlock = new ReentrantLock();
	public int stationnum;
	
	public Seat(int stationnum){
		this.stationnum = stationnum;
		seat_state = 0;					//init state
	}
	
	//validate a seat
	public boolean isEmpty(int ticket) {
		if((seat_state & ticket) == 0)
			return true;
		return false;
	}
	
	//buy a ticket on this seat
	public void buy(int ticket) {
		seat_state = seat_state | ticket;
	}
	
	//refund a ticket on this seat
	public void refund(int ticket) {
		seat_state = seat_state ^ ticket;
		
	}
}
