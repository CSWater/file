package ticketingsystem;

public class Train {
	public int coach_size;			//how many seats per coach
	public int coach_num;			//hou many coaches per train
	public int seat_num;			//how many seats per train
	private Seat[] seats;
	
	public Train(int num_coach_per_train, int num_seat_per_coach, int num_station, int thread_num) {
		coach_size = num_seat_per_coach;
		coach_num = num_coach_per_train;
		seat_num = coach_num * coach_size;
		seats = new Seat[seat_num];
		for(int i = 0; i < seat_num; ++i) {
			seats[i] = new Seat(num_station, thread_num);
		}
	}
	
	public int[] buyTicket(int departure, int arrival) {
		int ticket = (1 << arrival) - (1 << departure);			//get the ticket
		boolean flag = false;
		for(int i = 0; i < seat_num; ++i) {
			if(seats[i].isEmpty(ticket) ) {
				seats[i].seat_lock.lock();
				try {
					flag = seats[i].isEmpty(ticket);
					if(flag) {
						seats[i].buy(ticket);	
					}
				}	finally {
					seats[i].seat_lock.unlock();
				}
			}
			if(flag) {					//buy ticket successfully

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
		seat.seat_lock.lock();
		try {
			seat.refund(ticket);
		}finally {
			seat.seat_lock.unlock();
		}

		return true;
	}
		
	public int inquiry(int departure, int arrival) {
		int ticket = (1 << arrival) - (1 << departure);
		int count = 0;
		for(int i = 0; i < seat_num; i++) {
			if(seats[i].isEmpty(ticket))
				count++;
		}
		return count;
	}

}
