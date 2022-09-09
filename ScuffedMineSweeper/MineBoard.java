//package ScuffedMineSweeper;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

public  class MineBoard extends JPanel{
    private static int flagsLeft = 0;
    public int height;
    public static int tileSize;
    public int width;
    private int tilesChecked;
    Tile[][] tiles;
    public MineBoard(int height, int width, int mines) {
        super(new GridLayout(height, width));
        
        this.height = height;
        this.width = width;
        
        this.setBackground(Color.BLACK);
        flagsLeft = mines;
        tilesChecked = 0;
        
        tiles = new Tile[height][width];
        
        //create tiles
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                tiles[i][j]=new Tile(i,j);
                this.add(tiles[i][j]);
                tiles[i][j].setVisible(true);
            }
        }

        //populate board with mines
        for(int i = 0; i < mines; i++){
            int x = (int)(Math.random()*width);
            int y = (int)(Math.random()*height);
            Tile tile = (Tile)this.getComponent(x*height+y);
            if(tile.isMine()){i--;}
            tile.setMine();
        }

        //populate board with adjacent mines count per tile
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Tile tile = (Tile)this.getComponent(i*height+j);
                int adjacentMines = 0;
                for(int k = -1; k <= 1; k++){
                    for(int l = -1; l <= 1; l++){
                        if(i+k >= 0 && i+k < width && j+l >= 0 && j+l < height){
                            Tile adjacentTile = (Tile)this.getComponent((i+k)*height+(j+l));
                            if(adjacentTile.isMine()){
                                adjacentMines++;
                            }
                        }
                    }
                }
                tile.setAdjacentMines(adjacentMines);
            }
        }
        //select random tile with no adjacent mines and reveal it
        Tile tile = (Tile)this.getComponent((int)(Math.random()*width)*height+(int)(Math.random()*height));
        while(tile.getAdjacentMines() != 0){
            tile = tiles[(int)(Math.random()*width)][(int)(Math.random()*height)];
        }
        tile.setText("X");
    }

    public static int getFlagsLeft(){
        return flagsLeft;
    }
    public static void setFlagsLeft(int f){
        flagsLeft = f;
        Main.flagsLeft.setText("🚩" + (flagsLeft));
    }
    public static void disableAll(){
        for(int i = 0; i < Main.board.width; i++){
            for(int j = 0; j < Main.board.height; j++){
                Main.board.tiles[i][j].setEnabled(false);
            }
        }
    }
    public static void enableAll(){
        for(int i = 0; i < Main.board.width; i++){
            for(int j = 0; j < Main.board.height; j++){
                Main.board.tiles[i][j].setEnabled(true);
            }
        }
    }
    public static boolean isWon(){
        //check if all tiles that are not mines are revealed
        for(int i = 0; i < Main.board.width; i++){
            for(int j = 0; j < Main.board.height; j++){
                if(!Main.board.tiles[i][j].isMine() && !Main.board.tiles[i][j].isRevealed()){
                    return false;
                }
            }
        }
        return true;

    }
    public boolean tileCheck(boolean win){
        //if this is the last tile, cancel the timer
        if(tilesChecked == width*height){
            return true;
        }
        Tile tile = tiles[tilesChecked/height][tilesChecked%height];
        //if tile indicated by minesChecked is revealed, increment minesChecked and return
        if (win){
            if(tile.isRevealed()){
                tilesChecked++;
                //slightly add green to the color of the tile
                tile.setBackground(new Color(tile.getBackground().getRed(),tile.getBackground().getGreen()+20,tile.getBackground().getBlue()));
                return false;
            }
            //set background color of tile to new color(12,48,12)
            tile.setBackground(new Color(12,48,12));
        }else{
            if(!tile.isMine() && !tile.isFlagged()){
                tilesChecked++;
                //slightly add green to the color of the tile
                tile.setBackground(new Color(tile.getBackground().getRed()+20,tile.getBackground().getGreen(),tile.getBackground().getBlue()));
                return false;
            }
            //if tile is flagged, check if it is a mine and set background color to new color(12,48,12) if it is a mine and set background color to new color(48,12,12) if it is not a mine
            if(tile.isFlagged()){
                if(tile.isMine()){
                    tile.setBackground(new Color(12,48,12));
                }else{
                    tile.setBackground(new Color(48,12,12));
                }
            }else
            //if is a mine set text to "💥"
            if(tile.isMine()){
                tile.setText("💥");
            }

        }
        tilesChecked++;
        return false;
    }
}
