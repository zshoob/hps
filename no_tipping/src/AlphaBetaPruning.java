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

    Color turn = board.getTurn();
    int count = 0;
    Iterable<Move> possibleMoves = heuristic.getOrderedMoves(board);
    for(Move move:possibleMoves){
      count++;
      Board tmp = board.copy();
      tmp.makeMove(move);
      int childScore = alphaBeta(tmp, depth - 1, alpha, beta);
      if(turn == Color.RED){
        alpha = Math.max(alpha, childScore);
        if(alpha >= beta){
          break;
        }
      }else{
        beta = Math.min(beta, childScore);
        if(alpha <= beta){
          break;
        }
      }
    }

    return turn == Color.RED ? alpha : beta;

  }
}