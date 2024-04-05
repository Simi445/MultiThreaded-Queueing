package BusinessLogic;

import GUI.SimulationFrame;

public class SimCommImplement implements SimulationFrCommunication
{
    private int nrQueues;
    @Override
    public void setNrQueues(int nrQueues)
    {
        this.nrQueues = nrQueues;
    }
    @Override
    public int getNrQueues()
    {
        return this.nrQueues;
    }
}
