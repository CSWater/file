package ticketingsystem;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Seat {
	volatile long seat_state;
	final Lock seatlock = new ReentrantLock();
	public int stationnum;
	
	public Seat(int stationnum){
		this.stationnum = stationnum;
		seat_state = 0;					//init state
	}
	
	//validate a seat
	public boolean isEmpty(long ticket) {
		if((seat_state & ticket) == 0)
			return true;
		return false;
	}
	
	//buy a ticket on this seat
	public void buy(long ticket) {
		seat_state = seat_state | ticket;
	}
	
	//refund a ticket on this seat
	public void refund(long ticket) {
		seat_state = seat_state ^ ticket;
		
	}
}
