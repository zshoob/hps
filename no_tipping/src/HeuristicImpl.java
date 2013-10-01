//package edu.nyu.heuristic.hw3;
import java.util.Set;
import java.util.HashSet;

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
  //@Override
  public double getStateValue(Board board){
  	if( board.isTipping( ) ) {
  		return -1*Double.MAX_VALUE + 1;
  	}	
  	double score = 0.0;

		/*
			Feature 0 & 1: Weights on Board
		*/	

			double sum = 0.0;
			for( int idx = 0; idx < 31; idx++ ) {
				int position = idx - 15;
				Block b = board.slots[position];
				if( b != null && b.getColor( ) == board.getTurn( ) )
					sum += b.getWeight( );
			}
			score += 10.0 * sum;		

		/*
			Feature 2 - 3: Mobility
		*/				
			double my_locked = 0.0;
			double enemy_locked = 0.0;
			for( int idx = 0; idx < 31; idx++ ) {
				Block b = board.slots[idx];
				if( b == null )
					continue;
				int weight = b.getWeight( );
				b.setWeight( 0 );
				if( board.isTipping( ) ) {
					if( b.getColor( ) == board.getTurn( ) )
						my_locked++;
					else
						enemy_locked++;
				}
				b.setWeight( weight );
			}
			score += -10.0 * my_locked;
			score += 0.0 * enemy_locked;				

		/*
			Feature 4: Balance
		*/

			double left = 0.0;
			double right = 0.0;
			for( int idx = 0; idx < 31; idx++ ) {
				int position = idx - 15;
				Block b = board.slots[position];
				if( b == null )
					continue;
				if( b.getColor( ) == board.getTurn( ) && position < -3 )
					left += b.getWeight( ) * (position+3);
				else if( b.getColor( ) == board.getTurn( ) && position > -1 )
					right += b.getWeight( ) * (position+1);
			}
			score += -1.0 * Math.abs(left + right);

		/*
			Feature 7: Net Torque
		*/
			score += -1.0 * (right - left);

			return score;
		}

  /**
  * Get the possible moves given a board state
  * @param: board state
  * @return: a collection of the move
  */
  //@Override
  public Iterable<Move> getOrderedMoves(Board board, Mode mode){
  	if(mode == Mode.ADD){
  		return getAddMoves(board);
  	}
  	else{
  		return getRemoveMoves(board);
  	}
  }
  public Iterable<Move> getAddMoves(Board board){
  	Set<Block> blockSet = board.getTurn( ) == Color.RED ? board.getRedSet( ) : board.getBlueSet( );
  	Set<Move> unorderedMoves = new HashSet<Move>( );
  	for( int position = 0; position < 31; position++ ) {
  		if( board.slots[position] == null || board.slots[position].getWeight() == 0) {
  			for( Block block : blockSet ) {
  				Move m = new Move(true, position, block );
  				Board copy = board.copy();
  				copy.makeMove(m);
  				if(!copy.isTipping()){
  					unorderedMoves.add( m );
  				}
  			}
  		}
  	}
  	return unorderedMoves;
  }	 
  public Iterable<Move> getRemoveMoves(Board board){
  	boolean picky = false;
  	if( board.getTurn( ) == Color.RED ) {
  		for( Block block : board.slots ) {
  			if( block != null && block.getColor( ) == Color.RED ) {
  				picky = true;
  				break;
  			}  			
  		}
  	}
  	Set<Move> unorderedMoves = new HashSet<Move>( );  	
  	for( int position = 0; position < 31; position++ ) {
  		Block block = board.slots[position];
  		if( block == null )
  			continue;
  		if( block.getColor( ) == board.getTurn( ) || !picky ) {
  			Move m = new Move(false, position, block );
  			unorderedMoves.add( m );  			
  		}
  	}
  	return unorderedMoves;
  }   
}