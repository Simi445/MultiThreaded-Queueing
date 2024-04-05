package Model;

public class Task
{
    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private int processingStartTime = -1;

    public Task(int ID,int arrivalTime, int serviceTime)
    {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID=" + ID +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getID() {
        return ID;
    }

    public int getProcessingStartTime() {
        return processingStartTime;
    }

    public void setProcessingStartTime(int processingStartTime) {
        this.processingStartTime = processingStartTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}
