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
	public boolean validate(int departure, int arrival) {
		int check_up = (1 << departure) - 1;
		int check_bottom = (1 << arrival) - 1;
		int check_num = check_up ^ check_bottom;
		if((check_num & seat_state) == 0)
			return true;
		return false;
	}
	
	//buy a ticket on this seat
	public void lockSeat(int departure, int arrival) {
		int check_up = (1 << departure) - 1;
		int check_bottom = (1 << arrival) - 1;
		int check_num = check_up ^ check_bottom;
		seat_state = seat_state | check_num;
	}
	
	//refund a ticket on this seat
	public void unlockSeat(int departure, int arrival) {
		int check_up = (1 << departure) - 1;
		int check_bottom = (1 << arrival) - 1;
		int check_num = check_up ^ check_bottom;
		seat_state = seat_state ^ check_num;
		
	}
}
