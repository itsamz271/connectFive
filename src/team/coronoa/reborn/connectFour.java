
package team.coronoa.reborn;

import java.util.Scanner;

public class connectFour {


    public static void main(String[] args) {

        
        Scanner input = new Scanner(System.in);
        
        System.out.println("Hello, Would you like to play Single Player (vs AI) or Multiplayer?");
        System.out.println("Input 0 for Multiplayer");
        System.out.println("Input 1 for AI");
        int setting = input.nextInt();
        
        
        //A game instance for playing against another human player
        if (setting == 0) {
            Game game = new Game();
            System.out.println("Hello Game has Started");
            game.display();
            while (true) {

                if (game.getTurn() % 2 == 0) {
                    System.out.println("Player 1 make your MOVE");
                    int move = input.nextInt();
                    game.makeMove(move);

                    if (game.isGameOver(game.getBoard(),game.getTurn()%2)) {

                        game.display();
                        System.out.println("PLAYER 1 WINS");
                        break;
                    }
                    game.changeTurn();
                } else {
                    System.out.println("Player 2 make your MOVE");
                    int move = input.nextInt();
                    game.makeMove(move);

                    if (game.isGameOver(game.getBoard(),game.getTurn()%2)) {

                        game.display();
                        System.out.println("PLAYER 2 WINS");
                        break;
                    }
                    game.changeTurn();
                }
                game.display();

            }
        }
        //A game instance for playing against AI
        else 
        {
            //Setting up depth and time limit
            Game game = new Game();
            System.out.println("Hello Game has Started");
            game.display();
            
            System.out.println("Do you want to play with respect to depth of time? [0: depth. 1: time]");
            int answer = input.nextInt();
            int userDepth = 1000000000;
            int userTime = 0;
            
            switch(answer)
            {
                case 0:
                    System.out.println("Enter depth: ");
                    userDepth = input.nextInt();
                    break;
                case 1:
                    System.out.println("Enter time limit (in ms): ");
                    userTime = input.nextInt();
                    game = new Game(userTime);
                    break;    
            }
            
            System.out.println("Would you like to play first or second?");
                System.out.println("Input 0 if first or 1 if second");
                int turn = input.nextInt();
                if(turn== 1){
                    game.changeTurn();
                }
            while (true) {
                
                

                if (game.getTurn() % 2 == 0) {
                    System.out.println("Player 1 make your MOVE");
                    int move = input.nextInt();
                    game.makeMove(move);

                    if (game.isGameOver(game.getBoard(),game.getTurn()%2)) {

                        game.display();
                        System.out.println("PLAYER 1 WINS");
                        break;
                    }
                    game.changeTurn();
                } else {
                    System.out.println("AI MOVE");
                    
                    if(userTime > 0)
                    {
                        game.setCurrentTime();
                    }
                    
                    int move = game.MiniMaxAlphaBeta(game.getBoard(), userDepth, -1000000000, 1000000000, true)[0];
                    
                    game.makeMove(move);

                    if (game.isGameOver(game.getBoard(),game.getTurn()%2)) {

                        game.display();
                        System.out.println("PLAYER 2 WINS");
                        break;
                    }
                    game.changeTurn();
                }
                game.display();

            }
            
            
            

        }

    }

}
