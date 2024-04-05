package GUI;

import BusinessLogic.InterfaceIntermediary;
import BusinessLogic.SimulationFrCommunication;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class SimulationFrame{

    private JFrame frame;
    private SimulationFrCommunication intermediary;
    private ArrayList<JList<String>> lists;
    public ArrayList<DefaultListModel<String> >listModels;
    private ArrayList<JPanel> panels;
    private ArrayList<JLabel> labels;
    public JLabel waitingTimeValue;
    public JLabel waitingClientsValue;

    private int nrQueues;


    public SimulationFrame() {}


    public void startSimulation()
    {
        initialize();
        frame.setVisible(true);
    }
    private void initialize() {
        nrQueues = intermediary.getNrQueues();

        frame = new JFrame();
        frame.setBounds(100, 100, 700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.getContentPane().setLayout(new GridLayout(nrQueues + 2, 1));

        //panels
        panels = new ArrayList<>(nrQueues); // setup the capacity here
        for(int i=0;i<nrQueues;i++)
        {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panels.add(panel);
            frame.getContentPane().add(panel);
        }

        //The labels
        labels = new ArrayList<>(nrQueues);
        for(int i=0;i<nrQueues;i++)
        {
            JLabel label = new JLabel("Queue " + (i+1) + ": ");
            labels.add(label);
            panels.get(i).add(label);
        }

        //Lists
        listModels = new ArrayList<>(nrQueues);
        lists = new ArrayList<>(nrQueues);
        for(int i=0;i<nrQueues;i++)
        {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> list = new JList<>(listModel);
            JScrollPane scrollPane1 = new JScrollPane(list);
            listModels.add(listModel);
            lists.add(list);
            panels.get(i).add(scrollPane1);
        }


        // Panel for waiting time
        JPanel waitingTimePanel = new JPanel(new FlowLayout());
        JLabel waitingTimeLabel = new JLabel("Current Waiting Time: ");
        waitingTimeValue = new JLabel("0");
        waitingTimePanel.add(waitingTimeLabel);
        waitingTimePanel.add(waitingTimeValue);
        frame.getContentPane().add(waitingTimePanel);

        // Panel for current waiting clients
        JPanel waitingClientsPanel = new JPanel(new FlowLayout());
        JLabel waitingClientsLabel = new JLabel("Current Waiting Clients: ");
        waitingClientsValue = new JLabel("");
        waitingClientsPanel.add(waitingClientsLabel);
        waitingClientsPanel.add(waitingClientsValue);
        frame.getContentPane().add(waitingClientsPanel);

    }
    public void setCommunication(SimulationFrCommunication intermediary)
    {
        this.intermediary = intermediary;
    }
}