// package edu.nyu.heuristic.hw3;

import java.util.Set;
import java.util.HashSet;

public class Board{
  private final static int slotNum = 31;
  private final static int center = 15;
  private final static int boardWeight = 3;
  private final static int blockSize = 12;
  private final static int left = 12;
  private final static int right = 14;
  public Block[] slots;
  private Set<Block> redSet;
  private Set<Block> blueSet;
  private Color turn;

  public Board(){
    this.slots = new Block[slotNum];
    Block initial = new Block(3, Color.NONE);
    slots[11] = initial;
    this.redSet = new HashSet<Block>();
    this.blueSet = new HashSet<Block>();

    for(int i = 1; i <= blockSize; i++){
      Block redBlock = new Block(i, Color.RED);
      redSet.add(redBlock);
      Block blueBlock = new Block(i, Color.BLUE);
      blueSet.add(blueBlock);
    }
    this.turn = Color.RED;
  }

  public Board(Block[] tmp, Set<Block> redSet, Set<Block> blueSet, Color turn){
    this.slots = tmp;
    this.redSet = redSet;
    this.blueSet = blueSet;
    this.turn = turn;
  }
  

  public int[] getSupportScore(){
    int leftSupport = (left - center) * boardWeight;
    int rightSupport = (right - center) * boardWeight;
    for(int i = 0; i < slotNum; i++){
      if(slots[i] != null){
        leftSupport += slots[i].getWeight() * (left - i);
        rightSupport += slots[i].getWeight() * (right - i);
      }
    }
    int[] result = new int[2];
    result[0] = leftSupport;
    result[1] = rightSupport;
    return result;
  }

  public boolean isTipping(){
    int leftSupport = (left - center) * boardWeight;
    int rightSupport = (right - center) * boardWeight;
    for(int i = 0; i < slotNum; i++){
      if(slots[i] != null){
        leftSupport += slots[i].getWeight() * (left - i);
        rightSupport += slots[i].getWeight() * (right - i);
      }
    }

    //System.out.println("LEFT: " + leftSupport);
    //System.out.println("RIGHT: " + rightSupport);

    if(leftSupport > 0 || rightSupport < 0){
      return true;
    }else{
      return false;
    }
  }

  public void makeMove(Move move){
    
    boolean isPut = move.getIsPut();
    int position = move.getPosition();
    Block item = move.getBlock();
    Color itemColor = item.getColor();
    if(itemColor != turn){
      throw new RuntimeException("Not the turn");
    }
    
    if(isPut){
      if(slots[position] == null || slots[position].getWeight() == 0){
        slots[position] = item;
        // remove block from set
        if(itemColor == Color.RED){
          redSet.remove(item);
        }else{
          blueSet.remove(item);
        }

      }else{
        System.out.println("position: " + position);
        throw new RuntimeException("can not put to a position already taken");
      }
    }else{
      if(slots[position] == null|| slots[position].getWeight() == 0){
        throw new RuntimeException("cannot move a block not exists");
      }else{
        slots[position] = null;
        // put the block to return to set
        if(itemColor == Color.RED){
          redSet.add(item);
        }else{
          blueSet.add(item);
        }
      }
    }
    turn = turn == Color.RED ? Color.BLUE : Color.RED;
  }

  public Set<Block> getRedSet(){
    return this.redSet;
  }

  public Set<Block> getBlueSet(){
    return this.blueSet;
  }

  public Color getTurn(){
    return this.turn;
  }

  public void setTurn(Color color){
    this.turn = color;
  }

  public Board copy(){
    Board tmp = new Board(slots.clone(), new HashSet<Block>(redSet), new HashSet<Block>(blueSet), turn);
    return tmp;
  }

  public static void main(String[] args){
    Board board = new Board();
    System.out.println("Initial State: " + board.isTipping());

    Block block = new Block(1, Color.RED);
    Move move = new Move(true, 5, block);
    System.out.println("move? " + move.getIsPut() + ": " + move.getPosition());
    board.makeMove(move);
    System.out.println("After Move: " + board.isTipping());
    
  }


}