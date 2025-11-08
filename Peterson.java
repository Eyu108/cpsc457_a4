import java.util.concurrent.atomic.AtomicIntegerArray;

public class Peterson extends CriticalSection_Base {
    private AtomicIntegerArray flag;  // flag[0..n-1] in {-1, .., n-2}
    private AtomicIntegerArray turn;  // turn[0..n-2] in {0, .., n-1}
    private int numThreads;

    public Peterson(int numThreads) {
        this.numThreads = numThreads;
        this.flag = new AtomicIntegerArray(numThreads);
        this.turn = new AtomicIntegerArray(numThreads - 1);
        
        // Initialize flag array to -1 (idle state)
        for (int i = 0; i < numThreads; i++) {
            flag.set(i, -1);
        }
        
        // Initialize turn array to 0
        for (int i = 0; i < numThreads - 1; i++) {
            turn.set(i, 0);
        }
    }

    @Override
    public void EntrySection(Worker thread) {
        int i = thread.ID;
        
        // For k = 0 to n-2
        for (int k = 0; k < numThreads - 1; k++) {
            // flag[i] ← k
            flag.set(i, k);
            
            // turn[k] ← i
            turn.set(k, i);
            
            // while (∃j ≠ i, flag[j] ≥ k and turn[k] = i) do nothing
            boolean waiting = true;
            while (waiting) {
                waiting = false;
                for (int j = 0; j < numThreads; j++) {
                    if (j != i && flag.get(j) >= k && turn.get(k) == i) {
                        waiting = true;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void ExitSection(Worker thread) {
        int i = thread.ID;
        // flag[i] ← -1
        flag.set(i, -1);
    }
}
