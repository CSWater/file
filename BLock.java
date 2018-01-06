package ticketingsystem;

import java.util.concurrent.atomic.AtomicInteger;

public class BLock {
//	ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>();
    AtomicInteger tail;
    volatile long[] lock_state;			//lock_state each thread has, each bit represent a lock state of an atomic interval
    volatile long[] try_lock;			//indicate if a thread has tried to get lock
    volatile long new_lock_state;		//if a thread unlock, it should modify this value
    int size;
    
    public BLock(int capacity) {
    	size = capacity;
        tail = new AtomicInteger(0);
        lock_state = new long[size];
        try_lock = new long[size];
        for(int i = 0; i < size - 1; ++i) {
        	lock_state[i] = -1;
        	try_lock[i] = 0;				//0 means false, never tried
        }
        lock_state[size - 1] = 0;
        try_lock[size - 1] = 1;				//let the first thread can lock
    }


    public boolean lock(long wanted_lock) {
    	int me = tail.getAndIncrement() % size;
 //       mySlotIndex.set(me);
        int pred = (me + size - 1) % size;
        while(try_lock[pred] == 0) {}						//wait for the pred to finish its trying for lock
        if((wanted_lock & lock_state[pred]) == 0) {			//you can get the wanted lock
        	lock_state[me] = (wanted_lock | lock_state[pred]);
        	try_lock[me] = 1;								//indicate that I have tried to get the lock  
        	return true;
        }
        else {												//I can not get the lock now
        	lock_state[me] = lock_state[pred];				//transfer the pred lock state to the successor
        	try_lock[me] = 1;								//indicate that I have tried to get the lock
        	return false;
        }       
    }
    
    public void unlock(long free_lock) {
    	//new_lock_state = new_lock_state & free_lock;		//set the corresponding bits to 0 to free the lock
    	for(int i = 0; i < size; ++i) {
    		lock_state[i] = (lock_state[i] ^ free_lock);
    	}
    }
}
