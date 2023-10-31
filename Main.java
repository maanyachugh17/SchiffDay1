import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.Instant;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        NumberCounter numberCounter = new NumberCounter();
        numberCounter.countNumbers();
    }
}

class NumberCounter {
    public void countNumbers() {
        int totalNumbers = 100_000_000;
        int groupSize = 1_000_000;
        int numThreads = 100;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);


        final int e = 5;

        Instant startTime = Instant.now();

//        for (int i = 0; i < totalNumbers; i += groupSize) {
//            int start = i;
//            int end = Math.min(i + groupSize, totalNumbers);
//            Runnable task = new NumberCountTask(start, end);
//            executor.execute(task);
//        }
        Runnable task = new NumberCountTask(0, groupSize*e);
        for (int i = 0; i < 10; i ++) {
            //int end = Math.min(i + groupSize, totalNumbers);
            task.run();
            executor.execute(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Total Count: " + NumberCountTask.getTotalCount());
        System.out.println("Time taken: " + duration.toMillis() + " ms");
    }
}

class NumberCountTask implements Runnable {
    private int start;
    private int end;
    private static long totalCount = 0;

    public NumberCountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        long count = countNumbers(start, end);
        addCount(count);
    }

    private long countNumbers(int start, int end) {
        long count = 0;
        for (int i = start; i < end; i++) {
            count ++;
        }
        return count;
    }

    private synchronized void addCount(long count) {
        totalCount += count;
    }

    public static synchronized long getTotalCount() {
        return totalCount;
    }
}