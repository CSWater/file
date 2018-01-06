package ticketingsystem;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ALock implements Lock{
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>();
    AtomicInteger tail;
    volatile long[] flag;
    int size;

    public ALock(int capacity) {
    	size = capacity;
        tail = new AtomicInteger(0);
        flag = new long[capacity];
        flag[0] = 1;
    }

    public void lock() {
    	int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        while(flag[slot] == 0) {
        	;
        }
    }

    public void unlock() {
        int slot = mySlotIndex.get();
        flag[slot] = 0;
        flag[(slot + 1) % size] = 1;
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
