package ticketingsystem;


public class Train {
	public int coach_size;			//how many seats per coach
	public int coach_num;			//hou many coaches per train
	public int seat_num;			//how many seats per train
	Seat[] seats;
	
	public Train(int num_coach_per_train, int num_seat_per_coach, int num_station) {
		coach_size = num_seat_per_coach;
		coach_num = num_coach_per_train;
		seat_num = coach_num * coach_size;
		seats = new Seat[seat_num];	
		for(int i = 0;i < seat_num; ++i) {
			seats[i] = new Seat(num_station);
		}
	}
	
	public int[] buyTicket(int departure, int arrival) {	
		int obj_ticket = (1 << arrival) - (1 << departure);
		int[] ticketinf = new int[2];
		boolean flag = false;
			for(int seat_index = 0; seat_index < seat_num; ++seat_index) {
			Seat seat = seats[seat_index];
			if(seat.isEmpty(obj_ticket)) {
				seat.seatlock.lock();
				try {
					flag = seat.isEmpty(obj_ticket);
					if(flag) {
						seat.buy(obj_ticket);							
					}
				}finally {
					seat.seatlock.unlock();
				}
				if(flag) {
					ticketinf[0] = seat_index / coach_size + 1;
					ticketinf[1] = seat_index % coach_size + 1;
					return ticketinf;
				}
			}	
		}
		return null;
	}
	
	public boolean refund(int coachindex,int seatindex, int departure, int arrival) {	
		int obj_ticket = (1 << arrival) - (1 << departure);
		Seat seat = seats[coachindex * coach_size + seatindex];
		seat.seatlock.lock();
		try {
			seat.refund(obj_ticket);
		}finally {
			seat.seatlock.unlock();
		}
		return true;
	}
		
	public int inquiry(int departure, int arrival) {
		int obj_ticket = (1 << arrival) - (1 << departure);
		int count = 0;
		for(int seat_index = 0; seat_index < seat_num; ++seat_index) {
			if(seats[seat_index].isEmpty(obj_ticket)) {
				++count;
			}
		}
		return count;
	}

}
