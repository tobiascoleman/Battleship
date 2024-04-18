package battleship;

public class piece{
    private int length;
    private int front;//front of ship(where it was clicked to place)
    private int back;//back of ship
    private int[] locations;//array that contains each piece's 0-99 location
    private boolean horizantal;//indicated orientation of the piece(true = sideways)
    private int xPos;
    private int yPos;
    private int height;
    private int width;
    
    //Constructor
    public piece(int l){
        length = l;
        horizantal = false;
        locations = new int[length];
        //initialize locations to -1
        for(int i=0; i<length; i++){
            locations[i] = -1;
        }
    }
    

    //SETTERS
    public void setHorizantal(boolean h){
        horizantal = h;
    }

    //setFront calculates the 0-99 locations of all the squares in a piece
    //given its front
    public void setFront(int f){
        //diff = remaining blocks other than front and back
        int diff = length - 2;
        
        front = f;
        int temp = front;
        if(horizantal==false){//vertical case
            back = front + (length-1)*10;
            for(int i=1; i<=diff; i++){
                locations[i] = temp + 10;
                temp+=10;
            }
        }else{//horizontal case
            back = front + length-1;
            for(int i=1; i<=diff; i++){
                locations[i] = temp + 1;
                temp+=1;
            }
        }
        locations[0] = front;
        locations[length-1] = back;
    }
    void setDimensions(int x, int y, int h, int w){
        xPos = x;
        yPos = y;
        height = h;
        width = w;
    }
    
    //GETTERS
    public int getLocation(int index) {
        return locations[index];
    }
    public int getLength(){
        return length;
    }
    public boolean getHorizontal(){
        return horizantal;
    }
    public boolean isSunk(){
        if(length==0){
            return true;
        }else{
            return false;
        }
    }
    public int getXPos(){
        return xPos;   
    }
    public int getyPos(){
        return yPos;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    //calculates if the back goes past the bounds of the grid
    public boolean inBounds(){
        if(horizantal){
            if((front%10) > (back%10)){
                return false;
            }else{
                return true;
            }
        }else{
            if(back>99){
                return false;
            }else{
                return true;
            }
        }
    }
}