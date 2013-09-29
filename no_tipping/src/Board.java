// package edu.nyu.heuristic.hw3;

import java.util.Set;
import java.util.HashSet;

public class Board{
  private final static int slotNum = 31;
  private final static int center = 13;
  private final static int boardWeight = 3;
  private final static int blockSize = 12;
  private Block[] slots;
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

  public Board(Block[] slots, Set<Block> redSet, Set<Block> blueSet, Color turn){
    this.slots = slots;
    this.redSet = redSet;
    this.blueSet = blueSet;
    this.turn = turn;
  }

  public double getAbs(){
    double abs = 0.0;
    abs += getBoardAbs();
    System.out.println("BOARD: " + abs);
    for(int i = 0; i < slotNum; i++){
      if(slots[i] != null){
        int weight = slots[i].getWeight();
        abs += weight * (i - center);
      } 
    }
    return abs;
  }

  private double getBoardAbs(){
    int leftNum = 0 - center;
    int rightNum = slotNum - 1 - center;
    double leftWeight = boardWeight * (-leftNum) / (slotNum - 1.0);
    double rightWeight = boardWeight * (rightNum) / (slotNum - 1.0);
    double total = 0.0;
    System.out.println("WEIGHT: " + leftWeight);
    System.out.println("DISTANCE: " + (leftNum / 2.0 ));
    total += leftNum / 2.0 * leftWeight;
    System.out.println("LEFT: " + total);
    total += rightNum / 2.0 * rightWeight;
    System.out.println("RIGHT: " + total);
    return total;
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
      if(slots[position] == null){
        slots[position] = item;
        // remove block from set
        if(itemColor == Color.RED){
          redSet.remove(item);
        }else{
          blueSet.remove(item);
        }

      }else{
        throw new RuntimeException("can not put to a position already taken");
      }
    }else{
      if(slots[position] == null){
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

  public Color getTurn(){
    return this.turn;
  }

  public Board copy(){
    Board tmp = new Board(slots, redSet, blueSet, turn);
    return tmp;
  }

  public static void main(String[] args){
    Board board = new Board();
    System.out.println(board.getAbs());
  }


}