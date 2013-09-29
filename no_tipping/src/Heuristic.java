// package edu.nyu.heuristic.hw3;

public interface Heuristic{
  int getStateValue(Board board);
  Iterable<Move> getOrderedMoves(Board board);
}