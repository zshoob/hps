package edu.nyu.heuristic.hw3;

public class AlphaBetaPruning{
  private Heuristic heuristic;

  public AlphaBetaPruning(Heuristic heuristic){
    this.heuristic = heuristic;
  }

  public int alphaBeta(Board board, int depth, int alpha, int beta){
    if(depth == 0){
      return heuristic.getStateValue(board);
    }
  }
}