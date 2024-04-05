package BusinessLogic;

import GUI.PopUp;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;


public class SimulationManager implements Runnable
{
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy strategy;
    private int[] tasksProcessedPerSecond;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private PopUp popUp;
    private List<Task> generatedTasks;
    private FileWriting logger;

    public SimulationManager() {}

    public void setUpSimulation()
    {
        generatedTasks = new ArrayList<>();
        tasksProcessedPerSecond = new int[timeLimit];
        scheduler = new Scheduler(numberOfServers, 20);
        scheduler.changeStrategy(strategy);
        //for logging
        logger = new FileWriting("simulation.log");
        logger.log("Selected strategy: " + strategy);

        System.out.println("Selected strategy: " + strategy);
        generateNRandomTasks();


        //intialise fram - > until then
        SimCommImplement communication = new SimCommImplement();
        communication.setNrQueues(numberOfServers);
        this.frame = new SimulationFrame();
        this.frame.setCommunication(communication);
        this.frame.startSimulation();
    }

    public void generateNRandomTasks()
    {
        for(int i=0;i<numberOfClients;i++)
        {
            Random rand = new Random();
            int arrivalInterval = rand.nextInt(timeLimit/2);
            int serviceTime = (int)(Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime);
            Task task = new Task(i, arrivalInterval, serviceTime);
            generatedTasks.add(task);
        }
        Collections.sort(generatedTasks, (t1, t2) -> Integer.compare(t1.getArrivalTime(), t2.getArrivalTime()));

    }

    public void setStrategy(String strategy) {
        this.strategy = SelectionPolicy.valueOf(strategy);
    }

    public void GUIStatus(int currentTime)
    {
        frame.waitingTimeValue.setText("" + currentTime);
        logger.log("Current time: " + currentTime); // for logging purposes

        StringBuilder waitingClients = new StringBuilder();
        for (Task task : generatedTasks) {
            waitingClients.append("(").append(task.getID()).append(",").append(task.getArrivalTime()).append(",").append(task.getServiceTime()).append("); ");
        }
        frame.waitingClientsValue.setText(waitingClients.toString());
        logger.log("Waiting clients: " + waitingClients.toString());//logging purposes

        List<Server> servers = scheduler.getServers();
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            Task[] tasks = server.getTasks();
            DefaultListModel<String> listModel = frame.listModels.get(i);
            listModel.clear();
            for (Task task : tasks) {
                String taskInfo = "TaskID = " + task.getID() + ", service time: " + task.getServiceTime();
                listModel.addElement("TaskID = " + task.getID() + ", service time: " + task.getServiceTime());
                logger.log("Server " + i + ": " + taskInfo);
            }
        }
    }


    public double calculateAverageServiceTime() {
        int totalWaitingTime = 0;
        int totalTasks = 0;
        for (Server server : scheduler.getServers()) {
            totalWaitingTime += server.getTotalWaitingTime().get();
            totalTasks += server.getTotalTasksProcessed().get();
        }
        return totalTasks == 0 ? 0 : (double) totalWaitingTime / totalTasks;
    }

    public double calculateAverageFirstProcessingTime() {
        int totalFirstProcessingTime = 0;
        int totalProcessedTasks = 0;
        for (Server server : scheduler.getServers()) {
            totalFirstProcessingTime += server.getTotalProcessingStartTime().get();
            totalProcessedTasks += server.getTotalTasksProcessed().get();
        }
        return totalProcessedTasks == 0 ? 0 : (double) totalFirstProcessingTime / totalProcessedTasks;
    }

    @Override
    public void run()
    {
        int currentTime=0;
        while(currentTime<timeLimit)
        {
            List<Task> tasksToRemove = new ArrayList<>();
            for(Task t : generatedTasks)
            {
                if(t.getArrivalTime() == currentTime)
                {
                    scheduler.dispatchTask(t);
                    tasksToRemove.add(t);
                }
            }
            generatedTasks.removeAll(tasksToRemove);
            GUIStatus(currentTime);

            for (Server server : scheduler.getServers()) {
                tasksProcessedPerSecond[currentTime] += server.getTotalTasksProcessedNow().get();
                server.getTotalTasksProcessedNow().set(0); // Reset the count for the next second
            }
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (generatedTasks.isEmpty() && areAllServersEmpty()) {
                GUIStatus(currentTime);
                break;
            }
        }
        int peakSecond = 0;
        for (int i = 1; i < timeLimit; i++) {
            if (tasksProcessedPerSecond[i] > tasksProcessedPerSecond[peakSecond]) {
                peakSecond = i;
            }
        }
        System.out.println("Peak second: " + peakSecond);
        for (Server server : scheduler.getServers()) {
            server.stop();
        }
        double averageWaitingTime = calculateAverageFirstProcessingTime();
        double averageServiceTime = calculateAverageServiceTime();
        JOptionPane.showMessageDialog(null, "Average waiting time: " + averageWaitingTime + "\nAverage service time: " + averageServiceTime + "\nPeak second: "+ peakSecond);
        logger.log("Average waiting time: " + averageWaitingTime);
        logger.log("Average service time: " + averageServiceTime);
        logger.log("Peak second: " + peakSecond);
        System.exit(0);
    }

    private boolean areAllServersEmpty() {
        for (Server server : scheduler.getServers()) {
            if (server.getTasks().length > 0) {
                return false;
            }
        }
        return true;
    }

    public void setMaxProcessingTime(int maxProcessingTime) {
        this.maxProcessingTime = maxProcessingTime;
    }

    public void setMinProcessingTime(int minProcessingTime) {
        this.minProcessingTime = minProcessingTime;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public static void main(String[] args)
    {
        IntermediaryImplement intermediary = new IntermediaryImplement();
        SimulationManager simulationManager = new SimulationManager();
        PopUp popUp = new PopUp();
        popUp.setIntermediary((InterfaceIntermediary) intermediary);
        popUp.getFrame().setVisible(true);
        
        popUp.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                simulationManager.setTimeLimit(intermediary.getTimeLimit());
                simulationManager.setMaxProcessingTime(intermediary.getMaxProcessingTime());
                simulationManager.setMinProcessingTime(intermediary.getMinProcessingTime());
                simulationManager.setNumberOfServers(intermediary.getNumberOfServers());
                simulationManager.setNumberOfClients(intermediary.getNumberOfClients());
                simulationManager.setStrategy(intermediary.getStrategy());

                simulationManager.setUpSimulation();

                Thread t = new Thread(simulationManager);
                t.start();
            }
        });

    }

    private class FileWriting {
        private PrintWriter printWriter;

        public FileWriting(String fileName) {
            try {
                FileWriter fileWriter = new FileWriter(fileName, true);
                printWriter = new PrintWriter(fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void log(String message) {
            printWriter.println(message);
            printWriter.flush();
        }
    }
}
