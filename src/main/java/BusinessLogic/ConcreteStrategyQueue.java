package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy
{
    @Override
    public void addTask(List<Server> servers, Task task)
    {
        int min = Integer.MAX_VALUE;
        for(Server server : servers)
        {
            if(server.getTasks().length < min)
            {
                min = server.getTasks().length;
            }
        }
        for(Server server : servers)
        {
            if(server.getTasks().length == min)
            {
                server.addTask(task);
                break;
            }
        }
    }
}
