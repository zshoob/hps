import java.lang.Math;
import java.lang.Integer;

public class ZachHunter extends Hunter {
	public ZachHunter(int x, int y, BoardStates board, MoveType type, int n, int m) {
		super(x, y, board, type);
		this.n = n;
		this.m = m;
	}
	public ZachHunter( ZachHunter original ) {
		super(original.x,original.y,original.b, original.type);
		this.n = original.n;
		this.m = original.m;
	}
	String currentDirection( ) {
		String dir = this.delta_y < 0 ? "N" : "S";
		dir += this.delta_x < 0 ? "W" : "E";
		return dir;
	}
	boolean wallSeparatesPrey(Wall w, Prey prey ) {
		if( w.status == 1 ) {
			if( w.direction == 0 ) {		
				boolean a = this.y <= w.position;
				boolean b = prey.y <= w.position;				
				return a != b;
			}
			if( w.direction == 1 ) {		
				boolean a = this.x <= w.position;
				boolean b = prey.x <= w.position;				
				return a != b;
			}			
		}
		return false;
	}
	// Returns W if wall W separates H from P, and H is about to collide with W.
	public int approachingWall( Prey prey ) {
		for( int idx = 0; idx < m; idx++ ) {
			Wall w = b.walls[idx];
			if( w.status == 1 ) {
				if( !wallSeparatesPrey( w, prey ) )
					continue;
				if( w.direction == 0 ) {				
					if( (this.y < w.position) && (this.y + (2*this.delta_y) >= w.position) )
						return idx;
					if( (this.y > w.position) && (this.y + (2*this.delta_y) <= w.position) )
						return idx;								
				} else {
					if( (this.x < w.position) && (this.x + (2*this.delta_x) >= w.position) )
						return idx;
					if( (this.x > w.position) && (this.x + (2*this.delta_x) <= w.position) )
						return idx;		
					/*							
					if( Math.abs(this.x - w.position) <= 40 ) {
						for( int i = 0; i < 100; i++ )
							System.out.println(idx);
						return idx;
					}
					*/
				}
			}
		}			
		return -1;
	}		
	/*
		Returns W if wall W separates H from P, and H is about to collide with W.
		Else returns 0 if current number of walls < m. 
		Else returns id of the furthest wall from P.
	*/
	int removeWall(Prey prey) {
		int wall = approachingWall(prey);
		if( wall >= 0 ) {
			return wall;
		}
		/*
		for( int idx = 0; idx < m; idx++ ) {
			Wall wall = b.walls[idx];
			if( approachingWall( wall, prey ) )
				return idx;
		}
		*/
		int quad[ ] = enclosingQuadrangle(prey);
		int max = -1;
		int maxDistance = 0;
		for( int idx = 0; idx < m; idx++ ) {
			Wall w = b.walls[idx];
			if( w.status == 0 )
				return -1;
			if( w.direction == 0 ) {
				if( w.position == quad[0] || w.position == quad[1] )
					continue;
				int distance = Math.abs(w.position-prey.y);
				if( distance > maxDistance ) {
					maxDistance = distance;
					max = idx;
				}
			} else {
				if( w.position == quad[2] || w.position == quad[3] )
					continue;			
				int distance = Math.abs(w.position-prey.x);
				if( distance > maxDistance ) {
					maxDistance = distance;
					max = idx;
				}			
			}
		}
		return max;				
	}
	// Returns an array of the four walls enclosing P
	public int[ ] enclosingQuadrangle( Prey prey ) {
		int top = 0;
		int left = 0;
		int right = 499;
		int bottom = 499;
		for( Wall wall : b.walls ) {
			if( wall.status == 0 )
				continue;
			if( wall.direction == 0 ) { // horizontal
				if( wall.position < prey.y && wall.position > top )
					if( wall.x1 < prey.x && wall.x2 > prey.x )
						top = wall.position;
				if( wall.position > prey.y && wall.position < bottom )
					if( wall.x1 < prey.x && wall.x2 > prey.x )				
						bottom = wall.position;
			} else { // vertical
				if( wall.position < prey.x && wall.position > left )
					if( wall.y1 < prey.y && wall.y2 > prey.y )
						left = wall.position;
				if( wall.position > prey.x && wall.position < right )
					if( wall.y1 < prey.y && wall.y2 > prey.y )				
						right = wall.position;			
			}
		}
		return new int[]{top,bottom,left,right};
	}
	String translateCoords( boolean horizontal, Prey prey ) {
		int x1 = 0;
		int x2 = 499;
		int y1 = 0;
		int y2 = 499;
		if( horizontal ) {
			y1 = this.y;
			y2 = this.y;
			for( int idx = 0; idx < m; idx++ ) {
				Wall wall = b.walls[idx];
				if( wall.status == 0 || wall.direction == 0 )
					continue;
				if( wall.position < this.x && wall.position > x1 )
					if( wall.y1 <= this.y && wall.y2 >= this.y )				
						x1 = wall.position;
				if( wall.position > this.x && wall.position < x2 )
					if( wall.y1 <= this.y && wall.y2 >= this.y )								
						x2 = wall.position;
			}
			x1++;
			x2--;
		} else {
			x1 = this.x;
			x2 = this.x;
			for( int idx = 0; idx < m; idx++ ) {
				Wall wall = b.walls[idx];
				if( wall.status == 0 || wall.direction == 1 )
					continue;
				if( wall.position < this.y && wall.position > y1 )
					if( wall.x1 <= this.x && wall.x2 >= this.x )
						y1 = wall.position;
				if( wall.position > this.y && wall.position < y2 )
					if( wall.x1 <= this.x && wall.x2 >= this.x )
						y2 = wall.position;
			}
			y1++;
			y2--;
		}	
		String x1s = Integer.toString(x1);
		String x2s = Integer.toString(x2);
		String y1s = Integer.toString(y1);
		String y2s = Integer.toString(y2);		
		return "(" + x1s + "," + y1s + "),(" + x2s + "," + y2s + ")"; 
	}
	// conservative alternative to translateCoords
	String wallCoords( boolean horizontal ) {
		int i = 0;
		int j = 499;
		if( horizontal ) {
			for( int idx = 0; idx < m; idx++ ) {
				Wall wall = b.walls[idx];
				if( wall.status == 0 || wall.direction == 0 )
					continue;
				if( wall.position < this.x && wall.position > i )
					i = wall.position;
				if( wall.position > this.x && wall.position < j )
					j = wall.position;
			}
		} else {
			for( int idx = 0; idx < m; idx++ ) {
				Wall wall = b.walls[idx];
				if( wall.status == 0 || wall.direction == 1 )
					continue;
				if( wall.position < this.y && wall.position > i )
					i = wall.position;
				if( wall.position > this.y && wall.position < j )
					j = wall.position;
			}		
		}
		String coords[ ] = new String[]{"(","0",",","0","),(","499",",","499",")"};
		if( horizontal ) {
			coords[3] = Integer.toString(this.y);
			coords[7] = Integer.toString(this.y);
		} else {			
			coords[1] = Integer.toString(this.x);
			coords[5] = Integer.toString(this.x);
		}
		StringBuilder builder = new StringBuilder();
		for(String s : coords) {
		    builder.append(s);
		}
		return builder.toString();
	}
	// true if H can build a better wall by waiting <n steps
	boolean critDistance( Prey prey ) {
		int dist = 0;
		ZachHunter clone = new ZachHunter(this);
		for( int loop = 0; loop < n; loop++ ) {
			if( Math.abs(clone.x-prey.x) <= loop || Math.abs(clone.y-prey.y) <= loop ) {
				dist = loop;
				break;
			}
			clone.bounceMove( );
		}
		return dist > 1 && dist < n;
	}
	boolean isBlocked( Prey prey, int direction ) {
		for( int idx = 0; idx < m; idx++ ) {
			Wall wall = b.walls[idx];
			if( wall.status == 0 || wall.direction == direction )
				continue;
			if( wallSeparatesPrey(wall, prey ) )
				return true;
		}
		return false;
	}
	String getMove( int stepsToBuildWalls, Prey prey ) {	
		StringBuilder move = new StringBuilder();
		move.append( currentDirection( ) );
		/*
		for( int idx = 0; idx < m; idx++ ) {
			Wall wall = b.walls[idx];
			System.out.println(idx + "\t" + wall.status + "\t" + wall.x1 + "\t" + wall.x2 + "\t" + wall.y1 + "\t" + wall.y2 );			 		
		}
		*/
		int wall = removeWall(prey);
		if( wall >= 0 ) {
			String wallstr = Integer.toString(wall+1);
			move.append( "wx" + wallstr + "\n" );
			System.out.println(move.toString( ));
			return move.toString( );
		}
		if( stepsToBuildWalls > 0 || critDistance(prey) ) {
			move.append("\n");
			return move.toString( );
		}		
		int quad[ ] = enclosingQuadrangle(prey);
		int horz = 0;			
		int vert = 0;
		if( this.y < prey.y && this.y > quad[0] && !isBlocked(prey,0) )
			horz = this.y - quad[0];
		if( this.y > prey.y && this.y < quad[1] && !isBlocked(prey,0) )
			horz = quad[1] - this.y;
		if( this.x < prey.x && this.x > quad[2] && !isBlocked(prey,1) )
			vert = this.x - quad[2];
		if( this.x > prey.x && this.x < quad[3] && !isBlocked(prey,1) )
			vert = quad[3] - this.x;	
		if( horz == 0 && vert == 0 ) {
			move.append("\n");
			return move.toString( );		
		}
		boolean horizontal = horz > vert;
		move.append("w" + translateCoords(horizontal,prey) + "\n");
		return move.toString( );			
	}	
	int n;
	int m;
}
