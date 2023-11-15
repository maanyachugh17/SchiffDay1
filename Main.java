import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadCountingExample {
    public static void main(String[] args) {
        int numThreads = 100;
        int countPerThread = 1_000_000;
        AtomicLong total = new AtomicLong(0);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        Instant start = Instant.now();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(new CounterTask(countPerThread, total));
        }

        executorService.shutdown();

        // Wait for all threads to finish
        while (!executorService.isTerminated()) {
            Thread.yield();
        }

    
        Instant end = Instant.now();
        long totalCount = total.get();
        Duration duration = Duration.between(start, end);


        System.out.println("Total Count: " + totalCount);
        System.out.println("Time taken: " + duration.toMillis() + " milliseconds");
    }

    static class CounterTask implements Runnable {
        private int countPerThread;
        private AtomicLong total;

        CounterTask(int countPerThread, AtomicLong total) {
            this.countPerThread = countPerThread;
            this.total = total;
        }

        @Override
        public void run() {
            long threadTotal = 0;
            for (int i = 0; i < countPerThread; i++) {
                threadTotal++;
            }
            total.addAndGet(threadTotal);
        }
        ///update #7
        //branch #2 branch off
    }
}