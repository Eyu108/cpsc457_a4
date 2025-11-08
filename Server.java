public class Server {
    private static final int MIN_THREADS = 2;
    private static final int MAX_THREADS = 100;
    private static final int NUM_ITERATIONS = 5;

    public static void main(String[] args) {
        // Test Peterson's Algorithm
        System.out.println("CS Solution 1 – Peterson's (time in milli-seconds)");
        System.out.println("Threads,AVG TAT");
        testAlgorithm("Peterson");
        System.out.println();

        // Test Knuth's Algorithm
        System.out.println("CS Solution 2 – Knuth's (time in milli-seconds)");
        System.out.println("Threads,AVG TAT");
        testAlgorithm("Knuth");
        System.out.println();

        // Test De Bruijn's Algorithm
        System.out.println("CS Solution 3 – De Bruijn's (time in milli-seconds)");
        System.out.println("Threads,AVG TAT");
        testAlgorithm("DeBruijn");
    }

    private static void testAlgorithm(String algorithmName) {
        for (int numThreads = MIN_THREADS; numThreads <= MAX_THREADS; numThreads++) {
            double avgTAT = runTest(algorithmName, numThreads);
            System.out.printf("%d,%.2f%n", numThreads, avgTAT);
        }
    }

    private static double runTest(String algorithmName, int numThreads) {
        CriticalSection_Base section = null;
        
        // Create the appropriate critical section implementation
        switch (algorithmName) {
            case "Peterson":
                section = new Peterson(numThreads);
                break;
            case "Knuth":
                section = new Knuth(numThreads);
                break;
            case "DeBruijn":
                section = new DeBruijn(numThreads);
                break;
            default:
                System.err.println("Unknown algorithm: " + algorithmName);
                System.exit(1);
        }

        // Create worker threads
        Worker[] workers = new Worker[numThreads];
        long[] startTimes = new long[numThreads];
        long[] endTimes = new long[numThreads];

        for (int i = 0; i < numThreads; i++) {
            workers[i] = new Worker(section, i);
        }

        // Record start time and start all threads
        for (int i = 0; i < numThreads; i++) {
            startTimes[i] = System.currentTimeMillis();
            workers[i].start();
        }

        // Wait for all threads to complete and record end times
        for (int i = 0; i < numThreads; i++) {
            try {
                workers[i].join();
                endTimes[i] = System.currentTimeMillis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Calculate average turnaround time
        double totalTAT = 0.0;
        for (int i = 0; i < numThreads; i++) {
            long turnaroundTime = endTimes[i] - startTimes[i];
            totalTAT += turnaroundTime;
        }

        return totalTAT / numThreads;
    }
}
