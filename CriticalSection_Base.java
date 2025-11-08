abstract public class CriticalSection_Base {

    // Create your own implementation of these
    public abstract void EntrySection(Worker thread);
    public abstract void ExitSection(Worker thread);

    public void CriticalSection(Worker thread) throws InterruptedException {
        // Busy wait. Faster than sleep(1) since the overhead of that usually means much more than 1ms
        for (int i = 0; i < 5000; i++) {
            Thread.onSpinWait();
        }       
    }
}
