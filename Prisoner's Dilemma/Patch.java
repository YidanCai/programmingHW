/**
 * INCOMPLETE 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * 
 * @author Raffaele Tozzi   1592912
 * @author Yidan Cai        1630083 
 * assignment group 114
 * 
 * assignment copyright Kees Huizing
 */
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.Graphics2D;

class Patch extends JButton{
    //...
    private boolean coop;
    private double score;
    private ArrayList<Patch> neighbour=new ArrayList<Patch>();
    private boolean change;
    public static boolean changeWhenHighest;

    int length=PlayingField.GRID_LENGTH;
    int widt=PlayingField.GRID_WIDTH;


    Patch[][] patchgrid;


    // returns true if and only if patch is cooperating
    boolean isCooperating() {
        //...
        return this.coop; // CHANGE THIS
    }
    
    // set strategy to C if isC is true and to D if false
    void setCooperating(boolean isC) {
        //...
        this.coop=isC;

        
    }
    //did the agent change strategy?

    void setchange(boolean isc){
        this.change=isc;
    }
    
    // change strategy from C to D and vice versa
    void toggleStrategy() {
        // ...
        this.coop=!coop;
    }
    
    // return score of this patch in current round
    double getScore() {
        
        //...
        return this.score; // CHANGE THIS
    }
    void setScore(double score){
        this.score=score;
    }
    void addneighbour(Patch p){
        neighbour.add(p);
    }
// get the next strategy
    boolean strategy(){
        Double highest=-1.0;
        Random r=new Random();
        boolean str;

        ArrayList<Boolean> strategies= new ArrayList<Boolean>();
        for (Patch c:neighbour){
            if (c.getScore()>highest){
                highest=c.getScore();
                strategies.clear();
                strategies.add(c.coop);
            }
            else if(c.getScore()==highest){
                strategies.add(c.coop);
            }
        }
        if (score<highest){
            str=strategies.get(r.nextInt(strategies.size()));
        }

        else{
            str=coop;
        }
        //if we want the agent able to change strategy when it's the highest (but may have a tie)

        if (changeWhenHighest && score==highest){
 
            str=strategies.get(r.nextInt(strategies.size()));
            
        }
        return str;
    }

    //how many neighbours are cooporating
    int cooperateNeighbour(){
        int count=0;
        for (Patch c:neighbour){
            if (c.strategy()){
                count++;
            }
        }
        return count;
    }
   //draw the buttons
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.draw(new RoundRectangle2D.Double(0, 0, 10,10, 5, 5));

        //if the agent cooperating
        if (coop){
            //if it changed strategy
            if (change){
                g2.setColor(Color.cyan);
            }
            //if it didn't change
            else{
                g2.setColor(Color.blue);

            }
                    
                }
        // if the agent defect
        else {
            //if it changed strategy
            if (change){
                g2.setColor(Color.orange);
            }
            //if it didn't change strategy
            else{
                g2.setColor(Color.red);

            }
                    
                }
        g2.fillRoundRect(0, 0, 10,10, 5, 5);


        g2.dispose();
    }
    
}
