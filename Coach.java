package ticketingsystem;

public class Coach {
	Seat[] seats;
	public int seatnum;
	public Coach(int seatnum, int stationnum) {
		seats = new Seat[seatnum];
		this.seatnum = seatnum;
		for(int i = 0; i < seatnum; ++i) {
			seats[i] = new Seat(stationnum);
		}
	}
}
