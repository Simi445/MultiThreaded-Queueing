package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable
{
    private BlockingQueue<Task> tasks;
    private AtomicInteger totalWaitingTime;
    private AtomicInteger totalTasksProcessedNow;
    private AtomicInteger waitingPeriod;
    private int maxTasksPerServer;
    private AtomicInteger totalTasksProcessed;

    private AtomicInteger totalProcessingStartTime;
    private AtomicBoolean running;

    public Server(int maxTasksPerServer)
    {
        this.tasks = new ArrayBlockingQueue<Task>(maxTasksPerServer);
        this.waitingPeriod = new AtomicInteger(0);
        this.totalWaitingTime = new AtomicInteger(0);
        this.totalTasksProcessedNow = new AtomicInteger(0);
        this.totalTasksProcessed = new AtomicInteger(0);
        this.maxTasksPerServer = maxTasksPerServer;
        this.totalProcessingStartTime = new AtomicInteger(0);
        this.running = new AtomicBoolean(true);
    }
    public void addTask(Task newTask)
    {
        if(tasks.size() < maxTasksPerServer)
        {
            this.tasks.add(newTask);
            waitingPeriod.addAndGet(newTask.getServiceTime());
            totalWaitingTime.addAndGet(newTask.getServiceTime());
        }
    }

    @Override
    public void run() {
        int currentTime=0;
        while(running.get())
        {
            try {

                if(!tasks.isEmpty()) {
                    Task currentTask = tasks.peek();
                    Thread.sleep(1000);
                    currentTask.setServiceTime(currentTask.getServiceTime() - 1);

                    if (currentTask.getProcessingStartTime() == -1) {
                        totalProcessingStartTime.addAndGet(currentTime);
                        currentTask.setProcessingStartTime(currentTime); // Set the start time when the task starts being processed
                        System.out.println("Processing start time for task " + currentTask.getID() + ": " + currentTask.getProcessingStartTime());
                    }

                    waitingPeriod.decrementAndGet();
                    if (currentTask.getServiceTime() == 0) {
                        tasks.take();
                        totalTasksProcessed.incrementAndGet();
                        totalTasksProcessedNow.incrementAndGet();
                    }
                    currentTime++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void stop()
    {
        running.set(false);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public AtomicInteger getTotalTasksProcessedNow() {
        return totalTasksProcessedNow;
    }
    public AtomicInteger getTotalProcessingStartTime() {
        return totalProcessingStartTime;
    }


    public AtomicInteger getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public AtomicInteger getTotalTasksProcessed() {
        return totalTasksProcessed;
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }
}
