// package edu.nyu.heuristic.hw3;

public class Block{
  private int weight;
  private Color color;

  public Block(int weight, Color color){
    this.weight = weight;
    this.color = color;
  }

  public int getWeight(){
    return this.weight;
  }

  public void setWeight(int weight){
    this.weight = weight;
  }

  public Color getColor(){
    return this.color;
  }

  public void setColor(Color color){
    this.color = color;
  }

}