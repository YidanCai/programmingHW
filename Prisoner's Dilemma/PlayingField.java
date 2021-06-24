/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * @author Raffaele Tozzi   1592912
 * @author Yidan Cai        1630083 
 * assignment group 114
 * 
 * assignment copyright Kees Huizing
 */

import java.util.Random;
import java.util.*; 
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.event.*;
import java.awt.Dimension;

class PlayingField extends JPanel implements ActionListener /* possible implements ... */ {
    public static int GRID_LENGTH =50;
    public static int GRID_WIDTH =50;
   
    private Patch[][] grid=new Patch[GRID_LENGTH][GRID_WIDTH];
    
    private double alpha; // defection award factor 
    
    private Timer timer;
    private int sleep;


    private boolean[][] CoopGrid=new boolean[GRID_LENGTH][GRID_WIDTH];

    
    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random random = new Random( SEED );         
    
    //...
    PlayingField(){
        this.setPreferredSize(new Dimension(600,600));
    }
    //set the timer between each round
    public void settimer(){

        timer = new Timer(sleep, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
                
                step();
    
            }
        });
        timer.setRepeats(true);    

    }
    //set the time interval
    public void setsleep(int sleep){
        this.sleep=sleep;

    }

    
    //start or stop the timer
    public void evolve(boolean begin){           
            
            if (begin) {                   
            
                timer.start();

        }
        else {
            timer.stop();
            
        }
        
    
    }
    //fill the panel with agents
    public void fillgrid(){
        
        for (int i=0;i<GRID_LENGTH;i++){
            for (int j=0;j<GRID_WIDTH;j++){
                grid[i][j]=new Patch();
                setLayout(null);
                grid[i][j].setBounds(50+j*10,30+i*10,11,11);
                grid[i][j].addActionListener(this);
                this.add(grid[i][j]);
                
            }
        }
        
    }
    // an important function to get the neighbour of an agent
    //radius means how far it reach the neighbours, in this game we set it 1 so each agent has 8 neighbour
    public int[][] getNeighbours(int r, int c,int radius){
        int[][] neighbour =new int[(1+2*radius)*(1+2*radius)-1][2];
        List<Integer> row=new ArrayList<Integer>();
        List <Integer> column=new ArrayList<Integer>();
        int count=0;
        for (int i=Math.max(0,r-radius);i<=Math.min(GRID_LENGTH-1,r+radius);i++){
            row.add(i);
        }
        if (r+radius>=GRID_LENGTH){
            for (int i=0;i<Math.max(0,r+radius-GRID_LENGTH+1);i++){
                row.add(i);
            }
        }
        if (r-radius<0){
            for (int i=Math.min(r-radius+GRID_LENGTH,GRID_LENGTH);i<GRID_LENGTH;i++){
                row.add(i);
            }
        }        

        for (int j=Math.max(0,c-radius);j<=Math.min(GRID_WIDTH-1,c+radius);j++){
            column.add(j);
        }
        if (c+radius>=GRID_WIDTH){
            for (int j=0;j<Math.max(0,c+radius-GRID_WIDTH+1);j++){
                column.add(j);
            }
        }
        if(c-radius<0){
            for (int j=Math.min(c-radius+GRID_WIDTH,GRID_WIDTH);j<GRID_WIDTH;j++){
                column.add(j);
            }
        }

        for (int i=0;i<2*radius+1;i++){
            for (int j=0;j<2*radius+1;j++){
                if (row.get(i)!=r || column.get(j)!=c){
                    neighbour[count][0]=row.get(i);
                    neighbour[count][1]=column.get(j);
                    count++;
                }
            }
        }
        return neighbour;


    }
    //add the neighbour to each agent
    public void addNeighbour(){
        int radius=1;
        int[][] neighbour=new int[(1+2*radius)*(1+2*radius)-1][2];
        
        for (int i=0;i<GRID_LENGTH;i++){
            for (int j=0;j<GRID_WIDTH;j++){
                neighbour=getNeighbours(i, j, radius);
                for (int n=0;n<(1+2*radius)*(1+2*radius)-1;n++){
                    grid[i][j].addneighbour(grid[neighbour[n][0]][neighbour[n][1]]);

                }              
            }
        }        
    }
    
    //executed at the beginning and reset, filled with random choices
    public void randompatch(){

        for (int i=0;i<GRID_LENGTH;i++){
            for (int j=0;j<GRID_WIDTH;j++){
                CoopGrid[i][j]=random.nextBoolean();
            }
        }
        setGrid(CoopGrid);
        resetScore();
        resetchange();

    }
    /**
     * calculate and execute one step in the simulation 
     */
    public void step( ) {
        //...
        for (int i=0;i<GRID_LENGTH;i++){
            for (int j=0;j<GRID_WIDTH;j++){
                CoopGrid[i][j]=grid[i][j].strategy();
            }
        }
        setGrid(CoopGrid);
    }
    
    public void setAlpha( double alpha ) {
        //...
        this.alpha=alpha;
    }
    
    public double getAlpha( ) {
        //...
        return this.alpha; // CHANGE THIS
    }
    
    // return grid as 2D array of booleans
    // true for cooperators, false for defectors
    // precondition: grid is rectangular, has non-zero size and elements are non-null
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++ ) {
            for (int y = 0; y < grid[0].length; y++ ) {
                resultGrid[x][y] = grid[x][y].isCooperating();
            }
        }
        
        return resultGrid; 
    }
    
    // sets grid according to parameter inGrid
    // a patch should become cooperating if the corresponding
    // item in inGrid is true
    public void setGrid( boolean[][] inGrid) {
        // ...
        for (int x = 0; x < grid.length; x++ ) {
            for (int y = 0; y < grid[0].length; y++ ) {
                if (grid[x][y].isCooperating()!=inGrid[x][y]){
                    grid[x][y].setchange(true);
                }
                else{
                    grid[x][y].setchange(false);
                }
                grid[x][y].setCooperating(inGrid[x][y]);
                
                
                grid[x][y].repaint();
            }
        }  
        setScore();      
    }

    //return the whole patch grid
    
    public Patch[][] getPatch(){
        Patch[][] patchgrid = new Patch[GRID_LENGTH][GRID_WIDTH];
        for (int x = 0; x < GRID_LENGTH; x++ ) {
            for (int y = 0; y < GRID_WIDTH; y++ ) {
                patchgrid[x][y] = grid[x][y];
            }
        }
        return patchgrid;        
    }

    //set the scores of agents
    public void setScore(){
        double a_score;
        int count;
        
        for (int x = 0; x < GRID_LENGTH; x++ ) {
            for (int y = 0; y < GRID_WIDTH; y++ ) {
                count=grid[x][y].cooperateNeighbour();
                if (grid[x][y].strategy()){
                    a_score=count;
                }
                else{
                    a_score=alpha*count;
                }
                grid[x][y].setScore(a_score);                              
            }
        }
        

    }
    public void resetScore(){
        for (int x = 0; x < GRID_LENGTH; x++ ) {
            for (int y = 0; y < GRID_WIDTH; y++ ) {

                grid[x][y].setScore(0.0);                              
            }
        }

    }
    public void resetchange(){
        for (int x = 0; x < GRID_LENGTH; x++ ) {
            for (int y = 0; y < GRID_WIDTH; y++ ) {

                grid[x][y].setchange(false);                              
            }
        }

    }
    //do we want the agnet to change when it reach the highest score among neighbours
    public void highestchange(boolean changeWhenHighest){
        Patch.changeWhenHighest=changeWhenHighest;

    }

    
    //click the agent to change its strategy
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int x = 0; x < GRID_LENGTH; x++ ) {
            for (int y = 0; y < GRID_WIDTH; y++ ) {

                if (e.getSource()==grid[x][y]){
                    grid[x][y].toggleStrategy();
                    grid[x][y].setchange(true);

                }                         
            }
        }

    }



}

