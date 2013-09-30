//package edu.nyu.heuristic.hw3;

public interface Heuristic{
  double getStateValue(Board board);
  Iterable<Move> getOrderedMoves(Board board);
}