package battleship;

import java.awt.Graphics;

public class player {
    public board playerBoard;
    public piece[] gamePieces = new piece[5];//array of 5 ships
    private boolean turnFinished = false;
    private int[] masterArray = new int[17];//master array = list of all locations of all pieces
    private int masterIndex = 0;//tracker
    public player(){
        //creating pieces
        gamePieces[0] = new piece(2);
        gamePieces[1] = new piece(3);
        gamePieces[2] = new piece(3);
        gamePieces[3] = new piece(4);
        gamePieces[4] = new piece(5);
        //creating board
        playerBoard = new board();
        //initialize master array to -1
        for(int i=0; i<17; i++){
            masterArray[i] = -1;
        }
    }
    public void setTurnFinished(boolean fin){
        turnFinished = fin;
    }
    public boolean isTurnFinished(){
        return turnFinished;
    }
    
    public boolean isPiecePlaced(piece x){
        boolean check = false;
        for(int i=0; i<x.getLength(); i++){
            for(int j=0; j<masterIndex; j++){
                if(x.getLocation(i) == masterArray[j])
                    check = true;
            }
        }

        return check;
    }
    public void setBoard(){
        for(int i =0; i<masterArray.length; i++){
            playerBoard.setState(masterArray[i], 1);
        }
    }
    public void setMasterArray(piece x){
        int j = 0;
        for(int i = masterIndex; i < masterIndex + x.getLength(); i++){
            masterArray[i] = x.getLocation(j++);
        }
        masterIndex += x.getLength();
    }
    public int getMasterArray(int i){
        return masterArray[i];
    }
    public void drawShip(Graphics g, float x, float y, int h, int w, int piece){
        gamePieces[piece].setDimensions((int)x, (int)y, h, w);
        int length = gamePieces[piece].getLength();
        int horizH = (int)(h * 0.8);
        int vertW = (int)(w * 0.8);
        boolean horiz = gamePieces[piece].getHorizontal();
        if (horiz) {
            g.fillRect((int) x, (int) y+5, (w * length), horizH);
        }else
            g.fillRect((int) x+5, (int) y, vertW, (h  * length));
        
    }
    public void drawShip(Graphics g, float x, float y, int h, int w, piece p){
        int length = p.getLength();
        int horizH = (int)(h * 0.8);
        int vertW = (int)(w * 0.8);
        boolean horiz = p.getHorizontal();
        if (horiz) {
            g.fillRect((int) x, (int) y+5, (w * length), horizH);
        }else
            g.fillRect((int) x+5, (int) y, vertW, (h  * length));        
    }
    public void drawOutline(Graphics g, float x, float y, int h, int w, int piece){
        int length = gamePieces[piece].getLength();
        boolean horiz = gamePieces[piece].getHorizontal();
        if (horiz) {
            g.drawRect((int) x, (int) y, (w * length), h);
        }else
            g.drawRect((int) x, (int) y, w, (h  * length));

    }
    //0 = miss, 1 = hit, 2 & 3 = already guessed, 4 = game over (sending player won)
    //Send into other player's board
    public int checkAttack(int loc){
        int state = playerBoard.getState(loc);
        sendAttack(loc, state);
        if(playerBoard.gameOver())
            return 5;
        return state;
    }
    public void sendAttack(int loc, int state){
        int change;
        if(state == 0){
            change = 3;
        }else if(state == 1){
            change = 2;
        }else{
            change = state;
        }
        playerBoard.setState(loc, change);
    }
    public String checkSunk(){
        if(playerBoard.getState(masterArray[0]) == 2 && playerBoard.getState(masterArray[1]) == 2){
            playerBoard.setState(masterArray[0], 4);
            playerBoard.setState(masterArray[1], 4);
            return "You have sunk their Cruiser";
        } else if(playerBoard.getState(masterArray[2]) == 2 && playerBoard.getState(masterArray[3]) == 2
                  && playerBoard.getState(masterArray[4]) == 2){
            playerBoard.setState(masterArray[2], 4);
            playerBoard.setState(masterArray[3], 4);
            playerBoard.setState(masterArray[4], 4);
            return "You have sunk their submarine";
        }else if(playerBoard.getState(masterArray[5]) == 2 && playerBoard.getState(masterArray[6]) == 2
                  && playerBoard.getState(masterArray[7]) == 2){
            playerBoard.setState(masterArray[5], 4);
            playerBoard.setState(masterArray[6], 4);
            playerBoard.setState(masterArray[7], 4);
            return "You have sunk their Destroyer";
        }else if(playerBoard.getState(masterArray[8]) == 2 && playerBoard.getState(masterArray[9]) == 2
                  && playerBoard.getState(masterArray[10]) == 2 && playerBoard.getState(masterArray[11]) == 2){
            playerBoard.setState(masterArray[8], 4);
            playerBoard.setState(masterArray[9], 4);
            playerBoard.setState(masterArray[10], 4);
            playerBoard.setState(masterArray[11], 4);
            return "You have sunk their Battleship";
        }else if(playerBoard.getState(masterArray[12]) == 2 && playerBoard.getState(masterArray[13]) == 2
                  && playerBoard.getState(masterArray[14]) == 2 && playerBoard.getState(masterArray[15]) == 2
                  && playerBoard.getState(masterArray[16]) == 2){
            playerBoard.setState(masterArray[12], 4);
            playerBoard.setState(masterArray[13], 4);
            playerBoard.setState(masterArray[14], 4);
            playerBoard.setState(masterArray[15], 4);
            playerBoard.setState(masterArray[16], 4);
            return "You have sunk their Aircraft Carrier";
        }else return "Hit";
        
    }
}