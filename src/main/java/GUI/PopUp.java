package GUI;

import BusinessLogic.InterfaceIntermediary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.lang.System.exit;

public class PopUp {
    private JFrame frame;
    private InterfaceIntermediary intermediary;
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;

    private final int MAX_TIME_LIMIT = 200;
    private final int MAX_PROCESSING_TIME = 100;
    private final int MIN_PROCESSING_TIME = 2;
    private final int MAX_SERVERS = 20;
    private final int MAX_CLIENTS = 1000;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PopUp window = new PopUp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PopUp() {
        initialize();
    }

    public void setIntermediary(InterfaceIntermediary intermediary)
    {
        this.intermediary = intermediary;
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(7, 1));

        JPanel timeLimitPanel = new JPanel();
        JLabel lbltimeLimit = new JLabel("Time Limit (seconds)   ");
        JTextField txbtimeLimit = new JTextField(5);
        timeLimitPanel.add(lbltimeLimit);
        timeLimitPanel.add(txbtimeLimit);

        JPanel maxTimePanel = new JPanel();
        JLabel lblMaxTime = new JLabel("Max Processing Time");
        JTextField txbMaxTime = new JTextField(5);
        maxTimePanel.add(lblMaxTime);
        maxTimePanel.add(txbMaxTime);

        JPanel minTimePanel = new JPanel();
        JLabel lblMinTime = new JLabel("Min Processing Time");
        JTextField txbMinTime = new JTextField(5);
        minTimePanel.add(lblMinTime);
        minTimePanel.add(txbMinTime);

        JPanel serversPanel = new JPanel();
        JLabel lblServers = new JLabel("Number of Servers");
        JTextField txbServers = new JTextField(5);
        serversPanel.add(lblServers);
        serversPanel.add(txbServers);

        JPanel clientsPanel = new JPanel();
        JLabel lblClients = new JLabel("Number of Clients");
        JTextField txbClients = new JTextField(5);
        clientsPanel.add(lblClients);
        clientsPanel.add(txbClients);

        JComboBox<String> strategyComboBox = new JComboBox<>(new String[]{"SHORTEST_QUEUE", "SHORTEST_TIME"}); //For strategy

        mainPanel.add(timeLimitPanel);
        mainPanel.add(maxTimePanel);
        mainPanel.add(minTimePanel);
        mainPanel.add(serversPanel);
        mainPanel.add(clientsPanel);
        mainPanel.add(strategyComboBox);

        ArrayList<JTextField>fieldList = new ArrayList();
        fieldList.add(txbtimeLimit);
        fieldList.add(txbMaxTime);
        fieldList.add(txbMinTime);
        fieldList.add(txbServers);
        fieldList.add(txbClients);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseData(fieldList);
                if(timeLimit != 0 && maxProcessingTime != 0 && minProcessingTime != 0 && numberOfServers != 0 && numberOfClients != 0)
                {
                    int[] data = {timeLimit,maxProcessingTime,minProcessingTime,numberOfServers,numberOfClients};
                    intermediary.receiveData(data);
                    String selectedStrategy = (String) strategyComboBox.getSelectedItem();
                    intermediary.receiveStrategy(selectedStrategy);
                    frame.dispose();
                }
            }
        });

        mainPanel.add(btnSubmit);

        frame.getContentPane().add(mainPanel);
    }

    private void parseData(ArrayList<JTextField>fieldList)
    {
        int local_timeLimit = 0;
        int local_maxProcessingTime = 0;
        int local_minProcessingTime = 0;
        int local_numberOfServers = 0;
        int local_numberOfClients = 0;

        for(JTextField field:fieldList)
        {
            if(field.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields");
                return;
            }
            else {
                try {
                    switch (fieldList.indexOf(field)) {
                        case 0:
                            local_timeLimit = Integer.parseInt(field.getText());
                            if(local_timeLimit > MAX_TIME_LIMIT || local_timeLimit < 1)
                            {
                                JOptionPane.showMessageDialog(frame, "Time limit too high or low");
                                return;
                            }
                            else {
                                timeLimit = local_timeLimit;
                            }
                            break;
                        case 1:
                            local_maxProcessingTime = Integer.parseInt(field.getText());
                            if(local_maxProcessingTime > MAX_PROCESSING_TIME || local_maxProcessingTime < MIN_PROCESSING_TIME)
                            {
                                JOptionPane.showMessageDialog(frame, "Max processing time too high or low");
                                return;
                            }
                            else {
                                maxProcessingTime = local_maxProcessingTime;
                            }
                            break;
                        case 2:
                            local_minProcessingTime = Integer.parseInt(field.getText());
                            if(local_minProcessingTime > MAX_PROCESSING_TIME || local_minProcessingTime < MIN_PROCESSING_TIME || local_minProcessingTime > maxProcessingTime)
                            {
                                JOptionPane.showMessageDialog(frame, "Min processing time too high or low");
                                return;
                            }
                            else {
                                minProcessingTime = local_minProcessingTime;
                            }
                            break;
                        case 3:
                            local_numberOfServers = Integer.parseInt(field.getText());
                            if(local_numberOfServers > MAX_SERVERS || local_numberOfServers < 1)
                            {
                                JOptionPane.showMessageDialog(frame, "Number of servers too high or low");
                                return;
                            }
                            else {
                                numberOfServers = local_numberOfServers;
                            }
                            break;
                        case 4:
                            local_numberOfClients = Integer.parseInt(field.getText());
                            if(local_numberOfClients > MAX_CLIENTS || local_numberOfClients < 1)
                            {
                                JOptionPane.showMessageDialog(frame, "Number of clients too high or low");
                                return;
                            }
                            else
                            {
                                numberOfClients = local_numberOfClients;
                            }
                            break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
                    return;
                }
            }
        }
    }

    public JFrame getFrame() {
        return frame;
    }
}
