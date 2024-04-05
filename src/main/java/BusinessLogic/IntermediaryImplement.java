package BusinessLogic;

public class IntermediaryImplement implements InterfaceIntermediary
{
    private int[] data;
    private String strategy;
    @Override
    public void receiveData(int[] data)
    {
        this.data = data;
    }
    @Override
    public void receiveStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public int getTimeLimit() {
        return data[0];
    }

    public int getMaxProcessingTime() {
        return data[1];
    }

    public int getMinProcessingTime() {
        return data[2];
    }

    public int getNumberOfServers() {
        return data[3];
    }

    public int getNumberOfClients() {
        return data[4];
    }
}
