package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task)
    {
        int min = Integer.MAX_VALUE;
        Server minServer = null;
        for(Server server : servers)
        {
            int sumTime = 0;
            for(Task t : server.getTasks())
            {
                sumTime+= t.getServiceTime();
            }
            if(sumTime < min)
            {
                min = sumTime;
                minServer = server;
            }
        }
        if(minServer != null)
        {
            minServer.addTask(task);
        }
    }
}
