package runespancalc;

import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import javax.swing.event.*;
import java.util.*;

public class RunespanCalc extends JFrame
{
    private JTabbedPane tabbedPane;
    
    private JButton confirm;
    private JLabel nodeLabel;
    private JComboBox nodeBox;
    private JLabel xpLabel;
    private JTextField xpText;
    private JLabel timeLabel;
    private JTextField timeText;
    
    private File runespanFile;
    
    private java.util.List<String> entries;
    
    private java.util.List<String> nodes;
    private java.util.List<Integer> xps;
    private java.util.List<Integer> times;
    private java.util.List<Integer> counts;
    
    public RunespanCalc()
    {
        super("Runespan Calculator");
        
        tabbedPane =  new JTabbedPane();
        
        nodes = new ArrayList<>();
        nodes.add("Chaotic Cloud");
        nodes.add("Nebula");
        nodes.add("Shifter");
        nodes.add("Jumper");
        nodes.add("Skulls");
        nodes.add("Blood Pool");
        nodes.add("Bloody Skulls");
        nodes.add("Living Soul");
        nodes.add("Undead Soul");
        nodes.add("Blood Esswraith");
        nodes.add("Soul Esswraith");
        
        xps = new ArrayList<>(nodes.size());
        times = new ArrayList<>(nodes.size());
        counts = new ArrayList<>(nodes.size());
        
        for (int i = 0; i < nodes.size(); i++)
        {
            xps.add(0);
            times.add(0);
            counts.add(0);
        }
        
        nodeLabel = new JLabel("Node: ");
        nodeBox = new JComboBox(nodes.toArray());
        
        xpLabel = new JLabel("XP: ");
        xpText = new JTextField();
        
        timeLabel = new JLabel("Time (in seconds): ");
        timeText = new JTextField();
        
        confirm = new JButton("Confirm");
        
        entries = new ArrayList<>();
        
        runespanFile = new File("Runespan.txt");
        try
        {
            if(!runespanFile.exists())
            {
                runespanFile.createNewFile();
            }
            else
            {
                BufferedReader reader = new BufferedReader(new FileReader(runespanFile));
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    entries.add(line);
                    addData(line);
                }
                reader.close();
            }
        }
        catch (IOException e)
        {
            
        }
    }
    
    public void addComponents()
    {
        tabbedPane.removeAll();
        
        Panel add = new Panel(new GridBagLayout());
        GridBagConstraints addc = new GridBagConstraints();
        addc.anchor = GridBagConstraints.CENTER;

        addc.gridx = 0;
        addc.gridy = 0;
        
        add.add(nodeLabel, addc);
        addc.gridx = 1;
        add.add(nodeBox, addc);
        
        addc.gridx = 0;
        addc.gridy = 1;
        
        add.add(xpLabel, addc);
        addc.gridx = 1;
        addc.fill = GridBagConstraints.HORIZONTAL;
        add.add(xpText, addc);
        addc.fill = GridBagConstraints.NONE;
        
        addc.gridx = 0;
        addc.gridy = 2;
        
        add.add(timeLabel, addc);
        addc.gridx = 1;
        addc.fill = GridBagConstraints.HORIZONTAL;
        add.add(timeText, addc);
        addc.fill = GridBagConstraints.NONE;
        
        addc.gridx = 0;
        addc.gridy = 3;
        addc.gridwidth = 2;
        
        add.add(confirm, addc);
        
        tabbedPane.add("Add new time", add);
        
        
        Panel view = new Panel(new GridBagLayout());
        GridBagConstraints viewc = new GridBagConstraints();
        viewc.anchor = GridBagConstraints.CENTER;
        viewc.insets = new Insets(0, 10, 0, 10);

        viewc.gridx = 0;
        viewc.gridy = 0;
        
        JLabel headerLabel1 = new JLabel("Node");
        view.add(headerLabel1, viewc);
        
        viewc.gridx = 1;
        
        JLabel headerLabel2 = new JLabel("Times Siphoned");
        view.add(headerLabel2, viewc);
        
        viewc.gridx = 2;
        
        JLabel headerLabel3 = new JLabel("Total XP");
        view.add(headerLabel3, viewc);
        
        viewc.gridx = 3;
        
        JLabel headerLabel4 = new JLabel("Total Time");
        view.add(headerLabel4, viewc);
        
        viewc.gridx = 4;
        
        JLabel headerLabel5 = new JLabel("XP per Hour");
        view.add(headerLabel5, viewc);
        
        for (int i = 0; i < nodes.size(); i++)
        {
            viewc.gridy = i + 1;
            viewc.gridx = 0;
            
            int xp = xps.get(i);
            int time = times.get(i);
            int result;
            if (time == 0)
            {
                result = 0;
            }
            else
            {
                result = xp * 3600 / time;
            }
            
            JLabel tempLabel1 = new JLabel(nodes.get(i));
            view.add(tempLabel1, viewc);
            
            viewc.gridx = 1;
            
            JLabel tempLabel2 = new JLabel("" + counts.get(i));
            view.add(tempLabel2, viewc);
            
            viewc.gridx = 2;
            
            JLabel tempLabel3 = new JLabel("" + xp);
            view.add(tempLabel3, viewc);
            
            viewc.gridx = 3;
            
            JLabel tempLabel4 = new JLabel("" + time);
            view.add(tempLabel4, viewc);
            
            viewc.gridx = 4;
            
            JLabel tempLabel5 = new JLabel("" + result);
            view.add(tempLabel5, viewc);
        }
        
        tabbedPane.add("View Data", view);
        
        add(tabbedPane);
    }
    
    public void addListeners()
    {
        confirm.addActionListener(e -> {
            try
            {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(runespanFile, true)));
                String entry = nodeBox.getSelectedItem().toString() + "." + xpText.getText() + "." + timeText.getText();
                
                out.println(entry);
                addData(entry);
                
                out.close();
            }
            catch (IOException e1)
            {
                
            }
            
            nodeBox.setSelectedIndex(0);
            xpText.setText("");
            timeText.setText("");
            
            addComponents();
        });
    }
    
    public void addData(String line)
    {
        int index1 = line.indexOf(".");
        int index2 = line.indexOf(".", index1 + 1);
        
        String node = line.substring(0, index1);
        int xpCount = Integer.parseInt(line.substring(index1 + 1, index2));
        int timeCount = Integer.parseInt(line.substring(index2 + 1));
        
        int arrayIndex = nodes.indexOf(node);
        xps.set(arrayIndex, xps.get(arrayIndex) + xpCount);
        times.set(arrayIndex, times.get(arrayIndex) + timeCount);
        counts.set(arrayIndex, counts.get(arrayIndex) + 1);
    }

    public static void main(String[] args)
    {
        RunespanCalc rc = new RunespanCalc();
        rc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rc.addListeners();
        rc.addComponents();
        rc.pack();
        rc.setLocationRelativeTo(null);
        rc.setVisible(true);
    }
    
}
