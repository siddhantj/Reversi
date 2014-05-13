import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class GameState
{
    boolean flagMax;
    boolean flagMin;
    int alpha;
    int beta;
    ArrayList<GameState> children;
    ArrayList<Move> legalMoves;
    char board[][]=null;
    int totalDiskNumber;
    int currentDiskNumber;
    int XDiskNumber,ODiskNumber;
    Player currentPlayer;
    boolean flagPrintState;
    int score;

    // variable to indicate parent

    public GameState()  // for initialization
    {
        flagMax=true;
        flagMin=false;
        alpha=Integer.MIN_VALUE;
        beta=Integer.MAX_VALUE;
        children=new ArrayList<GameState>();
        legalMoves=new ArrayList<Move>();
        //gameState=new GameState();                  //newGameState();     //oldState + move u apply
        board=new char[8][8];
        totalDiskNumber=64;
        XDiskNumber=getXNumber();
        ODiskNumber=getONumber();
        currentDiskNumber=XDiskNumber + ODiskNumber;
        flagPrintState=false;
        currentPlayer=new Player(reversi.playerX);
        score=0;
    }

    public GameState(GameState game)
    {
        this.flagMax=game.getFlagMax();
        this.flagMin=game.getFlagMin();
        this.children=game.getChildren();
        //legalMoves=game.getMove();
        this.alpha=Integer.MIN_VALUE;
        this.beta=Integer.MAX_VALUE;
        this.legalMoves=new ArrayList<Move>();
        this.board=new char[8][8];
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                this.board[i][j]=game.board[i][j];
        this.totalDiskNumber=64;
        this.XDiskNumber=game.XDiskNumber;
        this.ODiskNumber=game.ODiskNumber;
        this.currentDiskNumber=this.XDiskNumber + this.ODiskNumber;
        this.flagPrintState=game.flagPrintState;
        this.currentPlayer=new Player(game.currentPlayer.getPlayer());
        this.score=0;

    }

    public int getXNumber()
    {
        int num=0,i,j;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                if(board[i][j]=='X')
                    num++;
            }
        }

        return num;
    }

    public int getONumber()
    {
        int num=0,i,j;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                if(board[i][j]=='O')
                    num++;
            }
        }
        return num;
    }

    public int getTotal()
    {
        int num=0,i,j;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                if(board[i][j]=='O' || board[i][j]=='X')
                    num++;
            }
        }
        return num;
    }

    public boolean getFlagMax()
    {
        return flagMax;
    }

    public boolean getFlagMin()
    {
        return flagMin;
    }

    public ArrayList<GameState> getChildren()
    {
        return children;
    }

    public ArrayList<Move> getMove()
    {
        return legalMoves;
    }

    public void applyValidMove(Move move)      /*applyMove(Move move,GameState newGame) */
    {
        int i,j;
        i=move.getMoveI();
        j=move.getMoveJ();
        char player=currentPlayer.getPlayer();
        if(player==reversi.playerO)
        {
            board[i][j]= reversi.playerO;
            ODiskNumber++;
        }
        else
        {
            board[i][j]=reversi.playerX;
            XDiskNumber++;
        }
        currentDiskNumber++;
        if(testSingleDirection(-1,-1,i,j)==true)
            switchColor(-1,-1,i,j);
        if(testSingleDirection(-1,0,i,j)==true)
            switchColor(-1,0,i,j);
        if(testSingleDirection(-1,1,i,j)==true)
            switchColor(-1,1,i,j);
        if(testSingleDirection(0,1,i,j)==true)
            switchColor(0,1,i,j);
        if(testSingleDirection(1,1,i,j)==true)
            switchColor(1,1,i,j);
        if(testSingleDirection(1,0,i,j)==true)
            switchColor(1,0,i,j);
        if(testSingleDirection(1,-1,i,j)==true)
            switchColor(1,-1,i,j);
        if(testSingleDirection(0,-1,i,j)==true)
            switchColor(0,-1,i,j);

    }

    public void switchColor(int p,int q,int i,int j)
    {
        while(board[i+p][j+q]==currentPlayer.getEnemyPlayer())
        {
            board[i+p][j+q]=currentPlayer.getPlayer();
            i=i+p;
            j=j+q;
            ODiskNumber=getONumber();
            XDiskNumber=getXNumber();
            currentDiskNumber=ODiskNumber + XDiskNumber;
        }
    }

    public void getLegalMoves()
    {
        int i,j;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                if(testLegalMove(i,j))
                    legalMoves.add(new Move(i,j));
            }
        }

    }

    public boolean testLegalMove(int i,int j)
    {
        //valid coordinates
        if(i<0 || i>=8 || j<0 || j>= 8)
            return false;

        //if square is free
        if(board[i][j]!=reversi.freeSquare)
            return false;

        //it produce atleast a switch
        if(testSingleDirection(-1,-1,i,j))
            return true;
        if(testSingleDirection(-1,0,i,j)==true)
            return true;
        if(testSingleDirection(-1,1,i,j)==true)
            return true;
        if(testSingleDirection(0,1,i,j)==true)
            return true;
        if(testSingleDirection(1,1,i,j)==true)
            return true;
        if(testSingleDirection(1,0,i,j)==true)
            return true;
        if(testSingleDirection(1,-1,i,j)==true)
            return true;
        if(testSingleDirection(0,-1,i,j)==true)
            return true;

        return false;
    }

    private boolean testSingleDirection(int p,int q,int i,int j)
    {
        char enemyPlayer='Z';
        char playerName=currentPlayer.getPlayer();
        boolean atleastOnce=false;
        if(playerName==reversi.playerO)
        {
            enemyPlayer=reversi.playerX;
        }
        else
        {
            enemyPlayer=reversi.playerO;
        }

        while(i+p<8 && j+q<8 && i+p>=0 && j+q>=0 && board[i+p][j+q]==enemyPlayer )
        {
            i=i+p;
            j=j+q;
            atleastOnce=true;
        }

        if(i+p<8 && j+q<8 && i+p>=0 && j+q>=0 && atleastOnce && board[i+p][j+q]==playerName )
        {
            return true;
        }

        return false;

    }
}

class Move {
    private int i;
    private int j;

    public Move(int i,int j)
    {
        this.i=i;
        this.j=j;
    }

    public int getMoveI()
    {
        return i;
    }

    public int getMoveJ()
    {
        return j;
    }

    public void setMove(int i,int j)
    {
        this.i=i;
        this.j=j;
    }
}


class Player {
    private char player;

    public Player(char p)
    {
        this.player=p;
    }

    public char getPlayer()
    {
        return player;
    }

    public char getEnemyPlayer()
    {
        char enemyPlayer;
        if(player=='X')
        {
            enemyPlayer='O';
            return enemyPlayer;
        }
        else
        {
            enemyPlayer='X';
            return enemyPlayer;
        }

    }

    public void setPlayer(char p)
    {
        this.player=p;
    }

    public void switchPlayer()
    {
        if(player=='X')
            player='O';
        else
            player='X';
    }
}




class Program
{

    public void initializeGame() throws IOException
    {
        GameState root=new GameState();
        root.flagMax=true;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                root.board[i][j]=reversi.adjMat[i][j];
            }

        }
        root.XDiskNumber=root.getXNumber();
        root.ODiskNumber=root.getONumber();
        root.currentDiskNumber=root.getTotal();
        if(reversi.option==1)
            miniMax(root);
        if(reversi.option==2 || reversi.option==3)
        {
            alphaBeta(root);
        }

    }

    public void miniMax(GameState start) throws IOException
    {       int x=1;
        char startPlayer,returnedPlayer;
        GameState gm=new GameState(start);
        GameState futureState;
        Move futureMove;

        reversi.output.write("STEP= "+x);
        reversi.output.newLine();
        reversi.output.write("BLACK");
        reversi.output.newLine();
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                reversi.output.write(start.board[i][j]);
            }
            reversi.output.newLine();
        }

        while(!endCondition(gm))
        {
            startPlayer=gm.currentPlayer.getPlayer();
            Move move=getDecision(gm);
            returnedPlayer=gm.currentPlayer.getPlayer();
            if(startPlayer!=returnedPlayer)
            {
                gm.currentPlayer.setPlayer(startPlayer);
            }
            if(move.getMoveI()!=-1 && move.getMoveJ()!=-1)
            {
                gm.applyValidMove(move);
            }

            gm.currentPlayer.switchPlayer();
            futureState=new GameState(gm);
            reversi.flag_one_time_minmax=0;
            futureMove=getDecision(futureState);

            reversi.output.newLine();
            reversi.output.write("STEP = "+ ++x);
            reversi.output.newLine();
            if(gm.currentPlayer.getPlayer()=='X')
            {
                if(futureMove.getMoveI()==-1 && futureMove.getMoveJ()==-1 && !endCondition(futureState))
                    reversi.output.write("BLACK PASS");
                else
                    reversi.output.write("BLACK");
            }
            if(gm.currentPlayer.getPlayer()=='O')
            {
                if(futureMove.getMoveI()==-1 && futureMove.getMoveJ()==-1 && !endCondition(futureState))
                    reversi.output.write("WHITE PASS");
                else
                    reversi.output.write("WHITE ");
            }

            reversi.output.newLine();
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    reversi.output.write(gm.board[i][j]);
                }
                reversi.output.newLine();
            }

            gm.legalMoves.clear();

        }
    }


    public Move getDecision(GameState game)
    {
        Move finalMove= new Move(-1,-1);
        Integer finalScore= new Integer(0);
        if(game.currentPlayer.getPlayer()==reversi.playerX)
            maxDecision(game,1,finalScore,finalMove);
        else
        {
            minDecision(game,1,finalScore,finalMove);
        }
        return finalMove;

    }


    public int maxDecision(GameState game,int depth,Integer finalScore,Move finalMove)
    {
        int x,y;
        char p;   /* */
        int q;      /*for displaying logs*/
        if(depth>=reversi.maxDepth1)
        {
            finalScore=evaluationFunction(game);
            LogsMinMax(finalMove,finalScore,depth);
            return finalScore;
        }


        else
        {
            LogsMinMax(finalMove,Integer.MIN_VALUE,depth);
            game.currentPlayer.setPlayer('X');
            game.getLegalMoves();
            if(game.legalMoves.size()==0)
            {
                finalScore=evaluationFunction(game);
                return finalScore;
            }
            else
            {
                int maxScore=Integer.MIN_VALUE;
                int bestMove=-1;
                for(int i=0;i<game.legalMoves.size();i++)
                {
                    GameState newGame=new GameState(game);
                    newGame.currentPlayer.setPlayer('X');
                    Move valid_move = game.legalMoves.get(i);
                    newGame.applyValidMove(valid_move);      //newGame.legalMoves.get(i)
                     /*x=game.legalMoves.get(i).getMoveI();
                     y=game.legalMoves.get(i).getMoveJ();
                      p= (char)(97+x);
                      q=y+1;
                      System.out.println(p+q+"  "+depth+ "  "+); */

                    //newGame.currentPlayer.switchPlayer();
                    Integer score=new Integer(0);
                    Move move=new Move(-1,-1);
                    move.setMove(valid_move.getMoveI(), valid_move.getMoveJ());
                    score=minDecision(newGame,depth + 1,score,move);   //score to finalscore
                   // LogsMinMax(finalMove,score,depth);
                    if(score>maxScore)
                    {
                        maxScore=score;
                        bestMove=i;
                        newGame.score=maxScore;
                    }
                    LogsMinMax(finalMove,maxScore,depth);
                }
                finalScore=maxScore;

                finalMove.setMove(game.legalMoves.get(bestMove).getMoveI(),game.legalMoves.get(bestMove).getMoveJ());
                return finalScore;
            }
        }


    }

    public int minDecision(GameState game,int depth,Integer finalScore,Move finalMove)
    {
        if(depth>=reversi.maxDepth1)
        {
            finalScore=evaluationFunction(game);
            LogsMinMax(finalMove,finalScore,depth);
            return finalScore;
        }
        else
        {   LogsMinMax(finalMove,Integer.MAX_VALUE,depth);
            game.currentPlayer.setPlayer('O');
            game.getLegalMoves();
            if(game.legalMoves.size()==0)
            {
                finalScore=evaluationFunction(game);
                return finalScore;
            }
            else
            {
                int minScore=Integer.MAX_VALUE;
                int bestMove=-1;
                for(int i=0;i<game.legalMoves.size();i++)
                {
                    GameState newGame=new GameState(game);
                    newGame.currentPlayer.setPlayer('O');
                    Move valid_move = game.legalMoves.get(i);
                    newGame.applyValidMove(valid_move);     //newGame.legalMoves.get(i)
                    //newGame.currentPlayer.switchPlayer();
                    Integer score=new Integer(0);
                    Move move=new Move(-1,-1);
                    move.setMove(valid_move.getMoveI(), valid_move.getMoveJ());
                    score=maxDecision(newGame,depth + 1,score,move);
                    //LogsMinMax(finalMove,score,depth);
                    if(score<minScore)
                    {
                        minScore=score;
                        bestMove=i;
                        newGame.score=minScore;
                    }
                    LogsMinMax(finalMove,minScore,depth);
                }
                finalScore=minScore;
                finalMove.setMove(game.legalMoves.get(bestMove).getMoveI(),game.legalMoves.get(bestMove).getMoveJ());
                return finalScore;
            }
        }
    }


    public void alphaBeta(GameState start) throws IOException
    {
        int x=1;
        GameState futureState;
        Move futureMove;
        char prevMat[][]=new char[8][8];
        char startPlayer,returnedPlayer;
        GameState gm=new GameState(start);
        reversi.output.write("STEP= "+x);
        reversi.output.newLine();
        reversi.output.write("BLACK");
        reversi.output.newLine();
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                reversi.output.write(start.board[i][j]);
            }
            reversi.output.newLine();
        }
        while(!endCondition(gm))
        {
            startPlayer=gm.currentPlayer.getPlayer();
            Move move=getDecisionAlphaBeta(gm);
            reversi.flag_one_time_alphabeta=0;
            returnedPlayer=gm.currentPlayer.getPlayer();
            if(startPlayer!=returnedPlayer)
            {
                gm.currentPlayer.setPlayer(startPlayer);
            }
            if(move.getMoveI()!=-1 && move.getMoveJ()!=-1)
            {
                gm.applyValidMove(move);
            }

            gm.currentPlayer.switchPlayer();
            futureState=new GameState(gm);
            futureMove=getDecisionAlphaBeta(futureState);

            reversi.output.newLine();
            reversi.output.write("STEP = "+ ++x);
            reversi.output.newLine();
            if(gm.currentPlayer.getPlayer()=='X')
            {
                if(futureMove.getMoveI()==-1 && futureMove.getMoveJ()==-1 && !endCondition(futureState))
                    reversi.output.write("BLACK PASS");
                else
                    reversi.output.write("BLACK");
            }
            if(gm.currentPlayer.getPlayer()=='O')
            {
                if(futureMove.getMoveI()==-1 && futureMove.getMoveJ()==-1 && !endCondition(futureState))
                    reversi.output.write("WHITE PASS");
                else
                    reversi.output.write("WHITE ");
            }


            reversi.output.newLine();
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    prevMat[i][j]=gm.board[i][j];
                    reversi.output.write(gm.board[i][j]);
                }
                reversi.output.newLine();
            }
            gm.legalMoves.clear();
            gm.alpha=Integer.MIN_VALUE;
            gm.beta=Integer.MAX_VALUE;
        }
    }

    public Move getDecisionAlphaBeta(GameState game)
    {
        Move finalMove= new Move(-1,-1);
        Move referenceMove=new Move(-1,-1);
        Integer finalScore= new Integer(0);
        if(game.currentPlayer.getPlayer()==reversi.playerX)
        {
            maxDecisionAlphaBeta(game,1,finalScore,finalMove,referenceMove);
        }
        else
        {
            minDecisionAlphaBeta(game,1,finalScore,finalMove,referenceMove);
        }
        return finalMove;

    }

    public int maxDecisionAlphaBeta(GameState game,int depth,Integer finalScore,Move finalMove,Move refMove)
    {
        int x,y;
        char p;   /* */
        int q;      /*for displaying logs*/

        if(depth>=reversi.maxDepth1)
        {
            finalScore=evaluationFunction(game);
            LogsAlphaBeta(refMove,finalScore,depth,game.alpha,game.beta);
            return finalScore;
        }


        else
        {   LogsAlphaBeta(refMove,Integer.MIN_VALUE,depth,game.alpha,game.beta);   //b1 at depth 3
            game.currentPlayer.setPlayer('X');
            game.getLegalMoves();
            if(game.legalMoves.size()==0)
            {
                finalScore=evaluationFunction(game);
                return finalScore;
            }
            else
            {
                int maxScore=Integer.MIN_VALUE;
                int bestMove=-1;
                for(int i=0;i<game.legalMoves.size();i++)
                {
                    if(game.alpha >= game.beta)
                    {
                        // do not expand the node
                       // LogsAlphaBeta(refMove,0,depth,game.alpha,game.beta);  //???
                    }
                    else
                    {       //expand that node
                        GameState newGame=new GameState(game);
                        newGame.alpha=game.alpha;
                        newGame.beta=game.beta;
                        newGame.currentPlayer.setPlayer('X');
                        //Move valid_move=game.legalMoves.get(i);
                        newGame.applyValidMove(game.legalMoves.get(i));      //newGame.legalMoves.get(i)
                        Move newRefMove=new Move(game.legalMoves.get(i).getMoveI(),game.legalMoves.get(i).getMoveJ());
                        Integer score=new Integer(0);
                        Move move=new Move(-1,-1);
                        //move.setMove(valid_move.getMoveI(),valid_move.getMoveJ());
                        score=minDecisionAlphaBeta(newGame,depth + 1,score,move,newRefMove);


                        if(score>maxScore)
                        {
                            maxScore=score;
                            bestMove=i;
                            newGame.score=maxScore;
                        }
                        if(score>game.alpha)
                        {
                            game.alpha=score;
                        }
                        LogsAlphaBeta(refMove,score,depth,game.alpha,game.beta);   //finalMove
                    }
                }
                finalScore=maxScore;

                finalMove.setMove(game.legalMoves.get(bestMove).getMoveI(),game.legalMoves.get(bestMove).getMoveJ());
                return finalScore;
            }
        }
    }

    public int minDecisionAlphaBeta(GameState game,int depth,Integer finalScore,Move finalMove,Move refMove)
    {

        if(depth>=reversi.maxDepth1)
        {
            finalScore=evaluationFunction(game);
            LogsAlphaBeta(refMove,finalScore,depth,game.alpha,game.beta);
            return finalScore;
        }
        else
        {   LogsAlphaBeta(refMove,Integer.MAX_VALUE,depth,game.alpha,game.beta);
            game.currentPlayer.setPlayer('O');
            game.getLegalMoves();
            if(game.legalMoves.size()==0)
            {
                finalScore=evaluationFunction(game);
                return finalScore;
            }
            else
            {
                int minScore=Integer.MAX_VALUE;
                int bestMove=-1;
                for(int i=0;i<game.legalMoves.size();i++)
                {
                    if(game.alpha >= game.beta)
                    {
                        // do not expand this node
                       // LogsAlphaBeta(refMove,0,depth,game.alpha,game.beta);   //?????
                    }
                    else
                    {       //expand that node
                        GameState newGame=new GameState(game);
                        newGame.alpha=game.alpha;
                        newGame.beta=game.beta;
                        newGame.currentPlayer.setPlayer('O');
                        //Move valid_move=game.legalMoves.get(i);

                        newGame.applyValidMove(game.legalMoves.get(i));     //newGame.legalMoves.get(i)
                        Move newRefMove=new Move(game.legalMoves.get(i).getMoveI(),game.legalMoves.get(i).getMoveJ());
                        //newGame.currentPlayer.switchPlayer();
                        Integer score=new Integer(0);
                        Move move=new Move(-1,-1);
                        // move.setMove(valid_move.getMoveI(),valid_move.getMoveJ());
                        score=maxDecisionAlphaBeta(newGame,depth + 1,score,move,newRefMove);


                        if(score<minScore)
                        {
                            minScore=score;
                            bestMove=i;
                            newGame.score=minScore;
                        }
                        if(score<game.beta)
                        {
                            game.beta=score;
                        }
                        LogsAlphaBeta(refMove,score,depth,game.alpha,game.beta);
                    }
                }
                finalScore=minScore;
                finalMove.setMove(game.legalMoves.get(bestMove).getMoveI(),game.legalMoves.get(bestMove).getMoveJ());
                return finalScore;
            }
        }
    }

    public Integer evaluationFunction(GameState game)
    {
        if(reversi.option==1 || reversi.option==2)
        {
            Integer num,Xnum,Onum;
            Xnum=game.XDiskNumber;
            Onum=game.ODiskNumber;
            num=Xnum - Onum;
            return num;
        }
        else
        {
            Integer XWeight=0,OWeight=0,weightDiff;
            int i,j;
            for(i=0;i<8;i++)
            {
                for(j=0;j<8;j++)
                {
                    if(game.board[i][j]=='X')    //black
                    {
                        XWeight=XWeight + reversi.pos[i][j];
                    }

                    if(game.board[i][j]=='O')    //white
                    {
                        OWeight=OWeight + reversi.pos[i][j];
                    }
                }
            }
            weightDiff=XWeight - OWeight;
            return weightDiff;
        }

    }

    public boolean endCondition(GameState game)
    {
        GameState dummyGame1=new GameState(game);
        dummyGame1.getLegalMoves();


        GameState dummyGame2=new GameState(game);

        dummyGame2.currentPlayer.switchPlayer();
        dummyGame2.getLegalMoves();

        if(game.currentDiskNumber>=game.totalDiskNumber || (dummyGame1.legalMoves.size()==0 && dummyGame2.legalMoves.size()==0))
            return true;
        else
            return false;

    }

    void runAlgorithms() throws IOException
    {
        initializeGame();
    }

    public void initializePosMatrix()
    {
        reversi.pos[0][0]=99;
        reversi.pos[0][1]=-8;
        reversi.pos[0][2]=8;
        reversi.pos[0][3]=6;
        reversi.pos[0][4]=6;
        reversi.pos[0][5]=8;
        reversi.pos[0][6]=-8;
        reversi.pos[0][7]=99;

        reversi.pos[1][0]=-8;
        reversi.pos[1][1]=-24;
        reversi.pos[1][2]=-4;
        reversi.pos[1][3]=-3;
        reversi.pos[1][4]=-3;
        reversi.pos[1][5]=-4;
        reversi.pos[1][6]=-24;
        reversi.pos[1][7]=-8;

        reversi.pos[2][0]=8;
        reversi.pos[2][1]=-4;
        reversi.pos[2][2]=7;
        reversi.pos[2][3]=4;
        reversi.pos[2][4]=4;
        reversi.pos[2][5]=7;
        reversi.pos[2][6]=-4;
        reversi.pos[2][7]=8;

        reversi.pos[3][0]=6;
        reversi.pos[3][1]=-3;
        reversi.pos[3][2]=4;
        reversi.pos[3][3]=0;
        reversi.pos[3][4]=0;
        reversi.pos[3][5]=4;
        reversi.pos[3][6]=-3;
        reversi.pos[3][7]=6;

        reversi.pos[4][0]=6;
        reversi.pos[4][1]=-3;
        reversi.pos[4][2]=4;
        reversi.pos[4][3]=0;
        reversi.pos[4][4]=0;
        reversi.pos[4][5]=4;
        reversi.pos[4][6]=-3;
        reversi.pos[4][7]=6;

        reversi.pos[5][0]=8;
        reversi.pos[5][1]=-4;
        reversi.pos[5][2]=7;
        reversi.pos[5][3]=4;
        reversi.pos[5][4]=4;
        reversi.pos[5][5]=7;
        reversi.pos[5][6]=-4;
        reversi.pos[5][7]=8;

        reversi.pos[6][0]=-8;
        reversi.pos[6][1]=-24;
        reversi.pos[6][2]=-4;
        reversi.pos[6][3]=-3;
        reversi.pos[6][4]=-3;
        reversi.pos[6][5]=-4;
        reversi.pos[6][6]=-24;
        reversi.pos[6][7]=-8;

        reversi.pos[7][0]=99;
        reversi.pos[7][1]=-8;
        reversi.pos[7][2]=8;
        reversi.pos[7][3]=6;
        reversi.pos[7][4]=6;
        reversi.pos[7][5]=8;
        reversi.pos[7][6]=-8;
        reversi.pos[7][7]=99;

    }

    public void LogsMinMax(Move move, int cost, int depth)
    {
        if(reversi.flag_one_time_minmax == 1)
        {
            String cost_str="";
            int movex=move.getMoveI();
            String x=String.valueOf(movex + 1);   // for rows like a1,a2...
            int movey=move.getMoveJ();
            String node="";
            if(movey==0)
            {
                node="a";
            }
            else if(movey==1)
            {
                node="b";
            }
            else if(movey==2)
            {
                node="c";
            }
            else if(movey==3)
            {
                node="d";
            }
            else if(movey==4)
            {
                node="e";
            }
            else if(movey==5)
            {
                node="f";
            }
            else if(movey==6)
            {
                node="g";
            }
            else if(movey==7)
            {
                node="h";
            }
            else
            {
                node="root";
            }
            if(!node.equalsIgnoreCase("root"))
                node=node + x;

            if(cost==Integer.MAX_VALUE)
                cost_str="Infinity";
            else if(cost==Integer.MIN_VALUE)
                cost_str="-Infinity";
            else
                cost_str=String.valueOf(cost);
            try
            {
                reversi.logs.write(node + " " + depth +" "+ cost_str);
                reversi.logs.newLine();
            }
            catch(Exception e)
            {
                e.toString();
            }
        }
    }

    public void LogsAlphaBeta(Move move, int cost, int depth,int alpha,int beta)
    {
        if(reversi.flag_one_time_alphabeta == 1)
        {
            String cost_string="";
            String alpha_string="";
            String beta_string="";
            int movex=move.getMoveI();
            String x=String.valueOf(movex + 1);   // for rows like a1,a2...
            int movey=move.getMoveJ();
            String node="";
            if(movey==0)
            {
                node="a";
            }
            else if(movey==1)
            {
                node="b";
            }
            else if(movey==2)
            {
                node="c";
            }
            else if(movey==3)
            {
                node="d";
            }
            else if(movey==4)
            {
                node="e";
            }
            else if(movey==5)
            {
                node="f";
            }
            else if(movey==6)
            {
                node="g";
            }
            else if(movey==7)
            {
                node="h";
            }
            else
            {
                node="root";
            }
            if(!node.equalsIgnoreCase("root"))
                node=node + x;

            if(cost==Integer.MAX_VALUE)
                cost_string="Infinity";
            else if(cost==Integer.MIN_VALUE)
                cost_string="-Infinity";
            else
                cost_string=String.valueOf(cost);

            if(alpha==Integer.MAX_VALUE)
                alpha_string ="Infinity";
            else if(alpha==Integer.MIN_VALUE)
                alpha_string="-Infinity";
            else
                alpha_string=String.valueOf(alpha);

            if(beta==Integer.MAX_VALUE)
                beta_string="Infinity";
            else if(beta==Integer.MIN_VALUE)
                beta_string="-Infinity";
            else
                beta_string=String.valueOf(beta);
            try
            {

                if(alpha >= beta)
                {
                    reversi.logs.write(node + " " + depth +" "+ cost_string + " " +alpha_string +" "+ beta_string + " CUT-OFF" );
                }
                else
                {
                    reversi.logs.write(node + " " + depth +" "+ cost_string + " " +alpha_string +" "+ beta_string );
                }
                reversi.logs.newLine();
            }
            catch(Exception e)
            {
                e.toString();
            }
        }
    }

}


public class reversi {

    public static BufferedWriter output,logs;
    public static final char playerX = 'X';
    public static final char playerO='O';
    public static final char freeSquare='*';
    public static int maxDepth;
    public static int maxDepth1;
    public static int flag_one_time_minmax=1;
    public static int flag_one_time_alphabeta=1;
    static int option;
    static char[][] adjMat=new char[8][8];
    static int[][] pos=new int[8][8];
    public static void main(String args[]) throws Exception
    {
        BufferedReader reader;
        String line=null;
        int i=0,j=0;
        output=new BufferedWriter(new FileWriter(args[7]));
        logs=new BufferedWriter(new FileWriter(args[9]));
        Scanner in=new Scanner(System.in);
        //System.out.println("Enter the file to read:");
        reader=new BufferedReader(new FileReader(args[5]));
        Program start=new Program();
        for(line=reader.readLine();line!=null;line=reader.readLine())
        {

            for(i=0;i<line.length();i++)
            {
                adjMat[j][i]=line.charAt(i);    //initialize matrix
            }
            j++;
        }

        start.initializePosMatrix();




        //System.out.println("Select option:");
        option=Integer.parseInt(args[1]);       //in.nextInt();
        maxDepth=Integer.parseInt(args[3]) ;
        maxDepth1=maxDepth + 1;

        start.runAlgorithms();
        output.newLine();
        output.write("Game End");
        logs.close();
        output.close();



    } }
