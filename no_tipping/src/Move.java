package edu.nyu.heuristic.hw3;

public class Move{
  private boolean isPut;
  private int position;
  private Block block;

  public Move(boolean isPut, int position, Block block){
    this.isPut = isPut;
    this.position = position;
    this.block = block;
  }

  public boolean getIsPut(){
    return this.isPut;
  }

  public int getPosition(){
    return this.position;
  }

  public Block getBlock(){
    return this.block;
  }

}