// package edu.nyu.heuristic.hw3;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class AlphaBetaPruning{
  private Heuristic heuristic;

  static class MoveScore implements Comparable<MoveScore> {
    Move move;
    double score;

    @Override
    public int compareTo(MoveScore o) {
      return (int) (o.score - score); 
    }
  }

  public AlphaBetaPruning(Heuristic heuristic){
    this.heuristic = heuristic;
  }

  public Move findBestMove(Board state, int depth, Mode mode){

    boolean isRed = state.getTurn() == Color.RED ? true : false;

    List<MoveScore> scores = new ArrayList<MoveScore>();
    Board newState = state.copy();
    Iterable<Move> possibleMoves = heuristic.getOrderedMoves(newState, mode);
    for (Move move : possibleMoves) {
        MoveScore score = new MoveScore();
        score.move = move;
        score.score = Double.MIN_VALUE;
        scores.add(score);
    }
    System.out.println("POSSIBLE MOVE LEN: " + scores.size());
    if(scores.size() == 0){
      throw new RuntimeException("No move, will modify latter for architecture interface");
    }

    try {
      for (int i = 0; i < depth; i++) {
        for (MoveScore moveScore : scores) {
          Move move = moveScore.move;
          newState = state.copy();
          newState.makeMove(move);
          double score =
              alphaBeta(newState, i, Double.MIN_VALUE, Double.MAX_VALUE, mode);
          if (!isRed) {
            score = -score;
          }
          moveScore.score = score;
        }
        Collections.sort(scores); 
      }
    } catch (Exception e) {
      e.printStackTrace();
      // OK, it should happen
    }

    Collections.sort(scores);
    //test
    for(MoveScore moveScore : scores){
      System.out.println(moveScore.move.getPosition() + ":" + moveScore.score);
    }
    
    return scores.get(0).move;
  }

  public double alphaBeta(Board board, int depth, double alpha, double beta, Mode mode){
    if(depth == 0){
      return heuristic.getStateValue(board);
    }

    Color turn = board.getTurn();
    int count = 0;
    Iterable<Move> possibleMoves = heuristic.getOrderedMoves(board, mode);
    for(Move move:possibleMoves){
      count++;
      Board tmp = board.copy();
      tmp.makeMove(move);
      double childScore = alphaBeta(tmp, depth - 1, alpha, beta, mode);
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