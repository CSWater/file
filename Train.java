package ticketingsystem;

public class Train {
	public int coachnum;
	Coach[] coachs;
	
	public Train(int coachnum, int seatnum, int stationnum) {
		coachs = new Coach[coachnum];
		this.coachnum = coachnum;	
		for(int i = 0;i < coachnum;++i) {
			coachs[i] = new Coach(seatnum, stationnum);
		}
	}
	
	public int[] tryBuyTicket(int departure, int arrival) {
		int[] ticketinf = new int[2];
		boolean flag1 = false;
		boolean flag2 = false;
		for(int coachindex = 0; coachindex < coachnum; ++coachindex) {
			Coach coach = coachs[coachindex];
			int seatnum = coach.seatnum;
			for(int seatindex = 0; seatindex < seatnum; ++seatindex) {
				Seat seat = coach.seats[seatindex];
				flag1 = seat.validate(departure, arrival);
				if(flag1) {
					seat.seatlock.lock();
					try {
						flag2 = seat.validate(departure, arrival);
						if(flag2) {
							seat.lockSeat(departure, arrival);
							ticketinf[1] = seatindex + 1;
							break;
						}
					}finally {
						seat.seatlock.unlock();
					}
				}
			}
			if(flag2) {
				ticketinf[0] = coachindex + 1;
				break;
			}
		}		
		if(flag2)
			return ticketinf;
		else
			return null;
	}
	
	public boolean refundTicket(int coachindex,int seatindex,int departure,int arrival) {		
		Seat seat = (coachs[coachindex].seats)[seatindex];
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
		for(int coachindex = 0; coachindex < coachnum; ++coachindex) {
			Coach coach = coachs[coachindex];
			int seatnum = coach.seatnum;
			for(int seatindex = 0; seatindex < seatnum; ++seatindex) {
				Seat seat = coach.seats[seatindex];
				if(seat.validate(departure, arrival))
					++count;
			}
		}
		return count;
	}

}
