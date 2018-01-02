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
	
	public int[] tryBuyTicket(int start_point, int departure, int arrival) {
		
		int[] ticketinf = new int[2];
		boolean flag1 = false;
		boolean flag2 = false;
		try {
			for(int seat_index = start_point; seat_index < seat_num; ++seat_index) {
			Seat seat = seats[seat_index];
			flag1 = seat.validate(departure, arrival);
			if(flag1) {
				seat.seatlock.lock();
				try {
					flag2 = seat.validate(departure, arrival);
					if(flag2) {
						seat.lockSeat(departure, arrival);							
					}
				}finally {
					seat.seatlock.unlock();
				}
			}
			if(flag2) {
				ticketinf[0] = seat_index / coach_size + 1;
				ticketinf[1] = seat_index % coach_size + 1;
				return ticketinf;
			}
		}
		} catch(Exception e) {
			System.out.println(start_point);
		}
		flag1 = flag2 = false;
		try {
		for(int seat_index = 0; seat_index < start_point; ++seat_index) {
			Seat seat = seats[seat_index];
			flag1 = seat.validate(departure, arrival);
			if(flag1) {
				seat.seatlock.lock();
				try {
					flag2 = seat.validate(departure, arrival);
					if(flag2) {
						seat.lockSeat(departure, arrival);
					}
				}finally {
					seat.seatlock.unlock();
				}
			}
			if(flag2) {
				ticketinf[0] = seat_index / coach_size + 1;
				ticketinf[1] = seat_index % coach_size + 1;
				return ticketinf;
			}
		}
		} catch (Exception e) {
			System.out.println(start_point);
		}
			return null;
	}
	
	public boolean refundTicket(int coachindex,int seatindex,int departure,int arrival) {	
		Seat seat = seats[coachindex * coach_size + seatindex];
		seat.seatlock.lock();
		try {
			seat.unlockSeat(departure, arrival);
		}finally {
			seat.seatlock.unlock();
		}
		return true;
	}
		
	public int inquiry(int departure, int arrival) {
		int count = 0;
		for(int seat_index = 0; seat_index < seat_num; ++seat_index) {
			if(seats[seat_index].validate(departure, arrival)) {
				++count;
			}
		}
		return count;
	}

}
