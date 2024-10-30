package pl.indianbartonka.util;

import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadUtilTest {

    @Test
    public void testSleepSeconds() {
        final long startTime = System.currentTimeMillis();
        ThreadUtil.sleep(1);
        final long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime >= 1000, "Sleep for 1 second should take at least 1000ms.");
    }

    @Test
    public void testSleepMilliseconds() {
        final long startTime = System.currentTimeMillis();
        ThreadUtil.sleep(500L);
        final long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime >= 500, "Sleep for 500 milliseconds should take at least 500ms.");
    }

    @Test
    public void testGetLogicalThreads() {
        final int logicalThreads = ThreadUtil.getLogicalThreads();
        assertTrue(logicalThreads > 0, "Should return a positive number of logical processors.");
    }

    @Test
    public void testGetPeakThreadsCount() {
        final int peakCount = ThreadUtil.getPeakThreadsCount();
        assertTrue(peakCount >= 0, "Peak threads count should be non-negative.");
    }

    @Test
    public void testNewThread() {
        final ThreadUtil threadUtil = new ThreadUtil("TestThread");
        final Thread thread = threadUtil.newThread();
        assertEquals("TestThread-1", thread.getName());
        assertFalse(thread.isDaemon(), "Thread should not be a daemon thread by default.");
    }

    @Test
    public void testNewThreadWithRunnable() {
        final Runnable runnable = () -> {
        };
        final ThreadUtil threadUtil = new ThreadUtil("TestRunnableThread", runnable);
        final Thread thread = threadUtil.newThread();
        assertEquals("TestRunnableThread-1", thread.getName());
        assertFalse(thread.isDaemon(), "Thread should not be a daemon thread by default.");
    }

    @Test
    public void testDaemonThread() {
        final ThreadUtil threadUtil = new ThreadUtil("DaemonThread", true);
        final Thread thread = threadUtil.newThread();
        assertTrue(thread.isDaemon(), "Thread should be a daemon thread.");
    }

    @Test
    public void testConcurrentThreadCreation() throws InterruptedException {
        final int threadCount = 10;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        final ThreadUtil threadUtil = new ThreadUtil("ConcurrentThread");

        for (int i = 0; i < threadCount; i++) {
            threadUtil.newThread(() -> {
                try {
                    // Simulate work
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await(); // wait for all threads to finish
        assertEquals(0, latch.getCount(), "All threads should have completed.");
    }
}

