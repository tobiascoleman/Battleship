package battleship;

/*
 * Future change ideas: 
 *      Implementing local and multiplayer: Menu shows Local and Online; Local click shows popup to right
 *      of gamemodes (add in combo gamemode as well. Not that hard). Online click shows popup to host or join
 *      once clicked then it takes you to connecting screen where once client joins you can choose gamemode.
 * 
 *      For online. End screen has return to gamemode select instead of main menu and then a button that disconnects
 *      and take user to main menu.
 * 
 *      Add in right click to rotate for pieces underneath setupboard. Also add a keybind to rotate bc why not.
 *    
 * 
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.IOException;

public class battleship{
    public static Object piece;

    //Main function, creates frame
    public static void main( String[] args ) {
        BFrame mainFrame = new BFrame();
        mainFrame.setSize(1024, 1024);
        mainFrame.setVisible(true);
    }
}

class BFrame extends JFrame{
    private player[] playerArray = {new player(), new player()};//2 player objects
    private int turn = 0;//whose turn is it
    private boolean setUp = false;
    private boolean gameStarted = false;
    private boolean initSetup = false;
    private JPanel outerPanel;//panel holds everything
    private JPanel menuPanel;//panel just for menu screen
    private JPanel container;//just inside outerpanel
    private JTabbedPane tp;//tabbed pane for attack and board panels
    private DragnDropPanel DragnDropPanel;
    private boolean salvo = false;
    private int attacksSent = 0; //keeps track of attacks sent for salvo

    //counts the pieces a player has placed
    //We use it to keep track of what piece a player is on
    private int piecesPlaced = 0;

    private JPanel southPanel = new JPanel();
    private Icon imgIcon;
    private JLabel bottomLabel = new JLabel();
    private JLabel topLabel = new JLabel();
    private JLabel bgLabel;
    //ImageIcon borderIcon = new ImageIcon( getClass().getResource( "pattern.jpg" ) );
    private Component bp1;
    


    public BFrame(){
        super("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loopSound("Hitman.wav");
        toMenuScreen();

    }

    

    public void toMenuScreen(){
        validate();
        menuPanel = new JPanel(new GridLayout(4, 1, 20, 20));//menu panel is 3 x 1 grid layout
        outerPanel = new JPanel(new GridBagLayout());//outer panel is grid bag
        GridBagConstraints gbc = new GridBagConstraints(); // for GridBagLayout positioning
        container = new JPanel();

        //background
        bgLabel = new JLabel();
        bgLabel.setLayout(new BorderLayout());
        imgIcon = new ImageIcon( getClass().getResource( "background.jpg") );
        bgLabel.setIcon(imgIcon);
        setContentPane(bgLabel);
        menuPanel.setOpaque(false);
        outerPanel.setOpaque(false);
        container.setOpaque(false);

        //title
        Icon titleImg = new ImageIcon( getClass().getResource( "BattleshipLogo.png" ) );
        JLabel title = new JLabel();
        title.setIcon(titleImg);

        // Position the title at the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // anchor to the top
        outerPanel.add(title, gbc);

        //declaring buttons
        JButton startGameButton = new JButton("Play(Normal mode)");
        JButton salvoButton = new JButton("Play(Salvo Mode)");
        JButton rulesButton = new JButton("Rules");
        JButton exitButton = new JButton("Exit");

        //resizing button fonts
        startGameButton.setFont(new Font("Serif", Font.PLAIN, 40));
        salvoButton.setFont(new Font("Serif", Font.PLAIN, 40));
        rulesButton.setFont(new Font("Serif", Font.PLAIN, 40));
        exitButton.setFont(new Font("Serif", Font.PLAIN, 40));

        //resizing buttons
        Dimension buttonSize = new Dimension(400, 50);
        startGameButton.setPreferredSize(buttonSize);
        startGameButton.setMaximumSize(buttonSize);
        salvoButton.setPreferredSize(buttonSize);
        salvoButton.setMaximumSize(buttonSize);
        rulesButton.setPreferredSize(buttonSize);
        rulesButton.setMaximumSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);

        //customizing buttons
        startGameButton.setForeground(Color.BLACK);
        startGameButton.setBackground(Color.GRAY);
        startGameButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        salvoButton.setForeground(Color.BLACK);
        salvoButton.setBackground(Color.GRAY);
        salvoButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        rulesButton.setForeground(Color.BLACK);
        rulesButton.setBackground(Color.GRAY);
        rulesButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.GRAY);
        exitButton.setBorder(new BevelBorder(BevelBorder.RAISED));


        //adding buttons
        menuPanel.add(startGameButton);
        menuPanel.add(salvoButton);
        menuPanel.add(rulesButton);
        menuPanel.add(exitButton);

        // Position the menuPanel below the title
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1; // give extra vertical space
        gbc.fill = GridBagConstraints.VERTICAL; // fill vertically
        outerPanel.add(menuPanel, gbc);

        // Add the outer panel to the frame
        add(outerPanel);
         
        //listens for when start game is pressed, calls startGame()
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerPanel.remove(title);
                playSound("button.wav");
                setupGame();
                

            }
        });

        salvoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvo = true;
                playSound("button.wav");
                outerPanel.remove(title);
                setupGame();
                

            }
        });

        //listens for when see rules is pressed
        rulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                seeRules();
                playSound("button.wav");
            }
        });

        //closes panel when exit is pressed
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                playSound("button.wav");
            }
        });
        repaint();
        revalidate();
    }
    
    //pulls up html file of rules
    public void seeRules(){
        URL url = battleship.class.getResource("Rules.html");
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        try {
            editorPane.setPage(url);
        }catch(IOException e){

        }
        JFrame rulesPane = new JFrame();
        JScrollPane scrollPane = new JScrollPane(editorPane);
        rulesPane.add(scrollPane);
        rulesPane.setVisible(true);
        rulesPane.setSize(500, 500);
    }
    
    //START GAME FUNCTION
    public void setupGame(){

        outerPanel.remove(menuPanel);//get rid of menu panel
        outerPanel.setLayout(new BorderLayout(10,10));//changes outer panel to a border layout
        outerPanel.setBackground(Color.WHITE);
        container.setBackground(Color.WHITE);
        container.setOpaque(false);
        outerPanel.setOpaque(false);

        //new background
        imgIcon = new ImageIcon( getClass().getResource( "Background2.jpg") );
        bgLabel.setIcon(imgIcon);
        //everything in north border
        JPanel northB = new JPanel();
        northB.setOpaque(false);

        JButton resignButton = new JButton("Resign");
        resignButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Dimension buttonSize = new Dimension(100, 45);
        resignButton.setPreferredSize(buttonSize);
        endTurnButton.setPreferredSize(buttonSize);
        resignButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        endTurnButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel playerTurn = new JLabel();
        playerTurn.setOpaque(false);
        playerTurn.setForeground(Color.WHITE);
        playerTurn.setSize(200, 10);
        playerTurn.setFont(new Font("Serif", Font.BOLD, 30));
        playerTurn.setText("Player " + (turn + 1) + "'s turn");
        resignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                turn = (turn + 1)%2;
                gameOver();
            }
        });
        endTurnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playSound("button.wav");
                if(playerArray[turn].isTurnFinished()){
                    turn++;
                    turn = turn%2;
                    attacksSent = 0;
                    playerTurn.setFont(new Font("Serif", Font.BOLD, 30));
                    playerTurn.setText("Player " + (turn + 1) + "'s turn");
                    if(!setUp) {
                        piecesPlaced = 0;
                        switchImg();
                    }
                    if(setUp && !gameStarted) {
                        startGame();
                    }
                    if(gameStarted) {
                        changeDisplay("");
                    }
                    playerArray[turn].setTurnFinished(false);
                    repaint();
                }
                return;
            }
        });
        endTurnButton.setBackground(Color.GRAY);
        endTurnButton.setForeground(Color.WHITE);
        endTurnButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        resignButton.setBackground(Color.GRAY);
        resignButton.setForeground(Color.WHITE);
        resignButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        northB.add(resignButton);
        northB.add(playerTurn, CENTER_ALIGNMENT);
        northB.add(endTurnButton);
        outerPanel.add(northB, BorderLayout.NORTH);

        //resize and add panels
        Dimension boardSize = new Dimension((int) outerPanel.getHeight()/2 + 50, (int) outerPanel.getHeight()/2);
        container.setSize(boardSize);
        DragnDropPanel = new DragnDropPanel();
        container.add(DragnDropPanel);
        DragnDropPanel.setPreferredSize(boardSize);
        DragnDropPanel.repaint();
        outerPanel.add(container, BorderLayout.CENTER);

        setUpSouthPanel();

    }

    public void setUpSouthPanel(){
        imgIcon = new ImageIcon( getClass().getResource( "Cruiser.png" ) );
        bottomLabel.setIcon(imgIcon);
        topLabel.setText("Place your Cruiser!");

        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        if(!initSetup){
        southPanel.add(Box.createVerticalStrut(20));
        southPanel.add(topLabel);
        southPanel.add(Box.createVerticalStrut(15));
        southPanel.add(bottomLabel);
        southPanel.add(Box.createVerticalStrut(20));
        initSetup = true;
        }

        bottomLabel.setPreferredSize(new Dimension(200, 100));
        topLabel.setFont(new Font("Serif", Font.BOLD, 25));
        //topLabel.setBackground(Color.WHITE);
        topLabel.setForeground(Color.WHITE);
        topLabel.setOpaque(false);
        bottomLabel.setFont(new Font("Serif", Font.BOLD, 25));
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Border blackline = BorderFactory.createMatteBorder(10, 20, 10, 20, borderIcon);
        //southPanel.setBorder(blackline);
        southPanel.setOpaque(false);

        outerPanel.add(southPanel, BorderLayout.SOUTH);
        outerPanel.repaint();
        outerPanel.revalidate();
    }
    public void startGame(){
        gameStarted = true;
        outerPanel.remove(container);
        container = new JPanel();
        container.setOpaque(false);
        Dimension boardSize = new Dimension((int) outerPanel.getWidth()/2, (int) outerPanel.getHeight()/2);
        container.setPreferredSize(boardSize);
        outerPanel.add(container, BorderLayout.CENTER);
        tp = new JTabbedPane();
        tp.setPreferredSize(boardSize);
        tp.setOpaque(false);
        //Border blackline = BorderFactory.createMatteBorder(10, 20, 20, 20, borderIcon);
        //tp.setBorder(blackline);
        attackPanel bp1 = new attackPanel();
        boardPanel bp2 = new boardPanel();
        tp.add("ATTACK SCREEN", bp1);
        tp.add("MY BOARD", bp2);
        container.add(tp);
        revalidate();

    }

    public void switchImg(){
        switch(piecesPlaced){
            case 0:
                topLabel.setText("Place your Cruiser!");
                imgIcon = new ImageIcon( getClass().getResource( "Cruiser.png" ) );
                bottomLabel.setIcon(imgIcon);
                break;
            case 1:
                topLabel.setText("Place your Submarine!");
                imgIcon = new ImageIcon( getClass().getResource( "Sub.png" ) );
                bottomLabel.setIcon(imgIcon);
                break;
            case 2:
                topLabel.setText("Place your Destroyer!");
                imgIcon = new ImageIcon( getClass().getResource( "Destroyer.png" ) );
                bottomLabel.setIcon(imgIcon);
                break;
            case 3:
                topLabel.setText("Place your Battleship!");
                imgIcon = new ImageIcon( getClass().getResource( "Battleship.png" ) );
                bottomLabel.setIcon(imgIcon);
                break;
            case 4:
                topLabel.setText("Place your Aircraft Carrier!");
                imgIcon = new ImageIcon( getClass().getResource( "AircraftCarrier.png" ) );
                bottomLabel.setIcon(imgIcon);
                break;
            default:
                topLabel.setText("Your fleet is ready! Press \"end turn\"");
                imgIcon = new ImageIcon( getClass().getResource( "fleet.png" ) );
                bottomLabel.setIcon(imgIcon);
        }
        southPanel.revalidate();
        outerPanel.revalidate();
        revalidate();
    }

    public void changeDisplay(String x){
        if(x.equals("Hit") || x.equals("")){
            topLabel.setText("Choose a square to send your attack!");
        }else{
            topLabel.setText(x);
        }
        bottomLabel.setText("");
        revalidate();
    }

    public void displayHit(){
        imgIcon = new ImageIcon( getClass().getResource( "hit.png" ) );
        bottomLabel.setIcon(imgIcon);
        revalidate();
    }

    public void displayMiss(){
        imgIcon = new ImageIcon( getClass().getResource( "Miss.png" ) );
        bottomLabel.setIcon(imgIcon);
        revalidate();
    }
    void loopSound(String soundFile){
        URL f = getClass().getResource("src/" + soundFile);
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }catch(java.net.MalformedURLException e){
            e.printStackTrace();
        }catch(javax.sound.sampled.UnsupportedAudioFileException e){
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();
        }catch(javax.sound.sampled.LineUnavailableException e){
            e.printStackTrace();
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
    }
    void playSound(String soundFile){
        URL f = getClass().getResource("src/" + soundFile);        
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }catch(java.net.MalformedURLException e){
            e.printStackTrace();
        }catch(javax.sound.sampled.UnsupportedAudioFileException e){
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();
        }catch(javax.sound.sampled.LineUnavailableException e){
            e.printStackTrace();
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    public void resetGame(){
        turn = 0;
        playerArray[0] = new player();
        playerArray[1] = new player();
        container = new JPanel();
        piecesPlaced = 0;
        attacksSent = 0;
        setUp = false;
        gameStarted = false;
    }

    public void gameOver(){
        container.removeAll();
        outerPanel.removeAll();
        //Winner Background

        bgLabel.setLayout(new BorderLayout());
        imgIcon = new ImageIcon( getClass().getResource( "Background2.jpg") );
        bgLabel.setIcon(imgIcon);
        setContentPane(bgLabel);
        menuPanel.setOpaque(false);
        outerPanel.setOpaque(false);
        container.setOpaque(false);
        repaint();
        revalidate();
        JPanel winnerPanel = new JPanel(new GridLayout(2, 1, 10, 1)); // 2 rows, 1 column
        winnerPanel.setPreferredSize(container.getSize());
        winnerPanel.setOpaque(false);
        
         // Initialize the winner label
        JLabel winnerJLabel = new JLabel("Player " + (turn + 1) + " wins!", SwingConstants.CENTER);
        winnerJLabel.setFont(new Font("Serif", Font.BOLD, 90)); // Set a large font size
        winnerJLabel.setPreferredSize(new Dimension(500, 300));
        winnerJLabel.setForeground(Color.WHITE);

        JButton playAgain = new JButton("Play Again");
        JButton menuButton = new JButton("Return to Menu");

        // Set a preferred size for the buttons
        Dimension buttonSize = new Dimension(300, 100); // You can adjust the size as needed
        playAgain.setPreferredSize(buttonSize);
        menuButton.setPreferredSize(buttonSize);
        playAgain.setFont(new Font("Serif", Font.BOLD, 30));
        menuButton.setFont(new Font("Serif", Font.BOLD, 30));

        winnerPanel.setPreferredSize(container.getSize());
        playAgain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerPanel.remove(winnerPanel);
                resetGame();
                setupGame();
            }
        });
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerPanel.remove(winnerPanel);
                remove(outerPanel);
                resetGame();
                toMenuScreen();
            }
        });
        
        // Create a panel to hold buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(playAgain);
        buttonPanel.add(menuButton);
        
        // Add components to the winner panel
        winnerPanel.add(winnerJLabel);
        winnerPanel.add(buttonPanel);

        // Add the winner panel to the outer panel
        outerPanel.add(winnerPanel);
        outerPanel.revalidate();
        outerPanel.repaint();
    }
    //Main panel where the setup takes place
    public class DragnDropPanel extends JPanel{

        //Constant grid size for creating squares
        private int GRID_SIZE = 10;
        //keeps track of the 0-99 number of each grid panel
        private int i;
        //graphics variable for drawing ships
        Graphics g;
        //tracks rotation of ships based off of right click
        boolean rotate = false;

        //Constructor
        DragnDropPanel(){
            //Border blackline = BorderFactory.createMatteBorder(10, 20, 20, 20, borderIcon);
            //setBorder(blackline);
            setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
            setOpaque(false);
            //creates 10x10 grid of panels
            for (i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
                //creates each individual square panel, i = 0-99 location number
                squarePanel panel = new squarePanel(i);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(panel);

                //MOUSE EVENTS
                panel.addMouseListener(new MouseAdapter() {

                    //MOUSE ENTER/HOVER
                    //When mouse enters panel, draw green outline if no ship is there and in bounds, else red
                    public void mouseEntered(MouseEvent e){
                        if(piecesPlaced == 5){
                            //exit function if all pieces have been placed
                            playerArray[turn].setTurnFinished(true);
                            if(turn == 1){
                                setUp = true;
                            }
                            return;
                        }
                        playSound("click_x.wav");

                        g = getGraphics();

                        //sets potential locations of ship in pieces locations array for future use
                        playerArray[turn].gamePieces[piecesPlaced].setFront(panel.getLocalLocation());

                        //checks if potential locations overlap with other pieces
                        if(!playerArray[turn].isPiecePlaced(playerArray[turn].gamePieces[piecesPlaced])){
                            g.setColor(Color.GREEN);
                            //no overlap & in bounds case
                            if(playerArray[turn].gamePieces[piecesPlaced].inBounds())
                                playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                            else{
                                //no overlap & out of bounds case
                                g.setColor(Color.RED);
                                playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                            }
                            //piece overlap case
                        } else {
                            g.setColor(Color.RED);
                            playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                        }

                    }

                    //resets panels when player moves mouse out of them
                    public void mouseExited(MouseEvent e){
                        if(piecesPlaced == 5){
                            playerArray[turn].setTurnFinished(true);
                            if(turn == 1){
                                setUp = true;}
                            return;
                        }
                        g = getGraphics();
                        g.setColor(Color.BLACK);
                        playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                    }
                    /*
                     * If player right clicks mouse it rotates the current ship
                     *
                     * If player left clicks it checks if it is a valid move and if it is draws
                     * the ship on the board
                     *
                     * If it is not a valid move, draws red outline
                     */
                    public void mouseClicked(MouseEvent e){
                        if(piecesPlaced == 5){
                            playerArray[turn].setTurnFinished(true);
                            if(turn == 1){
                                setUp = true;
                            }
                            return;
                        }
                        g = getGraphics();
                        //rotate on right click
                        if (SwingUtilities.isRightMouseButton(e)){
                            g.setColor(Color.BLACK);
                            playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                            rotate = !rotate;
                            g.setColor(Color.GREEN);
                            playerArray[turn].gamePieces[piecesPlaced].setHorizantal(rotate);
                            mouseEntered(e);

                        }else {//left click case
                            playerArray[turn].gamePieces[piecesPlaced].setFront(panel.getLocalLocation());
                            //in bounds case
                            if (playerArray[turn].gamePieces[piecesPlaced].inBounds() && !playerArray[turn].isPiecePlaced(playerArray[turn].gamePieces[piecesPlaced])) {

                                //code that actually stores locations of the drawn piece
                                playerArray[turn].gamePieces[piecesPlaced].setFront(panel.getLocalLocation());
                                playerArray[turn].setMasterArray(playerArray[turn].gamePieces[piecesPlaced]);

                                g.setColor(Color.BLACK);
                                playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                                playerArray[turn].drawShip(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced++);
                                boolean check = true;
                                for(int i=0; i<17; i++){
                                    if(playerArray[turn].getMasterArray(i)==-1){
                                        check = false;
                                    }
                                }
                                if(check)
                                    playerArray[turn].setBoard();
                                switchImg();
                                playSound("button.wav");
                            }else{
                                //out of bounds case
                                g.setColor(Color.RED);
                                playerArray[turn].drawOutline(g, panel.getX(), panel.getY(), panel.getHeight(), panel.getWidth(), piecesPlaced);
                            }
                        }
                    }
                });
            }
        }
        //class squarePanel = each individual panel of the grid
        class squarePanel extends JPanel{
            public int localLocation;
            squarePanel(int i){
                super();
                localLocation = i;
                setBackground(new Color(90, 205, 244));
            }
            public int getLocalLocation(){
                return localLocation;
            }

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                for(int i = 0; i < piecesPlaced; i++){
                    for(int j = 0; j < playerArray[turn].gamePieces[i].getLength(); j++){
                        if(localLocation == playerArray[turn].gamePieces[i].getLocation(j)){
                            if(playerArray[turn].gamePieces[i].getHorizontal()){
                                g.fillRect(0, 5, (getWidth())
                                        ,(int)(getHeight() * 0.8));
                            } else{
                                g.fillRect(5, 0, (int)(getWidth() * 0.8)
                                        ,(getHeight()));
                            }
                        }
                    }
                }

            }
        }

    }
    public class boardPanel extends JPanel{
        //Constant grid size for creating squares
        private int GRID_SIZE = 10;
        //keeps track of the 0-99 number of each grid panel
        private int i;
        //graphics variable for drawing ships
        Graphics g;

        boardPanel(){
            setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
            setOpaque(false);
            //creates 10x10 grid of panels
            for (i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
                //creates each individual square panel, i = 0-99 location number
                squarePanel panel = new squarePanel(i);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(panel);
            }
        }
        //class squarePanel = each individual panel of the grid
        class squarePanel extends JPanel{
            private int localLocation;
            squarePanel(int i){
                super();
                localLocation = i;
                setBackground(new Color(90, 205, 244));
            }
            public int getLocalLocation(){
                return localLocation;
            }

            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < playerArray[turn].gamePieces[i].getLength(); j++){
                        if(localLocation == playerArray[turn].gamePieces[i].getLocation(j)){
                            if(playerArray[turn].playerBoard.getState(localLocation) == 4)
                                g.setColor(Color.RED);
                            if(playerArray[turn].gamePieces[i].getHorizontal()){
                                g.fillRect(0, 5, (getWidth())
                                        ,(int)(getHeight() * 0.8));
                            } else{
                                g.fillRect(5, 0, (int)(getWidth() * 0.8)
                                        ,(getHeight()));
                            }
                        }
                    }
                }
                if(playerArray[turn].playerBoard.getState(localLocation) == 2){
                    String mark = "X";
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.RED);
                    g.drawString(mark, 15, 30);
                } else if(playerArray[turn].playerBoard.getState(localLocation) == 3){
                    String mark = "O";
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.BLUE);
                    g.drawString(mark, 15, 30);
                }
            }
        }
    }
    public class attackPanel extends JPanel{
        //Constant grid size for creating squares
        private int GRID_SIZE = 10;
        //keeps track of the 0-99 number of each grid panel
        private int i;
        //graphics variable for drawing ships
        Graphics g;


        attackPanel(){
            setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
            setOpaque(false);
            //creates 10x10 grid of panels
            for (i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
                //creates each individual square panel, i = 0-99 location number
                squarePanel panel = new squarePanel(i);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(panel);
                panel.addMouseListener(new MouseAdapter() {

                    //MOUSE ENTER/HOVER
                    //When mouse enters panel, draw green outline
                    public void mouseEntered(MouseEvent e){
                        g = getGraphics();
                        g.setColor(Color.GREEN);
                        g.drawRect(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
                    }

                    //resets panels when player moves mouse out of them
                    public void mouseExited(MouseEvent e){
                        g = getGraphics();
                        g.setColor(Color.BLACK);
                        g.drawRect((int)panel.getX(), (int) panel.getY(), panel.getWidth(), panel.getHeight());                    }
                    public void mouseClicked(MouseEvent e){
                        int state;
                        g = getGraphics();
                        if(!playerArray[turn].isTurnFinished() && !salvo){
                            state = playerArray[(turn+1)%2].checkAttack(panel.localLocation);
                            switch(state){
                                case 0: {
                                    displayMiss();
                                    playSound("Splash.wav");
                                    String mark = "O";
                                    g.setFont(new Font("Arial", Font.BOLD, 30));
                                    g.setColor(Color.BLUE);
                                    g.drawString(mark, panel.getX()+15, panel.getY()+30);
                                    playerArray[turn].setTurnFinished(true);
                                    break;
                                }
                                case 1: {
                                    displayHit();
                                    if(!(playerArray[(turn+1)%2].checkSunk()).equals("Hit")){
                                        playSound("Sink.wav");
                                    }
                                    changeDisplay(playerArray[(turn+1)%2].checkSunk());
                                    playSound("Hitmarker.wav");
                                    String mark = "X";
                                    g.setFont(new Font("Arial", Font.BOLD, 30));
                                    g.setColor(Color.RED);
                                    g.drawString(mark, panel.getX()+15, panel.getY()+30);
                                    repaint();
                                    playerArray[turn].setTurnFinished(true);
                                    break;
                                }
                                case 2: { //already guessed
                                    return;
                                }
                                case 3: { //already guessed
                                    return;
                                }
                                case 4: { //already guessed
                                    return;
                                }
                                case 5: {
                                    gameOver();
                                }
                            }
                        }else if(!playerArray[turn].isTurnFinished() && salvo){
                            state = playerArray[(turn+1)%2].checkAttack(panel.localLocation);
                            switch(state){
                                case 0: {
                                    String mark = "?";
                                    g.setFont(new Font("Arial", Font.BOLD, 30));
                                    g.setColor(Color.BLACK);
                                    g.drawString(mark, panel.getX()+15, panel.getY()+30);
                                    attacksSent++;
                                    if(attacksSent == 5){
                                        playSound("Salvo.wav");
                                        changeDisplay(playerArray[(turn+1)%2].checkSunk());
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[turn].setTurnFinished(true);
                                        repaint();
                                    }
                                    break;
                                }
                                case 1: {
                                    String mark = "?";
                                    g.setFont(new Font("Arial", Font.BOLD, 30));
                                    g.setColor(Color.BLACK);
                                    g.drawString(mark, panel.getX()+15, panel.getY()+30);
                                    attacksSent++;
                                    if(attacksSent == 5){
                                        playSound("Salvo.wav");
                                        changeDisplay(playerArray[(turn+1)%2].checkSunk());
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[(turn+1)%2].checkSunk();
                                        playerArray[turn].setTurnFinished(true);
                                        repaint();
                                    }
                                    break;
                                }
                                case 2: { //already guessed
                                    return;
                                }
                                case 3: { //already guessed
                                    return;
                                }
                                case 4: { //sunk ship (already guessed)
                                    return;
                                }
                                case 5: {
                                    playSound("Salvo.wav");
                                    gameOver();
                                    }
                                }
                            }
                        }
            
                    });
                }
        }
        public void drawOutline(Graphics g, int x, int y, int w, int h){
            g.drawRect((int) x, (int) y, w, h);
        }
        //class squarePanel = each individual panel of the grid
        class squarePanel extends JPanel{
            private int localLocation;
            squarePanel(int i){
                super();
                localLocation = i;
                setBackground(new Color(90, 205, 244));
            }
            public int getLocalLocation(){
                return localLocation;
            }
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < playerArray[turn].gamePieces[i].getLength(); j++){
                        if(localLocation == playerArray[(turn+1)%2].gamePieces[i].getLocation(j) && 
                            playerArray[(turn+1)%2].playerBoard.getState(localLocation) == 4){
                                g.setColor(Color.RED);
                            if(playerArray[(turn+1)%2].gamePieces[i].getHorizontal()){
                                g.fillRect(0, 5, (getWidth())
                                        ,(int)(getHeight() * 0.8));
                            } else{
                                g.fillRect(5, 0, (int)(getWidth() * 0.8)
                                        ,(getHeight()));
                            }
                        }
                    }
                }
                if(playerArray[(turn+1)%2].playerBoard.getState(localLocation) == 2){
                    String mark = "X";
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.RED);
                    g.drawString(mark, 15, 30);
                } else if(playerArray[(turn+1)%2].playerBoard.getState(localLocation) == 3){
                    String mark = "O";
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.BLUE);
                    g.drawString(mark, 15, 30);
                }
            }
        }
    }
}