import java.util.concurrent.atomic.AtomicIntegerArray;

public class Knuth extends CriticalSection_Base {
    // State constants for flag array
    private static final int IDLE = 0;
    private static final int REQUESTING = 1;
    private static final int IN_CS = 2;
    
    private AtomicIntegerArray flag;  // flag[0..n-1] in {idle, requesting, in-cs}
    private volatile int turn;         // turn in {0, .., n-1}
    private int numThreads;

    public Knuth(int numThreads) {
        this.numThreads = numThreads;
        this.flag = new AtomicIntegerArray(numThreads);
        this.turn = 0;
        
        // Initialize all flags to IDLE
        for (int i = 0; i < numThreads; i++) {
            flag.set(i, IDLE);
        }
    }

    @Override
    public void EntrySection(Worker thread) {
        int i = thread.ID;
        
        // repeat ... until loop
        boolean continueLoop = true;
        while (continueLoop) {
            // flag[i] ← requesting
            flag.set(i, REQUESTING);
            
            // j ← turn
            int j = turn;
            
            // while (j ≠ i) do
            while (j != i) {
                // if (flag[j] ≠ idle) then
                if (flag.get(j) != IDLE) {
                    // j ← turn
                    j = turn;
                } else {
                    // j ← (j - 1) mod n
                    j = (j - 1 + numThreads) % numThreads;
                }
            }
            
            // flag[i] ← in-cs
            flag.set(i, IN_CS);
            
            // until (∀j ≠ i, flag[j] ≠ in-cs)
            continueLoop = false;
            for (j = 0; j < numThreads; j++) {
                if (j != i && flag.get(j) == IN_CS) {
                    continueLoop = true;
                    break;
                }
            }
        }
        
        // turn ← i
        turn = i;
    }

    @Override
    public void ExitSection(Worker thread) {
        int i = thread.ID;
        
        // turn ← (i - 1) mod n
        turn = (i - 1 + numThreads) % numThreads;
        
        // flag[i] ← idle
        flag.set(i, IDLE);
    }
}
