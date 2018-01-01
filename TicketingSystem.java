package ticketingsystem;

class Ticket{
	public long tid;
	public String passenger;
	public int route;
	public int coach;
	public int seat;
	public int departure;
	public int arrival;
	
}


public interface TicketingSystem {
	Ticket buyTicket(String passenger, int route, int departure, int arrival);
	int inquiry(int route, int departure, int arrival);
	boolean refundTicket(Ticket ticket);
}

