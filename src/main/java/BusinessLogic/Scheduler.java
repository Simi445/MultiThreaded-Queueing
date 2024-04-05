package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler
{
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers,int maxTasksPerServer)
    {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();
        for(int i=0;i<maxNoServers;i++)
        {
            Server server = new Server(maxTasksPerServer);
            servers.add(server);
            new Thread(server).start();
        }
    }

    public void changeStrategy(SelectionPolicy policy)
    {
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
        {
            strategy = new ConcreteStrategyQueue();
        }
        else if(policy == SelectionPolicy.SHORTEST_TIME)
        {
            strategy = new ConcreteStrategyTime();
        }
    }
    public void dispatchTask(Task t)
    {
        strategy.addTask(servers,t);
    }

    public List<Server> getServers() {
        return servers;
    }
}
