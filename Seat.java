package ticketingsystem;

import java.util.concurrent.atomic.AtomicReference;

public class Seat {
	//every thread which want to buy a interval of the seat needs to initial a buy node 
	//and try to link its node to the buy list of this seat
	public class TicketNode{
		public int ticket;
		public AtomicReference<TicketNode> next = new AtomicReference<TicketNode>();
		public TicketNode() { }
		public TicketNode(int ticket) {
			this.ticket = ticket;
			this.next.set(null);
		}
	}
	private TicketNode head;

	public Seat(){
		this.head = new TicketNode();
		this.head.next.set(null);
	}
	
	public boolean buy(int ticket) {
		TicketNode me = new TicketNode(ticket);
		TicketNode pred = head;
		TicketNode curr = head.next.get();
		if(curr == null) {
			if(head.next.compareAndSet(null, me)) {		//I may be the first ticket
				return true;
			}
		}
		curr = head.next.get();					
		while(curr != null) {
			if((curr.ticket & ticket) != 0)
				return false;
			else if(curr.next.get() != null) {				//go on traveling
				pred = curr;
				curr = curr.next.get();
			}
			else {											//try to buy
				if(curr.ticket == 0) {						//remove the refund ticket
					curr = curr.next.get();
					pred.next.set(curr);						
				}
				else if(curr.next.compareAndSet(null, me)) {		//try to link me to the ticket list
					return true;
				}
				else {												//go on traveling
					pred = curr;
					curr = curr.next.get();
				}
			}
		}
		return false;
	}

	public void refund(int ticket) {
		TicketNode curr = head.next.get();
		while((curr.ticket ^ ticket) != 0) {
			curr = curr.next.get();
		}
		curr.ticket = (curr.ticket ^ ticket);	
	}
}
