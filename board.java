package battleship;

public class board{
    private int[] boardArray;

    board(){
        boardArray = new int[100]; //0 no ship 1 ship 2 hit 3 miss 4 ship sunk
        for(int i=0; i<100; i++){
            boardArray[i] = 0;
        }
    }


    public void setState(int loc, int x){
        if(x < 0 || x > 4){
            System.out.print("Invalid board state");
        }else
            boardArray[loc] = x;
    }

    public boolean gameOver(){
        boolean check = true;
        for(int i=0; i<100; i++){
            if(boardArray[i]==1)
                check = false;
        }
        return check;
    }

    public int getState(int loc){
        return boardArray[loc];
    }
}
