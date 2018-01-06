package ticketingsystem;

import java.util.concurrent.locks.Lock;

public class Seat {
	volatile int seat_state;
	final Lock seat_lock;
	public int station_num;
	
	public Seat(int station_num, int thread_num){
		this.station_num = station_num;
		this.seat_lock = new ALock(thread_num);
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
