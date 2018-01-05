package ticketingsystem;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ALock implements Lock{
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>();
    AtomicInteger tail;
    volatile boolean [] flag;
    int size;

    public ALock(int capacity) {
        size = 4 * capacity;
        tail = new AtomicInteger(0);
        flag = new boolean[4 * capacity];
        flag[0] = true;
    }

    public void lock() {
    	//System.out.println("lock");
        int slot = (4 * tail.getAndIncrement()) % size;
        mySlotIndex.set(slot);
        while (!flag[slot]) {
            ;
        }
    }

    public void unlock() {
        int slot = mySlotIndex.get();
        flag[slot] = false;
        flag[(slot + 4) % size] = true;
    }

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
}
