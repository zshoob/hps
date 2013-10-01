// package edu.nyu.heuristic.hw3;

import java.util.Objects;

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

  @Override
  public boolean equals(Object obj){
    if(this == obj){
      return true;
    }
    if(obj == null){
      return false;
    }
    if(!(obj instanceof Block)){
      return false;
    }

    Block other = (Block)obj;
    return weight == other.getWeight() && color == other.getColor();
  }

  @Override
  public int hashCode(){
    return Objects.hash(weight, color);
  }

}