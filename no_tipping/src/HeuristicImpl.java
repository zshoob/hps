//package edu.nyu.heuristic.hw3;

/**
* Implementation class for Heuristic 
*
*/
public class HeuristicImpl implements Heuristic{
  /**
  * Get the store of a given board state
  * @param: board the state of the board
  * @return: the calculated store used for alpha-beta pruning
  */
  @Override
  public int getStateValue(Board board){
    return 0;
  }

  /**
  * Get the possible moves given a board state
  * @param: board state
  * @return: a collection of the move
  */
  @Override
  public Iterable<Move> getOrderedMoves(Board board){
    return null;
  }
}