/*
THE GRID CLASS CONSTRUCTS THE BOARD OF THE GAME
 */
package team.coronoa.reborn;

import java.util.ArrayList;

/**
 *
 * @author AzizAshy-PC
 */
public class Grid {
    private int height; //no. of rows
    private int width; // no. of cols
    private int[][] map;
    
    /*
    A A A A A A A A A 
    */

    public Grid(int height, int width) {
        this.height = height;
        this.width = width;
        this.map = new int[height][width];
        
    }
    
    //A COPY CONSTRUCTOR
    public Grid(Grid x){
        this.height = x.getHeight();
        this.width = x.getWidth();
        this.map = new int[height][width];
        for(int i = 0; i<x.getHeight();i++){
            for(int j =0; j<x.getWidth();j++){
                this.map[i][j]=x.getMap()[i][j];
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }
    
    public boolean isValidMove(int col){
        return this.map[this.height-1][col] == 0;
    }
    
    //DROPS A PIECE IN THE BOARD IF IT IS A VALID MOVE
    public void makeMove(int col, int turn){
        if(isValidMove(col)){
            int row = getValidRow(col);

            if(turn%2 == 0){
                this.map[row][col]=1;
            } else{
                this.map[row][col]=2;
            }
        } else{
            System.out.println("ILLEGAL MOVE: " +col);
        }
    }
    
    //GETS THE ROW WHERE THE PIECE WILL BE DROPPED (WITH RESPECT TO THE COLUMN)
    public int getValidRow(int col)
    {
        

        for(int row = 0; row < this.height; row++)
            if(this.map[row][col] == 0)
                return row;
        return this.height+1;
    }
    
    public void display(){
        
        for(int i = this.map.length-1; i>=0; i--){
            
            for(int j = 0; j<this.map[0].length;j++){
                
                if(this.map[i][j]==0){
                    System.out.print("X");
                    System.out.print(" ");
                } else if(this.map[i][j]==1){
                    System.out.print("B");
                    System.out.print(" ");
                } else{
                    System.out.print("W");
                    System.out.print(" ");
                }   
            }
            System.out.println(" ");
            
        }
    }
    
    /*
    CHECKS IF THE GAME IS OVER.
    It uses a sliding window which colvolves over the array in a
    horizontal, vertical, positively diagonal, and negatively diagonal manner.
    */
    public boolean isGameOver(Grid state) {
        int piece;
        for(int i = 1; i<2;i++){
        piece = i;

        int rowCount = 0;

        //Checking horizontal winning conditions
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.getWidth(); col++) {
                if (state.getMap()[row][col] == piece) {
                    rowCount++;
                    if (rowCount == 5) {
                        return true;
                    }
                } else {
                    rowCount = 0;
                }
            }
            rowCount = 0;
        }

        
        //Checking vertical winning conditions

        for (int col = 0; col < this.getWidth(); col++) {
            for (int row = 0; row < this.getHeight(); row++) {

                if (state.getMap()[row][col] == piece) {
                    rowCount++;
                    if (rowCount == 5) {
                        return true;
                    }
                } else {
                    rowCount = 0;
                }
            }
            rowCount = 0;
        }
 
        //Checking diagonal positions (increasing)
        for(int col = 0; col < this.getWidth()-5; col++)
            for(int row = 0; row < this.getHeight()-5; row++)
                if(state.getMap()[row][col] == piece && state.getMap()[row+1][col+1] == piece && state.getMap()[row+2][col+2] == piece 
                        && state.getMap()[row+3][col+3] == piece && state.getMap()[row+4][col+4] == piece)
                    return true;
        
        //Checking diagonal positions (decreasing)
        for(int col = 0; col < this.getWidth()-5; col++)
            for(int row = 4; row < this.getHeight(); row++)
                if(state.getMap()[row][col] == piece && state.getMap()[row-1][col+1] == piece && state.getMap()[row-2][col+2] == piece 
                        && state.getMap()[row-3][col+3] == piece && state.getMap()[row-4][col+4] == piece)
                    return true;
        }
        
        return false;
        
         
    }
    
    //GETS A LIST OF VALID MOVES
    public ArrayList<Integer> moveList(){
        ArrayList<Integer> moveList = new ArrayList<Integer>();
        
        for(int i = 0;i<this.getWidth();i++){
            if(isValidMove(i))
                moveList.add(i);
        }
        return moveList;
    }
    
    //CHECKS IF THE BOARD STATE IS TERMINAL.
    //IF IT IS TERMINAL, THEN EITHER THERE ARE NO REMAINING MOVES LEFT OR
    //A PLAYER WON THE GAME
    public boolean isTerminal(){
        
        ArrayList<Integer> list = this.moveList();
        
        if(this.isGameOver(this) || list.isEmpty()){
            return true;
        }
        return false;
    }
    
    
}
