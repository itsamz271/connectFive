//CONNECT 5 GAME | TEAM CORONOA
//ABDUL MOIZ AMIR | 217210582
//ABDULAZIZ ASHY | 217210682

package team.coronoa.reborn;

import java.util.ArrayList;

public class Game {

    int COLS = 9;
    int ROWS = 6;
    boolean gameOver = false;
    Grid board = new Grid(ROWS, COLS); //Dimensions of the board
    int turn = 0; //keeps track of player turns
    final int WINDOWLENGTH = 5; //Length of window which is used to calculate heuristic
    double timeLimit = 0;
    double currentTime;
    
    public Game() 
    {

    }
    
    public Game(double timeLimit)
    {
        this.timeLimit = timeLimit;
    }
    
    /*
    CHECKS IF THE GAME IS OVER.
    It uses a sliding window which colvolves over the array in a
    horizontal, vertical, positively diagonal, and negatively diagonal manner.
    */
    public boolean isGameOver(Grid state, int turn) {
        
        int piece = 0;
        
        if(turn == 0)
            piece = 1;
        else
            piece = 2;
        
        int rowCount = 0;

        //Checking horizontal positions
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
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

        
        //Checking vertical positions

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {

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
        
        //Recheck if you have time
        //Checking diagonal positions (increasing)
        for(int col = 0; col < COLS-5; col++)
            for(int row = 0; row < ROWS-5; row++)
                if(state.getMap()[row][col] == piece && state.getMap()[row+1][col+1] == piece && state.getMap()[row+2][col+2] == piece 
                        && state.getMap()[row+3][col+3] == piece && state.getMap()[row+4][col+4] == piece)
                    return true;
        
        //Checking diagonal positions (decreasing)
        for(int col = 0; col < COLS-5; col++)
            for(int row = 4; row < ROWS; row++)
                if(state.getMap()[row][col] == piece && state.getMap()[row-1][col+1] == piece && state.getMap()[row-2][col+2] == piece 
                        && state.getMap()[row-3][col+3] == piece && state.getMap()[row-4][col+4] == piece)
                    return true;
        
        return false;
         
    }

    public Grid getBoard() {
        return board;
    }
    
    
    public int getTurn() {
        return this.turn;
    }

    public void changeTurn() {
        this.turn++;
    }
    
    //checks if the attempted move is valid
    public boolean isValidMove(int move) {
        return board.isValidMove(move);
    }
    
    //Drops a piece in the board
    public void makeMove(int col) {
        this.board.makeMove(col, this.turn);
    }
    
    //Displays the board
    public void display() {

        this.board.display();
    }

    //RUNS THE MINMAX ALGORITHM WITHOUT THE ALPHA BETA
    public int[] MiniMax(Grid board, int depth, boolean maximizingPlayer){
        ArrayList<Integer> validLocations = board.moveList();
        boolean isTerminal = board.isTerminal();
        if(depth == 0 || isTerminal){
            int[] pair = new int[2];
            pair[0] = -1;
            
            if(isTerminal)
            {
                if(this.isGameOver(board, 1)){
                    pair[1] = 1000000;
                    return pair;
                } else if(this.isGameOver(board, 0)){
                    pair[1] = -1000000;
                    return pair;
                } else {
                    pair[1] = 0;
                    return pair;
                }
            } 
            else {
                pair[1]=heuristic(board,1);
                return pair;
            }
        }
        //max level
        if (maximizingPlayer){
            int value = -1000000000;
            int column = getRandomValidMove(validLocations);
            
            int[] pair = new int[2];
            
            for(int col: validLocations)
            {
                Grid child = new Grid(board);
                child.makeMove(col, 1);
                int newScore = MiniMax(child, depth-1, false)[1];
                
                if(newScore > value)
                {
                    value = newScore;
                    column = col;
                }
            }
            
            pair[0] = column;
            pair[1] = value;
            
            return pair;
        } 
        else //min level
        {
            int col = getRandomValidMove(validLocations);
            int value = 1000000000;
            int[] pair = new int[2];
            for(int i = 0; i<validLocations.size();i++){
                
                Grid child = new Grid(board);
                child.makeMove(validLocations.get(i), 0);
                int[] new_score = MiniMax(child,depth-1,true);
                if(new_score[1]<value){
                    value = new_score[1];
                    col = i;
                }
                
                
            }
            pair[0]=col;
            pair[1]=value;
            return pair;
        }
    }
    //MINIMAX WITH ALPHA-BETA
    public int[] MiniMaxAlphaBeta(Grid board, int depth, int alpha, int beta, boolean maximizingPlayer)
    {
        //Checks if time limit has reached when exploring tree depths
        if(timeLimit >0)
        {
            if(System.currentTimeMillis()-currentTime > timeLimit)
            {
                int[] pair = new int[2];
                pair[0]= -1;
                pair[1]= -100000000;
                return pair;
            }
        }
        
        
        ArrayList<Integer> validLocations = board.moveList();
        boolean isTerminal = board.isTerminal();
        
        //checks if max depth is reached or if its a chid node
        if(depth == 0 || isTerminal){
            int[] pair = new int[2];
            pair[0] = -1;
            if(isTerminal){
                if(this.isGameOver(board, 1)){
                    pair[1] = 1000000;
                    return pair;
                } 
                else if(this.isGameOver(board, 0)){
                    pair[1] = -1000000;
                    return pair;
                } 
                else
                {
                    pair[1] = 0;
                    return pair;
                }
            } 
            else //gets heuristic of the board state
            {
                pair[1]=heuristic(board,1);
                return pair;
            }
        }
        //max level
        if (maximizingPlayer)
        {
            int value = -1000000000;
            int column = getRandomValidMove(validLocations);
            
            int[] pair = new int[2];
            
            for(int col: validLocations)
            {
                Grid child = new Grid(board);
                child.makeMove(col, 1);
                int newScore = MiniMaxAlphaBeta(child, depth-1, alpha, beta, false)[1];
                
                if(newScore > value)
                {
                    value = newScore;
                    column = col;
                }
                alpha = Math.max(alpha, value);
                
                if(alpha >= beta)
                    break;
            }
            
            pair[0] = column;
            pair[1] = value;
            
            return pair;
        } 
        //min level
        else 
        {
            int col = getRandomValidMove(validLocations);
            int value = 1000000000;
            int[] pair = new int[2];
            for(int i = 0; i<validLocations.size();i++){
                
                Grid child = new Grid(board);
                child.makeMove(validLocations.get(i), 0);
                int[] new_score = MiniMaxAlphaBeta(child,depth-1, alpha, beta, true);
                if(new_score[1]<value){
                    value = new_score[1];
                    col = i;
                }
                
                beta = Math.min(beta, value);
                if(alpha >= beta)
                    break;
                
            }
            pair[0]=col;
            pair[1]=value;
            return pair;
        }
    }
    
    
    //checking the board with a window size of 5
    public int heuristic(Grid board, int turn)
    {
        int piece = 0;
        if (turn%2 ==0)
            piece = 1;
        else
            piece = 2;
        
        int heuristic = 0;
        
        
          
        //CHECKING HORIZONTAL HEURISTIC
        for(int row = 0; row < ROWS; row++)
        {
            int[] rowArray = new int[COLS];
            
            
            
            //Getting a single row of the board
            for(int col = 0; col<COLS; col++)
                rowArray[col] = board.getMap()[row][col];
            
            
            
            for(int col = 0; col < COLS-4; col++)
            {
                int[] window = new int[WINDOWLENGTH];
                
                int index1 = 0;
                for(int index = col; index < col+WINDOWLENGTH; index++)
                {
                    window[index1] = rowArray[index];
                    index1++;
                }
                
                //COUNTING THE NUMBER OF SAME COLOR PIECES IN THE WINDOW
                
                
                heuristic += windowHeuristic(window, piece);
                
            }
        }
        
        //CHECKING VERTICAL HEURISTIC
        
        for(int col = 0; col <COLS; col++)
        {
            //Getting each column of the board
            int[] colArray = new int[ROWS];
            
            for(int row = 0; row < ROWS; row++)
                colArray[row] = board.getMap()[row][col];
            
            for(int row = 0; row < ROWS-4; row++)
            {
                int[] window = new int[WINDOWLENGTH];
                
                int index1 = 0;
                for(int index = row; index < row+WINDOWLENGTH; index++)
                {
                    window[index1] = colArray[index];
                    index1++;
                }
                
                //COUNTING THE NUMBER OF SAME COLOR PIECES IN THE WINDOW
                
                
                heuristic += windowHeuristic(window, piece);
          
            }
            
           
        }
        
        
        
        
        //Checking diagonal heuristic (positively sloped)
        for(int row = 0; row <ROWS-4; row++)
        {
            for(int col = 0; col <COLS-4; col++)
            {
                int[] window = new int[WINDOWLENGTH];
                int index1= 0;
                for(int index = 0; index < WINDOWLENGTH; index++)
                {
                    window[index1] = board.getMap()[row+index][col+index];
                    
                    heuristic += windowHeuristic(window, piece);
                }
            }
        }
        
        
        //Checking diagonal heuristic (negatively sloped)
        for(int row = 0; row <ROWS-4; row++)
        {
            for(int col = 0; col <COLS-4; col++)
            {
                int[] window = new int[WINDOWLENGTH];
                int index1= 0;
                
                for(int index = 0; index < WINDOWLENGTH; index++)
                {
                    window[index1] = board.getMap()[row+4-index][col+index];
                    
                    heuristic += windowHeuristic(window, piece);
                }
                
            }
        }
        
        //Center heuristic
        int centerColumn = (int) Math.floor(COLS/2);
        int[] centerArray = new int[ROWS];
        
        for(int row = 0; row < ROWS; row++)
        {
            centerArray[row] = board.getMap()[row][centerColumn];
        }
        
        int centerCount = pieceCount(centerArray, piece);
        heuristic += centerCount*3;
        
        return heuristic;
    }
    
    //calculates heuristic of window
    public int windowHeuristic(int[] window, int piece)
    {
        int oppPiece = 0;
        
        if(piece == 1)
            oppPiece = 2;
        else
            oppPiece = 1;
        
        int heuristic = 0;
        if(pieceCount(window, piece) == 5)
            heuristic = 100;
        else if(pieceCount(window, piece) == 4 && pieceCount(window, 0) == 1)
            heuristic = 50;
        else if(pieceCount(window, piece) == 3 && pieceCount(window, 0) == 2)
            heuristic = 15;
        else if(pieceCount(window, piece) == 2 && pieceCount(window, 0) == 3)
            heuristic = 5;
        
        if(pieceCount(window, oppPiece) == 4 && pieceCount(window, 0) == 1)
            heuristic -= 54;
        
        return heuristic;
        
    }
    
    //gets best move at depth 0
    public int getBestMove(Grid board, int turn)
    {
        int piece;
        if (turn%2 ==0)
            piece = 1;
        else
            piece = 2;
        
        ArrayList<Integer> validLocations = board.moveList();
        
        int bestHeuristic = 0;
        int bestMove = getRandomValidMove(validLocations);
        
        
        for(Integer col : validLocations)
        {
            
            int row = board.getValidRow(col);
            
            Grid child = new Grid(board);
           
            child.makeMove(col, turn);
            int heuristic = heuristic(child, turn);
            
            
            if (heuristic > bestHeuristic)
            {
                bestHeuristic = heuristic;
                bestMove = col;
            }
            
            System.out.println("Done");
        }
        
        return bestMove;
    }
    
    //counts the number of times piece occurs in the list window
    public int pieceCount(int[] window, int piece)
    {
        int pieceCount = 0;
        for(int i : window)
            if (i == piece)
                pieceCount++;
        return pieceCount;
    }
    
    //Selects a random valid move from the list of available moves
    public int getRandomValidMove(ArrayList<Integer> moves)
    {
        int move = (int) (Math.random()*9);
        
        while (!moves.contains(move))
            move = (int) (Math.random()*9);
        return move;
    }
    
    //utilized to keep track of time
    public void setCurrentTime()
    {
        currentTime = System.currentTimeMillis();
    }

}
