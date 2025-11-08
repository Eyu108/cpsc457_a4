public class Worker extends Thread {
    private CriticalSection_Base Section;
    //java threads have an ID already, however you can use this to make sure you have control over the starting value and increment
    int ID;

    public Worker(CriticalSection_Base Section, int ID) {
        this.Section = Section;
        this.ID = ID;
    }

    public void run() {
        // Each thread enters and exits the critical section 5 times
        for (int i = 0; i < 5; i++) {
            Section.EntrySection(this);
            try {
                Section.CriticalSection(this);
            } catch (InterruptedException e) {
                //elevate the exception, this should not happen
                throw new RuntimeException(e);
            }
            Section.ExitSection(this);
        }
    }
}