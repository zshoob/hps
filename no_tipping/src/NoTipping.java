
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class NoTipping{
  private Board board;
  // private Block[] slots;
  private Set<Block> redSet;
  private Set<Block> blueSet;
  private Color turn;
  private Mode mode;

  public NoTipping(){
    this.board = new Board();
    this.redSet = board.getRedSet();
    this.blueSet = board.getBlueSet();
  }

  public Board readBoard(String path, Color turn){
    Board newBoard = null;
    try{
      this.redSet = new Board().getRedSet();
      this.blueSet = new Board().getBlueSet();
      Block[] tmp = new Block[31];
      BufferedReader bf = new BufferedReader(new FileReader(path));
      String line = null;
      int num = 0;
      while((line = bf.readLine()) != null){
        String[] tmpArray = line.split("\\s+");
        int weight = Integer.parseInt(tmpArray[1]);
        int col = Integer.parseInt(tmpArray[2]);
        Color color = null;
        Block tmpBlock = null;
        //System.out.print("col: " + col);
        if(col == 1){
          //System.out.println(" Red");
          color = Color.RED;
          tmpBlock = new Block(weight, color);
          redSet.remove(tmpBlock);
        }else if(col == 2){
          color = Color.BLUE;
          //System.out.println(" Blue");
          tmpBlock = new Block(weight, color);
          blueSet.remove(tmpBlock);
        }else{
          color = Color.NONE;
          //System.out.println(" None");
          tmpBlock = new Block(weight, color);
        }
        tmp[num] = tmpBlock;

        num++;
      }
      newBoard = new Board(tmp, redSet, blueSet, turn);

    }catch(IOException e){
      System.out.println("File Read Exception!");
      e.printStackTrace();
    }

    return newBoard;
  }

  public void writeToFile(String path){
    FileWriter fr = null;
    BufferedWriter bw = null;
    try{
      fr = new FileWriter(path);
      bw = new BufferedWriter(fr);
      int count = -15;
      
      for(int i = 0; i < 31; i++){
        StringBuilder sb = new StringBuilder();
        sb.append(count);
        Block tmp = board.slots[i];
        sb.append(" ");
        sb.append(tmp.getWeight());
        sb.append(" ");
        if(tmp.getColor() == Color.RED){
          sb.append(1);
        }else if(tmp.getColor() == Color.BLUE){
          sb.append(2);
        }else{
          sb.append(0);
        }
        count++;
        String str = sb.toString();
        bw.write(str, 0, str.length());
        bw.newLine();
        
      }

    }catch(IOException e){
      e.printStackTrace();
    }finally{
      try{
        bw.close();
        fr.close();
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args){
    

    NoTipping notipping = new NoTipping();
    String path = "/Users/zhaohui/Documents/programs/hps/no_tipping/src/board1.txt";

    // work for the architecture

    /*
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

    

    notipping.board = notipping.readBoard(path, notipping.turn);
    Heuristic heuristic = new HeuristicImpl();
    AlphaBetaPruning alphabeta = new AlphaBetaPruning(heuristic);
    Move move = alphabeta.findBestMove(notipping.board.copy(), 100, notipping.mode); 

    System.out.println("Move Position: " + move.getPosition());
    System.out.println("Move Weight: " + move.getBlock().getWeight());
    */


    // work for test

    
    int count = 0;
    Heuristic heuristic = new HeuristicImpl();
    AlphaBetaPruning alphabeta = new AlphaBetaPruning(heuristic);
    notipping.turn = Color.RED;
    notipping.mode = Mode.ADD;
    while(true){
      notipping.board = notipping.readBoard(path, notipping.turn);
      if(notipping.board.isPutOver()){
        System.out.println("Add ALL!");
        break;
      }
      if(notipping.board.isTipping()){
        System.out.println("Tipping!");
        break;
      }

      
      Move move = alphabeta.findBestMove(notipping.board.copy(), 100, notipping.mode);

      System.out.println("Position: " + move.getPosition());
      System.out.println("Move Weight: " + move.getBlock().getWeight());

      notipping.board.makeMove(move);
      System.out.println(Arrays.toString(notipping.board.getSupportScore()));
      // System.out.println("AFTER: tipping?" + notipping.board.isTipping());
    
      notipping.writeToFile(path);
      notipping.turn = notipping.turn == Color.RED ? Color.BLUE : Color.RED;

      count++;
    }
    System.out.println("Loop No: " + count);

  

    /*
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
    notipping.board = notipping.readBoard(path, notipping.turn);
    Move move = alphabeta.findBestMove(notipping.board.copy(), 100, Mode.REMOVE);
    System.out.println("Position: " + move.getPosition());
    System.out.println("Move Weight: " + move.getBlock().getWeight());

    */

    int anotherCounter = 0;
    while(true){
      notipping.board = notipping.readBoard(path, notipping.turn);
      if(notipping.board.isRemoveOver()){
        System.out.println("Remove ALL!");
        break;
      }
      if(notipping.board.isTipping()){
        System.out.println("Tipping!");
        break;
      }
      Move move = alphabeta.findBestMove(notipping.board.copy(), 100, Mode.REMOVE);
      System.out.println("Position: " + move.getPosition());
      System.out.println("Move Weight: " + move.getBlock().getWeight());
      notipping.board.makeMove(move);
      System.out.println(Arrays.toString(notipping.board.getSupportScore()));
      // System.out.println("AFTER: tipping?" + notipping.board.isTipping());
    
      notipping.writeToFile(path);
      notipping.turn = notipping.turn == Color.RED ? Color.BLUE : Color.RED;
      anotherCounter++;
    }
    
    System.out.println("Remove Loop: " + anotherCounter);

    // System.out.println("Color: " + notipping.turn);
    // notipping.board = notipping.readBoard(path, notipping.turn);

    // // notipping.board = new Board();
    // System.out.println("BEFORE: tipping?" + notipping.board.isTipping());
    // Heuristic heuristic = new HeuristicImpl();
    // AlphaBetaPruning alphabeta = new AlphaBetaPruning(heuristic);
    // Move move = alphabeta.findBestMove(notipping.board.copy(), 100, notipping.mode); 

    // System.out.println("Move Position: " + move.getPosition());
    // System.out.println("Move Weight: " + move.getBlock().getWeight());

    // notipping.board.makeMove(move);
    // System.out.println(Arrays.toString(notipping.board.getSupportScore()));
    // System.out.println("AFTER: tipping?" + notipping.board.isTipping());
    
    // notipping.writeToFile(path);

  }


}