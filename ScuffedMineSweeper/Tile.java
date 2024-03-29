//package ScuffedMineSweeper;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Insets;

public class Tile extends JButton{
    private int gridx;
    private int gridy;
    private boolean isMine = false;
    private boolean isFlagged = false;
    private boolean isRevealed = false;
    private int adjacentMines = 0;

    public Tile(int x, int y){
        super();
        this.gridx = x;
        this.gridy = y;
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setBorder(null);
        
        if((x+y)%2==0){
            this.setBackground(new Color(105,105,105));
        }
        else{
            this.setBackground(new Color(90,90,90));
        }
        //when clicked reveal tile
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(!isFlagged){
                    if(!isRevealed){
                        reveal();
                    }else{
                        chord();
                    }
                }
            }
        });
        //when right clicked flag tile
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if(e.getButton() == 3){
                    if(!isRevealed){
                        if(!isFlagged){
                            if(Main.board.getFlagsLeft() > 0){
                                setText("🚩");
                            }
                        }else{
                            //since error is thrown when you try to set text to null and change it later
                            setText(" ");
                        }
                    }
                }
            }
        });
    }

    public void setMine(){
        this.isMine = true;
    }
    public void setFlagged(boolean isFlagged){
        this.isFlagged = isFlagged;
    }
    public void setAdjacentMines(int adjacentMines){
        this.adjacentMines = adjacentMines;
    }

    public boolean isMine(){
        return this.isMine;
    }
    public boolean isFlagged(){
        return this.isFlagged;
    }
    public boolean isRevealed(){
        return this.isRevealed;
    }
    public int getAdjacentMines(){
        return this.adjacentMines;
    }

    public void reveal(){
        if(this.isRevealed){return;}
        if(this.isFlagged){return;}
        this.isRevealed = true;
//        System.out.println("Revealed: " + this.gridx + ", " + this.gridy);
        if(this.isMine){
            this.setText("💥");
            //game over
            Main.gameOver(false);
            System.out.println("Game Lost");

        }else{
            if(this.adjacentMines == 0){
                this.setText("");
                //reveal adjacent tiles using Main.board.tiles[i][j] to get adjacent tiles
                for(int i = -1; i <= 1; i++){
                    for(int j = -1; j <= 1; j++){
                        if(!(i == 0 && j == 0) && (this.gridx+i >= 0 && this.gridx+i < Main.board.getDimWidth()) && (this.gridy+j >= 0 && this.gridy+j < Main.board.getDimHeight())){
                        //    System.out.println("..so Revealing: " + (this.gridx+i) + ", " + (this.gridy+j));
                            Main.board.getTiles()[this.gridx+i][this.gridy+j].reveal();
                        } else {
                            //System.out.println("Would Have Revealed: " + (this.gridx+i) + ", " + (this.gridy+j)+
                            //"\n but 0<"+(this.gridx+i)+"<"+(Main.board.getDimWidth()-1)+" or 0<"+(this.gridy+j)+"<"+(Main.board.getDimHeight()-1));
                    
                        }   
                    }
                }
            }else{
                this.setText(Integer.toString(this.adjacentMines));
            }
            //check if game is won
            if(Main.board.isWon()){
                Main.gameOver(true);
                System.out.println("Game Won");
            }
        }

    }
    public void chord(){
        //count flags in adjacent tiles
        int flags = 0;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(!(i == 0 && j == 0) && (this.gridx+i >= 0 && this.gridx+i < Main.board.getDimWidth()) && (this.gridy+j >= 0 && this.gridy+j < Main.board.getDimHeight())){
                    if(Main.board.getTiles()[this.gridx+i][this.gridy+j].isFlagged()){
                        flags++;
                    }
                }
            }
        }
        //if flags == adjacentMines, reveal all adjacent tiles
        if(flags == this.adjacentMines){
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(!(i == 0 && j == 0) && (this.gridx+i >= 0 && this.gridx+i < Main.board.getDimWidth()) && (this.gridy+j >= 0 && this.gridy+j < Main.board.getDimHeight())){
                        Main.board.getTiles()[this.gridx+i][this.gridy+j].reveal();
                    }
                }
            }
        }
    }

    //overide setText to change color
    public void setText(String text){
        super.setText(text);
        //make text half as big as the tile size
        this.setPreferredSize(new java.awt.Dimension(/*Main.board.getTileSize()*/1/2, /*Main.board.getTileSize()*/1/2));

        switch(text){
            case "":
                this.setBackground(Color.LIGHT_GRAY);
                break;
            case "1":
                this.setForeground(new Color(53, 125, 232));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "2":
                this.setForeground(new Color(34, 150, 31));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "3":
                this.setForeground(new Color(225,50, 50));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "4":
                this.setForeground(new Color(100, 50, 230));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "5":
                this.setForeground(new Color(166, 13, 13));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "6":
                this.setForeground(new Color(13, 143, 166));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "7":
                this.setForeground(new Color(175, 175, 23));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "8":
                this.setForeground(new Color(3, 3, 3));
                this.setBackground(new Color(170, 170, 170));
                break;
            case "💥":
                this.setBackground(Color.RED);
                break;
            case "X":
                this.setForeground(new Color(34, 150, 31));
                this.setBackground(Color.DARK_GRAY);
                break;
            case "🚩":
                this.isFlagged = true;
                Main.board.setFlagsLeft(Main.board.getFlagsLeft()-1);
                break;
            case " ":
                this.isFlagged = false;
                Main.board.setFlagsLeft(Main.board.getFlagsLeft()+1);
                break;
            default:
                this.setBackground(new Color(170, 170, 170));
                break;
        }
    }

    // make tile lighter when hovered over
    
}
