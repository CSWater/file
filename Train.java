package ticketingsystem;

public class Train {
	public int coach_size;			//how many seats per coach
	public int coach_num;			//hou many coaches per train
	public int seat_num;			//how many seats per train
	private Seat[] seats;
	
	private volatile int[] interval;
	
	public Train(int num_coach_per_train, int num_seat_per_coach, int num_station, int thread_num) {
		coach_size = num_seat_per_coach;
		coach_num = num_coach_per_train;
		seat_num = coach_num * coach_size;
		seats = new Seat[seat_num];
		for(int i = 0; i < seat_num; ++i) {
			seats[i] = new Seat();
		}
		interval = new int[num_station];
		for(int i = 0; i < num_station; ++i) {
			interval[i] = seat_num;
		}
	}
	
	public int[] buyTicket(int departure, int arrival) {
		int ticket = (1 << arrival) - (1 << departure);			//get the ticket
		boolean flag = false;
		for(int i = 0; i < seat_num; ++i) {
			flag = seats[i].buy(ticket);
			if(flag) {					//buy ticket successfully
				for(int j = departure; j < arrival; ++j)
					interval[j]--;
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
		seat.refund(ticket);
		for(int i = departure; i < arrival; ++i)
			interval[i]++;
		return true;
	}
		
	public int inquiry(int departure, int arrival) {
		int count = 0xffff;
		for(int i = departure; i < arrival; ++i) {
			if(count > interval[i])
				count = interval[i];
		}
		return count;
	}

}
