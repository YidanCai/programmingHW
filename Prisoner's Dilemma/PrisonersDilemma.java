
/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * main class
 * 
 * @author Raffaele Tozzi   1592912
 * @author Yidan Cai        1630083 
 * assignment group 114
 * 
 * assignment copyright Kees Huizing
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Hashtable;

class PrisonersDilemma implements ActionListener /* possible extends... */ {
    //...
    JButton Go,Pause,Reset;
    JPanel middle,bottom;
    JLabel a,f;
    JCheckBox change;
    PlayingField playpanel=new PlayingField();
    Timer timer;
    int interval;
    boolean bool; //excute or stop
    
    void buildGUI() {
        SwingUtilities.invokeLater( () -> {
            //...
            JFrame frame=new JFrame();

            frame.setSize(600,700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            

            playpanel.fillgrid();
            playpanel.addNeighbour();
            playpanel.randompatch();

            

            a=new JLabel("set alpha");
            f=new JLabel("speed");

            //a slider for setting alpha
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
            Hashtable labelTable = new Hashtable();
            labelTable.put( 0, new JLabel("0.0") );
            labelTable.put( 10, new JLabel("1.0") );
            labelTable.put(20, new JLabel("2.0") );
            labelTable.put(30, new JLabel("3.0") );
            slider.setMinorTickSpacing(5);  
            slider.setMajorTickSpacing(10);  
            slider.setLabelTable(labelTable); 
            slider.setPaintTicks(true);  
            slider.setPaintLabels(true);  
            playpanel.setAlpha(1.0);
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    playpanel.setAlpha(((JSlider)e.getSource()).getValue()/10.0);
                    
                }
            });
            //a slider for setting frequency, the higher the faster the change
            JSlider frequency = new JSlider(JSlider.HORIZONTAL, 1, 60,30);
            frequency.setMajorTickSpacing(59); 
            frequency.setMinorTickSpacing(10);   
            frequency.setPaintTicks(true);
            frequency.setPaintLabels(true);
            playpanel.setsleep(10000/30);
            frequency.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    interval=10000/((JSlider)e.getSource()).getValue();
                    if(bool){
                        playpanel.evolve(false);
                    }                   
                    playpanel.setsleep(interval);
                    playpanel.settimer();
                    if(bool){
                        playpanel.evolve(bool);

                    }
                    

                }
            });
            
            Go=new JButton("GO");
            Go.addActionListener(this);
            Pause=new JButton("Pause");
            Pause.addActionListener(this);
            Reset=new JButton("reset");
            Reset.addActionListener(this);
            Reset.setFocusable(false);

            middle =new JPanel();
            middle.setSize(600,100);
            middle.setLayout( new GridLayout(2, 2) );
            middle.add(a);
            middle.add(f);
            middle.add(slider);
            
            middle.add(frequency);
            
            bottom=new JPanel();
            bottom.setSize(new Dimension(600,100));
            bottom.setLayout(new FlowLayout());

            // a checkbox for deciding whether we allow the agent to change when it already reach the highest among neighbours
            change=new JCheckBox("change when highest?");
            change.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    if(e.getStateChange()==1){
                        playpanel.highestchange(true);
                    }
                    else{
                        playpanel.highestchange(false);
                    }
                }
            });


            bottom.add(change);
            bottom.add(Go);
            bottom.add(Pause);
            bottom.add(Reset);
            frame.add(playpanel);
            frame.add(playpanel);
            frame.add(middle);
            frame.add(bottom);
            

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);  
        }

        );


    }
    //action performed when relevant button clicked
    public void actionPerformed(ActionEvent e){
        
        if(e.getSource()==Reset){
            playpanel.randompatch();
        }
        else if (e.getSource()==Go){
            if (!bool){
                bool=true;
                playpanel.settimer();
                playpanel.evolve(bool);

            }

        }
        else if(e.getSource()==Pause){
            bool=false;
            playpanel.evolve(bool);

        }

    }
    //...
    
    public static void main( String[] a ) {
        (new PrisonersDilemma()).buildGUI();
    }
}
