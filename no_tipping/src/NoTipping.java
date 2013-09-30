

import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NoTipping{
  private Board board;
  private Block[] slots;
  private Set<Block> redSet;
  private Set<Block> blueSet;
  private Color turn;
  private Mode mode;

  public NoTipping(){
    this.board = new Board();
    this.slots = new Block[31];
    this.redSet = board.getRedSet();
    this.blueSet = board.getBlueSet();
  }

  public void readBoard(String path){
    try{
      BufferedReader bf = new BufferedReader(new FileReader(path));
      String line = null;
      int num = 0;
      while((line = bf.readLine()) != null){
        String[] tmpArray = line.split("\\s+");
        int weight = Integer.parseInt(tmpArray[1]);
        int col = Integer.parseInt(tmpArray[2]);
        Color color = null;
        Block tmpBlock = null;
        if(col == 1){
          color = Color.RED;
          tmpBlock = new Block(weight, color);
          redSet.remove(tmpBlock);
        }else if(col == 2){
          color = Color.BLUE;
          tmpBlock = new Block(weight, color);
          blueSet.remove(tmpBlock);
        }else{
          color = Color.NONE;
          tmpBlock = new Block(weight, color);
        }
        slots[num] = tmpBlock;

        num++;
      }
      this.board = new Board(slots, redSet, blueSet, turn);

    }catch(IOException e){
      System.out.println("File Read Exception!");
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    NoTipping notipping = new NoTipping();
    if("1".equals(args[0])){
      notipping.mode = Mode.ADD;
    }else if("2".equals(args[0])){
      notipping.mode = Mode.REMOVE;
    }else{
      throw new RuntimeException("Mode number error!");
    }

    if("1".equals(args[1])){
      notipping.turn = Color.RED;
    }else if("2".equals(args[1])){
      notipping.turn = Color.BLUE;
    }else{
      throw new RuntimeException("Player number error!");
    }

    // notipping.readBoard("/Users/zhaohui/Documents/programs/hps/no_tipping/src/board.txt");
    // notipping.
    Heuristic heuristic = new HeuristicImpl();
    AlphaBetaPruning alphabeta = new AlphaBetaPruning(heuristic);
    Move move = alphabeta.findBestMove(notipping.board, 10);
    
    System.out.println(move.getPosition());
    System.out.println(move.getBlock().getWeight());

  }


}