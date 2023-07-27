/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
/**
 *

 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    JPanel panel1 = new JPanel();
    JPanel line1 = new JPanel();
    JPanel line2 = new JPanel();

    // create the widgets for the firstLine Panel.
    JLabel shape = new JLabel("Shape: ");

    JComboBox comboBox = new JComboBox();

    JButton undo = new JButton("Undo");
    JButton clear = new JButton("Clear");
    
    JButton firstColor = new JButton("1st Color...");
    JButton secondColor = new JButton("2nd Color...");

    //create the widgets for the secondLine Panel.
    JCheckBox filled = new JCheckBox("Filled");
    JCheckBox gradient = new JCheckBox("Use Gradient");    
    JCheckBox dashed = new JCheckBox("Dashed");
    
    JSpinner width = new JSpinner();
    JSpinner length = new JSpinner();
        
    JLabel options = new JLabel("Options: ");
    JLabel dashLength = new JLabel("Dash Length: ");
    JLabel lineWidth = new JLabel("Line Width: ");



    // Variables for drawPanel.
    ArrayList<MyShapes> myShapes = new ArrayList<MyShapes>();
    Color color1;
    Color color2;
    Paint paint;
    Stroke stroke;
    
    DrawPanel drawPanel = new DrawPanel();

    // add status label
    JLabel status = new JLabel("(0,0)");
    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        setLayout(new BorderLayout());
        // add widgets to panels
        
        // firstLine widgets
        
        String shapes[] = { "Line", "Oval", "Rectangle" };

        comboBox.setModel(new DefaultComboBoxModel<>(shapes));
        comboBox.setMaximumRowCount(3);
        
        firstColor.addActionListener(new ButtonHandler());
        
        secondColor.addActionListener(new ButtonHandler());

        
        undo.addActionListener(new ButtonHandler());

        clear.addActionListener(new ButtonHandler());
        
        line1.add(shape);
        line1.add(comboBox);
        line1.add(firstColor);
        line1.add(secondColor);
        line1.add(undo);
        line1.add(clear);
        // secondLine widgets
        SpinnerNumberModel model = new SpinnerNumberModel(4, 0, 99, 1);  
        SpinnerNumberModel model2 = new SpinnerNumberModel(15, 1, 99, 1);  

        width.setModel(model);
        length.setModel(model2);

        line2.add(options);
        line2.add(filled);
        line2.add(gradient);
        line2.add(dashed);
        line2.add(lineWidth);
        line2.add(width);
        line2.add(dashLength);
        line2.add(length);
        // add top panel of two panels
        
        line1.setBackground(Color.CYAN);
        line2.setBackground(Color.CYAN);

        panel1.setLayout(new BorderLayout());
        panel1.add(line1, BorderLayout.NORTH);
        panel1.add(line2, BorderLayout.SOUTH);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        panel1.setBackground(Color.CYAN);
        add(panel1, BorderLayout.NORTH);
        
        add(drawPanel, BorderLayout.CENTER);
        
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(status, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
        //add listeners and event handlers
    }

    // Create event handlers, if needed
    private class ButtonHandler implements ActionListener{
        
        @Override 
        public void actionPerformed(ActionEvent event){
            
            if(event.getActionCommand() == "1st Color..."){
                color1 = JColorChooser.showDialog(firstColor, "Set Color", color1);
            }
            else if(event.getActionCommand() == "2nd Color..."){
                color2 = JColorChooser.showDialog(secondColor, "Set Color", color2);

            }
            else if(event.getActionCommand() == "Undo" && myShapes.size()>0){
                myShapes.remove(myShapes.size()-1);
                drawPanel.repaint();
            }
            else if(event.getActionCommand() == "Clear"){
                myShapes.clear();
                drawPanel.repaint();

            }
            
        }
    } 

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

                
        public DrawPanel()
        {
            super();
            setBackground(Color.WHITE);
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseHandler());
            color1 = Color.BLACK;
            color2 = Color.WHITE;
                        
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            //loop through and draw each shape in the shapes arraylist
            for (MyShapes shape : myShapes){
                shape.draw(g2d);
            }   
            
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            
            @Override
            public void mousePressed(MouseEvent event)
            {
                boolean fill = filled.isSelected();
                    
                if(gradient.isSelected()){
                    System.out.print("e");
                    paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);

                }else{
                    paint = color1;

                }
                if (dashed.isSelected()){
                    stroke = new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10,new float[]{(Integer)length.getValue()},  0);
                }else{
                    stroke = new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }

                if(comboBox.getSelectedItem()=="Line"){
                    myShapes.add(new MyLine(event.getPoint(),event.getPoint(), paint, stroke));
                    
                }else if (comboBox.getSelectedItem()=="Rectangle"){
                    myShapes.add(new MyRectangle(event.getPoint(),event.getPoint(), paint, stroke, fill));

                }else if (comboBox.getSelectedItem()=="Oval"){
                    myShapes.add(new MyOval(event.getPoint(),event.getPoint(), paint, stroke, fill));

                }
            }
           

            public void mouseReleased(MouseEvent event)
            {
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                myShapes.get( myShapes.size()-1).setEndPoint(event.getPoint());
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                status.setText("(" + event.getPoint().x + "," + event.getPoint().y + ")");
            }
        }

    }
}
