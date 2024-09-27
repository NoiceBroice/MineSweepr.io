//package ScuffedMineSweeper;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.KeyAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
public class Main{
    public static JFrame frame;
    public static MineBoard board;
    public static JLabel flagsLeft;
    public static int diff =0;
    private static Timer timer;
    private static JTextField customDim;
    private static Integer secondsPassed;
    private static JButton back;
    private static JPanel topBar;
    private static JLayeredPane game;
    private static JPanel difficulty;
    private static JButton[] menuButtons;
    private static JButton play;
    private static JButton easy;
    private static JButton medium;
    private static JButton hard;
    private static JButton custom;
    private static JPanel panel2;
    public static void main(String[] args) {
        frame = new JFrame("Brice's Scuffed MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        run();
    }
    public static void run(){
        frame.setLayout(new GridLayout(5, 1));
        frame.setBackground(Color.DARK_GRAY);
        //some space
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.DARK_GRAY);
        frame.add(panel1);

        JPanel titl = new JPanel();
        titl.setBackground(Color.DARK_GRAY);
        JLabel title = new JLabel();
        title.setText("Brice's Scuffed MineSweeper");

        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(22.0f));
        //center title text
        title.setHorizontalAlignment(JLabel.CENTER);
        titl.add(title);//haha funny
        frame.add(titl);

        play = new JButton("Play");
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(diff == -1){
                    //set text to "Play" with "select a difficulty" on the next line in a centered html
                    play.setText("<html><div style='text-align: center;'>Play<br /><br />Select a difficulty</html>");
                    play.setBackground(new Color(120,100,100));
                    return;
                }
                if(diff == 3){
                    //parse int from customDim catch exception set text to "enter number between 9 and 100"
                    try{
                        if(!(customDim.getText().isEmpty()) || (Integer.parseInt(customDim.getText()) >= 9 && Integer.parseInt(customDim.getText()) <= 100)){
                            throw new NumberFormatException();
                        }
                    }catch(NumberFormatException ex){
                        customDim.setText("Enter number between 9 and 100");
                        customDim.setBackground(new Color(255,100,100));
                        return;
                    }
                }
                frame.remove(play);
                frame.remove(titl);
                frame.remove(panel1);
                frame.remove(difficulty);
                frame.remove(easy);
                frame.remove(medium);
                frame.remove(hard);
                frame.remove(panel2);
                frame.repaint();
                init();
            }
        });
        frame.add(play);
        
        difficulty = new JPanel(new GridLayout(1, 3));
        difficulty.setBackground(Color.DARK_GRAY);

        easy = new JButton("<html><div style='text-align: center;'>Easy<br /><br />9x9<br />10 mines</html>");
        medium = new JButton("<html><div style='text-align: center;'>Medium<br /><br />15x15<br />40 mines</html>");
        hard = new JButton("<html><div style='text-align: center;'>Hard<br /><br />24x24<br />96 mines</html>");
        custom = new JButton("<html><div style='text-align: center;'>Custom<br /><br />Enter Your Own<br />Dimension</html>");

        menuButtons = new JButton[]{easy, medium, hard, custom, play};
        for(JButton b : menuButtons){
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
            //fix clicked shenanigans
            b.setContentAreaFilled(false);
            //set opaque to true so that background color is visible
            b.setOpaque(true);

            //when hovered over, make background slightly lighter 
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(b.getBackground().getRed()+20,b.getBackground().getGreen()+20,b.getBackground().getBlue()+20));
                }
                // when pressed make background slightly darker
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(b.getBackground().getRed()-20,b.getBackground().getGreen()-20,b.getBackground().getBlue()-20));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(b.getBackground().getRed()-20,b.getBackground().getGreen()-20,b.getBackground().getBlue()-20));
                }
            });
            if (b == play){
                b.setFont(b.getFont().deriveFont(22.0f));
                continue;
            }
            //for each button, add action listener to change diff to corresponding value
            b.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    for(int i = 0; i < menuButtons.length; i++){
                        if(b == menuButtons[i]){
                            setDiff(i);
                        }
                    }
                }
            });
            difficulty.add(b);
        }
        setDiff(-1);

        frame.add(difficulty);

        panel2 = new JPanel();
        panel2.setBackground(Color.DARK_GRAY);
        panel2.setLayout(new GridLayout(1, 4));

        //add 3 space fillers
        JPanel filler1 = new JPanel();
        filler1.setBackground(Color.DARK_GRAY);
        panel2.add(filler1);

        JPanel filler2 = new JPanel();
        filler2.setBackground(Color.DARK_GRAY);
        panel2.add(filler2);

        JPanel filler3 = new JPanel();
        filler3.setBackground(Color.DARK_GRAY);
        panel2.add(filler3);
        //add text box for a custom dimension
        customDim = new JTextField();
        customDim.setBackground(Color.WHITE);
        customDim.setForeground(Color.DARK_GRAY);
        customDim.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));
        //set south border to 2/3 of the height of the text box
        customDim.setBorder(BorderFactory.createCompoundBorder(customDim.getBorder(), BorderFactory.createEmptyBorder(0, 0, (int)(customDim.getHeight()*0.66), 0))) ;
        
        customDim.setText("Enter custom dimension");
        customDim.setHorizontalAlignment(JTextField.CENTER);
        customDim.setEditable(true);

        //when custom is clicked set focus to customDim
        custom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                customDim.requestFocus();
            }
        });

        //when activated (not clicked) clear text
        customDim.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                customDim.setText("");
                customDim.setBackground(Color.WHITE);
                setDiff(3);
            }
        });
        //when focus is lost, if text is empty, set text to default
        customDim.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if(customDim.getText().isEmpty()){
                    customDim.setText("Enter custom dimension");
                    customDim.setBackground(Color.WHITE);
                }
            }
        });
        
        customDim.addKeyListener(new KeyAdapter() { // if it's not a number or backspace, ignore the event
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != java.awt.event.KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  
                }
             }
        });
        panel2.add(customDim);

        frame.add(panel2);


        panel1.setVisible(true);
        panel2.setVisible(true);
        difficulty.setVisible(true);
        titl.setVisible(true);
        title.setVisible(true);
        play.setVisible(true);
        frame.setVisible(true);
    }

    private static void init(){
        game = new JLayeredPane();
        int width=9;
        int height=9;
        int mines=10;
        switch(diff){
            case 0:
                width = 9;
                height = 9;
                mines = 10;
                break;
            case 1:
                width = 15;
                height = 15;
                mines = 40;
                break;
            case 2:
                width = 24;
                height = 24;
                mines = 96;
                break;
            case 3:
                width = Integer.parseInt(customDim.getText());
                height = Integer.parseInt(customDim.getText());
                mines = (int)Math.floor(Math.pow(Integer.parseInt(customDim.getText()), 2)/5.625);
                break;
        }
        frame.setLayout(new BorderLayout());
        game.setLayout(new BorderLayout());
        board = new MineBoard(height,width,mines);

        topBar = new JPanel();
        topBar.setLayout(new GridLayout(1,3));
        topBar.setBackground(Color.DARK_GRAY);
        topBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        topBar.setPreferredSize(new DimensionUIResource(frame.getWidth(), 50));

        back = new JButton("Back");
        back.setBackground(Color.LIGHT_GRAY);
        
        back.setFont(back.getFont().deriveFont(22.0f));
        back.addActionListener( new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    timer.cancel();
                    timer.purge();

                    game.remove(board);
                    frame.remove(topBar);
                    frame.remove(game);
                    frame.remove(back);
                    frame.remove(flagsLeft);
                    frame.remove(panel2);
                    frame.repaint();

                    game.repaint();
                    run();
                }
            }
        );
        topBar.add(back);

        JLabel time = new JLabel();
        time.setText("Time: 0:00");
        time.setForeground(Color.WHITE);
        time.setFont(time.getFont().deriveFont(22.0f));
        //center title text
        time.setHorizontalAlignment(JLabel.CENTER);

        secondsPassed = 0;
        timer = new Timer();
        TimerTask task = new TimerTask(){
          public void run(){
            addsecond();
            time.setText("Time: " + (secondsPassed/60) + ":" + ((((secondsPassed%60)<10)) ? "0"+(secondsPassed%60): (secondsPassed%60)));
          }  
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

        topBar.add(time);

        flagsLeft = new JLabel();
        flagsLeft.setText("🚩" + board.getFlagsLeft());
        flagsLeft.setForeground(Color.WHITE);
        flagsLeft.setFont(flagsLeft.getFont().deriveFont(22.0f));
        //center title text
        flagsLeft.setHorizontalAlignment(JLabel.CENTER);
        topBar.add(flagsLeft);

        back.setVisible(true);
        time.setVisible(true);
        flagsLeft.setVisible(true);
        topBar.setVisible(true);
               
        board.setVisible(true);
        game.setVisible(true);

        game.add(board);

        frame.add(topBar,java.awt.BorderLayout.NORTH);
        frame.add(game,java.awt.BorderLayout.CENTER);
        frame.setVisible(true);
    }

     //create layered pane with game over message, restart button, back to menu button, and disable all tiles
    public static void gameOver(boolean win){
        //stop timer
        timer.cancel();
        timer.purge();
        board.disableAll();
        topBar.setBackground(win ? new Color(12,48,12) : new Color(48,12,12));
        timer = new Timer();
        TimerTask task = new TimerTask(){ //new end game 
            public void run(){
                if(board.tileCheck(win)){
                    timer.cancel();
                    timer.purge();
                    gameOverlay(win);
                }
            }  
          };//check a new tile every 100ms
          timer.scheduleAtFixedRate(task, (win) ? 0 : 100, (24/(diff+1)));
    }
    public static void gameOverlay(boolean win) {

        game.setLayout(null);
        back.setEnabled(false);
        //add black tint over game board 
       /* JLabel noGame = new JLabel();
        noGame.setBackground(new Color(0,0,0,150));
        noGame.setBounds(0,0,game.getWidth(),game.getHeight());
        noGame.setVisible(true);
        game.add(noGame);
        game.moveToFront(noGame);*/

        JPanel gameEnd = new JPanel();
        gameEnd.setLayout(null);
        //set bounds of game over message
        gameEnd.setBounds(frame.getWidth()*1/4, frame.getHeight()*1/4, frame.getWidth()/2, frame.getHeight()/2);
        gameEnd.setBackground(win ? new Color(12, 48, 12) : new Color(48, 12, 12));
        gameEnd.setBorder(BorderFactory.createLineBorder(win ? Color.GREEN : (new Color(191, 56, 44)), 2));
        gameEnd.setLayout(new GridLayout(2,1));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setLayout(new GridLayout(1,2));
        JLabel gameOver = new JLabel(win ? "<html><div style='text-align: center;'>You Win!<br />"+(secondsPassed/60) + ":" + ((((secondsPassed%60)<10)) ? "0"+(secondsPassed%60): (secondsPassed%60))+"</html>" : "You Lose!"); 
        //serialize time won?
        gameOver.setForeground(Color.WHITE);
        gameOver.setBackground(Color.DARK_GRAY);
        gameOver.setFont(gameOver.getFont().deriveFont(15.0f));
        gameOver.setHorizontalAlignment(JLabel.CENTER);
        


        JButton restart = new JButton("Restart");
        restart.setBackground(Color.LIGHT_GRAY);
        restart.setFont(restart.getFont().deriveFont(15.0f));
        restart.addActionListener( new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    timer.cancel();
                    timer.purge();

                    frame.remove(game);
                    frame.remove(topBar);
                    frame.repaint();
                    init();
                }
            }
        );
        
        JButton backEnd = new JButton("Back");
        backEnd.setBackground(Color.LIGHT_GRAY);
        backEnd.setFont(backEnd.getFont().deriveFont(15.0f));
        backEnd.addActionListener( new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    timer.cancel();
                    timer.purge();

                    frame.remove(backEnd);
                    frame.remove(game);
                    frame.remove(topBar);
                    //frame.remove(noGame);
                    frame.remove(gameEnd);
                    //clear frame

                    frame.repaint();
                    run();
                }
            }
        );
        
        gameOver.setVisible(true);
        restart.setVisible(true);
        backEnd.setVisible(true);

        buttonPanel.add(restart);
        buttonPanel.add(backEnd);
        buttonPanel.setVisible(true);
        
        gameEnd.add(gameOver);
        gameEnd.add(buttonPanel);
        gameEnd.setVisible(true);

        //game.add(noGame);
       // game.moveToFront(noGame);
        game.add(gameEnd);
        game.moveToFront(gameEnd);
        game.repaint();
    }
    
    private static void addsecond(){
        secondsPassed++;
    }

    private static void setDiff(int num){
        diff = num;

        //everything in diffs gray with for each loop
        for (JButton b:menuButtons){
            if(b.equals(play)){
                if (diff == -1){
                    b.setBackground(Color.GRAY);
                    continue;
                }
                b.setText("Play");
            }
            b.setBackground(Color.LIGHT_GRAY);
        }
        switch (diff){
            case 0:
                easy.setBackground(new Color(30,200,30));
                break;
            case 1:
                medium.setBackground(new Color(30,200,30));
                break;
            case 2:
                hard.setBackground(new Color(30,200,30));
                break;
            case 3:
                custom.setBackground(new Color(30,200,30));
                break;
        }
    }
    //when frame is resized it will resize the board
}